package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.register.fragments.*
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 09/07/2020.
 */
@Suppress("unused")
@Module
abstract class RegisterFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRegisterAccountFragment(): RegisterAccountFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRegisterBeneficiaryFragment(): RegisterBeneficiaryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideTermsAndConditionsFragment(): TermsAndConditionsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideVerifyRemitterFragment(): VerifyRemitterFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBeneficiaryRemitterFragment(): VerifyBeneficiaryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideOtpAuthenticationFragment(): OtpAuthenticationFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCityFragment(): SelectCityFragment

}
