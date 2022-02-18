package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.receiver.fragments.ManageReceiverFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverDetailsFragment
import com.onelink.nrlp.android.features.receiver.fragments.ReceiverTypeFragment
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Suppress("unused")
@Module
abstract class ReceiverManagementFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideManageReceiverFragment(): ManageReceiverFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideBeneficiaryDetailsFragment(): ReceiverDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideReceiverTypeFragment(): ReceiverTypeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideCountryFragment(): SelectCountryFragment
}
