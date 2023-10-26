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

package com.podcast.core.domain.data.repository

import com.google.samples.apps.nowinandroid.core.network.Dispatcher
import com.google.samples.apps.nowinandroid.core.network.NiaDispatchers.IO
import com.podcast.core.domain.repository.ItunesTopListRepository
import com.podcast.model.feed.Feed
import com.podcast.net.discovery.ItunesTopListLoader
import com.podcast.net.discovery.PodcastSearchResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ItunesTopListRepositoryImpl @Inject constructor(
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher,
    private val itunesTopListLoader: ItunesTopListLoader,
) : ItunesTopListRepository {

    override fun loadTopList(
        countryCode: String,
        numberSuggest: Int,
        subscribed: List<Feed>,
    ): Flow<List<PodcastSearchResult>> = flow {
        emit(
            topList(countryCode, numberSuggest, subscribed),
        )
    }.flowOn(ioDispatcher)

    private fun topList(
        countryCode: String,
        numberSuggest: Int,
        subscribed: List<Feed>,
    ) = itunesTopListLoader.loadToplist(
        countryCode, numberSuggest, subscribed,
    )
}
