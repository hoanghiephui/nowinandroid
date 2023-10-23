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
 */package com.podcast.player.feature.player.mini

import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.google.samples.apps.nowinandroid.core.designsystem.icon.PodIcons
import com.podcast.player.R

@Composable
internal fun MiniPlayerMediaButtons(
    isPlaying: Boolean,
    onPlayClick: () -> Unit,
    onPauseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = isPlaying,
        animationSpec = spring(),
        label = "CrossfadeAnimation"
    ) { targetIsPlaying ->
        val iconResource = if (targetIsPlaying) {
            PodIcons.Play.imageVector
        } else {
            PodIcons.Pause.imageVector
        }

        val contentDescriptionResource = if (targetIsPlaying) {
            R.string.play
        } else {
            R.string.pause
        }

        val onClick = if (targetIsPlaying) onPlayClick else onPauseClick

        MediaButton(
            imageVector = iconResource,
            contentDescriptionResource = contentDescriptionResource,
            onClick = onClick
        )
    }
}

@Composable
private fun MediaButton(
    imageVector: ImageVector,
    @StringRes contentDescriptionResource: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: IconButtonColors = IconButtonDefaults.iconButtonColors(
        contentColor = MaterialTheme.colorScheme.primary
    ),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedScale else 1f,
        animationSpec = MediaButtonPressedAnimation,
        label = "ScaleAnimation"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) MediaButtonPressedAlpha else 1f,
        animationSpec = MediaButtonPressedAnimation,
        label = "AlphaAnimation"
    )

    IconButton(
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        onClick = onClick,
        colors = colors,
        interactionSource = interactionSource
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = stringResource(id = contentDescriptionResource)
        )
    }
}

private const val MediaButtonPressedScale = 0.85f
private const val MediaButtonPressedAlpha = 0.75f
private val MediaButtonPressedAnimation = tween<Float>()
