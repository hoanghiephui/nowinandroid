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

package com.podcast.net.discovery

import com.podcast.core.service.download.AntennapodHttpClient
import com.podcast.net.discovery.feed.FeedUrlNotFoundException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class ItunesPodcastSearcher: PodcastSearcher {
    companion object {
        private const val ITUNES_API_URL = "https://itunes.apple.com/search?media=podcast&term=%s"
        private const val PATTERN_BY_ID = ".*/podcasts\\.apple\\.com/.*/podcast/.*/id(\\d+).*"
    }

    override suspend fun search(query: String): Flow<List<PodcastSearchResult>> = flow {
        val encodedQuery: String = try {
            URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            // Handle the encoding exception if necessary
            query
        }

        val formattedUrl = String.format(ITUNES_API_URL, encodedQuery)

        val client = AntennapodHttpClient.getHttpClient()
        val httpReq = Request.Builder().url(formattedUrl).build()
        val podcasts = mutableListOf<PodcastSearchResult>()

        try {
            val response = withContext(Dispatchers.IO) {
                client.newCall(httpReq).execute()
            }

            if (response.isSuccessful) {
                val resultString = response.body?.string() ?: throw IOException(response.toString())
                val result = JSONObject(resultString)
                val j = result.getJSONArray("results")

                for (i in 0 until j.length()) {
                    val podcastJson = j.getJSONObject(i)
                    val podcast = PodcastSearchResult.fromItunes(podcastJson)
                    if (podcast.feedUrl != null) {
                        podcasts.add(podcast)
                    }
                }

                emit(podcasts)
            } else {
                throw IOException(response.toString())
            }
        } catch (e: Exception) {
            throw e
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun lookupUrl(resultUrl: String): Flow<String> = flow {
        val pattern = Pattern.compile(PATTERN_BY_ID)
        val matcher = pattern.matcher(resultUrl)
        val lookupUrl = if (matcher.find()) {
            "https://itunes.apple.com/lookup?id=" + matcher.group(1)
        } else {
            resultUrl
        }

        try {
            val client = AntennapodHttpClient.getHttpClient()
            val httpReq = Request.Builder().url(lookupUrl).build()
            val response = withContext(Dispatchers.IO) {
                client.newCall(httpReq).execute()
            }

            if (response.isSuccessful) {
                val resultString = response.body?.string() ?: throw IOException(response.toString())
                val result = JSONObject(resultString)
                val results = result.getJSONArray("results").getJSONObject(0)
                val feedUrlName = "feedUrl"

                if (!results.has(feedUrlName)) {
                    val artistName = results.getString("artistName")
                    val trackName = results.getString("trackName")
                    throw FeedUrlNotFoundException(artistName, trackName)
                }

                val feedUrl = results.getString(feedUrlName)
                emit(feedUrl)
            } else {
                throw IOException(response.toString())
            }
        } catch (e: IOException) {
            throw e
        } catch (e: JSONException) {
            throw e
        }
    }.flowOn(Dispatchers.IO)

    override fun urlNeedsLookup(resultUrl: String): Boolean {
        return resultUrl.contains("itunes.apple.com") || resultUrl.matches(PATTERN_BY_ID.toRegex())
    }

    override fun getName(): String = "Apple"
}
