package com.onelink.nrlp.android.features.home.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentNadraVerificationRequiredBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.TAG_CONFIRM_LOGOUT_DIALOG
import com.onelink.nrlp.android.features.register.fragments.OtpAuthenticationFragment
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

const val NADRA_VERIFICATION_REQUIRED_SCREEN = 3

class NadraVerificationRequiredFragment : BaseFragment<HomeFragmentViewModel, FragmentNadraVerificationRequiredBinding>
    (HomeFragmentViewModel::class.java),  OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: SharedViewModel? = null

    override fun getLayoutRes() = R.layout.fragment_nadra_verification_required

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    override fun getTitle(): String = resources.getString(R.string.nadra_verification_title)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
        sharedViewModel?.maxProgress?.postValue(NADRA_VERIFICATION_REQUIRED_SCREEN)
    }

    private fun initListeners() {
        binding.btnCancel.setOnClickListener {
            showLogoutConfirmationDialog()
        }
        binding.btnAccept.setOnClickListener {
            fragmentHelper.addFragment(
                NadraVerificationDetailsFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
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
        fun newInstance() = NadraVerificationRequiredFragment()
    }

}