package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.home.fragments.*
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
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

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNadraVerificationRequiredFragment(): NadraVerificationRequiredFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNadraVerificationDetailsFragment(): NadraVerificationDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun selectCityFragment(): SelectCityFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideFatherNameVerificationFragment(): FatherNameVerificationFragment
}