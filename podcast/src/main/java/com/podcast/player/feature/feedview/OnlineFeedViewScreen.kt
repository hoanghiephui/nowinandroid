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

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaButton
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaOverlayLoadingWheel
import com.google.samples.apps.nowinandroid.core.designsystem.component.PodCard
import com.google.samples.apps.nowinandroid.core.ui.NewsResourceShortDescription
import com.podcast.core.R.string
import com.podcast.core.util.DateFormatter
import com.podcast.core.util.syndication.HtmlToPlainText
import com.podcast.player.feature.feedview.OnlineFeedUiState.Error
import com.podcast.player.feature.feedview.OnlineFeedUiState.ErrorAunauthorized
import com.podcast.player.ui.component.PodArtworkImage
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineFeedView(
    viewModel: OnlineFeedViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    feedUrl: String,
) {
    viewModel.lookupUrlAndDownload(feedUrl)
    val sheetState = rememberModalBottomSheetState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    ModalBottomSheet(
        onDismissRequest = {
            viewModel.onClear()
            onDismissRequest.invoke()
        },
        sheetState = sheetState,
    ) {
        Crossfade(uiState, label = "uiState") { viewState ->
            when (viewState) {
                OnlineFeedUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                    ) {
                        NiaOverlayLoadingWheel(
                            modifier = Modifier.align(Alignment.Center),
                            contentDesc = "loading",
                        )
                    }
                }

                is OnlineFeedUiState.Success -> {
                    val feed = viewState.feed
                    val alternateFeedUrls = viewState.alternateFeedUrls
                    Surface(
                        modifier = Modifier
                            //.padding(horizontal = 16.dp)
                            .sizeIn(minHeight = 300.dp),
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp,
                            ),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            item {
                                Row(verticalAlignment = Alignment.Top) {
                                    PodCard(modifier = Modifier.size(110.dp), onClick = {}) {
                                        PodArtworkImage(
                                            modifier = Modifier.aspectRatio(1f),
                                            artworkUri = Uri.parse(feed.imageUrl),
                                            contentDescription = "discovers.title",
                                        )
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = feed.feedTitle,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        Text(
                                            text = feed.author,
                                            modifier = Modifier.padding(start = 8.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                        )
                                        NiaButton(
                                            onClick = {},
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(8.dp),
                                        ) {
                                            Text(text = stringResource(id = string.subscribe_label))
                                        }
                                    }

                                }
                                Text(
                                    text = feed.description,
                                    style = MaterialTheme.typography.titleSmall,
                                )
                                Divider(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp, bottom = 24.dp),
                                )

                                Text(
                                    text = stringResource(
                                        id = string.episode_label,
                                        feed.items.size.toString(),
                                    ),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.primary,
                                )
                            }
                            items(
                                items = feed.items,
                                key = { it.title },
                                itemContent = { item ->
                                    Column {
                                        Text(
                                            text = DateFormatter.formatAbbrev(
                                                context,
                                                item.pubDate,
                                            ),
                                            style = MaterialTheme.typography.labelSmall,
                                        )
                                        Text(
                                            item.title.replaceFirstChar {
                                                if (it.isLowerCase()) it.titlecase(
                                                    Locale.getDefault(),
                                                ) else it.toString()
                                            },
                                            style = MaterialTheme.typography.titleMedium,
                                        )
                                        val description: String =
                                            HtmlToPlainText.getPlainText(item.description)
                                                .replace("\n", " ")
                                                .replace("\\s+", " ")
                                                .trim()
                                        NewsResourceShortDescription(description)
                                    }
                                },
                            )
                        }
                    }
                }

                is ErrorAunauthorized -> {
                }

                is Error -> {
                }
            }
        }
    }
}
