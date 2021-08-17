package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.nrlpBenefits.fragments.NrlpBenefitsFragment
import com.onelink.nrlp.android.features.nrlpBenefits.fragments.NrlpPartnersFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NrlpBenefitsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNrlpPartnersFragment(): NrlpPartnersFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideNrlpBenefitsFragment(): NrlpBenefitsFragment
}