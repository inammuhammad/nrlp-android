package com.onelink.nrlp.android.features.changePassword.view

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.ChangepasswordFragmentBinding
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ChangePasswordFragment :
    BaseFragment<ChangePasswordFragmentViewModel, ChangepasswordFragmentBinding>(
        ChangePasswordFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.changepassword_fragment
    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle() = resources.getString(R.string.change_password_title)
    override fun getViewM(): ChangePasswordFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(ChangePasswordFragmentViewModel::class.java)


    private fun initOnFocusChangeListeners() {

        binding.etOldPassword.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isOldPasswordValidationPassed.postValue(
                    viewModel.checkPassValidation(
                        binding.etOldPassword.text.toString()
                    )
                )
            }
        }

        binding.etNewPassword.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isPasswordValidationPassed.postValue(
                    viewModel.checkNewPassValidation(
                        binding.etNewPassword.text.toString()
                    )
                )
            }
        }
        binding.etReEnterPassword.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isRePasswordValidationPassed.postValue(
                    viewModel.checkRePassValidation(
                        binding.etNewPassword.text.toString(),
                        binding.etReEnterPassword.text.toString()
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.isPasswordValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed) binding.tilNewPassword.error =
                    resources.getString(R.string.enter_valid_password)
                else {
                    binding.tilNewPassword.clearError()
                    binding.tilNewPassword.isErrorEnabled = false
                }
            }
        })

        viewModel.isRePasswordValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed) binding.tilReEnterPassword.error =
                    resources.getString(R.string.error_password_not_match)
                else {
                    binding.tilReEnterPassword.clearError()
                    binding.tilReEnterPassword.isErrorEnabled = false
                }
            }
        })

        viewModel.isOldPasswordValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed) binding.tilOldPassword.error =
                    resources.getString(R.string.old_password_error)
                else {
                    binding.tilOldPassword.clearError()
                    binding.tilOldPassword.isErrorEnabled = false
                }
            }
        })
    }

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        initOnFocusChangeListeners()
        initObservers()
        initListeners()

    }

    private fun initListeners() {
        binding.btnsave.setOnClickListener {
            viewModel.changePasswordCall(
                binding.etOldPassword.text.toString(),
                binding.etNewPassword.text.toString(),
                binding.etReEnterPassword.text.toString()
            )
        }

        viewModel.observerChangePassword().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    activity?.let {
                        it.startActivity(ChangePassSuccessfulActivity.newRegisterSuccessIntent(it))
                        it.finish()
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChangePasswordFragment()
    }
}