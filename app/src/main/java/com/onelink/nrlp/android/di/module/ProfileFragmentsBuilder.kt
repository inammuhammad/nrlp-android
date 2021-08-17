package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.profile.fragments.EditProfileFragment
import com.onelink.nrlp.android.features.profile.fragments.EditProfileOtpAuthentication
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
@Suppress("unused")
@Module
abstract class ProfileFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideEditProfileFragment(): EditProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesEditProfileOtpAuthenticationFragment(): EditProfileOtpAuthentication

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment

}
