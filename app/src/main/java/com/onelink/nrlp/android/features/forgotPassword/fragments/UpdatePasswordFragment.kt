package com.onelink.nrlp.android.features.forgotPassword.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentUpdatePasswordBinding
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordFlowDataModel
import com.onelink.nrlp.android.features.forgotPassword.models.UpdatePasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.view.PasswordChangeSuccessActivity
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.ForgotPasswordSharedViewModel
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.UpdatePasswordFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class UpdatePasswordFragment :
    BaseFragment<UpdatePasswordFragmentViewModel, FragmentUpdatePasswordBinding>(
        UpdatePasswordFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel? = null

    private lateinit var forgotPasswordFlowDataModel: ForgotPasswordFlowDataModel

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_update_password

    override fun getTitle(): String = resources.getString(R.string.forgot_password_title)

    override fun getViewM(): UpdatePasswordFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(UpdatePasswordFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            forgotPasswordSharedViewModel =
                ViewModelProvider(it).get(ForgotPasswordSharedViewModel::class.java)
        }

        initObservers()
        initListeners()
        initOnFocusChangeListeners()
    }

    private fun initOnFocusChangeListeners() {
        binding.etPassword.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isPasswordValidationPassed.postValue(
                    viewModel.checkPassValidation(
                        binding.etPassword.text.toString()
                    )
                )
            }
        }

        binding.etRePassword.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isRePasswordValidationPassed.postValue(
                    viewModel.checkRePassValidation(
                        binding.etPassword.text.toString(),
                        binding.etRePassword.text.toString()
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.observeUpdateForgotPasswordResponse().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        oneLinkProgressDialog.hideProgressDialog()
                        activity?.startActivity(context?.let { context ->
                            PasswordChangeSuccessActivity.newPasswordChangeSuccessIntent(context)
                        })
                        activity?.finish()
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

        forgotPasswordSharedViewModel?.forgotPasswordFlowDataModel?.observe(this,
            Observer {
                forgotPasswordFlowDataModel = it
            })


        viewModel.isPasswordValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilPassword.error = resources.getString(R.string.enter_valid_password)
                else {
                    binding.tilPassword.clearError()
                    binding.tilPassword.isErrorEnabled = false
                }
            }
        })

        viewModel.isRePasswordValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilRePassword.error =
                        resources.getString(R.string.error_password_not_match)
                else {
                    binding.tilRePassword.clearError()
                    binding.tilRePassword.isErrorEnabled = false
                }
            }
        })


    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            if (viewModel.validationsPassed(
                    binding.etPassword.text.toString(),
                    binding.etRePassword.text.toString()
                )
            ) {
                viewModel.updatePassword(
                    UpdatePasswordRequestModel(
                        nicNicop = forgotPasswordFlowDataModel.cnicNicop,
                        userType = forgotPasswordFlowDataModel.accountType,
                        password = binding.etPassword.text.toString()
                    )
                )
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = UpdatePasswordFragment()
    }
}