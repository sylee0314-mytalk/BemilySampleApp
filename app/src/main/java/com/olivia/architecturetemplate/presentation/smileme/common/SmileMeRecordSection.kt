package com.olivia.architecturetemplate.presentation.smileme.common

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * # bemily_messenger-app-android
 * # Smile me record section
 * @author mjkim
 * @since 2022-03-03
 */

@Composable
fun SmileMeRecordSection(
    modifier: Modifier = Modifier,
    progress: Float? = null,
    icon: ImageVector? = null,
    desc: String? = null,
    title: String = "",
    contentColor: Color = LocalContentColor.current,
    recordColor: Color = contentColor,
    progressColor: Color = contentColor,
    enabled: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier.padding(vertical = 8.dp)) {
            LoadInfoBigComponent        (
            color = contentColor,
            icon = {
                if (icon != null) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = icon,
                        contentDescription = desc
                    )
                }
            },
            circle = {
                val clickModifier = if (progress == null) {
                    Modifier.clickable(enabled = enabled, onClick = onClick)
                } else Modifier

                Box {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(
                                width = 6.dp,
                                shape = CircleShape,
                                color = if (progress == null) recordColor else MaterialTheme.colors.primary
                            )
                            .then(clickModifier)
                    )
                    if (progress != null) {
                        val progressAnimation by animateFloatAsState(targetValue = progress)
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize(),
                            color = progressColor,
                            progress = progressAnimation,
                            strokeWidth = 6.dp
                        )
                    }
                }
            },
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        )
    }
}

@Composable
fun LoadInfoBigComponent(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    circle: @Composable () -> Unit,
    title: @Composable () -> Unit,
    color: Color
) {
    CompositionLocalProvider(
        LocalContentColor provides color
    ) {
        Column(
            modifier = modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                circle()

                Box(modifier = Modifier.size(36.dp)) {
                    icon()
                }
            }

            ProvideTextStyle(
                value = MaterialTheme.typography.subtitle2,
                content = title
            )
        }
    }
}