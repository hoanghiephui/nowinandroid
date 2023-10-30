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

package com.podcast.player.ui

import android.content.Context
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration.Indefinite
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.google.samples.apps.nowinandroid.core.data.repository.UserNewsResourceRepository
import com.google.samples.apps.nowinandroid.core.data.util.NetworkMonitor
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaGradientBackground
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaNavigationBar
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaNavigationBarItem
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaTopAppBar
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.GradientColors
import com.google.samples.apps.nowinandroid.core.designsystem.theme.LocalGradientColors
import com.podcast.player.MainActivityViewModel
import com.podcast.player.R
import com.podcast.player.R.string
import com.podcast.player.feature.feedview.OnlineFeedView
import com.podcast.player.feature.player.FullPlayer
import com.podcast.player.feature.player.mini.MiniPlayer
import com.podcast.player.navigation.PodcastNavHost
import com.podcast.player.navigation.TopLevelDestination

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMotionApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun PodcastApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    userNewsResourceRepository: UserNewsResourceRepository,
    onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit,
    viewModel: MainActivityViewModel,
    appState: NiaAppState = rememberNiaAppState(
        networkMonitor = networkMonitor,
        windowSizeClass = windowSizeClass,
        userNewsResourceRepository = userNewsResourceRepository,
        openFeedView = {
            viewModel.openFeedView(it)
        },
    ),
    context: Context = LocalContext.current,
) {
    val shouldShowGradientBackground =
        appState.currentTopLevelDestination == TopLevelDestination.FOR_YOU
    val stateOpenFeed by viewModel.stateOpenFeed.collectAsStateWithLifecycle()
    var showFeedView by remember { mutableStateOf(false) }
    LaunchedEffect(
        key1 = stateOpenFeed,
        block = {
            showFeedView = stateOpenFeed != null
        },
    )
    NiaBackground {
        NiaGradientBackground(
            gradientColors = if (shouldShowGradientBackground) {
                LocalGradientColors.current
            } else {
                GradientColors()
            },
        ) {
            val snackbarHostState = remember { SnackbarHostState() }
            val isOffline by appState.isOffline.collectAsStateWithLifecycle()

            // If user is not connected to the internet show a snack bar to inform them.
            val notConnectedMessage = stringResource(string.not_connected)
            LaunchedEffect(isOffline) {
                if (isOffline) {
                    snackbarHostState.showSnackbar(
                        message = notConnectedMessage,
                        duration = Indefinite,
                    )
                }
            }

            val unreadDestinations by appState.topLevelDestinationsWithUnreadResources.collectAsStateWithLifecycle()

            val motionSceneContent = remember {
                context.resources
                    .openRawResource(R.raw.motion_scene)
                    .readBytes()
                    .decodeToString()
            }
            MotionLayout(
                modifier = Modifier.fillMaxSize(),
                motionScene = MotionScene(content = motionSceneContent),
                progress = appState.motionProgress,
            ) {
                Box(modifier = Modifier.layoutId("topBar")) {
                    val destination = appState.currentTopLevelDestination
                    if (destination != null) {
                        NiaTopAppBar(
                            titleRes = destination.titleTextId,
                            navigationIcon = NiaIcons.Search,
                            navigationIconContentDescription = "",
                            actionIcon = NiaIcons.Settings,
                            actionIconContentDescription = "",
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.Transparent,
                            ),
                            onActionClick = { },
                            onNavigationClick = { appState.navigateToSearch() },
                        )
                    }
                }
                Box(modifier = Modifier.layoutId("content")) {
                    PodcastNavHost(
                        appState = appState,
                        onShowSnackbar = { message, action ->
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = Short,
                            ) == ActionPerformed
                        },
                    )
                }

                Box(modifier = Modifier.layoutId("miniPlayer")) {
                    MiniPlayer(
                        modifier = Modifier.playerSwipe(
                            swipeableState = appState.swipeableState,
                            anchors = appState.anchors,
                        ),
                        onNavigateToPlayer = appState::openPlayer,
                    )
                }
                Box(modifier = Modifier.layoutId("fullPlayer")) {
                    FullPlayer(
                        modifier = Modifier.playerSwipe(
                            swipeableState = appState.swipeableState,
                            anchors = appState.anchors,
                        ),
                        isPlayerOpened = appState.isPlayerOpened,
                        onSetSystemBarsLightIcons = onSetSystemBarsLightIcons,
                        onResetSystemBarsIcons = onResetSystemBarsIcons,
                        onBackClick = appState::closePlayer,
                    )
                }

                Box(modifier = Modifier.layoutId("bottomBar")) {
                    NiaBottomBar(
                        destinations = appState.topLevelDestinations,
                        destinationsWithUnreadResources = unreadDestinations,
                        onNavigateToDestination = appState::navigateToTopLevelDestination,
                        currentDestination = appState.currentDestination,
                        modifier = Modifier.testTag("NiaBottomBar"),
                    )
                }
            }

            if (showFeedView) {
                OnlineFeedView(
                    onDismissRequest = {
                        showFeedView = false
                    },
                    feedUrl = stateOpenFeed!!,
                )
            }
        }
    }
}

@Composable
private fun NiaBottomBar(
    destinations: List<TopLevelDestination>,
    destinationsWithUnreadResources: Set<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
) {
    NiaNavigationBar(
        modifier = modifier,
    ) {
        destinations.forEach { destination ->
            val hasUnread = destinationsWithUnreadResources.contains(destination)
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NiaNavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.unselectedIcon,
                        contentDescription = null,
                    )
                },
                selectedIcon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = null,
                    )
                },
                label = { Text(stringResource(destination.iconTextId)) },
                modifier = if (hasUnread) Modifier.notificationDot() else Modifier,
            )
        }
    }
}

private fun Modifier.notificationDot(): Modifier = composed {
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    drawWithContent {
        drawContent()
        drawCircle(
            tertiaryColor,
            radius = 5.dp.toPx(),
            // This is based on the dimensions of the NavigationBar's "indicator pill";
            // however, its parameters are private, so we must depend on them implicitly
            // (NavigationBarTokens.ActiveIndicatorWidth = 64.dp)
            center = center + Offset(
                64.dp.toPx() * .45f,
                32.dp.toPx() * -.45f - 6.dp.toPx(),
            ),
        )
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.name, true) ?: false
    } ?: false

@OptIn(ExperimentalMaterialApi::class)
private fun Modifier.playerSwipe(
    swipeableState: SwipeableState<Int>,
    anchors: Map<Float, Int>,
) = swipeable(
    state = swipeableState,
    anchors = anchors,
    orientation = Orientation.Vertical,
    thresholds = { _, _ -> FractionalThreshold(SwipeFraction) },
)

private const val SwipeFraction = 0.3f
