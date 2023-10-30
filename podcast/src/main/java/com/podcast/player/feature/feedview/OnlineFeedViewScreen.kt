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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.designsystem.component.PodCard
import com.google.samples.apps.nowinandroid.core.designsystem.theme.spacing
import com.podcast.player.ui.component.PodArtworkImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnlineFeedView(
    viewModel: OnlineFeedViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
    feedUrl: String
) {
    viewModel.lookupUrlAndDownload(feedUrl)
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        /*Row {
            PodCard(modifier = Modifier, onClick = {}) {
                Column(
                    modifier = Modifier.padding(MaterialTheme.spacing.smallMedium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    PodArtworkImage(
                        modifier = Modifier.aspectRatio(1f),
                        artworkUri = Uri.parse("discovers.imageUrl"),
                        contentDescription = "discovers.title",
                    )
                }
            }
        }*/
    }
}
