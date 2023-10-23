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

package com.podcast.player.feature.player.mini

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.google.samples.apps.nowinandroid.core.designsystem.theme.spacing
import com.podcast.player.model.PlaybackState.READY

@Composable
fun MiniPlayer(
    onNavigateToPlayer: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(MiniPlayerStartElevation),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(MiniPlayerEndElevation)
                    )
                )
            )
            .clickable(onClick = onNavigateToPlayer)
    ) {
        Row(
            modifier = Modifier
                .padding(MaterialTheme.spacing.extraSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                MiniPlayerArtworkImage(
                    artworkUri = Uri.EMPTY,
                    contentDescription = ""
                )
                MiniPlayerTitleArtist(
                    title = "Demo",
                    artist = "hhhh"
                )
            }
            MiniPlayerMediaButtons(
                isPlaying = true,
                onPlayClick = {},
                onPauseClick = {},
            )
        }
        MiniPlayerTimeProgress(
            playbackState = READY,
            currentPosition = 1,
            duration = 200
        )
    }
}

private val MiniPlayerStartElevation = 1.dp
private val MiniPlayerEndElevation = 3.dp
