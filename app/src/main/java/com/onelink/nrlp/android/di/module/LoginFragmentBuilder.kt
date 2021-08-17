package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.login.view.LoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class LoginFragmentBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideLoginFragment(): LoginFragment
}