package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintResponseFragment
import com.onelink.nrlp.android.features.complaint.fragment.registered.RegComplaintTypeFragment
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.select.generic.view.SelectBranchCenterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class RegComplaintFragmentsBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRegComplaintTypeFragment(): RegComplaintTypeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRegComplaintDetailsFragment(): RegComplaintDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintResponseFragment(): RegComplaintResponseFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBranchCenterFragment(): SelectBranchCenterFragment
}