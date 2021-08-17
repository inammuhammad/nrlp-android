package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.managePoints.view.ManagePointsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ManagePointsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideManagePointFragment(): ManagePointsFragment
}