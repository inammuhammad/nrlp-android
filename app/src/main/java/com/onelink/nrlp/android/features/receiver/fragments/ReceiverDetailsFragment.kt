package com.onelink.nrlp.android.features.receiver.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.*
import android.text.style.StyleSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentReceiverDetailsBinding
import com.onelink.nrlp.android.features.profile.disabled
import com.onelink.nrlp.android.features.profile.enabled
import com.onelink.nrlp.android.features.receiver.models.AddReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.BankDetailsModel
import com.onelink.nrlp.android.features.receiver.models.DeleteReceiverRequestModel
import com.onelink.nrlp.android.features.receiver.models.ReceiverDetailsModel
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverDetailsViewModel
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.features.redeem.fragments.REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TAG_REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.select.bank.view.SelectBankFragment
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertCityDialogFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

const val BENEFICIARY_CREATION_DIALOG = 3000
const val BENEFICIARY_DELETION_DIALOG = 3001
const val BENEFICIARY_UPDATION_DIALOG=3002
const val BENEFICIARY_RESEND_OTP_DIALOG=3003
const val RECEIVER_CREATION_DIALOG = 3004
const val RECEIVER_DELETE_SUPPLEMENTARY_CONFIRM_DIALOG=3005
const val FIVE_MINUTE_TIMER_MILLIS = 5 * 60 * 1000L
const val TIMER_MILLIS = 1* 60000L
const val TIMER_INTERVAL = 1000L
const val SERVER_TIME_PATTERN="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
const val LOCAL_TIME_PATTERN="MMM d, yyyy h:m:s a"
const val TAG_BENEFICIARY_DELETION = "beneficiary_deletion_dialog"
const val TAG_BENEFICIARY_CREATION = "beneficiary_creation_dialog"
const val TAG_BENEFICIARY_UPDATION = "beneficiary_updation_dialog"
const val TAG_BENEFICIARY_RESEND_OTP = "beneficiary_otp_resent_dialog"
const val TAG_RECEIVER_CREATION = "receiver_creation_dialog"
const val TAG_RECEIVER_DELETE_SUPPLEMENTARY_CONFIRM = "receiver_delete_supplementary_confirm"

class ReceiverDetailsFragment :
    BaseFragment<ReceiverDetailsViewModel, FragmentReceiverDetailsBinding>(
        ReceiverDetailsViewModel::class.java
    ),
    OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners,
    SelectCountryFragment.OnSelectCountryListener, SelectCityFragment.OnSelectCityListener,
    SelectBankFragment.OnSelectBankListener,
    OneLinkAlertCityDialogFragment.OneLinkAlertDialogListeners{

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var receiverSharedViewModel: ReceiverSharedViewModel? = null

    private var isDeleteBeneficiary: Boolean = false

    private var countryCodeLength: Int? = 10

    private lateinit var beneficiaryDetailsModel: ReceiverDetailsModel

    private lateinit var relation: String

    private var listenerInitializedBR: Boolean = false

    private var timeRemaining:Long = FIVE_MINUTE_TIMER_MILLIS

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_receiver_details

    override fun getTitle() = resources.getString(R.string.remittance_receiver_manager)


    override fun getViewM(): ReceiverDetailsViewModel =
        ViewModelProvider(this, viewModelFactory).get(ReceiverDetailsViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            receiverSharedViewModel = ViewModelProvider(it).get(
                ReceiverSharedViewModel::class.java
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

    /*override fun onResume() {
        super.onResume()
        fiveMinuteTimer.start()
    }

    override fun onPause() {
        super.onPause()
        fiveMinuteTimer.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        fiveMinuteTimer.cancel()
    }*/

    private fun initViews(){
        /*if(receiverSharedViewModel?.receiverDetails?.value.linkStatus == ""){
            binding.lytPosNegButtons.visibility=View.VISIBLE
            isTimePassed()
            //val result=gmtTime-timeUpdated
        }else{
            binding.lytPosNegButtons.visibility=View.GONE
        }*/
    }
    fun String.toDate(dateFormat: String = LOCAL_TIME_PATTERN, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    fun Date.formatTo(dateFormat: String, timeZone: TimeZone = TimeZone.getDefault()): String {
        val formatter = SimpleDateFormat(dateFormat, Locale.ENGLISH)
        formatter.timeZone = timeZone
        return formatter.format(this)
    }
    fun getCurrentTime() :String {
        val dateFormat = SimpleDateFormat(LOCAL_TIME_PATTERN, Locale.ENGLISH)
        return dateFormat.format(Calendar.getInstance().getTime())

    }

    /*private fun isTimePassed(){
        val df = DateFormat.getDateTimeInstance()
        df.timeZone = TimeZone.getTimeZone("gmt")
        val gmtTime = df.format(Date())
        val timeUpdated=receiverSharedViewModel?.receiverDetails?.value!!.updatedAt
        val parser =  SimpleDateFormat(SERVER_TIME_PATTERN)
        val date1=timeUpdated.toDate(dateFormat = SERVER_TIME_PATTERN)
        // val parser2 =  SimpleDateFormat(LOCAL_TIME_PATTERN,Locale.ENGLISH)
        val date2=getCurrentTime().toDate(timeZone = TimeZone.getDefault())
        val diff: Long = Math.abs(date1.time - date2.time)
        val differenceInMilis= diff

        if(differenceInMilis>= FIVE_MINUTE_TIMER_MILLIS)
        {
            binding.btnResendOtp.visibility=View.VISIBLE
            binding.textViewTimer.visibility=View.GONE
        }
        else
        {
            binding.btnResendOtp.visibility=View.GONE
            binding.textViewTimer.visibility=View.VISIBLE
            timeRemaining=
                FIVE_MINUTE_TIMER_MILLIS -differenceInMilis
            fiveMinuteTimer.setRemainingTimeinMillis(timeRemaining)
            fiveMinuteTimer.reset()
        }
    }*/


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

        binding.etBankName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBankNamePassed.postValue(
                    viewModel.checkBankNameValidation(
                        binding.etBankName.text.toString()
                    )
                )
            }
        }

        binding.etIbanNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationIbanPassed.postValue(
                    viewModel.checkIbanValidation(
                        binding.etIbanNumber.text.toString()
                    )
                )
            }
        }

        /*binding.etMotherMaidenName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationMotherMaidenPassed.postValue(
                    viewModel.checkMotherMaidenNameValidation(
                        binding.etMotherMaidenName.text.toString()
                    )
                )
            }
        }*/
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
            hideKeyboard()
            if(viewModel.validationsPassedCnicReceiver(binding.eTCnicNumber.text.toString()))
                makeReceiverAddCall()
        }
        binding.btnNext1.setOnSingleClickListener {
            hideKeyboard()
            if(
                viewModel.validationsPassedIbanReceiver(binding.eTCnicNumber.text.toString(),
                    binding.etIbanNumber.text.toString())
            )
                makeReceiverAddCall()
        }
        binding.btnDelete.setOnSingleClickListener {
            showConfirmBeneficiaryDeletionDialog(beneficiaryDetailsModel)
        }
        binding.tvPlaceOfBirth.setOnClickListener {
            fragmentHelper.addFragment(
                SelectCityFragment.newInstance("beneficiary"),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }
        binding.etBankName.setOnSingleClickListener {
            fragmentHelper.addFragment(
                SelectBankFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        binding.spinnerRelationShip.setOnClickListener {
            binding.spinnerSelectRelationship.performClick()
        }
        binding.btnEdit.setOnSingleClickListener {
            //enableEdit(beneficiaryDetailsModel)
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
        binding.tvCnicIssuanceDate.setOnClickListener {
            openDatePickerDialog()
            hideKeyboard()
        }
        binding.icHelpFullName.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_name_help))
        }
        binding.icHelpPlaceOfBirth.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_place_of_birth))
        }
        binding.icHelpCountry.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_country_of_residence))
        }
        binding.icHelpMotherMaidenName.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_mother_maiden_name))
        }
        binding.icHelpBankName.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_bank_name))
        }
        binding.icHelpIbanNumber.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.receiver_iban))
        }
    }

    private fun makeReceiverAddCall() {
        if(viewModel.beneficiaryRelation.value.toString() == "Other") {
            relation = binding.txtOther.text.toString()
        } else {
            relation = viewModel.beneficiaryRelation.value.toString()
        }
        viewModel.addReceiver(
            AddReceiverRequestModel(
                nicNicop = binding.eTCnicNumber.text.toString().cleanNicNumber(),
                mobileNo = binding.tvCountryCode.text.toString() + binding.etMobileNumber.text.toString(),
                fullName = binding.etAlias.text.toString(),
                motherMaidenName = binding.etMotherMaidenName.text.toString(),
                cnicIssuanceDate = binding.tvCnicIssuanceDate.text.toString(),
                placeOfBirth = binding.tvPlaceOfBirth.text.toString(),
                accountNumberIban = binding.etIbanNumber.text.toString(),
                bankName = binding.etBankName.text.toString()
            )
        )
    }

    private fun initObservers() {
        //viewModel.getBanksList()
        receiverSharedViewModel?.isDeleteBeneficiary?.observe(this, { isDeleteBeneficiary ->
            if (isDeleteBeneficiary) {
                receiverSharedViewModel?.receiverDetails?.observe(this, {
                    beneficiaryDetailsModel = it
                    makeDeleteBeneficiaryView(beneficiaryDetailsModel)
                    initViews()
                })
            }
        })

        receiverSharedViewModel?.receiverType?.observe(this, {
            if(it == R.id.rdBtnCnic) {
               hideIbanView()
            }
        })

        viewModel.observeReceiverAddResponse().observe(this, { response ->
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

        viewModel.observeReceiverDeleteResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        //fragmentHelper.onBack()
                        showReceiverDeletedDialog(beneficiaryDetailsModel.receiverName)
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

        viewModel.observeBanksListResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data.let {
                        if (it != null) {
                            getBanksStringArray(it.data)
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

        /*viewModel.observeBeneficiaryResendOtp().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    showTimer()
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
        })*/

        viewModel.countryNotEmpty.observe(this, {
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

        viewModel.validationMotherMaidenPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilMotherMaidenName.error = getString(R.string.error_not_valid_mother_name)
                else {
                    binding.tilMotherMaidenName.clearError()
                    binding.tilMotherMaidenName.isErrorEnabled = false
                }
            }
        })

        viewModel.validationBankNamePassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilBankName.error = getString(R.string.error_invalid_bank_name)
                else {
                    binding.tilBankName.clearError()
                    binding.tilBankName.isErrorEnabled = false
                }
            }
        })

        viewModel.validationIbanPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilIbanNumber.error = getString(R.string.error_invalid_account_number)
                else {
                    binding.tilIbanNumber.clearError()
                    binding.tilIbanNumber.isErrorEnabled = false
                }
            }
        })

        viewModel.beneficiaryRelation.observe(this, androidx.lifecycle.Observer {
            if(it != Constants.SPINNER_BENEFICIARY_HINT) {
                binding.tvRelationShip.text = it
                binding.tvRelationShip.colorToText(R.color.pure_black)
            }

            if(viewModel.beneficiaryRelation.value.toString() == resources.getString(R.string.other)) {
                binding.txtOther.visibility = View.VISIBLE
                context?.let {
                    binding.txtOther.enabled(it)
                }
            } else {
                binding.txtOther.visibility = View.GONE
            }
        })
    }

    private fun getBanksStringArray(banks: ArrayList<BankDetailsModel>){
        val banksList = ArrayList<String>()
        for (bank in banks){
            banksList.add(bank.name)
        }
        val stringArray: Array<String> = banksList.toTypedArray()
    }

    private fun hideIbanView() {
        binding.apply {
            tilBankName.visibility = View.GONE
            etBankName.visibility = View.GONE
            tvBankName.visibility = View.GONE
            icHelpBankName.visibility = View.GONE
            tilIbanNumber.visibility = View.GONE
            tvIbanNumber.visibility = View.GONE
            icHelpIbanNumber.visibility = View.GONE
            btnNext1.visibility = View.GONE
            if(!isDeleteBeneficiary)
                btnNext.visibility = View.VISIBLE
        }
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

    private fun makeDeleteBeneficiaryView(it: ReceiverDetailsModel) {
        //Managing views visibilities
        isDeleteBeneficiary = true
        binding.btnNext.visibility=View.GONE
        binding.btnNext1.visibility=View.GONE
        binding.btnDelete.visibility=View.VISIBLE
        context?.let { it1 -> binding.btnNext.enabled(it1) }
        //binding.textViewCountry.visibility = View.GONE
        //binding.etCountry.visibility = View.GONE
        binding.tvCountryCode.visibility = View.GONE
        binding.prefixTv.visibility = View.GONE
        binding.lytUpdateCancel.visibility = View.GONE
        binding.tvMotherMaidenName.visibility = View.GONE
        binding.icHelpMotherMaidenName.visibility = View.GONE
        binding.tilMotherMaidenName.visibility = View.GONE
        binding.tvCnicIssuanceDateLabel.visibility = View.GONE
        binding.tvCnicIssuanceDate.visibility = View.GONE
        binding.textViewPlaceOfBirth.visibility = View.GONE
        binding.icHelpPlaceOfBirth.visibility = View.GONE
        binding.tvPlaceOfBirth.visibility = View.GONE
        binding.textViewCountry.visibility = View.GONE
        binding.textViewCountry.visibility = View.GONE
        binding.etCountry.visibility = View.GONE
        binding.icHelpCountry.visibility = View.GONE
        binding.tvPlaceOfBirth.visibility = View.GONE
        binding.icHelpFullName.visibility = View.GONE
        binding.icHelpBankName.visibility = View.GONE
        binding.icHelpIbanNumber.visibility = View.GONE
        if(it.recBankIban.isNullOrEmpty()){
            binding.tilIbanNumber.visibility = View.GONE
            binding.tvIbanNumber.visibility = View.GONE
            binding.icHelpIbanNumber.visibility = View.GONE
            binding.tvBankName.visibility = View.GONE
            binding.icHelpBankName.visibility = View.GONE
            binding.tilBankName.visibility = View.GONE
            binding.etBankName.visibility = View.GONE
        }
        /*if(it.linkStatus != "LINKED")
            binding.btnDelete.visibility = View.GONE*/

        //Disabling EditTexts
        context?.let {
            binding.eTCnicNumber.disabled(it)
            binding.etAlias.disabled(it)
            binding.etMobileNumber.disabled(it)
            binding.beneficiaryLL.disabled()
            binding.spinnerRelationShip.disabled()
            binding.tvRelationShip.disabled(it)
            binding.etCountry.disabled(it)
            binding.etBankName.disabled(it)
            binding.etIbanNumber.disabled(it)
        }

        //Setting Form Fields
        binding.viewModel = viewModel
        viewModel.alias.value = it.receiverName
        viewModel.cnicNumber.value = it.receiverCnic.toString().formattedCnicNumberNoSpaces()
        viewModel.mobileNumber.value = it.receiverMobileNumber
        viewModel.bankName.value = it.recBankName
        viewModel.ibanNumber.value = it.recBankIban
        //binding.tvRelationShip.text = it.relationship
        /*binding.etCountry.text =
        if(it.country.isNullOrEmpty())
            binding.etCountry.text = " "*/
        viewModel.aliasNotEmpty.value = true
        viewModel.cnicNumberNotEmpty.value = true
        viewModel.mobileNumberNotEmpty.value = true
        viewModel.countryNotEmpty.value = true

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
    }

    /*private fun enableEdit(it: ReceiverDetailsModel){
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
        context?.let {
            binding.eTCnicNumber.enabled(it)
            binding.etAlias.enabled(it)
            binding.etMobileNumber.enabled(it)
            binding.beneficiaryLL.enabled()
            binding.spinnerRelationShip.enabled()
            binding.tvRelationShip.enabled(it)
            binding.etCountry.enabled(it)
        }

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
        viewModel.countryNotEmpty.value = true

        //TextColor
        //TextColor
        binding.etAlias.colorToText(R.color.black)
        binding.etAlias.alpha = 1f
        binding.eTCnicNumber.colorToText(R.color.black)
        binding.eTCnicNumber.alpha = 1f
        binding.etMobileNumber.colorToText(R.color.black)
        binding.etMobileNumber.alpha = 1f
        binding.tvRelationShip.colorToText(R.color.black)
        binding.tvRelationShip.alpha = 1f
        binding.etCountry.colorToText(R.color.black)
        binding.etCountry.alpha = 1f

        binding.lytPosNegButtons.visibility = View.VISIBLE
    }*/

    private fun updatedBeneficiaryView(){
        //Managing views visibilities
        binding.btnNext.text = getString(R.string.delete_beneficiary)
        binding.btnNext.visibility=View.VISIBLE
        //binding.textViewCountry.visibility = View.GONE
        //binding.etCountry.visibility = View.GONE
        binding.tvCountryCode.visibility = View.GONE
        binding.prefixTv.visibility = View.GONE
        binding.lytUpdateCancel.visibility = View.GONE

        //Disabling EditTexts
        context?.let {
            binding.eTCnicNumber.disabled(it)
            binding.etAlias.disabled(it)
            binding.etMobileNumber.disabled(it)
            binding.beneficiaryLL.disabled()
            binding.spinnerRelationShip.disabled()
            binding.tvRelationShip.disabled(it)
            binding.etCountry.disabled(it)
            binding.txtOther.disabled(it)
        }

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
        binding.textViewAlias.colorToText(R.color.black)

        //hiding beneficiary relationship
        //binding.beneficiaryLL.visibility = View.GONE
        binding.ivDropDown.visibility = View.GONE

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiverDetailsFragment()
    }

    private fun showConfirmBeneficiaryDeletionDialog(receiverDetailsModel: ReceiverDetailsModel) {
        val str =
            String.format(getString(R.string.beneficiary_delete_msg), receiverDetailsModel.receiverName)
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            false,
            R.drawable.ic_cancel_register_dialog,
            "Delete Receiver",
            str.toSpanned(),
            positiveButtonText = getString(R.string.yes),
            negativeButtonText = getString(R.string.no)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            BENEFICIARY_DELETION_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager,
            TAG_BENEFICIARY_DELETION
        )
    }

    private fun showBeneficiaryCreatedDialog() {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_beneficairy_created,
            getString(R.string.request_received),
            getString(R.string.receiver_will_be_added_upon_nadra).toSpanned(),
            getString(R.string.done),
            positiveButtonText = "",
            negativeButtonText = ""
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            RECEIVER_CREATION_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager,
            TAG_RECEIVER_CREATION
        )
    }

    private fun showReceiverDeletedDialog(name: String) {
        val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment.newInstance(
            true,
            R.drawable.ic_beneficairy_created,
            getString(R.string.receiver_removed),
            getString(R.string._has_been_removed, name).toSpanned(),
            getString(R.string.done),
            positiveButtonText = "",
            negativeButtonText = ""
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            RECEIVER_DELETE_SUPPLEMENTARY_CONFIRM_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager,
            TAG_RECEIVER_DELETE_SUPPLEMENTARY_CONFIRM
        )
    }

    override fun onNeutralButtonClicked(targetCode: Int) {
        super.onNeutralButtonClicked(targetCode)
        when (targetCode) {
            RECEIVER_CREATION_DIALOG -> {
                if((activity as ReceiverActivity).isFromHome)
                    (activity as ReceiverActivity).finish()
                else {
                    fragmentHelper.onBack()
                    fragmentHelper.onBack()
                }
            }
            BENEFICIARY_UPDATION_DIALOG -> updatedBeneficiaryView()
            BENEFICIARY_RESEND_OTP_DIALOG -> updatedBeneficiaryView()
            RECEIVER_DELETE_SUPPLEMENTARY_CONFIRM_DIALOG -> fragmentHelper.onBack()
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int) {
        super.onPositiveButtonClicked(targetCode)
        when (targetCode) {
            BENEFICIARY_DELETION_DIALOG -> makeDeleteBeneficiaryCall()
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int, city: String) {
        viewModel.placeOfBirth.value = city
        binding.tvPlaceOfBirth.colorToText(R.color.black)
        fragmentHelper.onBack()
    }

    private fun makeDeleteBeneficiaryCall() {
        viewModel.deleteReceiver(DeleteReceiverRequestModel(beneficiaryDetailsModel.receiverCnic.toString().cleanNicNumber()))
    }

    private fun makeResendOtp(){
        //viewModel.addBeneficiaryResendOtp(ResendBeneficiaryOtpRequestModel(beneficiaryDetailsModel.id.toString()))
    }

    private fun updateBeneficiary(){
        /*var relation:String
        if(viewModel.beneficiaryRelation.value.toString() == "Other") {
            relation = binding.txtOther.text.toString()
        } else {
            relation = binding.tvRelationShip.text.toString()
        }
        viewModel.updateBeneficiary(
            UpdateBeneficiaryRequestModel(
            beneficiaryDetailsModel.id.toString(),
            viewModel.cnicNumber.value.toString().removeDashes(),
            viewModel.alias.value.toString(),
            viewModel.mobileNumber.value.toString(),
            beneficiaryRelation=relation,
            viewModel.country.value
        )
        )*/
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


    override fun onSelectCityListener(citiesModel: CitiesModel) {
        val s = String.format(
            getString(R.string.enter_code)
        )
        val str = SpannableStringBuilder(s)
        str.setSpan(
            StyleSpan(Typeface.BOLD),
            s.indexOf(s),
            s.indexOf(s) + s.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if(citiesModel.city == "Other") {
            showEnterCityDialog(str)
        }
        else {
            viewModel.placeOfBirth.value = citiesModel.city
            binding.tvPlaceOfBirth.colorToText(R.color.black)
            fragmentHelper.onBack()
        }
    }

    override fun onSelectBankListener(bankDetailsModel: com.onelink.nrlp.android.features.select.bank.model.BankDetailsModel) {
        viewModel.bankName.value = bankDetailsModel.name
        fragmentHelper.onBack()
    }

    private fun showEnterCityDialog(str: Spanned) {
        val oneLinkAlertDialogsFragment = OneLinkAlertCityDialogFragment.newInstance(
            false,
            R.drawable.ic_redem_dialog,
            getString(R.string.place_of_birth),
            str,
            positiveButtonText =  getString(R.string.confirm),
            negativeButtonText = getString(R.string.cancel)
        )
        oneLinkAlertDialogsFragment.setTargetFragment(
            this,
            REDEMPTION_CREATE_DIALOG
        )
        oneLinkAlertDialogsFragment.show(parentFragmentManager, TAG_REDEMPTION_CREATE_DIALOG)
    }

    private fun updatedBeneficiarySuccessDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this,
                BENEFICIARY_UPDATION_DIALOG
            )
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_uuid_success_dialog)
            .setTitle(getString(R.string.beneficiary_updated))
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager,
                TAG_BENEFICIARY_UPDATION
            )
    }

    private fun resentBeneficiaryOTPSuccessDialog() {
        OneLinkAlertDialogsFragment.Builder()
            .setTargetFragment(this,
                BENEFICIARY_RESEND_OTP_DIALOG
            )
            .setIsAlertOnly(true).setDrawable(R.drawable.ic_uuid_success_dialog)
            .setTitle(getString(R.string.otp_resent))
            .setNeutralButtonText(getString(R.string.okay)).setNegativeButtonText("")
            .setPositiveButtonText("").setCancelable(false).show(parentFragmentManager,
                TAG_BENEFICIARY_RESEND_OTP
            )
    }

    private val timer = object : CountDownTimer(
        TIMER_MILLIS,
        TIMER_INTERVAL
    ) {
        override fun onTick(millisUntilFinished: Long) {
            // binding.textViewResendOTP.isEnabled = false
            binding.btnResendOtp.visibility=View.GONE
        }

        override fun onFinish() {
            //if (otpAttempts < RETRIES_COUNT) {
            //binding.textViewResendOTP.isEnabled = true
            binding.btnResendOtp.visibility=View.VISIBLE
            binding.textViewTimer.visibility=View.GONE
            //otpAttempts++
            //}
        }
    }

    private val fiveMinuteTimer =
        object : CountDownTimerCanBePause(
            timeRemaining,
            TIMER_INTERVAL
        ) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val value=resources.getString(R.string.resend_registration_code) + "(" +formattedCountDownTimer(millisUntilFinished) + ")"
                binding.textViewTimer.text = value
            }

            override fun onFinish() {
                binding.textViewTimer.text = getString(R.string.timer_default)
                binding.btnResendOtp.visibility=View.VISIBLE
                binding.textViewTimer.visibility=View.GONE
            }
        }

    private fun showTimer()
    {
        binding.textViewTimer.visibility=View.VISIBLE
        binding.btnResendOtp.visibility=View.GONE
        fiveMinuteTimer.setRemainingTimeinMillis(FIVE_MINUTE_TIMER_MILLIS)
        fiveMinuteTimer.reset()

    }
}