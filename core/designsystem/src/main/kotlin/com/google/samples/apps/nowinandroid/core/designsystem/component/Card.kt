package com.google.samples.apps.nowinandroid.core.designsystem.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shapeSize: Dp = CardShapeSize,
    clickedShapeSize: Dp = CardClickedShapeSize,
    colors: CardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable ColumnScope.() -> Unit
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) CardPressedScale else 1f,
        animationSpec = CardPressedAnimation,
        label = "ScaleAnimation"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isPressed) CardPressedAlpha else 1f,
        animationSpec = CardPressedAnimation,
        label = "AlphaAnimation"
    )
    val animatedShapeSize by animateDpAsState(
        targetValue = if (isPressed) clickedShapeSize else shapeSize,
        label = "ShapeSizeAnimation"
    )

    Card(
        onClick = onClick,
        modifier = modifier.graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        enabled = enabled,
        shape = RoundedCornerShape(animatedShapeSize),
        colors = colors,
        elevation = elevation,
        border = border,
        interactionSource = interactionSource,
        content = content
    )
}

private const val CardPressedScale = 0.95f
private const val CardPressedAlpha = 0.75f
private val CardPressedAnimation = tween<Float>()
private val CardShapeSize = 12.dp
private val CardClickedShapeSize = 16.dp
