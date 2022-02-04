package com.onelink.nrlp.android.features.complaint.fragment.registered

import android.annotation.SuppressLint
import android.app.DatePickerDialog
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
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentRegComplaintDetailsBinding
import com.onelink.nrlp.android.features.complaint.view.RegComplaintActivity
import com.onelink.nrlp.android.features.complaint.view.UnregComplaintActivity
import com.onelink.nrlp.android.features.complaint.viewmodel.RegComplaintSharedViewModel
import com.onelink.nrlp.android.features.login.helper.CnicTextHelper
import com.onelink.nrlp.android.features.profile.disabled
import com.onelink.nrlp.android.features.redeem.adapter.RedemPartnerAdapter
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.widgets.OneLinkEditText
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.loyalty_statement_list_item.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class RegComplaintDetailsFragment:
    BaseFragment<RegComplaintSharedViewModel, FragmentRegComplaintDetailsBinding>(
        RegComplaintSharedViewModel::class.java
    ), SelectCountryFragment.OnSelectCountryListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    private var countryCodeLength: Int? = 10
    private val REGISTERED:Int=1
    private var listenerInitializedTR: Boolean = false
    private var listenerInitializedPR: Boolean = false
    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): RegComplaintSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(RegComplaintSharedViewModel::class.java)

    override fun getLayoutRes()= R.layout.fragment_reg_complaint_details

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel=viewModel

        initListeners()
        initOnFocusChangeListeners()
        initObservers()
        initViews()
    }

    private fun getUserType():String{
        UserData.getUser()?.let {
            if(it.accountType==Constants.BENEFICIARY.toLowerCase(Locale.getDefault()))
               return Constants.BENEFICIARY
            else
                return Constants.REMITTER
        }
        return Constants.REMITTER
    }

    private fun initTransactionSpinner(){
        val transactionTypes=resources.getStringArray(R.array.transactionType)

        if(getUserType().equals(Constants.BENEFICIARY,true)){
            transactionTypes.toMutableList().remove(resources.getString(R.string.self_awarding))
        }

        binding.spinnerSelectTransaction.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.transactionType)
            )
        } as SpinnerAdapter

        binding.spinnerSelectTransaction.onItemSelectedListener =
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
                    if (listenerInitializedTR) {
                        viewModel.transactionType.postValue(transactionTypes[position])
                    } else {
                        listenerInitializedTR = true
                        binding.spinnerSelectTransaction.setSelection(-1)
                    }
                }
            }
    }

    private fun initPartnerSpinner(){
        if(viewModel.partnerList.value.isNullOrEmpty() ){
            viewModel.getRedeemPartner()
            oneLinkProgressDialog.showProgressDialog(context)
            viewModel.observerRedeemPartner().observe(this, androidx.lifecycle.Observer { response->
                when(response.status){
                    Status.SUCCESS -> {
                        oneLinkProgressDialog.hideProgressDialog()
                        response.data?.let {
                            val redeemPartnerList: MutableList<String> = mutableListOf()
                            it.data.let {
                                it.forEach {
                                   redeemPartnerList.add(it.partnerName)
                                }
                                viewModel.partnerList.postValue(redeemPartnerList)
                            }
                            binding.spinnerSelectPartner.adapter=context?.let {
                                ArrayAdapter(
                                    it,
                                    R.layout.custom_spinner_item,
                                    redeemPartnerList
                                )
                            } as SpinnerAdapter

                            binding.spinnerSelectPartner.onItemSelectedListener =
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
                                        if (listenerInitializedPR) {
                                            viewModel.redemptionPartners.postValue(
                                                viewModel.partnerList.value!![position]
                                            )
                                        } else {
                                            listenerInitializedPR = true
                                            binding.spinnerSelectPartner.setSelection(-1)
                                        }
                                    }
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
        }

    }

    private fun initListeners(){

        binding.btnNext.setOnSingleClickListener {
            if(detailsValidationPassed()){
                oneLinkProgressDialog.showProgressDialog(context)
                viewModel.makeComplainCall(getJsonObject())
            }
        }

        binding.spinnerTransaction.setOnClickListener{
            binding.spinnerSelectTransaction.performClick()
        }

        binding.spinnerRedemption.setOnClickListener{
            binding.spinnerSelectPartner.performClick()
        }


        binding.BeneficaryCountry.setOnSingleClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(UserData.getUser()?.accountType!!),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.etTransactionDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog()
        }


        binding.icHelpTransaction.setOnClickListener {
            //showWarningDialog(getString(R.string.transaction_eligibity_for_self_award))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.specify_bank_exchange_from_where_remittance))
        }
    }

    private fun initViews(){
        binding.BeneficaryCountry.isClickable=true
        binding.etTransactionDate.isClickable=true

         when (viewModel.complaintText.value!!.toLowerCase(Locale.getDefault())){
            resources.getString(RegisteredComplaintTypes.UNABLE_TO_RECEIVE_OTP).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyUnableToOtp.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP
                initTransactionSpinner()
            }

            resources.getString(RegisteredComplaintTypes.UNABLE_TO_ADD_BENEFICIARY).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyUnableToAddBeneficiary.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY
            }

            resources.getString(RegisteredComplaintTypes.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyTransferPoints.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY
            }

            resources.getString(RegisteredComplaintTypes.UNABLE_TO_SELF_AWARDS_POINTS).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyUnableToSelfAward.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS
            }

            resources.getString(RegisteredComplaintTypes.REDEMPTION_ISSUES).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyRedemptionIssues.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.REDEMPTION_ISSUES
                initPartnerSpinner()
            }

            resources.getString(RegisteredComplaintTypes.OTHERS).
            toLowerCase(Locale.getDefault()) -> {
                binding.lyOthers.visibility=View.VISIBLE
                viewModel.complaintType.value = COMPLAINT_TYPE.OTHERS
            }

        }

    }

    private fun initOnFocusChangeListeners() {

        binding.etPointsbeneficiaryCnicNicp.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationCnicPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etPointsbeneficiaryCnicNicp.text.toString()
                    )
                )
            }
        }
        binding.etAddbeneficiaryCnicnicop.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationCnicPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etAddbeneficiaryCnicnicop.text.toString()
                    )
                )
            }
        }

        binding.etPhoneNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationPhoneNumberPassed.postValue(
                    viewModel.checkPhoneNumberValidation(
                        binding.etPhoneNumber.text.toString(),
                        countryCodeLength
                    )
                )
            }
        }
    }

    private fun initObservers() {
        viewModel.observeAddComplainResponse().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        viewModel.complaintId.postValue(it.complaintId)
                        viewModel.gotoComplaintResponseFragment(resources,fragmentHelper)
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
        binding.etPointsbeneficiaryCnicNicp.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etPointsbeneficiaryCnicNicp.clearFocus()
                true
            } else false
        }
        binding.etAddbeneficiaryCnicnicop.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                binding.etAddbeneficiaryCnicnicop.clearFocus()
                true
            } else false
        }

        CnicValidator(binding.etAddbeneficiaryCnicnicop)
        CnicValidator(binding.etPointsbeneficiaryCnicNicp)

        (activity as RegComplaintActivity).selectedCountry.observe(this, androidx.lifecycle.Observer { countryCodeModel ->
            viewModel.country.value = countryCodeModel.country
            binding.BeneficaryCountry.colorToText(R.color.black)
            binding.tvCountryCode.text = countryCodeModel.code
            binding.etPhoneNumber.isEnabled = true
            binding.tvCountryCode.colorToText(R.color.black)
            binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        })

        viewModel.validationPhoneNumberPassed.observe(
            this,
            { validationsPassed ->
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
    }

    private fun CnicValidator(editText:OneLinkEditText){
        editText.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    editText.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12) +
                                inputString.substring(12, 13)
                    )
                    editText.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    editText.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    editText.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    editText.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(editText.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    editText.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    editText.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                editText.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = editText.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    viewModel.cnicNumberNotEmpty.postValue(true)
                    when {
                        Pattern.matches(regex1, inputString)
                                || Pattern.matches(regex2, inputString) -> {
                            editText.setText("$inputString-")
                            editText.setSelection(inputString.length + 1)
                        }
                        Pattern.matches(regex3, inputString) -> {
                            editText.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            editText.setSelection(inputString.length + 1)
                        }
                    }
                }
                editText.addTextChangedListener(this)
            }
        })
    }

    private fun getJsonObject():JsonObject{
        val jsonObject=JsonObject()
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Registered,
            REGISTERED
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.User_type,
            getUserType().toLowerCase(Locale.getDefault())
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Complaint_type_id,
            viewModel.complaintType.value
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Nic_nicop,
            UserData.getUser()?.cnicNicop.toString()
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Name,
            UserData.getUser()?.fullName
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Email,
            UserData.getUser()?.email
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Mobile_no,
            UserData.getUser()?.mobileNo
        )
        jsonObject.addProperty(
            ComplaintRequestModelConstants.Country_of_residence,
            UserData.getUser()?.placeOfBirth
        )
        when(viewModel.complaintType.value){

            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP ->{
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Mobile_Operator,
                    viewModel.mobileOperator.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_type,
                    viewModel.transactionType.value
                )

            }

            COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY ->{

                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Nic_nicop,
                    viewModel.cnicNumber.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Country_of_residence,
                    viewModel.country.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Mobile_Number,
                    viewModel.mobileNumber.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Mobile_Operator,
                    viewModel.mobileOperator.value
                )

            }

            COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Nic_nicop,
                    viewModel.cnicNumber.value
                )
            }

            COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_amount,
                    viewModel.transactionAmount.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_date,
                    viewModel.transactionDate.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_id,
                    viewModel.transactionId.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Remitting_entity,
                    viewModel.remittingEntity.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Nic_Account,
                    viewModel.cnicAccountNumber.value
                )
            }

            COMPLAINT_TYPE.REDEMPTION_ISSUES ->{
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Comments,
                    viewModel.details.value
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Redemption_Partner,
                    viewModel.redemptionPartners.value
                )

            }

            COMPLAINT_TYPE.OTHERS ->{
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Comments,
                    viewModel.otherDetails.value
                )
            }
        }
        return jsonObject

    }

    private fun detailsValidationPassed():Boolean{
        when(viewModel.complaintType.value){
            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP -> {
                return binding.etMobileOperator.text.toString().isNotEmpty() &&
                        binding.tvTransaction.text.toString().isNotEmpty()
            }
            COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY ->{
                return viewModel.checkCnicValidation(binding.etAddbeneficiaryCnicnicop.text.toString()) &&
                        binding.BeneficaryCountry.text.toString().isNotEmpty() &&
                        binding.etPhoneNumber.text.toString().isNotEmpty() &&
                        viewModel.checkPhoneNumberValidation(binding.etPhoneNumber.text.toString()) &&
                        binding.etBeneficiaryMobileOperator.text.toString().isNotEmpty()

            }
            COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY ->{
                return viewModel.checkCnicValidation(binding.etPointsbeneficiaryCnicNicp.text.toString())

            }
            COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS ->{
                return  binding.etBeneficiaryAccount.text.toString().isNotEmpty() &&
                        binding.etTransactionDate.text.toString().isNotEmpty() &&
                        binding.etTransactionamount.text.toString().isNotEmpty() &&
                        binding.etTransactionid.text.toString().isNotEmpty() &&
                        binding.etRemitting.text.toString().isNotEmpty()

            }
            COMPLAINT_TYPE.REDEMPTION_ISSUES ->{
                return binding.etDetails.text.toString().isNotEmpty() &&
                        binding.tvRedemption.text.toString().isNotEmpty()

            }
            COMPLAINT_TYPE.OTHERS ->{
                return binding.etDetails2.text.toString().isNotEmpty()
            }
            else -> return false
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCodeLength = countryCodeModel.length.toInt()
        viewModel.country.value = countryCodeModel.country
        binding.BeneficaryCountry.colorToText(R.color.black)
        binding.tvCountryCode.text = countryCodeModel.code
        binding.etPhoneNumber.isEnabled = true
        binding.tvCountryCode.colorToText(R.color.black)
        binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        /*binding.etPhoneNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
        fragmentHelper.onBack()
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
                    viewModel.transactionDate.value=
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString()

                    //viewModel.rawRemittanceDate.value = viewModel.rawDate
                    viewModel.transactionDate.value = viewModel.getDateInStringFormat(c)


                }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = AdvancedLoyaltyStatementFragment.MILLIS_MINIMUM_DATE
        datePickerDialog?.datePicker?.maxDate = System.currentTimeMillis()
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegComplaintDetailsFragment ()
    }
}