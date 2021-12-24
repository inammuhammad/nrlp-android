package com.onelink.nrlp.android.features.register.fragments

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentBeneficiaryVerificationBinding
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.models.VerifyRegistrationCodeRequest
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.register.viewmodel.VerifyBeneficiaryFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class VerifyBeneficiaryFragment :
    BaseFragment<VerifyBeneficiaryFragmentViewModel, FragmentBeneficiaryVerificationBinding>(
        VerifyBeneficiaryFragmentViewModel::class.java
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

    override fun getLayoutRes() = R.layout.fragment_beneficiary_verification

    override fun getTitle(): String = resources.getString(R.string.beneficiary_verification_title)

    override fun getViewM(): VerifyBeneficiaryFragmentViewModel =
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(VerifyBeneficiaryFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
        }
        initObservers()
        initListeners()
        initEditTextListeners()
    }


    private fun initObservers() {

        viewModel.observeVerifyRegistrationCode().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        fragmentHelper.addFragment(
                            TermsAndConditionsFragment.newInstance(),
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

        sharedViewModel?.registerFlowDataModel?.observe(this,
            Observer {
                registerFlowDataModel = it
            })

        viewModel.validEditText1.observe(this, Observer {
            if (it)
                binding.etOTP2.requestFocus()
        })
        viewModel.validEditText2.observe(this, Observer {
            if (it)
                binding.etOTP3.requestFocus()
        })
        viewModel.validEditText3.observe(this, Observer {
            if (it)
                binding.etOTP4.requestFocus()
        })
        viewModel.validEditText4.observe(this, Observer {
            if (it)
                hideKeyboard()
        })
    }

    private fun initListeners() {
        binding.etOTP1.requestFocus()

        binding.btnNext.setOnSingleClickListener {
            sharedViewModel?.setRegisterFlowDataModel(
                RegisterFlowDataModel(
                    fullName = registerFlowDataModel.fullName,
                    cnicNicop = registerFlowDataModel.cnicNicop,
                    phoneNumber = registerFlowDataModel.phoneNumber,
                    email = registerFlowDataModel.email,
                    country = registerFlowDataModel.country,
                    residentId = "-",
                    passportType = "-",
                    passportId = "-",
                    password = registerFlowDataModel.password,
                    rePassword = registerFlowDataModel.rePassword,
                    accountType = registerFlowDataModel.accountType,
                    referenceNumber = "",
                    transactionAmount = "",
                    registrationCode = viewModel.getRegistrationCode(),
                    otpCode = "",
                    motherMaidenName = registerFlowDataModel.motherMaidenName,
                    placeOfBirth =  registerFlowDataModel.placeOfBirth,
                    cnicNicopIssueDate = registerFlowDataModel.cnicNicopIssueDate
                )
            )

            viewModel.verifyRegistrationCode(
                VerifyRegistrationCodeRequest(
                    nicNicop = registerFlowDataModel.cnicNicop,
                    userType = registerFlowDataModel.accountType,
                    registrationCode = registerFlowDataModel.registrationCode
                )
            )
            hideKeyboard()
        }
    }

    private fun initEditTextListeners() {
        binding.etOTP1.setOnKeyListener(onKeyListenerOTP(binding.etOTP1, binding.etOTP1))
        binding.etOTP2.setOnKeyListener(onKeyListenerOTP(binding.etOTP2, binding.etOTP1))
        binding.etOTP3.setOnKeyListener(onKeyListenerOTP(binding.etOTP3, binding.etOTP2))
        binding.etOTP4.setOnKeyListener(onKeyListenerOTP(binding.etOTP4, binding.etOTP3))
    }

    private fun onKeyListenerOTP(etCurrent: EditText, etPrevious: EditText): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL
                && event.action == KeyEvent.ACTION_DOWN
                && etCurrent.hasFocus()
            ) {
                if (etCurrent.text.toString().length == 1) {
                    etCurrent.setText("")
                    etCurrent.requestFocus()
                } else if (etCurrent.text.toString().isEmpty()) {
                    etPrevious.setText("")
                    etPrevious.requestFocus()
                }
                return@OnKeyListener true
            }
            false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = VerifyBeneficiaryFragment()
    }
}
