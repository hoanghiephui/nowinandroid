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

package com.podcast.player.feature.player

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.samples.apps.nowinandroid.core.designsystem.component.NiaBackground

@Composable
fun FullPlayer(
    isPlayerOpened: Boolean,
    onSetSystemBarsLightIcons: () -> Unit,
    onResetSystemBarsIcons: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NiaBackground {

    }

    LaunchedEffect(isPlayerOpened, onSetSystemBarsLightIcons, onResetSystemBarsIcons) {
        if (isPlayerOpened) onSetSystemBarsLightIcons() else onResetSystemBarsIcons()
    }

    BackHandler(enabled = isPlayerOpened, onBack = onBackClick)
}
