package com.olivia.architecturetemplate.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * # bemily_messenger-app-android
 * # smile me emoticon repository
 * @author hsjun85
 * @since 2022-03-02
 */
interface SmileMeEmoticonRepository {
    fun addSmileMeEmoticon(image: String, imageThumb: String): Flow<Long>
    fun deleteSmileMeEmoticon(emoticonId: String): Flow<Unit>
    fun clearSmileMeEmoticonCache(cachePath: String)
    fun getSmileMeEmoticonCount(): Int
}