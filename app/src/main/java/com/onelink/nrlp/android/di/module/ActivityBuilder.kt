package com.onelink.nrlp.android.di.module

import com.onelink.nrlp.android.di.annotations.ActivityScope
import com.onelink.nrlp.android.features.beneficiary.view.BeneficiaryActivity
import com.onelink.nrlp.android.features.changePassword.view.ChangePassSuccessfulActivity
import com.onelink.nrlp.android.features.changePassword.view.ChangePasswordActivity
import com.onelink.nrlp.android.features.contactus.view.ContactUsActivity
import com.onelink.nrlp.android.features.faqs.view.FAQsActivity
import com.onelink.nrlp.android.features.forgotPassword.view.ForgotPasswordActivity
import com.onelink.nrlp.android.features.forgotPassword.view.PasswordChangeSuccessActivity
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.NadraVerificationsSuccessActivity
import com.onelink.nrlp.android.features.language.view.LanguageActivity
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.managePoints.view.ManagePointsActivity
import com.onelink.nrlp.android.features.managePoints.view.TransferPointsSuccessFulActivity
import com.onelink.nrlp.android.features.nrlpBenefits.view.NrlpBenefitsActivity
import com.onelink.nrlp.android.features.profile.view.ProfileActivity
import com.onelink.nrlp.android.features.profile.view.ProfileUpdateSuccessActivity
import com.onelink.nrlp.android.features.redeem.view.RedeemActivity
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.register.view.RegisterActivity
import com.onelink.nrlp.android.features.register.view.RegisterSuccessActivity
import com.onelink.nrlp.android.features.selfAwardPoints.view.SelfAwardPointsActivity
import com.onelink.nrlp.android.features.selfAwardPoints.view.SelfAwardPointsSuccessActivity
import com.onelink.nrlp.android.features.splash.view.SplashActivity
import com.onelink.nrlp.android.features.uuid.view.UUIDActivity
import com.onelink.nrlp.android.features.viewStatement.view.StatementGeneratedActivity
import com.onelink.nrlp.android.features.viewStatement.view.ViewStatementActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Umar Javed.
 */

@Suppress("unused")
@Module
abstract class ActivityBuilder {

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindSplashActivity(): SplashActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LoginFragmentBuilder::class])
    internal abstract fun bindLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [UUIDFragmentBuilder::class])
    internal abstract fun bindUUIDActivity(): UUIDActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ChangePasswordFragmentBuilder::class])
    internal abstract fun bindChangePasswordActivity(): ChangePasswordActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ManagePointsFragmentBuilder::class])
    internal abstract fun bindManagePointsActivity(): ManagePointsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SelfAwardPointsFragmentBuilder::class])
    internal abstract fun bindSelfAwardPointsActivity(): SelfAwardPointsActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindSelfAwardPointsSuccessActivity(): SelfAwardPointsSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [BeneficiaryFragmentsBuilder::class])
    internal abstract fun bindBeneficiaryActivity(): BeneficiaryActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [RegisterFragmentsBuilder::class])
    internal abstract fun bindRegisterActivity(): RegisterActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindRegisterSuccessActivity(): RegisterSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindNadraVerificationSuccessActivity(): NadraVerificationsSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindChangePasswordSuccessActivity(): ChangePassSuccessfulActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [HomeFragmentBuilder::class])
    internal abstract fun bindHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ViewStatementFragmentsBuilder::class])
    internal abstract fun bindViewStatementActivity(): ViewStatementActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindStatementGeneratedActivity(): StatementGeneratedActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ForgotPasswordFragmentsBuilder::class])
    internal abstract fun bindForgotPasswordActivity(): ForgotPasswordActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindPasswordChangeSuccessActivity(): PasswordChangeSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [FaqsFragmentBuilder::class])
    internal abstract fun bindFaqsActivity(): FAQsActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindTransferPointsSuccessFulActivity(): TransferPointsSuccessFulActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ProfileFragmentsBuilder::class])
    internal abstract fun bindProfileActivity(): ProfileActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindProfileUpdateSuccessActivity(): ProfileUpdateSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [RedeemFragmentsBuilder::class])
    internal abstract fun bindRedeemActivity(): RedeemActivity

    @ActivityScope
    @ContributesAndroidInjector
    internal abstract fun bindRedeemSuccessActivity(): RedeemSuccessActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [ContactUsFragmentBuilder::class])
    internal abstract fun bindContactUsActivity(): ContactUsActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [LanguageFragmentBuilder::class])
    internal abstract fun bindLanguageActivity(): LanguageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [NrlpBenefitsFragmentBuilder::class])
    internal abstract fun bindNrlpBenefitsActivity(): NrlpBenefitsActivity

}
