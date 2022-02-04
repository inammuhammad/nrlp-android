package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintDetailsFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintResponseFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UnregComplaintTypeFragment
import com.onelink.nrlp.android.features.complaint.fragment.unregistered.UserTypeComplaintFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class UnregComplaintFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideUserTypeComplaintFragment(): UserTypeComplaintFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintTypeFragment(): UnregComplaintTypeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintDetailsFragment(): UnregComplaintDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideComplaintResponseFragment(): UnregComplaintResponseFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment
}