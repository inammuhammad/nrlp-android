package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.uuid.view.UUIDOtpAuthentication
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class UUIDFragmentBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideUUIDOtpAuthenticationFragment(): UUIDOtpAuthentication
}