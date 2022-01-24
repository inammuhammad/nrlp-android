package com.onelink.nrlp.android.features.beneficiary.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentBeneficiaryDetailsBinding
import com.onelink.nrlp.android.features.beneficiary.models.AddBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.DeleteBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.ResendBeneficiaryOtpRequestModel
import com.onelink.nrlp.android.features.beneficiary.models.UpdateBeneficiaryRequestModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiaryDetailsViewModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiarySharedViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.uuid.view.UUIDOtpAuthentication
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.util.regex.Pattern
import javax.inject.Inject

const val BENEFICIARY_CREATION_DIALOG = 3000
const val BENEFICIARY_DELETION_DIALOG = 3001
const val BENEFICIARY_UPDATION_DIALOG=3002
const val BENEFICIARY_RESEND_OTP_DIALOG=3003
const val TAG_BENEFICIARY_DELETION = "beneficiary_deletion_dialog"
const val TAG_BENEFICIARY_CREATION = "beneficiary_creation_dialog"
const val TAG_BENEFICIARY_UPDATION = "beneficiary_updation_dialog"
const val TAG_BENEFICIARY_RESEND_OTP = "beneficiary_otp_resent_dialog"

class BeneficiaryDetailsFragment :
    BaseFragment<BeneficiaryDetailsViewModel, FragmentBeneficiaryDetailsBinding>(
        BeneficiaryDetailsViewModel::class.java
    ),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    SelectCountryFragment.OnSelectCountryListener {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var beneficiarySharedViewModel: BeneficiarySharedViewModel? = null

    private var isDeleteBeneficiary: Boolean = false

    private var countryCodeLength: Int? = 10

    private lateinit var beneficiaryDetailsModel: BeneficiaryDetailsModel

    private lateinit var relation: String

    private var listenerInitializedBR: Boolean = false

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_beneficiary_details

    override fun getTitle() =
        if (isDeleteBeneficiary) resources.getString(R.string.beneficiary_details_title) else resources.getString(
            R.string.add_beneficiary
        )


    override fun getViewM(): BeneficiaryDetailsViewModel =
        ViewModelProvider(this, viewModelFactory).get(BeneficiaryDetailsViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            beneficiarySharedViewModel = ViewModelProvider(it).get(
                BeneficiarySharedViewModel::class.java
            )
        }

        binding.spinnerSelectRelationship.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.benificiaryRelationTypes)
            )
        } as SpinnerAdapter
        binding.spinnerSelectRelationship.onItemSelectedListener =
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
                    if (listenerInitializedBR) {
                        viewModel.beneficiaryRelation.postValue(resources.getStringArray(R.array.benificiaryRelationTypes)[position])
                    } else {
                        listenerInitializedBR = true
                        binding.spinnerSelectRelationship.setSelection(-1)
                    }
                }
            }

        initTextWatchers()
        initObservers()
        initListeners()
        initOnFocusChangeListeners()
    }

    private fun initOnFocusChangeListeners() {
        binding.etAlias.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
                binding.etAlias.clearFocus()
                true
            } else false
        }

        binding.eTCnicNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationCnicPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.eTCnicNumber.text.toString()
                    )
                )
            }
        }

        binding.etAlias.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationAliasPassed.postValue(
                    viewModel.checkAliasValidation(
                        binding.etAlias.text.toString()
                    )
                )
            }
        }

        binding.etMobileNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationPhoneNumberPassed.postValue(
                    viewModel.checkPhoneNumberValidation(
                        binding.etMobileNumber.text.toString(),
                        countryCodeLength
                    )
                )
            }
        }
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
                        Pattern.matches(regex1, inputString) ||
                                Pattern.matches(regex2, inputString) -> {
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

    private fun initListeners() {
        binding.etAlias.setCapitalizeTextWatcher()

        binding.etCountry.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) hideKeyboard()
        }

        binding.etCountry.setOnClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.btnNext.setOnSingleClickListener {
            if (isDeleteBeneficiary)
                showConfirmBeneficiaryDeletionDialog(beneficiaryDetailsModel)
            else {
                if (viewModel.validationsPassed(
                        binding.eTCnicNumber.text.toString(),
                        binding.etAlias.text.toString(),
                        binding.etMobileNumber.text.toString(),
                        countryCodeLength
                    )
                ) {
                    makeBeneficiaryAddCall()
                }
            }
        }
        binding.spinnerRelationShip.setOnClickListener {
            binding.spinnerSelectRelationship.performClick()
        }
        binding.btnEdit.setOnSingleClickListener {
            enableEdit(beneficiaryDetailsModel)
        }
        binding.btnCancel.setOnSingleClickListener {
            makeDeleteBeneficiaryView(beneficiaryDetailsModel)
        }
        binding.btnResendOtp.setOnSingleClickListener {
            makeResendOtp()
        }
        binding.btnUpdate.setOnSingleClickListener {
            updateBeneficiary()
        }
    }

    private fun makeBeneficiaryAddCall() {
        if(viewModel.beneficiaryRelation.value.toString() == "Other") {
            relation = binding.txtOther.text.toString()
        } else {
            relation = viewModel.beneficiaryRelation.value.toString()
        }
        viewModel.addBeneficiary(
            AddBeneficiaryRequestModel(
                beneficiaryNicNicop = binding.eTCnicNumber.text.toString().replace("-", ""),
                beneficiaryAlias = binding.etAlias.text.toString(),
                beneficiaryMobileNo = binding.tvCountryCode.text.toString() + binding.etMobileNumber.text.toString(),
                beneficiaryRelation = relation,
                country = binding.etCountry.text.toString()
            )
        )
    }

    private fun initObservers() {
        beneficiarySharedViewModel?.isDeleteBeneficiary?.observe(this, { isDeleteBeneficiary ->
            if (isDeleteBeneficiary) {
                beneficiarySharedViewModel?.beneficiaryDetails?.observe(this, {
                    beneficiaryDetailsModel = it
                    makeDeleteBeneficiaryView(beneficiaryDetailsModel)
                })
            }
        })

        viewModel.observeBeneficiaryAddResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let { showBeneficiaryCreatedDialog() }
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let { showGeneralErrorDialog(this, it) }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })

        viewModel.observeBeneficiaryDeleteResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        fragmentHelper.onBack()
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

        viewModel.observeBeneficiaryResendOtp().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    resentBeneficiaryOTPSuccessDialog()
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

        viewModel.observeBeneficiaryUpdateResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    updatedBeneficiarySuccessDialog()
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

        viewModel.ccountryNotEmpty.observe(this, {
            binding.etMobileNumber.isClickable = it
        })

        viewModel.validationPhoneNumberPassed.observe(this, { validationsPassed ->
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
        })

        viewModel.validationAliasPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilAlias.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilAlias.clearError()
                    binding.tilAlias.isErrorEnabled = false
                }
            }
        })

        viewModel.validationCnicPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilCnicNicop.error = getString(R.string.error_cnic)
                else {
                    binding.tilCnicNicop.clearError()
                    binding.tilCnicNicop.isErrorEnabled = false
                }
            }
        })

        viewModel.beneficiaryRelation.observe(this, Observer {
            if(it != Constants.SPINNER_BENEFICIARY_HINT) {
                binding.tvRelationShip.text = it
                binding.tvRelationShip.colorToText(R.color.pure_black)
            }

            if(viewModel.beneficiaryRelation.value.toString() == resources.getString(R.string.other)) {
                binding.txtOther.visibility = View.VISIBLE
            } else {
                binding.txtOther.visibility = View.GONE
            }
        })
    }

    private fun makeDeleteBeneficiaryView(it: BeneficiaryDetailsModel) {
        //Managing views visibilities
        isDeleteBeneficiary = true
        binding.btnNext.text = getString(R.string.delete_beneficiary)
        binding.btnNext.visibility=View.VISIBLE
        //binding.textViewCountry.visibility = View.GONE
        //binding.etCountry.visibility = View.GONE
        binding.tvCountryCode.visibility = View.GONE
        binding.prefixTv.visibility = View.GONE
        binding.lytUpdateCancel.visibility = View.GONE

        //Disabling EditTexts
        binding.eTCnicNumber.isEnabled = false
        binding.etAlias.isEnabled = false
        binding.etMobileNumber.isEnabled = false
        binding.beneficiaryLL.isEnabled = false
        binding.spinnerRelationShip.isEnabled = false
        binding.tvRelationShip.isEnabled = false
        binding.etCountry.isEnabled = false

        //Setting Form Fields
        binding.viewModel = viewModel
        viewModel.alias.value = it.alias
        viewModel.cnicNumber.value = it.nicNicop.toString().formattedCnicNumberNoSpaces()
        viewModel.mobileNumber.value = it.mobileNo
        binding.tvRelationShip.text = it.relationship
        binding.etCountry.text = it.country
        if(it.country.isNullOrEmpty())
            binding.etCountry.text = " "
        viewModel.aliasNotEmpty.value = true
        viewModel.cnicNumberNotEmpty.value = true
        viewModel.mobileNumberNotEmpty.value = true
        viewModel.ccountryNotEmpty.value = true

        //TextColor
        binding.etAlias.colorToText(R.color.black)
        binding.etAlias.alpha = 0.5f
        binding.eTCnicNumber.colorToText(R.color.black)
        binding.eTCnicNumber.alpha = 0.5f
        binding.etMobileNumber.colorToText(R.color.black)
        binding.etMobileNumber.alpha = 0.5f
        binding.tvRelationShip.colorToText(R.color.black)
        binding.tvRelationShip.alpha = 0.5f
        binding.etCountry.colorToText(R.color.black)
        binding.etCountry.alpha = 0.5f

        //hiding beneficiary relationship
        //binding.beneficiaryLL.visibility = View.GONE
        binding.ivDropDown.visibility = View.GONE

        if(!it.isActive)
            binding.lytPosNegButtons.visibility = View.VISIBLE
    }

    private fun enableEdit(it: BeneficiaryDetailsModel){
        binding.lytPosNegButtons.visibility = View.GONE
        binding.lytUpdateCancel.visibility = View.VISIBLE
        binding.btnNext.text = getString(R.string.delete_beneficiary)
        binding.btnNext.visibility=View.GONE
        //binding.textViewCountry.visibility = View.GONE
        //binding.etCountry.visibility = View.GONE
        binding.tvCountryCode.visibility = View.GONE
        binding.prefixTv.visibility = View.GONE
        binding.ivDropDown.visibility = View.VISIBLE

        //Disabling EditTexts
        binding.eTCnicNumber.isEnabled = true
        binding.etAlias.isEnabled = true
        binding.etMobileNumber.isEnabled = true
        binding.beneficiaryLL.isEnabled = true
        binding.spinnerRelationShip.isEnabled = true
        binding.tvRelationShip.isEnabled = true
        binding.etCountry.isEnabled = true

        //Setting Form Fields
        viewModel.alias.value = it.alias
        viewModel.cnicNumber.value =it.nicNicop.toString().formattedCnicNumberNoSpaces()
        viewModel.mobileNumber.value = it.mobileNo
        binding.tvRelationShip.text = it.relationship
        binding.etCountry.text = it.country
        if(it.country.isNullOrEmpty())
            binding.etCountry.text = " "
        viewModel.aliasNotEmpty.value = true
        viewModel.cnicNumberNotEmpty.value = true
        viewModel.mobileNumberNotEmpty.value = true
        viewModel.ccountryNotEmpty.value = true

        //TextColor
        binding.etAlias.colorToText(R.color.black)
        binding.eTCnicNumber.colorToText(R.color.black)
        binding.etMobileNumber.colorToText(R.color.black)
        binding.tvRelationShip.colorToText(R.color.black)
        binding.etCountry.colorToText(R.color.black)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BeneficiaryDetailsFragment()
    }

    private fun showConfirmBeneficiaryDeletionDialog(beneficiaryDetailsModel: BeneficiaryDetailsModel) {
        val str =
            String.format(getString(R.string.beneficiary_delete_msg), beneficiaryDetailsModel.alias)
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_cancel_register_dialog,
            getString(R.string.delete_beneficiary),
            str.toSpanned(),
            positiveButtonText = getString(R.string.yes),
            negativeButtonText = getString(R.string.no)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            BENEFICIARY_DELETION_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_BENEFICIARY_DELETION)
    }

    private fun showBeneficiaryCreatedDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_beneficairy_created,
            getString(R.string.beneficiary_added),
            (getString(R.string.beneficiary_creation_success_msg)).toSpanned(),
            getString(R.string.done),
            positiveButtonText = "",
            negativeButtonText = ""
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            BENEFICIARY_CREATION_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_BENEFICIARY_CREATION)
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        super.onNeutralButtonClicked(targetCode)
        when (targetCode) {
            BENEFICIARY_CREATION_DIALOG -> fragmentHelper.onBack()
            BENEFICIARY_UPDATION_DIALOG -> fragmentHelper.onBack() //makeDeleteBeneficiaryView(beneficiaryDetailsModel)
            BENEFICIARY_RESEND_OTP_DIALOG -> fragmentHelper.onBack()
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            BENEFICIARY_DELETION_DIALOG -> makeDeleteBeneficiaryCall()
        }
    }

    private fun makeDeleteBeneficiaryCall() {
        viewModel.deleteBeneficiary(DeleteBeneficiaryRequestModel(beneficiaryDetailsModel.id))
    }

    private fun makeResendOtp(){
        viewModel.addBeneficiaryResendOtp(ResendBeneficiaryOtpRequestModel(beneficiaryDetailsModel.id.toString()))
    }

    private fun updateBeneficiary(){
        var relation:String
        if(viewModel.beneficiaryRelation.value.toString() == "Other") {
            relation = binding.txtOther.text.toString()
        } else {
            relation = binding.tvRelationShip.text.toString()
        }
        viewModel.updateBeneficiary(UpdateBeneficiaryRequestModel(
            beneficiaryDetailsModel.id.toString(),
            viewModel.cnicNumber.value.toString().removeDashes(),
            viewModel.alias.value.toString(),
            viewModel.mobileNumber.value.toString(),
            beneficiaryRelation=relation,
            viewModel.country.value
        ))
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCodeLength = countryCodeModel.length.toInt()
        binding.tvCountryCode.text = countryCodeModel.code
        viewModel.country.value = countryCodeModel.country
        binding.tvCountryCode.colorToText(R.color.black)
        binding.etMobileNumber.isEnabled = true
        binding.etMobileNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        binding.etCountry.colorToText(R.color.pure_black)
        /*binding.etMobileNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
        fragmentHelper.onBack()
        binding.etMobileNumber.setText("")
        binding.etMobileNumber.requestFocus()
        showKeyboard()
    }


    private fun updatedBeneficiarySuccessDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this, BENEFICIARY_UPDATION_DIALOG)
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_uuid_success_dialog)
            .setTitle(getString(R.string.beneficiary_updated))
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager,
                TAG_BENEFICIARY_UPDATION
            )
    }
    private fun resentBeneficiaryOTPSuccessDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this, BENEFICIARY_RESEND_OTP_DIALOG)
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_uuid_success_dialog)
            .setTitle(getString(R.string.otp_resent))
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager,
                TAG_BENEFICIARY_RESEND_OTP
            )
    }
}