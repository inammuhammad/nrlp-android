package com.onelink.nrlp.android.features.login.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.ViewModelProvider
import com.guardsquare.dexguard.runtime.detection.RootDetector
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseError
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.LoginFragmentBinding
import com.onelink.nrlp.android.features.forgotPassword.view.ForgotPasswordActivity
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.login.helper.CnicTextHelper
import com.onelink.nrlp.android.features.login.viewmodel.LoginFragmentViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.view.NrlpBenefitsActivity
import com.onelink.nrlp.android.features.register.view.RegisterActivity
import com.onelink.nrlp.android.features.uuid.view.UUIDActivity
import com.onelink.nrlp.android.models.LoginCredentials
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class LoginFragment :
    BaseFragment<LoginFragmentViewModel, LoginFragmentBinding>(LoginFragmentViewModel::class.java),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.login_fragment

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): LoginFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(LoginFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.accountType.postValue(binding.radio1.id)

        dexRootDetect(context)
        if (!AppUtils.isValidInstallation(context)) showInvalidInstallDialog()

        binding.btnLogin.setOnSingleClickListener {

            viewModel.getAuthKey(
                binding.rgSelectAccountType.checkedRadioButtonId,
                binding.etCnic.text.toString(),
                Constants.REMITTER,
                Constants.BENEFICIARY
            )
        }

        binding.tvForgotPassword.setOnSingleClickListener {
            activity?.let {
                it.startActivity(ForgotPasswordActivity.newForgotPasswordIntent(it))
            }
        }

        viewModel.observeAuthKey().observe(this, { response ->
            when (response.status) {

                Status.SUCCESS -> {
                    response.data?.let {
                        if(it.data.key == "" || it.data.key.isEmpty())
                            showGeneralErrorDialog(this, BaseError("", resources.getString(R.string.something_went_wrong)))
                        else {
                            this.viewModel.loginCall(
                                binding.rgSelectAccountType.checkedRadioButtonId,
                                binding.etCnic.text.toString(),
                                binding.etPass.text.toString(),
                                Constants.REMITTER,
                                Constants.BENEFICIARY,
                                UniqueDeviceID.getUniqueId() ?: ""
                            )
                        }
                    }
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })
        val sharedPref = activity?.getSharedPreferences("beneficiarySp", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref?.edit() ?: return
        viewModel.observeLogin().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.no_of_beneficiaries_allowed?.let {
                        editor.putInt("no_of_beneficiaries_allowed",
                            it
                        )
                    }
                    editor.commit()
                    activity?.let {
                        it.startActivity(HomeActivity.newHomeIntent(it))
                        it.finish()
                    }
                }
                Status.ERROR -> {

                    response.error?.let {
                        it.errorCode?.let { errorCode ->

                            when (errorCode) {
                                ErrorCodesConstants.UNVERIFIED_DEVICE -> {

                                    if (!viewModel.isUnverifiedDeviceFlow) {
                                        viewModel.isUnverifiedDeviceFlow = true
                                        this.viewModel.loginCall(
                                            binding.rgSelectAccountType.checkedRadioButtonId,
                                            binding.etCnic.text.toString(),
                                            binding.etPass.text.toString(),
                                            Constants.REMITTER,
                                            Constants.BENEFICIARY,
                                            LukaKeRakk.kctw()
                                        )
                                    } else {
                                        oneLinkProgressDialog.hideProgressDialog()
                                        viewModel.isUnverifiedDeviceFlow = false
                                        verifyDevice()
                                    }
                                }
                                else -> {
                                    viewModel.isUnverifiedDeviceFlow = false
                                    oneLinkProgressDialog.hideProgressDialog()
                                    showGeneralErrorDialog(this, it)
                                }
                            }
                        }
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })

        binding.tvAboutNrlp.setOnSingleClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(UriConstants.ABOUT_NRLP_URL))
            startActivity(browserIntent)
        }
        binding.cvAboutNrlp.setOnSingleClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(UriConstants.ABOUT_NRLP_URL))
            startActivity(browserIntent)
        }
        binding.tvComplaint.setOnSingleClickListener {
            showComingSoonDialog()
        }
        binding.cvComplaintNrlp.setOnSingleClickListener {
            showComingSoonDialog()
        }

        binding.tvSecReg.setOnSingleClickListener {
            activity?.let {
                it.startActivity(RegisterActivity.newRegisterUserIntent(it))
            }
        }
        viewModel.isCnicValidationPassed.observe(this, { validationsPassed ->
            if (!validationsPassed) binding.tilCnic.error =
                resources.getString(R.string.error_cnic)
            else {
                binding.tilCnic.clearError()
                binding.tilCnic.isErrorEnabled = false
            }
        })
        viewModel.isPasswordValidationPassed.observe(this, { validationsPassed ->
            if (!validationsPassed) binding.tilPass.error =
                resources.getString(R.string.error_password_not_match_login)
            else {
                binding.tilPass.clearError()
                binding.tilPass.isErrorEnabled = false
            }
        })
        viewModel.isSelectAccountValidationPassed.observe(this, {
            // not in user for now
        })

        binding.etCnic.setOnFocusChangeListener { _, b ->
            if (!b) viewModel.isCnicValidationPassed.postValue(
                viewModel.checkCnicValidation(
                    binding.etCnic.text.toString()
                )
            )
        }

        binding.etPass.setOnFocusChangeListener { _, b ->
            if (!b) viewModel.isPasswordValidationPassed.postValue(
                viewModel.checkPassValidation(
                    binding.etPass.text.toString()
                )
            )
        }

        binding.etPass.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(ValidationUtils.isPasswordValid(binding.etPass.text.toString())) {
                    binding.tilPass.clearError()
                    binding.tilPass.isErrorEnabled = false
                } else {
                    binding.tilPass.error =  getString(R.string.error_not_valid_password)
                }

            }
        })

        binding.tvBenefitsLink.setOnSingleClickListener {
            startActivity(context?.let { NrlpBenefitsActivity.newViewStatementIntent(it) })
        }
        binding.cvBenefitsNrlp.setOnSingleClickListener {
            startActivity(context?.let { NrlpBenefitsActivity.newViewStatementIntent(it) })
        }

        binding.etCnic.addTextChangedListener(getCnicTextWatcher())
    }

    private fun getCnicTextWatcher() = object : TextWatcher {

        @SuppressLint("SetTextI18n")
        override fun afterTextChanged(s: Editable?) {
            CnicTextHelper.afterTextChange(s, binding)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // not in use for now
        }

        @SuppressLint("SetTextI18n")
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            binding.etCnic.removeTextChangedListener(this)
            CnicTextHelper.onTextChange(s, before, count, binding)
            binding.etCnic.addTextChangedListener(this)
        }
    }

    private fun verifyDevice() {
        activity?.let {
            val intent = UUIDActivity.newUUIDIntent(it)
            intent.putExtra(
                IntentConstants.LOGIN_CREDENTIALS_PARCELABLE,
                LoginCredentials(
                    viewModel.cnicNicopNumber.value.toString(),
                    viewModel.password.value.toString(),
                    viewModel.getUserType(
                        binding.rgSelectAccountType.checkedRadioButtonId,
                        Constants.REMITTER,
                        Constants.BENEFICIARY
                    )
                )
            )
            it.startActivity(intent)
        }
    }

    private fun showInvalidInstallDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this, REQUEST_CODE_INVALID_INSTALLATION)
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_oh_snap)
            .setTitle(getString(R.string.oh_snap))
            .setMessage(getString(R.string.msg_invalid_installation).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager, TAG)
    }

    private fun showComingSoonDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_coming_soon_alert)
            .setTitle(getString(R.string.coming_soon))
            .setMessage((getString(R.string.coming_soon_msg)).toSpanned())
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager, TAG)
    }

    companion object {
        private const val REQUEST_CODE_INVALID_INSTALLATION = 0x123
        private const val TAG = "login.fragment"

        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        super.onNeutralButtonClicked(targetCode)
        when (targetCode) {
            REQUEST_CODE_INVALID_INSTALLATION -> activity?.finishAffinity()
        }
    }

    private fun dexRootDetect(context: Context?){
        RootDetector.checkDeviceRooted(context){ rootOK, returnedValue -> callback(rootOK, returnedValue) }
    }

    fun callback(okValue: Int, returnedValue: Int){
        if (okValue != returnedValue)
            showInvalidInstallDialog()
    }



}
