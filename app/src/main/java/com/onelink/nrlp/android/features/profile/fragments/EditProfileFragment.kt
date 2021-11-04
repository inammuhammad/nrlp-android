package com.onelink.nrlp.android.features.profile.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.data.local.UserModel
import com.onelink.nrlp.android.databinding.FragmentEditProfileBinding
import com.onelink.nrlp.android.features.profile.disabled
import com.onelink.nrlp.android.features.profile.enabled
import com.onelink.nrlp.android.features.profile.viewmodel.EditProfileViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.ProfileSharedViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

const val MOBILE_UPDATE_DIALOG = 4002
const val TAG_MOBILE_UPDATE_DIALOG = "mobile_update_dialog"

class EditProfileFragment :
    BaseFragment<EditProfileViewModel, FragmentEditProfileBinding>(
        EditProfileViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    SelectCountryFragment.OnSelectCountryListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    private lateinit var profileSharedViewModel: ProfileSharedViewModel
    private lateinit var countriesList: List<CountryCodeModel>
    private var countryCode: String = ""
    private var isDisablingView: Boolean = false
    private var listenerInitializedPT: Boolean = false
    private var isEditEnable: Boolean = false

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_edit_profile

    override fun getTitle() = resources.getString(R.string.profile_title)

    override fun getViewM(): EditProfileViewModel =
        ViewModelProvider(this, viewModelFactory).get(EditProfileViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            profileSharedViewModel = ViewModelProvider(it).get(ProfileSharedViewModel::class.java)
        }
        UserData.getUser()?.accountType?.let { viewModel.getCountryCodes(it) }


       // binding.spinnerSelectPassportType.forceEnabled(false)
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
                        } else {
                            listenerInitializedPT = true
                            binding.spinnerSelectPassportType.setSelection(-1)
                        }
                    }
                }
        initTextWatchers()
        initObservers()
        initListeners()
        initOnFocusChangeListeners()
    }

    private fun AppCompatSpinner?.setFEnabled(enable:Boolean) {
        if (this != null) {
            isEnabled = enable
            alpha = if (enable) 1.0f else 0.5f
        }
    }

    fun AppCompatSpinner.forceEnabled(isEnabled : Boolean){
        setEnabled(isEnabled)
        getChildAt(0)?.let{ childView ->
            childView.alpha = if (this.isEnabled) 1.0f else 0.33f
        }
        invalidate()
    }

    private fun initOnFocusChangeListeners() {
        binding.etEmailAddress.setOnFocusChangeListener { _, b ->
            when (b && !isDisablingView) {
                false -> viewModel.validationEmailPassed.postValue(
                    viewModel.checkEmailValidation(
                        binding.etEmailAddress.text.toString()
                    )
                )
            }
        }

        binding.etMobileNumber.setOnFocusChangeListener { _, b ->
            when (b && !isDisablingView) {
                false -> viewModel.validationPhoneNumberPassed.postValue(
                    viewModel.checkPhoneNumberValidation(
                        binding.etMobileNumber.text.toString(),
                        viewModel.mobileNumberLength.value
                    )
                )
            }
        }
    }


    private fun initListeners() {
        binding.tvCountry.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideKeyboard()
        }

        binding.tvCountry.setOnClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.btnCancel.setOnClickListener {
            makeViewNonEditable()
        }

        binding.btnSave.setOnSingleClickListener {
            if (viewModel.validationsPassed(
                    email = binding.etEmailAddress.text.toString(),
                    phoneNumber = binding.etMobileNumber.text.toString(),
                    phoneNumberLength = viewModel.mobileNumberLength.value)) {
                showConfirmationDialog()
            }
        }

        binding.spinnerPassport.setOnClickListener {
            binding.spinnerSelectPassportType.performClick()
        }

        binding.btnNext.setOnSingleClickListener {
            makeViewEditable()
        }

        binding.btnNext1.setOnSingleClickListener {
            makeViewEditable()
        }
    }

    private fun initObservers() {
        viewModel.observerCountryCodes().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        countriesList = it.countryCodesList
                        UserData.getUser()?.let { userModel ->

                            if(userModel.accountType == "beneficiary"){
                                binding.remitterItemContainer.visibility = View.GONE

                                binding.btnNext.visibility = View.GONE
                                binding.btnNext1.visibility = View.VISIBLE
                            }
                            else{
                                binding.remitterItemContainer.visibility = View.VISIBLE

                                binding.btnNext.visibility = View.VISIBLE
                                binding.btnNext1.visibility = View.GONE
                            }
                            showData(userModel)
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

        viewModel.passportType.observe(this, Observer {
            if (it != Constants.SPINNER_PASSPORT_TYPE_HINT) {
                if(!isEditEnable) {
                    binding.tvPassportType.text = it
                    binding.tvPassportType.colorToText(R.color.nonEditableText)
                }else {
                    binding.tvPassportType.text = it
                    binding.tvPassportType.colorToText(R.color.pure_black)
                }
            }
        })

        viewModel.observeUpdateProfile().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    profileSharedViewModel.updateProfileRequestModel.postValue(viewModel.getUpdateProfileRequestObject())
                    fragmentHelper.addFragment(
                        EditProfileOtpAuthentication.newInstance(),
                        clearBackStack = false, addToBackStack = true
                    )
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

        viewModel.mobileNumberLength.observe(this, Observer {
            setEditTextLengthFilter(it)
        })

        viewModel.countryNotEmpty.observe(this,
            Observer {
                binding.etMobileNumber.isClickable = it
            })

        viewModel.validationPhoneNumberPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed) {
                    showPhoneNumberValidationError()
                } else {
                    hidePhoneNumberValidationError()
                }
            }
        })

        viewModel.validationEmailPassed.observe(this, Observer { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilEmail.error = resources.getString(R.string.error_email_not_valid)
                else {
                    binding.tilEmail.clearError()
                    binding.tilEmail.isErrorEnabled = false
                }
            }
        })
    }


    private fun hidePhoneNumberValidationError() {
        binding.imageViewPhoneError.visibility = View.GONE
        binding.errorTextPhone.visibility = View.GONE
        binding.lyPhoneNumber.setBackgroundDrawable(R.drawable.edit_text_background)
    }

    private fun showPhoneNumberValidationError() {
        binding.imageViewPhoneError.visibility = View.VISIBLE
        binding.errorTextPhone.visibility = View.VISIBLE
        binding.lyPhoneNumber.setBackgroundDrawable(R.drawable.edit_text_error_background)
        binding.errorTextPhone.text =
            getString(R.string.error_mobile_num_not_valid)
    }

    private fun makeViewEditable() {
        enableFields()
    }

    private fun makeViewNonEditable() {
        disableFields()
        clearAllValidationErrors()
    }

    private fun clearAllValidationErrors() {
        binding.tilCnicNicop.clearError()
        binding.tilFullName.clearError()
        hidePhoneNumberValidationError()
    }

    private fun enableFields() {
        context?.let {
            binding.tvCountry.enabled(it)
            binding.etMobileNumber.enabled(it)
            binding.etEmailAddress.enabled(it)
            binding.etPassportNo.enabled(it)
            binding.etResidentId.enabled(it)
            binding.spinnerPassport.enabled()
        }

        //setting mobileNumber without code for editing
        viewModel.mobileNumber.value = viewModel.oldMobileNumber.value
        viewModel.mobileNumberLength.postValue(viewModel.oldMobileNumber.value?.length)

        binding.btnCancel.visibility = View.VISIBLE
        binding.tvCountryCode.visibility = View.VISIBLE
        binding.prefixTv.visibility = View.VISIBLE
        binding.btnNext.visibility = View.GONE
        binding.btnNext1.visibility = View.GONE
        binding.btnSave.visibility = View.VISIBLE
        binding.prefixTv.visibility = View.VISIBLE

        binding.tvPassportType.colorToText(R.color.pure_black)

        isEditEnable = true

    }

    private fun disableFields() {
        //Disabling EditTexts
        context?.let {
            binding.tvCountry.disabled(it)
            binding.etMobileNumber.disabled(it)
            binding.etEmailAddress.disabled(it)
            binding.etPassportNo.disabled(it)
            binding.etResidentId.disabled(it)
            binding.spinnerPassport.disabled()

        }
        binding.btnCancel.visibility = View.GONE
        UserData.getUser()?.let { userModel ->
            if(userModel.accountType == "beneficiary")
                binding.btnNext1.visibility = View.VISIBLE
            else
                binding.btnNext.visibility = View.VISIBLE
        }
        binding.btnSave.visibility = View.GONE
        binding.tvCountryCode.visibility = View.GONE
        binding.prefixTv.visibility = View.GONE

        binding.tvPassportType.colorToText(R.color.grey)

        isEditEnable = false

        //Setting old values for Email and Mobile Number
        viewModel.mobileNumFromApi.value?.length?.let { setEditTextLengthFilter(it) }
        viewModel.mobileNumber.value = viewModel.mobileNumFromApi.value
        viewModel.email.value = viewModel.oldEmail.value
        viewModel.residentId.value = viewModel.oldResidentID.value
        viewModel.passportType.value = viewModel.oldPassportType.value
        viewModel.passportId.value = viewModel.oldPassportId.value
        viewModel.country.value = viewModel.countryFromApi.value
        viewModel.countryCode.value = viewModel.countryCodeFromApi.value
    }

    private fun showData(it: UserModel) {
        populateFields(it)
        if (countriesList.isNotEmpty())
            setCountryDetails()
        makeViewNonEditable()
    }

    private fun populateFields(it: UserModel) {
        viewModel.alias.value = it.fullName
        viewModel.cnicNumber.value = it.cnicNicop.toString().formattedCnicNumberNoSpaces()
        val mobileNo = it.mobileNo
        val email = it.email
        viewModel.mobileNumber.value = mobileNo
        viewModel.mobileNumFromApi.value = mobileNo

        val residentId = it.residentId
        val passportType = it.passportType
        val passportId = it.passportId

//        viewModel.residentId.value = it.residentId
//        viewModel.passportType.value = it.passportType
//        viewModel.passportId.value = it.passportId
        if (email != "null" && !email.isNullOrEmpty()) {
            viewModel.email.value = email
            viewModel.oldEmail.value = email
        }

        if (residentId != "null" && !residentId.isNullOrEmpty()) {
            viewModel.residentId.value = residentId
            viewModel.oldResidentID.value = residentId
        }

        if (passportId != "null" && !passportId.isNullOrEmpty()) {
            viewModel.passportId.value = passportId
            viewModel.oldPassportId.value = passportId
        }


        if (passportType != "null" && !passportType.isNullOrEmpty()) {
            viewModel.passportType.value = passportType
            viewModel.oldPassportType.value = passportType
        }


        viewModel.mobileNumberNotEmpty.value = true
        viewModel.countryNotEmpty.value = true
        viewModel.validationPhoneNumberPassed.value = true
    }

    private fun setCountryDetails() {
        var countryCodeModel = countriesList[0]
        countryCode =
            getString(R.string.country_code_prefix) + viewModel.mobileNumber.value?.getCountryCode()
        val countryIndex = getCountryIndex(countryCode.removePlusCharacter())
        try {
            countryCodeModel = countriesList[countryIndex]
        }catch(e: Exception){
            countryCodeModel = countriesList[0]
        }
        viewModel.countryFromApi.value = countryCodeModel.country
        viewModel.country.value = countryCodeModel.country
        viewModel.countryCode.value = countryCodeModel.code
        viewModel.countryCodeFromApi.value = countryCodeModel.code
        viewModel.oldMobileNumber.value =
            viewModel.mobileNumber.value?.substring(countryCode.length) ?: ""
    }

    private fun showConfirmationDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_cancel_register_dialog,
            getString(R.string.update_profile),
            (getString(R.string.confirm_profile_update_msg)).toSpanned(),
            positiveButtonText = getString(R.string.yes),
            negativeButtonText = getString(R.string.no)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            MOBILE_UPDATE_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_MOBILE_UPDATE_DIALOG)
    }


    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            MOBILE_UPDATE_DIALOG -> {
                if (viewModel.validationsPassed(
                        email = binding.etEmailAddress.text.toString(),
                        phoneNumber = binding.etMobileNumber.text.toString(),
                        phoneNumberLength = viewModel.mobileNumberLength.value
                    )
                ) {
                    viewModel.mobileNumUpdated.value =
                        viewModel.countryCode.value + viewModel.mobileNumber.value
                    viewModel.updateProfile()
                }
            }
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCode = countryCodeModel.code
        binding.tvCountryCode.visibility = View.VISIBLE
        viewModel.mobileNumberLength.postValue(countryCodeModel.length.toInt())
        viewModel.countryCode.value = countryCodeModel.code
        viewModel.country.value = countryCodeModel.country
        binding.etMobileNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        binding.tvCountry.colorToText(R.color.pure_black)
        fragmentHelper.onBack()
        binding.etMobileNumber.setText("")
    }

    private fun setEditTextLengthFilter(length: Int) {
        binding.etMobileNumber.filters =
            arrayOf(InputFilter.LengthFilter(length))
    }

    private fun getCountryIndex(countryCode: String?): Int {
        var count = 0
        for (d in countriesList) {
            if (d.code.removePlusCharacter() == countryCode) break
            else count++
        }
        return count
    }

    private fun initTextWatchers() {
        binding.eTCnicNumber.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    binding.eTCnicNumber.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12)
                                +
                                inputString.substring(12, 13)
                    )
                    binding.eTCnicNumber.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    binding.eTCnicNumber.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    binding.eTCnicNumber.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    binding.eTCnicNumber.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(binding.eTCnicNumber.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    binding.eTCnicNumber.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.eTCnicNumber.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // before text change
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.eTCnicNumber.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = binding.eTCnicNumber.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    when {
                        Pattern.matches(regex1, inputString)
                                || Pattern.matches(regex2, inputString) -> {
                            binding.eTCnicNumber.setText("$inputString-")
                            binding.eTCnicNumber.setSelection(inputString.length + 1)
                        }
                        Pattern.matches(regex3, inputString) -> {
                            binding.eTCnicNumber.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            binding.eTCnicNumber.setSelection(inputString.length + 1)
                        }
                    }
                }
                binding.eTCnicNumber.addTextChangedListener(this)
            }
        })

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            EditProfileFragment()
    }

}