package com.onelink.nrlp.android.features.register.fragments

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentTermsAndConditionsBinding
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.register.viewmodel.TermsAndConditionsViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.parseHtml
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_terms_and_conditions.*
import javax.inject.Inject

const val CONFIRM_CANCEL_REGISTER_DIALOG = 5000
const val TAG_CONFIRM_CANCEL_REGISTER_DIALOG = "confirm_cancel_register_dialog"

class TermsAndConditionsFragment :
    BaseFragment<TermsAndConditionsViewModel, FragmentTermsAndConditionsBinding>(
        TermsAndConditionsViewModel::class.java
    ),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: SharedViewModel? = null

    private lateinit var registerFlowDataModel: RegisterFlowDataModel

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_terms_and_conditions

    override fun getTitle(): String = resources.getString(R.string.terms_and_conditions)


    override fun getViewM(): TermsAndConditionsViewModel =
        ViewModelProvider(this, viewModelFactory).get(TermsAndConditionsViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
        }
        makeGetTermsAndConditionCall()
        initObservers()
        initListeners()

    }

    private fun initObservers() {
        viewModel.observeTermsAndConditions().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    response.data?.let {
                        oneLinkProgressDialog.hideProgressDialog()
                        tvTermsAndConditions.text = it.termsAndConditions.content.parseHtml()
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

        sharedViewModel?.registerFlowDataModel?.observe(this,
            Observer {
                registerFlowDataModel = it
            })

        viewModel.checked.observe(this, Observer {
            tvAccept.isEnabled = it
        })

        viewModel.observeRegisterUser().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        activity?.finish()
                        sharedViewModel?.startRegisterSuccessActivity?.postValue(true)
                    }
                }
                Status.ERROR -> {
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                    oneLinkProgressDialog.hideProgressDialog()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })
    }


    private fun initListeners() {
        tvAccept.setOnSingleClickListener {
            viewModel.makeRegisterCall(registerFlowDataModel)
        }

        checkboxTermsAndConditions.setOnCheckedChangeListener { _, isChecked ->
            viewModel.checked.postValue(isChecked)
        }

        btnCancel.setOnClickListener {
            showConfirmCancelRegistrationDialog()
        }
    }

    private fun showConfirmCancelRegistrationDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_cancel_register_dialog,
            getString(R.string.cancel_registration),
            (getString(R.string.cancel_registration_confirm_msg)).toSpanned(),
            positiveButtonText = resources.getString(R.string.yes),
            negativeButtonText = resources.getString(R.string.no)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            CONFIRM_CANCEL_REGISTER_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_CONFIRM_CANCEL_REGISTER_DIALOG)
    }

    private fun makeGetTermsAndConditionCall() {
        viewModel.getTermsAndConditions()
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            CONFIRM_CANCEL_REGISTER_DIALOG -> {
                val intent = Intent(activity, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity?.startActivity(intent)
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = TermsAndConditionsFragment()
    }
}