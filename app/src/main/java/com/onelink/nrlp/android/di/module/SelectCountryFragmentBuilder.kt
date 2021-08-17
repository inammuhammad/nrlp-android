package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SelectCountryFragmentBuilder {


    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideSelectCountryFragment(): SelectCountryFragment
}