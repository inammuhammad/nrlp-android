package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.redeem.fragments.*
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
    abstract fun redemFBRDescriptionFragment(): RedemptionFBRDescriptionFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemFBRPSIDFragment(): RedemptionFBRPSIDFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemPIADescriptionFragment(): RedemptionPIADescriptionFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemPIAPSIDFragment(): RedemptionPIAPSIDFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemNadraDescriptionFragment(): RedemptionNadraDescriptionFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemNadraTrackingIDFragment(): RedemptionNadraTrackingIDFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemUSCDescriptionFragment(): RedemptionUSCDescriptionFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemUSCPSIDFragment(): RedemptionUSCPSIDFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemOPFVoucherFragment(): RedemptionOPFVoucherFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemSLICPolicyFragment(): RedemptionSLICPolicyFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemBEOECNICFragment(): RedemptionBEOECNICFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun redemPartnerServiceFragment(): RedemptionPartnerServiceFragment
}
