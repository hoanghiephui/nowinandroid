
package com.podcast.player

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

internal fun convertToProgress(count: Long, total: Long) =
    ((count * ProgressDivider) / total / ProgressDivider).takeIf(Float::isFinite) ?: ZeroProgress

internal fun convertToPosition(value: Float, total: Long) = (value * total).toLong()

private const val ProgressDivider = 100f
private const val ZeroProgress = 0f
