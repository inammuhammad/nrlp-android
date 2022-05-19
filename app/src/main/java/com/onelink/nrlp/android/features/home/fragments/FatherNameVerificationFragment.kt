package com.onelink.nrlp.android.features.home.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentFatherNameVerificationBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.NadraVerificationsSuccessActivity
import com.onelink.nrlp.android.features.home.view.TAG_CONFIRM_LOGOUT_DIALOG
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class FatherNameVerificationFragment : BaseFragment<HomeFragmentViewModel, FragmentFatherNameVerificationBinding>
    (HomeFragmentViewModel::class.java), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getLayoutRes(): Int = R.layout.fragment_father_name_verification

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        (activity as HomeActivity).hideHomeScreenTools()
        initListeners()
        initObservers()
        initTextObservers()
    }

    private fun initListeners() {
        binding.btnNext.setOnSingleClickListener {
            if(viewModel.validateFatherName(binding.etFatherName.text.toString()))
                viewModel.verifyFatherName(binding.etFatherName.text.toString())
        }
        binding.btnCancel.setOnSingleClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun initObservers() {
        viewModel.observeVerifyFatherName().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    activity?.let { NadraVerificationsSuccessActivity.start(it) }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(context)
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let { showGeneralErrorDialog(this, it) }
                }
            }
        })
    }

    private fun initTextObservers() {
        viewModel.isFullNameValidationPassed.observe(this, { validationsPassed ->
            if (!validationsPassed) binding.tilFatherName.error =
                resources.getString(R.string.error_not_valid_father_name)
            else {
                binding.tilFatherName.clearError()
                binding.tilFatherName.isErrorEnabled = false
            }
        })
    }

    private fun showLogoutConfirmationDialog() {
        OneLinkAlertDialogsFragment.Builder().setIsAlertOnly(true)
            .setDrawable(R.drawable.ic_error_dialog).setIsAlertOnly(false)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.nadra_confirmation_msg).toSpanned())
            .setNeutralButtonText(getString(R.string.okay))
            .setPositiveButtonText(resources.getString(R.string.yes))
            .setNegativeButtonText(resources.getString(R.string.no)).setCancelable(true)
            .setCancelable(false)
            .show(parentFragmentManager, TAG_CONFIRM_LOGOUT_DIALOG)
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        (activity as HomeActivity).logoutUser()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FatherNameVerificationFragment()
    }
}