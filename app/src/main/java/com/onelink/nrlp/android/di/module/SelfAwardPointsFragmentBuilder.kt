package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.selfAwardPoints.view.SelfAwardPointsFragment
import com.onelink.nrlp.android.features.selfAwardPoints.view.SelfAwardPointsOTPFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class SelfAwardPointsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideSelfAwardPointFragment(): SelfAwardPointsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideSelfAwardPointOTPFragment(): SelfAwardPointsOTPFragment
}