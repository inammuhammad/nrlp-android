package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.language.view.LanguageFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class LanguageFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideLanguageFragment(): LanguageFragment
}