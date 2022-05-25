package com.olivia.architecturetemplate.data.di

import com.olivia.architecturetemplate.data.repositoryImpl.SmileMeEmoticonRepositoryImpl
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonBitmapGetUseCase
import com.olivia.architecturetemplate.domain.inspactor.smileme.SmileMeEmoticonBitmapPutUseCase
import com.olivia.architecturetemplate.domain.repository.SmileMeEmoticonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

/**
 * # bemily_messenger-app-android
 * # app module
 * @author hsjun85
 * @since 2022-01-12
 */
@InstallIn(SingletonComponent::class)
@Module
class SmileMeModule {
    @Provides
    fun provideSmileMeEmoticonBitmapPutUseCase(): SmileMeEmoticonBitmapPutUseCase =
        SmileMeEmoticonBitmapPutUseCase(Dispatchers.IO)

    @Provides
    fun provideSmileMeEmoticonBitmapGetUseCase(): SmileMeEmoticonBitmapGetUseCase =
        SmileMeEmoticonBitmapGetUseCase(Dispatchers.IO)

    @Provides
    fun providesSmileMeEmoticonRepository(smileMeEmoticonRepositoryImpl: SmileMeEmoticonRepositoryImpl): SmileMeEmoticonRepository {
        return smileMeEmoticonRepositoryImpl
    }

}