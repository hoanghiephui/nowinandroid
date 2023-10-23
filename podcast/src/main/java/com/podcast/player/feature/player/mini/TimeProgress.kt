package com.podcast.player.feature.player.mini

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.podcast.player.convertToProgress
import com.podcast.player.model.PlaybackState

@Composable
internal fun MiniPlayerTimeProgress(
    playbackState: PlaybackState,
    currentPosition: Long,
    duration: Long,
    modifier: Modifier = Modifier
) {
    val progress by animateFloatAsState(
        targetValue = convertToProgress(count = currentPosition, total = duration),
        label = "ProgressAnimation"
    )

    Column(modifier = modifier) {

        if (playbackState == PlaybackState.BUFFERING) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(1.dp))
        } else {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth().height(1.dp), progress = progress)
        }
    }
}
