package com.olivia.architecturetemplate.presentation.smileme.camera


import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bemily.messenger.smileme.domain.model.SmileMeDecor
import com.bemily.messenger.smileme.domain.model.SmileMeStore
import com.olivia.architecturetemplate.presentation.ui.theme.SampleAppTheme

/**
 * # bemily_messenger-app-android
 * # SmileMeDecorListSection
 * @author M.Y.Seong
 * @since 2022-03-03
 */

@Composable
fun SmileMeDecorListSection(
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    smileMeDecorList: List<SmileMeDecor>,
    selected: SmileMeDecor = smileMeDecorList.first(),
    onClickDecor: (SmileMeDecor) -> Unit = {}
) {
    Box(modifier = modifier.height(86.dp)) {
        if (isVisible) {
            LazyRow(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 14.dp
                )
            ) {
                items(smileMeDecorList) { smileMeDecor ->
                    SmileMeDecorListItemSection(
                        modifier = Modifier.clickable { onClickDecor(smileMeDecor) },
                        smileMeDecor = smileMeDecor,
                        isSelected = smileMeDecor.id == selected.id
                    )
                }
            }
        }
    }
}

@Preview("light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SmileMeEmoticonListSectionPreview() {
    SampleAppTheme {
        SmileMeDecorListSection(
            smileMeDecorList = SmileMeStore.stickers
        )
    }
}