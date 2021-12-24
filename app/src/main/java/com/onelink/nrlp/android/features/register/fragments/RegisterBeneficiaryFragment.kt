package com.onelink.nrlp.android.features.register.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.SpinnerAdapter
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentRegisterAccountBinding
import com.onelink.nrlp.android.databinding.FragmentRegisterBeneficiaryBinding
import com.onelink.nrlp.android.features.register.models.RegisterFlowDataModel
import com.onelink.nrlp.android.features.register.view.RegisterActivity
import com.onelink.nrlp.android.features.register.viewmodel.RegisterAccountFragmentViewModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setBackgroundDrawable
import com.onelink.nrlp.android.utils.setCapitalizeTextWatcher
import dagger.android.support.AndroidSupportInjection
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class RegisterBeneficiaryFragment : BaseFragment<RegisterAccountFragmentViewModel, FragmentRegisterBeneficiaryBinding>(
    RegisterAccountFragmentViewModel::class.java
), SelectCountryFragment.OnSelectCountryListener, SelectCityFragment.OnSelectCityListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    private var countryCodeLength: Int? = 10
    private var sharedViewModel: SharedViewModel? = null
    private var listenerInitialized: Boolean = false
    private var listenerInitializedPT: Boolean = false
    private var tvCountryClicked: Boolean = false
    private lateinit var registerFlowDataModel: RegisterFlowDataModel

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_register_beneficiary

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
        viewModel.accountType.postValue(resources.getString(R.string.beneficiary))
        initEditTextListeners()

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

        binding.etMotherMaidenName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isMotherMaidenNameValidationPassed.postValue(
                    viewModel.checkMotherNameValidation(
                        binding.etMotherMaidenName.text.toString()
                    )
                )
            }
        }

        binding.etCnicNicopIssuanceDate.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isCnicNicopIssuanceDateValidationPassed.postValue(
                    viewModel.checkCnicDateIssueValid(
                        binding.etCnicNicopIssuanceDate.text.toString()
                    )
                )
            }
        }
    }

    private fun initObservers() {
        sharedViewModel?.registerFlowDataModel?.observe(this,
            androidx.lifecycle.Observer {
                registerFlowDataModel = it
            })

        binding.etCnicNicop.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
                binding.etCnicNicop.clearFocus()
                true
            } else false
        }

        viewModel.countryNotEmpty.observe(this, androidx.lifecycle.Observer {
            binding.etPhoneNumber.isClickable = it
        })

        viewModel.isCnicValidationPassed.observe(this, androidx.lifecycle.Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilCnicNicop.error = getString(R.string.error_cnic)
                else {
                    binding.tilCnicNicop.clearError()
                    binding.tilCnicNicop.isErrorEnabled = false
                }
            }
        })

        viewModel.isPhoneNumberValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
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

        viewModel.isPasswordValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
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

        viewModel.isRePasswordValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
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

        viewModel.isFullNameValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilFullName.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilFullName.clearError()
                    binding.tilFullName.isErrorEnabled = false
                }
            }
        })

        viewModel.isEmailValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
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

        (activity as RegisterActivity).selectedCountry.observe(this, androidx.lifecycle.Observer { countryCodeModel ->
            viewModel.country.value = countryCodeModel.country
            viewModel.placeOfBirth.value = countryCodeModel.country
            binding.tvCountry.colorToText(R.color.black)
            binding.tvCountryCode.text = countryCodeModel.code
            binding.tvPlaceOfBirth.text = countryCodeModel.country
            binding.etPhoneNumber.isEnabled = true
            binding.tvCountryCode.colorToText(R.color.black)
            binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
            /*binding.etPhoneNumber.filters =
                arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
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
        binding.etCnicNicopIssuanceDate.setOnClickListener {
            openDatePickerDialog()
            hideKeyboard()
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
                moveToNextFragment()
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
            tvCountryClicked = true
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(getUserType()),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.tvPlaceOfBirth.setOnClickListener {
            tvCountryClicked = false
            fragmentHelper.addFragment(
                SelectCityFragment.newInstance(getUserType()),
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
                cnicNicop = registerFlowDataModel.cnicNicop,
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
                registrationCode = registerFlowDataModel.registrationCode,
                otpCode = "",
                motherMaidenName = binding.etMotherMaidenName.text.toString(),
                placeOfBirth =  binding.tvPlaceOfBirth.text.toString(),
                cnicNicopIssueDate = binding.etCnicNicopIssuanceDate.text.toString()
            )
        )
        viewModel.addBeneficiaryTermsFragment(resources, fragmentHelper)
        hideKeyboard()
    }

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = activity?.let {
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    c.set(year, monthOfYear, dayOfMonth)
                    viewModel.rawDate =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString()
                    viewModel.rawFromDate.value = viewModel.rawDate
                    viewModel.cnicNicopDateOfIssuance.value =
                        viewModel.getDateInStringFormat(c)
                }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = -2208958096000L
        datePickerDialog?.datePicker?.maxDate = System.currentTimeMillis()
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }


    private fun initEditTextListeners() {
        binding.etOTP1.setOnKeyListener(onKeyListenerOTP(binding.etOTP1, binding.etOTP1))
        binding.etOTP2.setOnKeyListener(onKeyListenerOTP(binding.etOTP2, binding.etOTP1))
        binding.etOTP3.setOnKeyListener(onKeyListenerOTP(binding.etOTP3, binding.etOTP2))
        binding.etOTP4.setOnKeyListener(onKeyListenerOTP(binding.etOTP4, binding.etOTP3))
    }

    private fun onKeyListenerOTP(etCurrent: EditText, etPrevious: EditText): View.OnKeyListener {
        return View.OnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && etCurrent.hasFocus()) {
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
        fun newInstance() = RegisterBeneficiaryFragment()
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

    override fun onSelectCityListener(countryCodeModel: CountryCodeModel) {
        viewModel.placeOfBirth.value = countryCodeModel.country
        binding.tvPlaceOfBirth.colorToText(R.color.black)
        fragmentHelper.onBack()
    }


    private fun getUserType(): String {
        val selectedType = binding.tvAccountType.text.toString()
        if(selectedType.contains(resources.getString(R.string.select_account_type), true))
        {
            return "beneficiary"
        }
        return selectedType.toLowerCase()
    }

}