package com.onelink.nrlp.android.di

import com.onelink.nrlp.android.OneLinkApplication
import com.onelink.nrlp.android.di.module.ActivityBuilder
import com.onelink.nrlp.android.di.module.AppModule
import com.onelink.nrlp.android.di.module.NetworkModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Umar Javed.
 */

@Suppress("DEPRECATION")
@Singleton
@Component(
    modules = [AppModule::class,
        AndroidSupportInjectionModule::class,
        NetworkModule::class,
        ActivityBuilder::class]
)

interface AppComponent : AndroidInjector<OneLinkApplication> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<OneLinkApplication>() {
        abstract override fun build(): AppComponent
    }
}
