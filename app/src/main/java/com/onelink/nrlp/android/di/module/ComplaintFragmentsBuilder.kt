package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.complaint.fragment.ComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.ComplaintResponseFragment
import com.onelink.nrlp.android.features.complaint.fragment.ComplaintTypeFragment
import com.onelink.nrlp.android.features.complaint.fragment.UserTypeComplaintFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ComplaintFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideUserTypeComplaintFragment(): UserTypeComplaintFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintTypeFragment(): ComplaintTypeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintDetailsFragment(): ComplaintDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintResponseFragment(): ComplaintResponseFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment
}