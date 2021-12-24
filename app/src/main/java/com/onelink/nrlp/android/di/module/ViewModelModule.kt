package com.onelink.nrlp.android.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.di.ViewModelFactory
import com.onelink.nrlp.android.di.annotations.ViewModelKey
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryDetailsViewModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryViewModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.ManageBeneficiaryViewModel
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordFragmentViewModel
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordSuccessViewModel
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordViewModel
import com.onelink.nrlp.android.features.contactus.viewmodel.ContactUsActivityViewModel
import com.onelink.nrlp.android.features.faqs.viewmodel.FAQsFragmentViewModel
import com.onelink.nrlp.android.features.faqs.viewmodel.FaqsViewModel
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.*
import com.onelink.nrlp.android.features.home.fragments.HomeFragmentViewModel
import com.onelink.nrlp.android.features.home.viewmodel.HomeActivityViewModel
import com.onelink.nrlp.android.features.language.viewmodel.LanguageActivityViewModel
import com.onelink.nrlp.android.features.language.viewmodel.LanguageFragmentViewModel
import com.onelink.nrlp.android.features.login.viewmodel.LoginFragmentViewModel
import com.onelink.nrlp.android.features.login.viewmodel.LoginViewModel
import com.onelink.nrlp.android.features.managePoints.viewmodel.ManagePointsFragmentViewModel
import com.onelink.nrlp.android.features.managePoints.viewmodel.ManagePointsViewModel
import com.onelink.nrlp.android.features.managePoints.viewmodel.TransferPointsSuccessFulViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsFragmentViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpPartnersFragmentViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.EditProfileOtpFragmentViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.EditProfileViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.ProfileUpdateSuccessViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.ProfileViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.*
import com.onelink.nrlp.android.features.register.viewmodel.*
import com.onelink.nrlp.android.features.select.city.viewmodel.SelectCityFragmentViewModel
import com.onelink.nrlp.android.features.select.country.viewmodel.SelectCountryFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsOTPFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsSuccessViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsViewModel
import com.onelink.nrlp.android.features.splash.viewmodel.SplashViewModel
import com.onelink.nrlp.android.features.uuid.viewmodel.LoginOtpFragmentViewModel
import com.onelink.nrlp.android.features.uuid.viewmodel.UUIDActivityViewModel
import com.onelink.nrlp.android.features.viewStatement.viewmodel.AdvancedLoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.features.viewStatement.viewmodel.LoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.features.viewStatement.viewmodel.ViewStatementViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Umar Javed.
 */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    internal abstract fun splashViewModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FaqsViewModel::class)
    internal abstract fun faqsViewModel(viewModel: FaqsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FAQsFragmentViewModel::class)
    internal abstract fun faqsFragmentViewModel(viewModel: FAQsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginFragmentViewModel::class)
    internal abstract fun loginFragmentViewModel(viewModel: LoginFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun loginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginOtpFragmentViewModel::class)
    internal abstract fun loginOtpFragmentViewModel(viewModel: LoginOtpFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordViewModel::class)
    internal abstract fun changePasswordViewModel(viewModel: ChangePasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordFragmentViewModel::class)
    internal abstract fun changePasswordFragmentViewModel(viewModel: ChangePasswordFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(SelectCountryFragmentViewModel::class)
    internal abstract fun selectCountryFragmentViewModel(viewModel: SelectCountryFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectCityFragmentViewModel::class)
    internal abstract fun selectCityFragmentViewModel(viewModel: SelectCityFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(BeneficiaryViewModel::class)
    internal abstract fun beneficiaryViewModel(viewModel: BeneficiaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun registerViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterAccountFragmentViewModel::class)
    internal abstract fun registerAccountFragmentViewModel(viewModel: RegisterAccountFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyRemitterFragmentViewModel::class)
    internal abstract fun verifyRemitterFragmentViewModel(viewModel: VerifyRemitterFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VerifyBeneficiaryFragmentViewModel::class)
    internal abstract fun verifyBeneficiaryFragmentViewModel(viewModel: VerifyBeneficiaryFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(OtpAuthenticationFragmentViewModel::class)
    internal abstract fun otpAuthenticationFragmentViewModel(viewModel: OtpAuthenticationFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TermsAndConditionsViewModel::class)
    internal abstract fun termsAndConditionsViewModel(viewModel: TermsAndConditionsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterSuccessViewModel::class)
    internal abstract fun registerSuccessViewModel(viewModel: RegisterSuccessViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ChangePasswordSuccessViewModel::class)
    internal abstract fun changePasswordSuccessViewModel(viewModel: ChangePasswordSuccessViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(ManageBeneficiaryViewModel::class)
    internal abstract fun manageBeneficiaryViewModel(viewModel: ManageBeneficiaryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BeneficiaryDetailsViewModel::class)
    internal abstract fun beneficiaryDetailsViewModel(viewModel: BeneficiaryDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    internal abstract fun homeActivityViewModel(viewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeFragmentViewModel::class)
    internal abstract fun homeFragmentViewModel(viewModel: HomeFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ViewStatementViewModel::class)
    internal abstract fun viewStatementViewModel(viewModel: ViewStatementViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AdvancedLoyaltyStatementFragmentViewModel::class)
    internal abstract fun advancedloyaltyStatementFragmentViewModel(viewModelAdvanced: AdvancedLoyaltyStatementFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoyaltyStatementFragmentViewModel::class)
    internal abstract fun loyaltyStatementFragmentViewModel(viewModelAdvanced: LoyaltyStatementFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordViewModel::class)
    internal abstract fun forgotPasswordViewModel(viewModel: ForgotPasswordViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordFragmentViewModel::class)
    internal abstract fun forgotPasswordFragmentViewModel(viewModel: ForgotPasswordFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ForgotPasswordOTPFragmentViewModel::class)
    internal abstract fun forgotPasswordOTPFragmentViewModel(viewModel: ForgotPasswordOTPFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpdatePasswordFragmentViewModel::class)
    internal abstract fun updatePasswordFragmentViewModel(viewModel: UpdatePasswordFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PasswordChangeSuccessViewModel::class)
    internal abstract fun passwordChangeSuccessViewModel(viewModel: PasswordChangeSuccessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManagePointsViewModel::class)
    internal abstract fun managePointsViewModel(managePointsViewModel: ManagePointsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ManagePointsFragmentViewModel::class)
    internal abstract fun managePointsFragmentViewModel(managePointsFragmentViewMode: ManagePointsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelfAwardPointsViewModel::class)
    internal abstract fun selfAwardPointsViewModel(selfAwardPointsViewModel: SelfAwardPointsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelfAwardPointsSuccessViewModel::class)
    internal abstract fun selfAwardPointsSuccessViewModel(selfAwardPointsSuccessViewModel: SelfAwardPointsSuccessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelfAwardPointsFragmentViewModel::class)
    internal abstract fun selfAwardPointsFragmentViewModel(selfAwardPointsFragmentViewModel: SelfAwardPointsFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelfAwardPointsOTPFragmentViewModel::class)
    internal abstract fun selfAwardPointsOTPFragmentViewModel(selfAwardPointsOTPFragmentViewModel: SelfAwardPointsOTPFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(TransferPointsSuccessFulViewModel::class)
    internal abstract fun transferPointsSuccessFulViewModel(transferPointsSuccessFulViewModel: TransferPointsSuccessFulViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun profileViewModel(viewModelAdvanced: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileViewModel::class)
    internal abstract fun editProfileViewModel(viewModelAdvanced: EditProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditProfileOtpFragmentViewModel::class)
    internal abstract fun editProfileOtpFragmentViewModel(viewModelAdvanced: EditProfileOtpFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileUpdateSuccessViewModel::class)
    internal abstract fun profileUpdateSuccessViewModel(viewModelAdvanced: ProfileUpdateSuccessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedeemActivityViewModel::class)
    internal abstract fun redeemActivityViewModel(viewModelAdvanced: RedeemActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CompleteRedemptionViewModel::class)
    internal abstract fun completeRedemptionViewModel(viewModelAdvanced: CompleteRedemptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemtionFragmentPartnerViewModel::class)
    internal abstract fun redemFragmentPartnerViewModel(viewModelAdvanced: RedemtionFragmentPartnerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionFBRDescriptionViewModel::class)
    internal abstract fun redemFBRDescriptionViewModel(viewModelAdvanced: RedemptionFBRDescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionFBRPSIDViewModel::class)
    internal abstract fun redemFBRPSIDViewModel(viewModelAdvanced: RedemptionFBRPSIDViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionPIADescriptionViewModel::class)
    internal abstract fun redemPIADescriptionViewModel(viewModelAdvanced: RedemptionPIADescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionPIAPSIDViewModel::class)
    internal abstract fun redemPIAPSIDViewModel(viewModelAdvanced: RedemptionPIAPSIDViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionNadraDescriptionViewModel::class)
    internal abstract fun redemNadraDescriptionViewModel(viewModelAdvanced: RedemptionNadraDescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionNadraTrackingIDViewModel::class)
    internal abstract fun redemNadraTrackingIDViewModel(viewModelAdvanced: RedemptionNadraTrackingIDViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionUSCDescriptionViewModel::class)
    internal abstract fun redemUSCDescriptionViewModel(viewModelAdvanced: RedemptionUSCDescriptionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionUSCPSIDViewModel::class)
    internal abstract fun redemUSCPSIDViewModel(viewModelAdvanced: RedemptionUSCPSIDViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionOPFVoucherViewModel::class)
    internal abstract fun redemOPFVoucherViewModel(viewModelAdvanced: RedemptionOPFVoucherViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionSLICPolicyViewModel::class)
    internal abstract fun redemSLICPolicyViewModel(viewModelAdvanced: RedemptionSLICPolicyViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionBEOECNICViewModel::class)
    internal abstract fun redemBEOECNICViewModel(viewModelAdvanced: RedemptionBEOECNICViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedeemOtpFragmentViewModel::class)
    internal abstract fun redeemOtpFragmentViewModel(viewModelAdvanced: RedeemOtpFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedeemAgentConfirmationViewModel::class)
    internal abstract fun redeemAgentConfirmationViewModel(viewModelAdvanced: RedeemAgentConfirmationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedeemSuccessViewModel::class)
    internal abstract fun redeemSuccessViewModel(viewModelAdvanced: RedeemSuccessViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RedemptionFragmentPartnerServiceViewModel::class)
    internal abstract fun redeemPartnerServiceViewModel(viewModelAdvanced: RedemptionFragmentPartnerServiceViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UUIDActivityViewModel::class)
    internal abstract fun uuidActivityViewModel(viewModelAdvanced: UUIDActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactUsActivityViewModel::class)
    internal abstract fun contactUsActivityViewModel(viewModelAdvanced: ContactUsActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageFragmentViewModel::class)
    internal abstract fun languageFragmentViewModel(viewModel: LanguageFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LanguageActivityViewModel::class)
    internal abstract fun languageActivityViewModel(viewModel: LanguageActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NrlpBenefitsViewModel::class)
    internal abstract fun nrlpBenefitsViewModel(viewModel: NrlpBenefitsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NrlpPartnersFragmentViewModel::class)
    internal abstract fun nrlpPartnersFragmentViewModel(viewModel: NrlpPartnersFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NrlpBenefitsFragmentViewModel::class)
    internal abstract fun nrlpBenefitsFragmentViewModel(viewModel: NrlpBenefitsFragmentViewModel): ViewModel
}

