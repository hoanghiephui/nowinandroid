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

package com.podcast.player.ui

import android.content.res.Configuration
import androidx.compose.animation.core.tween
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.tracing.trace
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.ui.TrackDisposableJank
import com.podcast.player.feature.explore.exploreRoute
import com.podcast.player.feature.explore.navigateToExplore
import com.podcast.player.feature.foryou.forYouNavigationRoute
import com.podcast.player.feature.foryou.navigateToForYou
import com.podcast.player.feature.library.libraryRoute
import com.podcast.player.feature.library.navigateToLibraryGraph
import com.podcast.player.navigation.TopLevelDestination
import com.podcast.player.navigation.TopLevelDestination.EXPLORE
import com.podcast.player.navigation.TopLevelDestination.FOR_YOU
import com.podcast.player.navigation.TopLevelDestination.LIBRARY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun rememberNiaAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
    swipeableState: SwipeableState<Int> = rememberSwipeableState(
        initialValue = 0,
        animationSpec = tween(),
    ),
    density: Density = LocalDensity.current,
    configuration: Configuration = LocalConfiguration.current,
): NiaAppState {
    val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
    val swipeAreaHeight = screenHeight - SwipeAreaOffset
    NavigationTrackingSideEffect(navController)
    return remember(
        navController,
        coroutineScope,
        windowSizeClass,
        networkMonitor,
        userNewsResourceRepository,
    ) {
        NiaAppState(
            navController,
            coroutineScope,
            networkMonitor,
            userNewsResourceRepository,
            swipeableState = swipeableState,
            swipeAreaHeight = swipeAreaHeight,
        )
    }
}

@Stable
@OptIn(ExperimentalMaterialApi::class)
class NiaAppState(
    val navController: NavHostController,
    private val coroutineScope: CoroutineScope,
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    val swipeableState: SwipeableState<Int>,
    val swipeAreaHeight: Float,
) {
    private val swipeProgress @Composable get() = swipeableState.offset.value / -swipeAreaHeight
    val motionProgress @Composable get() = max(0f, min(swipeProgress, 1f))
    val anchors = mapOf(0f to 0, -swipeAreaHeight to 1)
    val isPlayerOpened @Composable get() = swipeableState.currentValue == 1
    fun openPlayer() = coroutineScope.launch { swipeableState.animateTo(1) }
    fun closePlayer() = coroutineScope.launch { swipeableState.animateTo(0) }
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            forYouNavigationRoute -> FOR_YOU
            exploreRoute -> EXPLORE
            libraryRoute -> LIBRARY
            else -> null
        }

    val isOffline = networkMonitor.isOnline
        .map(Boolean::not)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    /**
     * Map of top level destinations to be used in the TopBar, BottomBar and NavRail. The key is the
     * route.
     */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    /**
     * The top level destinations that have unread news resources.
     */
    val topLevelDestinationsWithUnreadResources: StateFlow<Set<TopLevelDestination>> =
        userNewsResourceRepository.observeAllForFollowedTopics()
            .combine(userNewsResourceRepository.observeAllBookmarked()) { forYouNewsResources, bookmarkedNewsResources ->
                setOfNotNull(
                    FOR_YOU.takeIf { forYouNewsResources.any { !it.hasBeenViewed } },
                    LIBRARY.takeIf { bookmarkedNewsResources.any { !it.hasBeenViewed } },
                )
            }.stateIn(
                coroutineScope,
                SharingStarted.WhileSubscribed(5_000),
                initialValue = emptySet(),
            )

    /**
     * UI logic for navigating to a top level destination in the app. Top level destinations have
     * only one copy of the destination of the back stack, and save and restore state whenever you
     * navigate to and from it.
     *
     * @param topLevelDestination: The destination the app needs to navigate to.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // reselecting the same item
                launchSingleTop = true
                // Restore state when reselecting a previously selected item
                restoreState = true
            }

            when (topLevelDestination) {
                FOR_YOU -> navController.navigateToForYou(topLevelNavOptions)
                EXPLORE -> navController.navigateToExplore(topLevelNavOptions)
                LIBRARY -> navController.navigateToLibraryGraph(topLevelNavOptions)
            }
        }
    }

    fun navigateToSearch() {
        //navController.navigateToSearch()
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}

private const val SwipeAreaOffset = 400