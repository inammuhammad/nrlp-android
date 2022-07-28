package com.onelink.nrlp.android.features.receiver.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentAddRemittanceReceiverBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.TAG_CONFIRM_LOGOUT_DIALOG
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class AddRemittanceReceiverFragment :
    BaseFragment<ReceiverSharedViewModel, FragmentAddRemittanceReceiverBinding>
        (ReceiverSharedViewModel::class.java),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getTitle() = resources.getString(R.string.remittance_receiver_manager)

    override fun getLayoutRes() = R.layout.fragment_add_remittance_receiver

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): ReceiverSharedViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(ReceiverSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        oneLinkProgressDialog.hideProgressDialog()
        binding.btnNext.setOnSingleClickListener {
            fragmentHelper.addFragment(
                ReceiverTypeFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        binding.btnSkip.setOnSingleClickListener {
            //fragmentHelper.onBack()
            showLogoutConfirmationDialog()
        }
    }

    private fun initObservers() {
        viewModel.observeLogout().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS, Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    UserData.emptyUserData()
                    launchLoginActivity()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(context)
                }
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

    private fun launchLoginActivity() {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        viewModel.performLogout()
    }


    companion object {
        @JvmStatic
        fun newInstance() = AddRemittanceReceiverFragment()
    }

}