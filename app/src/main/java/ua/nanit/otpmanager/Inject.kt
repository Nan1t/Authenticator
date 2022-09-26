package ua.nanit.otpmanager

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.nanit.otpmanager.domain.encode.AndroidBase64Coder
import ua.nanit.otpmanager.domain.encode.Base64Coder
import ua.nanit.otpmanager.domain.storage.AccountJsonStorage
import ua.nanit.otpmanager.domain.storage.AccountStorage
import java.io.File
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Provides
    @Named("appDir")
    fun provideAppDir(@ApplicationContext ctx: Context): File {
        return ctx.filesDir
    }

    @Provides
    fun provideDispatcher(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun provideBase64Coder(): Base64Coder {
        return AndroidBase64Coder
    }

}

@Module
@InstallIn(SingletonComponent::class)
interface BindingsModule {

    @Binds
    fun bindAccountStorage(jsonStorage: AccountJsonStorage): AccountStorage

}