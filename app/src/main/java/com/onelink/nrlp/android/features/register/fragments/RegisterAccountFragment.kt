package com.onelink.nrlp.android.features.register.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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
import com.onelink.nrlp.android.features.register.view.RegisterActivity
import com.onelink.nrlp.android.features.register.viewmodel.RegisterAccountFragmentViewModel
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.features.viewStatement.fragments.FROM_DATE
import com.onelink.nrlp.android.features.viewStatement.fragments.TO_DATE
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
    ), SelectCountryFragment.OnSelectCountryListener, SelectCityFragment.OnSelectCityListener {

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
                binding.fullNameLL.visibility = View.GONE
                binding.motherMaidenNameLL.visibility = View.GONE
                binding.placeOfBirthLL.visibility = View.GONE
                binding.countryLL.visibility = View.GONE
                binding.placeOfBirthLL.visibility = View.GONE
                binding.cnicIssuanceLL.visibility = View.GONE
                binding.mobileNoLL.visibility = View.GONE
                binding.emailAddressLL.visibility = View.GONE
                binding.passwordLL.visibility = View.GONE
                binding.rePasswordLL.visibility = View.GONE


                binding.btnNext1.visibility = View.VISIBLE
                binding.registrationCodeLL.visibility = View.VISIBLE
            }
            else{
                binding.residentIdLL.visibility = View.VISIBLE
                binding.passportTypeLL.visibility = View.VISIBLE
                binding.passportNoLL.visibility = View.VISIBLE
                binding.lblMobileNo.text = getString(R.string.mobile_number)
                binding.btnNext.visibility = View.VISIBLE
                binding.fullNameLL.visibility = View.VISIBLE
                binding.motherMaidenNameLL.visibility = View.VISIBLE
                binding.placeOfBirthLL.visibility = View.VISIBLE
                binding.countryLL.visibility = View.VISIBLE
                binding.placeOfBirthLL.visibility = View.VISIBLE
                binding.cnicIssuanceLL.visibility = View.VISIBLE
                binding.mobileNoLL.visibility = View.VISIBLE
                binding.emailAddressLL.visibility = View.VISIBLE
                binding.passwordLL.visibility = View.VISIBLE
                binding.rePasswordLL.visibility = View.VISIBLE

                binding.btnNext1.visibility = View.GONE
                binding.registrationCodeLL.visibility = View.GONE
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

        viewModel.isMotherMaidenNameValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilMotherMaidenName.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilMotherMaidenName.clearError()
                    binding.tilMotherMaidenName.isErrorEnabled = false
                }
            }
        })

        /*viewModel.isCnicNicopIssuanceDateValidationPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilCnicNicopIssuanceDate.error = getString(R.string.error_cnic)
                else {
                    binding.tilCnicNicopIssuanceDate.clearError()
                    binding.tilCnicNicopIssuanceDate.isErrorEnabled = false
                }
            }
        })*/

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
                        //moveToNextFragment()
                        viewModel.makeRegisterCall(getRegisterFlowModel())
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

        viewModel.observeRegisterUser().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    moveToNextFragment()
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

        viewModel.validEditText1.observe(this, Observer {
            if (it) binding.etOTP2.requestFocus()
        })
        viewModel.validEditText2.observe(this, Observer {
            if (it) binding.etOTP3.requestFocus()
        })
        viewModel.validEditText3.observe(this, Observer {
            if (it) binding.etOTP4.requestFocus()
        })
        viewModel.validEditText4.observe(this, Observer {
            if (it) hideKeyboard()
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

        binding.btnNext.setOnClickListener {
            if (viewModel.validationsPassed(
                    binding.etCnicNicop.text.toString(),
                    binding.etFullName.text.toString(),
                    binding.etPhoneNumber.text.toString(),
                    countryCodeLength,
                    binding.etEmailAddress.text.toString(),
                    binding.etPassword.text.toString(),
                    binding.etRePassword.text.toString(),
                    binding.etMotherMaidenName.text.toString(),
                    binding.etCnicNicopIssuanceDate.text.toString()
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
                //moveToNextFragment()
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

        binding.tvPlaceOfBirth.setOnClickListener {
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
                registrationCode = getRegistrationCode(),
                otpCode = "",
                motherMaidenName = binding.etMotherMaidenName.text.toString(),
                placeOfBirth =  binding.tvPlaceOfBirth.text.toString(),
                cnicNicopIssueDate = binding.etCnicNicopIssuanceDate.text.toString()
            )
        )
        /*if(getUserType() == resources.getString(R.string.beneficiary).toLowerCase(Locale.ROOT))
            viewModel.addRegisterBeneficiaryFragment(resources, fragmentHelper)
        else
            viewModel.addNextFragment(resources, fragmentHelper)*/
        viewModel.addNextFragment(resources, fragmentHelper)
        hideKeyboard()
    }

    private fun getRegisterFlowModel(): RegisterFlowDataModel{
        return RegisterFlowDataModel(
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
            registrationCode = getRegistrationCode(),
            otpCode = "",
            motherMaidenName = binding.etMotherMaidenName.text.toString(),
            placeOfBirth =  binding.tvPlaceOfBirth.text.toString(),
            cnicNicopIssueDate = binding.etCnicNicopIssuanceDate.text.toString()
        )
    }

    private fun getRegistrationCode(): String{
        return binding.etOTP1.text.toString() + binding.etOTP2.text.toString() +
                binding.etOTP3.text.toString() + binding.etOTP4.text.toString()
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
        /*binding.etPhoneNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
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
            return "remitter"
        }
        return selectedType.toLowerCase()
    }

}