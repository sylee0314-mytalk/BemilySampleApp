package com.olivia.architecturetemplate.presentation.smileme.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * # bemily_messenger-app-android
 * # Smile me bottom component
 * @author mjkim
 * @since 2022-03-03
 */

@Composable
fun SmileMeBottomComponent(
    modifier: Modifier = Modifier,
    leftButton: @Composable (() -> Unit)? = null,
    centerButton: @Composable () -> Unit = {},
    rightButton: @Composable (() -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalContentAlpha provides ContentAlpha.high
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(223.dp)
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(modifier = Modifier.padding(36.dp).size(48.dp)) {
                if (leftButton != null) {
                    leftButton()
                }
            }

            centerButton()

            Box(modifier = Modifier.padding(36.dp).size(48.dp)) {
                if (rightButton != null) {
                    rightButton()
                }
            }
        }
    }
}