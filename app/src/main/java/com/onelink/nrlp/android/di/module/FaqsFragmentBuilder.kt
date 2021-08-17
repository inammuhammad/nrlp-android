package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.faqs.view.FAQsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FaqsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): FAQsFragment
}