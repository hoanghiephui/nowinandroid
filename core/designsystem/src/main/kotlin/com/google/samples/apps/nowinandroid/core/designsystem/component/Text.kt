package com.google.samples.apps.nowinandroid.core.designsystem.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SingleLineText(
    text: String,
    modifier: Modifier = Modifier,
    shouldUseMarquee: Boolean = false,
    color: Color = Color.Unspecified,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        modifier = modifier.then(if (shouldUseMarquee) Modifier.basicMarquee() else Modifier),
        text = text,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        style = style
    )
}
