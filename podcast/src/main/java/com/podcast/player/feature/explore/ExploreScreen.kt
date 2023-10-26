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

package com.podcast.player.feature.explore

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridCells.Fixed
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOverlayLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.component.PodCard
import com.google.samples.apps.nowinandroid.core.designsystem.component.SingleLineText
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.scrollbarState
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons
import com.google.samples.apps.nowinandroid.core.designsystem.theme.spacing
import com.podcast.net.discovery.PodcastSearchResult
import com.podcast.player.feature.explore.ExploreUiState.Loading
import com.podcast.player.feature.explore.ExploreUiState.Success
import com.podcast.player.ui.component.PodArtworkImage

@Composable
fun ExploreRouter(
    viewModel: ExploreViewModel = hiltViewModel(),
) {
    ExploreScreen(viewModel = viewModel)
}

@Composable
private fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel,
) {

    val stateUI by viewModel.uiState.collectAsStateWithLifecycle()

    val state = rememberLazyStaggeredGridState()
    val scrollbarState = state.scrollbarState(
        itemsAvailable = 1,
    )
    LazyVerticalStaggeredGrid(
        columns = Adaptive(300.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalItemSpacing = 24.dp,
        modifier = modifier,
        state = state,
    ) {
        searchView(modifier)
        discoveryFeed(
            stateUI,
            modifier = Modifier.layout { measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                    ),
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyStaggeredGridScope.discoveryFeed(
    stateUI: ExploreUiState,
    modifier: Modifier,
) {
    item(span = StaggeredGridItemSpan.FullLine, contentType = "discovery") {
        Column(modifier = modifier) {
            SingleLineText(
                text = "Discover",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Discovery(stateUI, modifier)
        }

    }
}

@Composable
fun Discovery(
    stateUI: ExploreUiState,
    modifier: Modifier,
) {
    val lazyGridState = rememberLazyGridState()
    Box(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        when (stateUI) {
            Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    NiaOverlayLoadingWheel(
                        modifier = Modifier
                            .align(Alignment.Center),
                        contentDesc = "loading",
                    )
                }
            }
            is Success -> {
                LazyHorizontalGrid(
                    state = lazyGridState,
                    rows = Fixed(1),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(24.dp),
                    modifier = Modifier
                        // LazyHorizontalGrid has to be constrained in height.
                        // However, we can't set a fixed height because the horizontal grid contains
                        // vertical text that can be rescaled.
                        // When the fontScale is at most 1, we know that the horizontal grid will be at most
                        // 240dp tall, so this is an upper bound for when the font scale is at most 1.
                        // When the fontScale is greater than 1, the height required by the text inside the
                        // horizontal grid will increase by at most the same factor, so 240sp is a valid
                        // upper bound for how much space we need in that case.
                        // The maximum of these two bounds is therefore a valid upper bound in all cases.
                        .heightIn(max = max(180.dp, with(LocalDensity.current) { 180.sp.toDp() }))
                        .fillMaxWidth()
                ) {
                    items(items = stateUI.discovers, key = PodcastSearchResult::title) { discover ->
                        DiscoverItem(
                            discovers = discover,
                            onClick = {

                            },
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = stateUI is Loading,
            enter = slideInVertically(
                initialOffsetY = { fullHeight -> -fullHeight },
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { fullHeight -> -fullHeight },
            ) + fadeOut(),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                NiaOverlayLoadingWheel(
                    modifier = Modifier
                        .align(Alignment.Center),
                    contentDesc = "loading",
                )
            }
        }
    }
}

@Composable
private fun DiscoverItem(
    discovers: PodcastSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PodCard(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.smallMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
        ) {
            PodArtworkImage(
                modifier = Modifier.aspectRatio(1f),
                artworkUri = Uri.parse(discovers.imageUrl),
                contentDescription = discovers.title,
            )
        }
    }
}

private fun LazyStaggeredGridScope.searchView(modifier: Modifier) {
    item(span = StaggeredGridItemSpan.FullLine, contentType = "onboarding") {
        OutlinedCard(
            modifier = modifier
                .fillMaxWidth()
                .height(55.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxHeight()
                    .clickable {

                    },
            ) {
                Image(
                    imageVector = NiaIcons.Search,
                    contentDescription = "Search",
                    modifier = modifier.padding(start = 12.dp),
                )
                Text(
                    text = "Search...",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = modifier
                        .padding(horizontal = 12.dp)
                        .weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }
    }
}
