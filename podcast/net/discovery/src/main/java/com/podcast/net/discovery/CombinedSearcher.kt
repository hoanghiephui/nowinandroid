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

import android.text.TextUtils
import com.podcast.net.discovery.PodcastSearcherRegistry.SearcherInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.single
import java.util.concurrent.CountDownLatch

class CombinedSearcher: PodcastSearcher {
    override suspend fun search(query: String): Flow<List<PodcastSearchResult>> = flow {
        val searchProviders = PodcastSearcherRegistry.searchProviders
        val singleResults = MutableList(searchProviders.size) { null as List<PodcastSearchResult>? }
        val latch = CountDownLatch(searchProviders.size)

        for (i in searchProviders.indices) {
            val searchProviderInfo = searchProviders[i]
            val searcher = searchProviderInfo.searcher
            if (searchProviderInfo.weight <= 0.00001f || searcher is CombinedSearcher) {
                latch.countDown()
                continue
            }

            try {
                val results = searcher.search(query)
                singleResults[i] = results.single()
                latch.countDown()
            } catch (throwable: Throwable) {
                // Handle the error here if needed
                throwable.printStackTrace()
                latch.countDown()
            }
        }

        latch.await()

        val results = weightSearchResults(singleResults)
        emit(results)
    }.flowOn(Dispatchers.IO)

    override suspend fun lookupUrl(resultUrl: String): Flow<String> =
        PodcastSearcherRegistry.lookupUrl(resultUrl)

    override fun urlNeedsLookup(resultUrl: String): Boolean =
        PodcastSearcherRegistry.urlNeedsLookup(resultUrl)

    override fun getName(): String {
        val names = ArrayList<String?>()
        for (i in 0 until PodcastSearcherRegistry.searchProviders.size) {
            val searchProviderInfo: SearcherInfo =
                PodcastSearcherRegistry.searchProviders[i]
            val searcher: PodcastSearcher = searchProviderInfo.searcher
            if (searchProviderInfo.weight > 0.00001f && searcher::class != CombinedSearcher::class.java) {
                names.add(searcher.getName())
            }
        }
        return TextUtils.join(", ", names)
    }

    private fun weightSearchResults(singleResults: List<List<PodcastSearchResult>? >): List<PodcastSearchResult> {
        val resultRanking = HashMap<String, Float>()
        val urlToResult = HashMap<String, PodcastSearchResult>()

        for (i in singleResults.indices) {
            val providerPriority = PodcastSearcherRegistry.searchProviders[i].weight
            val providerResults = singleResults[i] ?: continue

            for (position in providerResults.indices) {
                val result = providerResults[position]
                result.feedUrl?.let { urlToResult.put(it, result) }

                var ranking = 0f

                if (result.feedUrl in resultRanking) {
                    ranking = resultRanking[result.feedUrl] ?: 0f
                }

                ranking += 1f / (position + 1f)
                result.feedUrl?.let {
                    resultRanking[result.feedUrl] = ranking * providerPriority
                }
            }
        }

        val sortedResults = resultRanking.entries.sortedByDescending { it.value }
        val results = sortedResults.mapNotNull { entry ->
            urlToResult[entry.key]
        }

        return results
    }
}
