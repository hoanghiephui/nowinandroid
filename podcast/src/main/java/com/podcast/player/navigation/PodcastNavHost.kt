/*
 * Copyright 2022 The Android Open Source Project
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

package com.podcast.player.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.podcast.player.feature.explore.exploreScreen
import com.podcast.player.feature.foryou.forYouNavigationRoute
import com.podcast.player.feature.foryou.forYouScreen
import com.podcast.player.feature.library.libraryGraph
import com.podcast.player.ui.NiaAppState

/**
 * Top-level navigation graph. Navigation is organized as explained at
 * https://d.android.com/jetpack/compose/nav-adaptive
 *
 * The navigation graph defined in this file defines the different top level routes. Navigation
 * within each route is handled using state and Back Handlers.
 */
@Composable
fun PodcastNavHost(
    appState: NiaAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    startDestination: String = forYouNavigationRoute,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        forYouScreen()
        exploreScreen(
            onTopicClick = {},
            onShowSnackbar = onShowSnackbar,
        )
        libraryGraph(
            onTopicClick = {},
            nestedGraphs = {
                /*topicScreen(
                    onBackClick = navController::popBackStack,
                    onTopicClick = navController::navigateToTopic,
                )*/
            },
        )
    }
}