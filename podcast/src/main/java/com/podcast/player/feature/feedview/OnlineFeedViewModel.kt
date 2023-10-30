/*
 * Copyright 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.podcast.player.feature.feedview

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import com.podcast.core.service.download.DownloadRequestCreator
import com.podcast.core.service.download.Downloader
import com.podcast.core.service.download.HttpDownloader
import com.podcast.core.storage.DBReader
import com.podcast.core.util.syndication.FeedDiscoverer
import com.podcast.model.download.DownloadResult
import com.podcast.model.feed.Feed
import com.podcast.net.common.UrlChecker
import com.podcast.net.discovery.PodcastSearcherRegistry
import com.podcast.net.download.DownloadRequest
import com.podcast.parser.feed.FeedHandler
import com.podcast.parser.feed.FeedHandlerResult
import com.podcast.parser.feed.UnsupportedFeedtypeException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class OnlineFeedViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private var selectedDownloadUrl: String? = null
    private var username: String? = null
    private var passpword: String? = null
    private var downloader: Downloader? = null

    private var feeds: List<Feed>? = null
    fun lookupUrlAndDownload(url: String) {
        viewModelScope.launch(ioDispatcher) {
            PodcastSearcherRegistry.lookupUrl(url).collect {
                startFeedDownload(it)
            }
        }
    }

    fun startFeedDownload(url: String) {
        selectedDownloadUrl = UrlChecker.prepareUrl(url)
        val request = DownloadRequestCreator.create(
            Feed(
                selectedDownloadUrl,
                null,
            ),
        ).withAuthentication(username, passpword).withInitiatedByUser(true).build()
        viewModelScope.launch(ioDispatcher) {
            feeds = DBReader.getFeedList()
            val status = onDownload(request)
            status.collect {
                checkDownloadResult(it, request.destination)
            }
        }
    }

    private fun checkDownloadResult(status: DownloadResult, destination: String) {
        if (status.isSuccessful) {
            parseFeed(destination)
        }
    }

    private fun parseFeed(destination: String) {
        viewModelScope.launch(ioDispatcher) {
            doParseFeed(destination).collect {
                it?.let {
                    showFeedInformation(it.feed, it.alternateFeedUrls)
                }
            }
        }
    }

    private suspend fun doParseFeed(destination: String): Flow<FeedHandlerResult?> = flow {
        val handler = FeedHandler()
        val feed = Feed(selectedDownloadUrl, null)
        feed.file_url = destination
        val destinationFile = File(destination)

        try {
            emit(handler.parseFeed(feed))
        } catch (e: UnsupportedFeedtypeException) {
            if ("html".equals(e.rootElement, ignoreCase = true)) {
                val dialogShown = showFeedDiscoveryDialog(destinationFile, selectedDownloadUrl!!)
                if (dialogShown) {
                    emit(null) // Should not display an error message
                } else {
                    throw UnsupportedFeedtypeException("The podcast host\\'s server sent a website, not a podcast.")
                }
            } else {
                throw e
            }
        } catch (e: Exception) {
            emit(null) // Handle the exception as needed
            Log.e("AAA", Log.getStackTraceString(e))
            throw e
        } finally {
            val rc = destinationFile.delete()
            Log.d("AAA", "Deleted feed source file. Result: $rc")
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun onDownload(downloadRequest: DownloadRequest) = flow {
        downloader = HttpDownloader(downloadRequest)
        val call = downloader?.call()
        call?.result?.let { emit(it) }
    }

    private fun showFeedInformation(feed: Feed, alternateFeedUrls: Map<String, String>) {
        Log.d("AAA", feed.feedTitle)
    }

    private fun showFeedDiscoveryDialog(feedFile: File, baseUrl: String): Boolean {
        val fd = FeedDiscoverer()
        val urlsMap: Map<String, String>?
        try {
            urlsMap = fd.findLinks(feedFile, baseUrl)
            if (urlsMap.isNullOrEmpty()) {
                return false
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return false
        }

        val titles = mutableListOf<String>()
        val urls = ArrayList(urlsMap.keys)
        for (url in urls) {
            titles.add(urlsMap[url]!!)
        }

        if (urls.size == 1) {
            // Skip dialog and display the item directly
            savedStateHandle[ARG_FEEDURL] = urls[0]
            startFeedDownload(urls[0])
            return true
        }

        return true
    }

    companion object {
        const val ARG_FEEDURL = "arg.feedurl"
    }
}
