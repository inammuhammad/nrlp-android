package com.onelink.nrlp.android.features.forgotPassword.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseError
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentForgotPasswordBinding
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordFlowDataModel
import com.onelink.nrlp.android.features.forgotPassword.models.ForgotPasswordRequestModel
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.ForgotPasswordFragmentViewModel
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.ForgotPasswordSharedViewModel
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.cleanNicNumber
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


class ForgotPasswordFragment :
    BaseFragment<ForgotPasswordFragmentViewModel, FragmentForgotPasswordBinding>(
        ForgotPasswordFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var listenerInitialized: Boolean = false

    private var forgotPasswordSharedViewModel: ForgotPasswordSharedViewModel? = null

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_forgot_password

    override fun getTitle() = resources.getString(R.string.forgot_password_title)

    override fun getViewM(): ForgotPasswordFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(ForgotPasswordFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            forgotPasswordSharedViewModel =
                ViewModelProvider(it).get(ForgotPasswordSharedViewModel::class.java)
        }

        initSpinner()
        initListeners()
        initObservers()
        initTextWatchers()
        initOnFocusChangeListeners()

    }

    private fun initOnFocusChangeListeners() {
        binding.etCnicNicop.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isCnicValidationPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etCnicNicop.text.toString()
                    )
                )
            }
        }
    }

    private fun initTextWatchers() {
        binding.etCnicNicop.addTextChangedListener(object : TextWatcher {

            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    binding.etCnicNicop.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12)
                                +
                                inputString.substring(12, 13)
                    )
                    binding.etCnicNicop.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    binding.etCnicNicop.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    binding.etCnicNicop.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    binding.etCnicNicop.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(binding.etCnicNicop.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    binding.etCnicNicop.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.etCnicNicop.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.etCnicNicop.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = binding.etCnicNicop.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    viewModel.cnicNotEmpty.postValue(true)
                    when {
                        Pattern.matches(regex1, inputString) ||
                                Pattern.matches(regex2, inputString) -> {
                            binding.etCnicNicop.setText("$inputString-")
                            binding.etCnicNicop.setSelection(inputString.length + 1)
                        }
                        Pattern.matches(regex3, inputString) -> {
                            binding.etCnicNicop.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            binding.etCnicNicop.setSelection(inputString.length + 1)
                        }
                    }
                }
                binding.etCnicNicop.addTextChangedListener(this)
            }
        })
    }

    private fun initObservers() {

        viewModel.observeAuthKey().observe(this, { response ->
            when (response.status) {

                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        if(it.data.key == "" || it.data.key.isEmpty())
                            showGeneralErrorDialog(this, BaseError("", resources.getString(R.string.something_went_wrong)))
                        else {
                            viewModel.forgotPassword(
                                ForgotPasswordRequestModel(
                                    nicNicop = binding.etCnicNicop.text.toString().cleanNicNumber(),
                                    userType = viewModel.getAccountType(resources)
                                        .toLowerCase(Locale.getDefault())
                                )
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



        viewModel.observeForgotPasswordResponse().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data.let {
                        fragmentHelper.addFragment(
                            ForgotPasswordOTPFragment.newInstance(),
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

        viewModel.accountType.observe(this,
            Observer {
                if (it != Constants.SPINNER_ACCOUNT_TYPE_HINT) {
                    binding.tvAccountType.text = it
                    binding.tvAccountType.colorToText(R.color.pure_black)
                }
            })

        viewModel.isCnicValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilCnicNicop.error = resources.getString(R.string.error_cnic)
                else {
                    binding.tilCnicNicop.clearError()
                    binding.tilCnicNicop.isErrorEnabled = false
                }
            }
        })
    }

    private fun initListeners() {
        binding.spinnerLy.setOnClickListener {
            binding.etCnicNicop.clearFocus()
            binding.spinnerLy.requestFocus()
            binding.spinnerSelectAccountType.performClick()
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.validationsPassed(binding.etCnicNicop.text.toString())) {
                forgotPasswordSharedViewModel?.setForgotPasswordFlowDataModel(
                    ForgotPasswordFlowDataModel(
                        binding.etCnicNicop.text.toString().cleanNicNumber(),
                        viewModel.getAccountType(resources).toLowerCase(Locale.getDefault())
                    )
                )

                viewModel.getAuthKey(
                    binding.etCnicNicop.text.toString().cleanNicNumber(),
                    viewModel.getAccountType(resources)
                        .toLowerCase(Locale.getDefault())
                )


            }
        }
    }

    private fun initSpinner() {
        binding.spinnerSelectAccountType.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.accountTypes)
            )
        } as SpinnerAdapter
        binding.spinnerSelectAccountType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (listenerInitialized) {
                        viewModel.accountType.postValue(resources.getStringArray(R.array.accountTypes)[position])
                    } else {
                        listenerInitialized = true
                        binding.spinnerSelectAccountType.setSelection(-1)
                    }
                }
            }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ForgotPasswordFragment()
    }
}
