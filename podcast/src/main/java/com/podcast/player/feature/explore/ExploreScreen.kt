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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells.Adaptive
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.designsystem.component.scrollbar.scrollbarState
import com.google.samples.apps.nowinandroid.core.designsystem.icon.NiaIcons

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
