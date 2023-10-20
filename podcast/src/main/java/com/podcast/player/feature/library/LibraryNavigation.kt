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

package com.podcast.player.feature.library

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation

private const val LIBRARY_GRAPH_ROUTE_PATTERN = "library_graph"
const val libraryRoute = "library_route"

fun NavController.navigateToLibraryGraph(navOptions: NavOptions? = null) {
    this.navigate(LIBRARY_GRAPH_ROUTE_PATTERN, navOptions)
}

fun NavGraphBuilder.libraryGraph(
    onTopicClick: (String) -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit,
) {
    navigation(
        route = LIBRARY_GRAPH_ROUTE_PATTERN,
        startDestination = libraryRoute,
    ) {
        composable(route = libraryRoute) {
            LibraryRouter()
        }
        nestedGraphs()
    }
}
