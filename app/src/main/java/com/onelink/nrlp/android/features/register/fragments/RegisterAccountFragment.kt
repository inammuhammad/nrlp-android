package com.onelink.nrlp.android.features.register.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRegisterAccountBinding
import com.onelink.nrlp.android.features.redeem.fragments.REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TAG_REDEMPTION_CREATE_DIALOG_FBR
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.viewmodel.RegisterAccountFragmentViewModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


const val REMITTER_FLOW_SCREENS = 4
const val BENEFICIARY_FLOW_SCREENS = 3

class RegisterAccountFragment :
    BaseFragment<RegisterAccountFragmentViewModel, FragmentRegisterAccountBinding>(
        RegisterAccountFragmentViewModel::class.java
    ), SelectCountryFragment.OnSelectCountryListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    
    private var countryCodeLength: Int? = 10
    private var sharedViewModel: SharedViewModel? = null
    private var listenerInitialized: Boolean = false
    private var listenerInitializedPT: Boolean = false

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_register_account

    override fun getTitle(): String = getString(R.string.register_account_title)

    override fun getViewM(): RegisterAccountFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(RegisterAccountFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)

        }

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
                    // on nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (listenerInitialized) {
                        when (position) {
                            0 ->{ sharedViewModel?.maxProgress?.postValue(REMITTER_FLOW_SCREENS)
                                //showGeneralAlertDialog(this@RegisterAccountFragment,"Register",getString(R.string.register_warning))
                            }
                            1 -> sharedViewModel?.maxProgress?.postValue(BENEFICIARY_FLOW_SCREENS)
                        }
                        viewModel.accountType.postValue(resources.getStringArray(R.array.accountTypes)[position])
                    } else {
                        listenerInitialized = true
                        binding.spinnerSelectAccountType.setSelection(-1)
                    }
                }
            }

        binding.spinnerSelectPassportType.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.passportTypes)
            )
        } as SpinnerAdapter
        binding.spinnerSelectPassportType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // on nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (listenerInitializedPT) {
                        viewModel.passportType.postValue(resources.getStringArray(R.array.passportTypes)[position])
                        binding.passportNoLL.visibility = View.VISIBLE
                    } else {
                        listenerInitializedPT = true
                        binding.spinnerSelectPassportType.setSelection(-1)
                    }
                }
            }

        initObservers()
        initListeners()
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

        binding.etFullName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isFullNameValidationPassed.postValue(
                    viewModel.checkFullNameValidation(
                        binding.etFullName.text.toString()
                    )
                )
            }
        }

        binding.etPhoneNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isPhoneNumberValidationPassed.postValue(
                    viewModel.checkPhoneNumberValidation(
                        binding.etPhoneNumber.text.toString(),
                        countryCodeLength
                    )
                )
            }
        }

        binding.etEmailAddress.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isEmailValidationPassed.postValue(
                    viewModel.checkEmailValidation(
                        binding.etEmailAddress.text.toString()
                    )
                )
            }
        }

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
        binding.etCnicNicop.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
                binding.etCnicNicop.clearFocus()
                true
            } else false
        }

        viewModel.countryNotEmpty.observe(this, Observer {
            binding.etPhoneNumber.isClickable = it
        })

        viewModel.accountType.observe(this, Observer {
            if (it != Constants.SPINNER_ACCOUNT_TYPE_HINT) {
                binding.tvAccountType.text = it
                binding.tvCountry.text = null
                binding.tvCountryCode.text = null
                binding.etPhoneNumber.text = null
                binding.tvAccountType.colorToText(R.color.pure_black)
            }

            if(viewModel.accountType.value.toString() == resources.getString(R.string.beneficiary)){
                binding.residentIdLL.visibility = View.GONE
                binding.passportTypeLL.visibility = View.GONE
                binding.passportNoLL.visibility = View.GONE
                binding.lblMobileNo.text = getString(R.string.beneficiary_mobile_number)

                binding.btnNext.visibility = View.GONE
                binding.btnNext1.visibility = View.VISIBLE
            }
            else{
                binding.residentIdLL.visibility = View.VISIBLE
                binding.passportTypeLL.visibility = View.VISIBLE
                binding.passportNoLL.visibility = View.VISIBLE
                binding.lblMobileNo.text = getString(R.string.mobile_number)

                binding.btnNext.visibility = View.VISIBLE
                binding.btnNext1.visibility = View.GONE
            }
        })

        viewModel.passportType.observe(this, Observer {
            if (it != Constants.SPINNER_PASSPORT_TYPE_HINT) {
                binding.tvPassportType.text = it
                binding.tvPassportType.colorToText(R.color.pure_black)
            }
        })

        viewModel.isCnicValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilCnicNicop.error = getString(R.string.error_cnic)
                else {
                    binding.tilCnicNicop.clearError()
                    binding.tilCnicNicop.isErrorEnabled = false
                }
            }
        })

        viewModel.isPhoneNumberValidationPassed.observe(this, Observer{ validationsPassed ->
            run {
                if (!validationsPassed) {
                    binding.imageViewPhoneError.visibility = View.VISIBLE
                    binding.errorTextPhone.visibility = View.VISIBLE
                    binding.lyPhoneNumber.setBackgroundDrawable(R.drawable.edit_text_error_background)
                    binding.errorTextPhone.text = getString(R.string.error_mobile_num_not_valid)
                } else {
                    binding.imageViewPhoneError.visibility = View.GONE
                    binding.errorTextPhone.visibility = View.GONE
                    binding.lyPhoneNumber.setBackgroundDrawable(R.drawable.edit_text_background)
                }
            }
        })

        viewModel.isPasswordValidationPassed.observe(this, Observer{ validationsPassed ->
            run {
                if (!validationsPassed) {
                    binding.tilPassword.error =
                        getString(R.string.error_not_valid_password)
                    binding.tvPasswordCriteria.visibility = View.GONE
                } else {
                    binding.tvPasswordCriteria.visibility = View.VISIBLE
                    binding.tilPassword.clearError()
                    binding.tilPassword.isErrorEnabled = false
                }
            }
        })

        viewModel.isRePasswordValidationPassed.observe(this, Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilRePassword.error =
                        getString(R.string.error_password_not_match)
                else {
                    binding.tilRePassword.clearError()
                    binding.tilRePassword.isErrorEnabled = false
                }
            }
        })

        viewModel.isFullNameValidationPassed.observe(this, Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilFullName.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilFullName.clearError()
                    binding.tilFullName.isErrorEnabled = false
                }
            }
        })

        viewModel.isEmailValidationPassed.observe(this, Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilEmailAddress.error =
                        getString(R.string.error_email_not_valid)
                else {
                    binding.tilEmailAddress.clearError()
                    binding.tilEmailAddress.isErrorEnabled = false
                }
            }
        })

        viewModel.observeAuthKey().observe(this, Observer{ response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        moveToNextFragment()
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
        binding.etFullName.setCapitalizeTextWatcher()

        binding.icHelpFullName.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_full_name))
        }
        binding.icHelpCountry.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.enter_residence_country))
        }
        binding.icHelpMobile.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_mobile_number))
        }
        binding.icHelpPassportNo.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_passport_no))
        }
        binding.icHelpPassportType.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_passport_type))
        }
        binding.icHelpUniqueId.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_unique_id))
        }
        binding.icHelpPassword.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_password))
        }
        binding.icHelpConfirmPassword.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_confirm_password))
        }
        binding.icHelpUsertype.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_user_type))
        }

        binding.btnNext.setOnClickListener {
            if (viewModel.validationsPassed(
                    binding.etCnicNicop.text.toString(),
                    binding.etFullName.text.toString(),
                    binding.etPhoneNumber.text.toString(),
                    countryCodeLength,
                    binding.etEmailAddress.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etRePassword.text.toString()
                )
            ) {
                viewModel.getAuthKey(
                    viewModel.getAccountType(resources),
                    binding.etCnicNicop.text.toString().replace("-", "")
                )
            }
        }

        binding.btnNext1.setOnClickListener {
            if (viewModel.validationsPassed(
                    binding.etCnicNicop.text.toString(),
                    binding.etFullName.text.toString(),
                    binding.etPhoneNumber.text.toString(),
                    countryCodeLength,
                    binding.etEmailAddress.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etRePassword.text.toString()
                )
            ) {
                viewModel.getAuthKey(
                    viewModel.getAccountType(resources),
                    binding.etCnicNicop.text.toString().replace("-", "")
                )
            }
        }

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
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12) +
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
                        Pattern.matches(regex1, inputString)
                                || Pattern.matches(regex2, inputString) -> {
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

        binding.tvCountry.setOnClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(getUserType()),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.spinnerPassport.setOnClickListener {
            binding.spinnerSelectPassportType.performClick()
        }

        binding.spinnerLy.setOnClickListener {
            binding.spinnerSelectAccountType.performClick()
        }
    }


    fun moveToNextFragment() {

        sharedViewModel?.setRegisterFlowDataModel(
            RegisterFlowDataModel(
                fullName = binding.etFullName.text.toString(),
                cnicNicop = binding.etCnicNicop.text.toString().replace("-", ""),
                phoneNumber = binding.tvCountryCode.text.toString() + binding.etPhoneNumber.text.toString(),
                email = binding.etEmailAddress.text.toString(),
                residentId = binding.etResidentId.text.toString(),
                passportType = viewModel.getPassportType(resources)
                        .toLowerCase(Locale.getDefault()),
                passportId = binding.etPassportNo.text.toString(),
                country = binding.tvCountry.text.toString(),
                password = binding.etPassword.text.toString(),
                rePassword = binding.etRePassword.text.toString(),
                accountType = viewModel.getAccountType(resources)
                    .toLowerCase(Locale.getDefault()),
                referenceNumber = "",
                transactionAmount = "",
                registrationCode = "",
                otpCode = ""
            )
        )
        viewModel.addNextFragment(resources, fragmentHelper)
        hideKeyboard()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegisterAccountFragment()
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCodeLength = countryCodeModel.length.toInt()
        viewModel.country.value = countryCodeModel.country
        binding.tvCountry.colorToText(R.color.black)
        binding.tvCountryCode.text = countryCodeModel.code
        binding.etPhoneNumber.isEnabled = true
        binding.tvCountryCode.colorToText(R.color.black)
        binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        binding.etPhoneNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))
        fragmentHelper.onBack()
       // binding.etPhoneNumber.setText("")
        //binding.etPhoneNumber.requestFocus()
        //showKeyboard()
    }

    private fun getUserType(): String {
        val selectedType = binding.tvAccountType.text.toString()
        if(selectedType.contains(resources.getString(R.string.select_account_type), true))
        {
            return "remitter"
        }
        return selectedType.toLowerCase()
    }
}