package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.redeem.fragments.RedeemAgentConfirmationFragment
import com.onelink.nrlp.android.features.redeem.fragments.RedeemOtpAuthentication
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionPartnerFragment
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionPartnerServiceFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 11/07/2020.
 */
@Suppress("unused")
@Module
abstract class RedeemFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRedeemOtpAuthentication(): RedeemOtpAuthentication

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideRedeemAgentConfirmationFragment(): RedeemAgentConfirmationFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemPartnerFragment(): RedemptionPartnerFragment


    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemPartnerServiceFragment(): RedemptionPartnerServiceFragment
}
