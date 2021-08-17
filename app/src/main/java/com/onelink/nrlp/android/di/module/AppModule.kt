package com.onelink.nrlp.android.di.module

import android.app.Application
import android.content.Context
import com.onelink.nrlp.android.OneLinkApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Umar Javed.
 */

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Provides
    @Singleton
    fun provideApplicationContext(application: OneLinkApplication): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApplication(application: OneLinkApplication): Application {
        return application
    }
}
