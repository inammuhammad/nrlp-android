package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */

@Suppress("unused")
@Module
abstract class BeneficiaryFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideManageBeneficiaryFragment(): ManageBeneficiaryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBeneficiaryDetailsFragment(): BeneficiaryDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment
}
