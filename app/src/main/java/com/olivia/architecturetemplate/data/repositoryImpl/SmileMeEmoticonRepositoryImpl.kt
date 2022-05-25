package com.olivia.architecturetemplate.data.repositoryImpl

import com.olivia.architecturetemplate.data.retrofit.api.SmileMeApi
import com.olivia.architecturetemplate.data.util.FileUtils
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * # bemily_messenger-app-android
 * # smile me emoticon repository
 * @author hsjun85
 * @since 2022-03-02
 */
class SmileMeEmoticonRepositoryImpl @Inject constructor(
    private val api: SmileMeApi
) : SmileMeEmoticonRepository {

    override fun addSmileMeEmoticon(image: String, imageThumb: String): Flow<Long> = flow {
        emit(0L)
    }

    override fun deleteSmileMeEmoticon(emoticonId: String) = flow {
        emit(Unit)
    }

    override fun clearSmileMeEmoticonCache(cachePath: String) {
        FileUtils.deleteFile(cachePath)
    }

    override fun getSmileMeEmoticonCount(): Int {
        return 0
    }

}