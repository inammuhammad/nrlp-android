package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.changePassword.view.ChangePasswordFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ChangePasswordFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideChangePasswordFragment(): ChangePasswordFragment
}