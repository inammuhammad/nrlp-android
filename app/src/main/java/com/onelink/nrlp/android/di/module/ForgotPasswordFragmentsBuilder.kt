package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.FragmentScope
import com.onelink.nrlp.android.features.forgotPassword.fragments.ForgotPasswordFragment
import com.onelink.nrlp.android.features.forgotPassword.fragments.ForgotPasswordOTPFragment
import com.onelink.nrlp.android.features.forgotPassword.fragments.UpdatePasswordFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Qazi Abubakar on 16/07/2020.
 */
@Suppress("unused")
@Module
abstract class ForgotPasswordFragmentsBuilder {

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideForgotPasswordFragment(): ForgotPasswordFragment


    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideForgotPasswordOTPFragment(): ForgotPasswordOTPFragment


    @FragmentScope
    @ContributesAndroidInjector
    abstract fun provideUpdatePasswordOTPFragment(): UpdatePasswordFragment

}
