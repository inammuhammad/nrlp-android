package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.contactus.fragments.ContactUsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ContactUsFragmentBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideContactUsFragment(): ContactUsFragment
}