package com.onelink.nrlp.android.features.register.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentRemitterVerificationBinding
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.models.VerifyReferenceNumberRequest
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.register.viewmodel.VerifyRemitterFragmentViewModel
import com.onelink.nrlp.android.utils.LukaKeRakk
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class VerifyRemitterFragment :
    BaseFragment<VerifyRemitterFragmentViewModel, FragmentRemitterVerificationBinding>(
        VerifyRemitterFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var sharedViewModel: SharedViewModel? = null

    private lateinit var registerFlowDataModel: RegisterFlowDataModel

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_remitter_verification

    override fun getTitle(): String = resources.getString(R.string.remitter_verification_title)

    override fun getViewM(): VerifyRemitterFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(VerifyRemitterFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
        }
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        sharedViewModel?.registerFlowDataModel?.observe(this,
            Observer {
                registerFlowDataModel = it
            })

        viewModel.observeVerifyReferenceNumber().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        fragmentHelper.addFragment(
                            OtpAuthenticationFragment.newInstance(),
                            clearBackStack = false,
                            addToBackStack = true
                        )
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

    private fun initListeners() {
        binding.btnNext.setOnSingleClickListener {
            sharedViewModel?.setRegisterFlowDataModel(
                RegisterFlowDataModel(
                    fullName = registerFlowDataModel.fullName,
                    cnicNicop = registerFlowDataModel.cnicNicop,
                    phoneNumber = registerFlowDataModel.phoneNumber,
                    email = registerFlowDataModel.email,
                    country = registerFlowDataModel.country,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    password = registerFlowDataModel.password,
                    rePassword = registerFlowDataModel.rePassword,
                    accountType = registerFlowDataModel.accountType,
                    referenceNumber = binding.eTReferenceNumber.text.toString(),
                    transactionAmount = binding.etTransactionNumber.text.toString()
                        .replace(",", ""),
                    registrationCode = "",
                    otpCode = "",
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    placeOfBirth =  registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate
                )
            )
            viewModel.verifyReferenceNumber(
                VerifyReferenceNumberRequest(
                    referenceNo = registerFlowDataModel.referenceNumber,
                    nicNicop = registerFlowDataModel.cnicNicop,
                    residentId = registerFlowDataModel.residentId,
                    passportType = registerFlowDataModel.passportType,
                    passportId = registerFlowDataModel.passportId,
                    userType = registerFlowDataModel.accountType,
                    mobileNo = registerFlowDataModel.phoneNumber,
                    amount = registerFlowDataModel.transactionAmount,
                )
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = VerifyRemitterFragment()
    }
}