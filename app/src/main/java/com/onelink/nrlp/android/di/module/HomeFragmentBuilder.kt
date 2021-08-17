package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.home.fragments.BeneficiaryHomeFragment
import com.onelink.nrlp.android.features.home.fragments.HomeFragment
import com.onelink.nrlp.android.features.home.fragments.RemitterHomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class HomeFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRemitterHomeFragment(): RemitterHomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBeneficiaryHomeFragment(): BeneficiaryHomeFragment
}