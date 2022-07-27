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
import androidx.core.view.isVisible
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
import com.onelink.nrlp.android.features.select.banksandexchange.view.SelectBanksAndExchangeFragment
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.features.select.generic.model.BranchCenterModel
import com.onelink.nrlp.android.features.select.generic.view.SelectBranchCenterFragment
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.widgets.OneLinkEditText
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.loyalty_statement_list_item.*
import java.math.BigInteger
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class RegComplaintDetailsFragment :
    BaseFragment<RegComplaintSharedViewModel, FragmentRegComplaintDetailsBinding>(
        RegComplaintSharedViewModel::class.java
    ), SelectCountryFragment.OnSelectCountryListener,
    SelectBanksAndExchangeFragment.OnSelectBanksExchangeListener,
    SelectBranchCenterFragment.OnSelectBranchCenterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    private var countryCodeLength: Int? = 10
    private val REGISTERED: Int = 1
    private var listenerInitializedTR: Boolean = false
    private var listenerInitializedPR: Boolean = false
    val list = arrayListOf<BeneficiaryDetailsModel>()
    private val listSpinner: ArrayList<String> = ArrayList()
    private var listenerInitialized: Boolean = false
    private var selectedBeneficiary =
        BeneficiaryDetailsModel(-1, BigInteger.ONE, "12312", 0, "", "", "", "", "")

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): RegComplaintSharedViewModel =
        ViewModelProvider(
            requireActivity(),
            viewModelFactory
        ).get(RegComplaintSharedViewModel::class.java)

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
        val list= mutableListOf<String>()
        for(transaction in transactionTypes){
            list.add(transaction)
        }
        if(getUserType().equals(Constants.BENEFICIARY,true)){
            list.remove(resources.getString(R.string.self_awarding))
           // transactionTypes.toMutableList().remove(resources.getString(R.string.self_awarding))
        }

        binding.spinnerSelectTransaction.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                list
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
                        viewModel.validationTransactionTypePassed.postValue(true)
                    } else {
                        listenerInitializedTR = true
                        binding.spinnerSelectTransaction.setSelection(-1)
                    }
                }
            }
    }

    private fun initPartnerSpinner(){
        //if(viewModel.partnerList.value.isNullOrEmpty() ){
            viewModel.getRedeemPartner()
            oneLinkProgressDialog.showProgressDialog(context)
            viewModel.observerRedeemPartner().observe(
                this,
                androidx.lifecycle.Observer { response ->
                    when (response.status) {
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
                                binding.spinnerSelectPartner.adapter = context?.let {
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
                                                viewModel.validationRedemptionPartnerPassed.postValue(
                                                    true
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
        //}

    }

    private fun initSelfAwardSpinner() {
        val transactionTypes = resources.getStringArray(R.array.selfAwardTransactionTypes)
        val list = mutableListOf<String>()
        for (transaction in transactionTypes) {
            list.add(transaction)
        }
        if (getUserType().equals(Constants.BENEFICIARY, true)) {
            list.remove(resources.getString(R.string.self_awarding))
            // transactionTypes.toMutableList().remove(resources.getString(R.string.self_awarding))
        }

        binding.spinnerSelectSelfAwardType.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                list
            )
        } as SpinnerAdapter

        binding.spinnerSelectSelfAwardType.onItemSelectedListener =
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
                        viewModel.selfAwardType.postValue(transactionTypes[position])
                        viewModel.validationSelfAwardTypePassed.postValue(true)
                    } else {
                        listenerInitializedTR = true
                        //binding.spinnerSelectSelfAwardType.setSelection(-1)
                    }
                }
            }
    }

    private fun initListeners() {

        binding.btnNext.setOnSingleClickListener {
            if (detailsValidationPassed()) {
                oneLinkProgressDialog.showProgressDialog(context)
                viewModel.makeComplainCall(getJsonObject())
            }
        }

        binding.spinnerTransaction.setOnClickListener {
            binding.spinnerSelectTransaction.performClick()
        }

        binding.spinnerRedemption.setOnClickListener {
            binding.spinnerSelectPartner.performClick()
        }

        binding.spinnerSelfAwardType.setOnClickListener {
            binding.spinnerSelectSelfAwardType.performClick()
        }

        binding.ivDropDown3.setOnClickListener {
            binding.spinnerSelectSelfAwardType.performClick()
        }


        binding.BeneficaryCountry.setOnSingleClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(UserData.getUser()?.accountType!!),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        binding.NadraPassportCountry.setOnSingleClickListener {
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(""),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }

        /*binding.BranchCenter.setOnSingleClickListener {
            fragmentHelper.addFragment(
                SelectBranchCenterFragment.newInstance(viewModel.redemptionPartners.value.toString()),
                clearBackStack = false,
                addToBackStack = true
            )
        }*/

        binding.vBanksAndExchange.setOnSingleClickListener {
            hideKeyboard()
            fragmentHelper.addFragment(
                SelectBanksAndExchangeFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }

        binding.etTransactionDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog()
        }


        binding.icHelpTransaction.setOnClickListener {
            //showWarningDialog(getString(R.string.transaction_eligibity_for_self_award))
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.specify_bank_exchange_from_where_remittance)
            )
        }

        binding.tvNadraPassportCountry.setOnSingleClickListener {
            showGeneralAlertDialog(this, "SelfAward", getString(R.string.redemption_country_help))
        }

        binding.tvBranchCenter.setOnSingleClickListener {
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.redemption_branch_center_help)
            )
        }

        binding.tvMobileNumber.setOnSingleClickListener {
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.redemption_mobile_number_help)
            )
        }

        binding.tvDetails.setOnSingleClickListener {
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.error_enter_details)
            )
        }

        binding.tvUnableToSelfAward.setOnSingleClickListener {
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.redemption_service_help)
            )
        }

        binding.icHelpPassport.setOnClickListener {
            //showWarningDialog(getString(R.string.transaction_eligibity_for_self_award))
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.beneficiary_passport_help)
            )
        }

        binding.spinnerLy.setOnClickListener {
            binding.spinnerSelectBene.performClick()
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

             resources.getString(RegisteredComplaintTypes.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY)
                 .toLowerCase(Locale.getDefault()) -> {
                 binding.lyTransferPoints.visibility = View.VISIBLE
                 viewModel.complaintType.value =
                     COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY
                 viewModel.getBeneficiaries()
             }

             resources.getString(RegisteredComplaintTypes.UNABLE_TO_SELF_AWARDS_POINTS)
                 .toLowerCase(Locale.getDefault()) -> {
                 binding.lyUnableToSelfAward.visibility = View.VISIBLE
                 viewModel.complaintType.value = COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS
                 initSelfAwardSpinner()
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

        binding.etUSCBEOENumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationUscBeoeNumberPassed.postValue(
                    viewModel.checkPhoneNumberValidation(
                        binding.etUSCBEOENumber.text.toString(),
                        countryCodeLength
                    )
                )
            }
        }

        binding.etAddbeneficiaryCnicnicop.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBeneficiaryCnicPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etAddbeneficiaryCnicnicop.text.toString()
                    )
                )
            }
        }

        binding.etBeneficiaryMobileOperator.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBeneficiaryMobileOperatorPassed.postValue(
                    viewModel.checkMobileOperatorValidation(
                        binding.etBeneficiaryMobileOperator.text.toString()
                    )
                )
            }
        }

        binding.etMobileOperator.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationMobileOperatorPassed.postValue(
                    viewModel.checkMobileOperatorValidation(
                        binding.etMobileOperator.text.toString()
                    )
                )
            }
        }

        binding.etBeneficiaryAccount.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBeneficiaryAccountPassed.postValue(
                    viewModel.checkBeneficiaryAccount(
                        binding.etBeneficiaryAccount.text.toString()
                    )
                )
            }
        }

        binding.etRemitting.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationRemittingEntityPassed.postValue(
                    viewModel.checkRemittingEntity(
                        binding.etRemitting.text.toString()
                    )
                )
            }
        }

        binding.etTransactionid.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationTransactionIdPassed.postValue(
                    viewModel.checkTransactionId(
                        binding.etTransactionid.text.toString()
                    )
                )
            }
        }

        binding.etTransactionamount.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationTransactionAmountPassed.postValue(
                    viewModel.checkTransactionAmount(
                        binding.etTransactionamount.text.toString()
                    )
                )
            }
        }

        binding.etDetails.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationSpecifyDetailsPassed.postValue(
                    viewModel.checkNotEmpty(
                        binding.etDetails.text.toString()
                    )
                )
            }
        }

        binding.etDetails2.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationSpecifyOtherDetailsPassed.postValue(
                    viewModel.checkNotEmpty(
                        binding.etDetails2.text.toString()
                    )
                )
            }
        }

        binding.etPointsbeneficiaryCnicNicp.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBeneficiaryCnicPointsPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etPointsbeneficiaryCnicNicp.text.toString()
                    )
                )
            }
        }

        binding.etBeneficiaryPassport.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationPassportPassed.postValue(
                    viewModel.checkPassportNumber(
                        binding.etBeneficiaryPassport.text.toString()
                    )
                )
            }
        }

        binding.etBeneficiaryIban.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationIbanPassed.postValue(
                    viewModel.checkIban(
                        binding.etBeneficiaryIban.text.toString()
                    )
                )
            }
        }

        binding.etBranchCenter.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationBranchCenterPassed.postValue(
                    viewModel.checkBranchCenter(
                        binding.etBranchCenter.text.toString()
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
                        sendNotification(
                            "Complaint Registered",
                            it.message, //"Complaint registered with ID ${it.complaintId}",
                            getString(R.string.channel_id)
                        )
                        viewModel.gotoComplaintResponseFragment(resources, fragmentHelper)
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
        CnicValidator(binding.etBeneficiaryAccount)

        (activity as RegComplaintActivity).selectedCountry.observe(
            this,
            androidx.lifecycle.Observer { countryCodeModel ->
                viewModel.country.value = countryCodeModel.country
                viewModel.redemptionCountry.value = countryCodeModel.country
                binding.BeneficaryCountry.colorToText(R.color.black)
                binding.NadraPassportCountry.colorToText(R.color.black)
                binding.tvCountryCode.text = countryCodeModel.code
                binding.etPhoneNumber.isEnabled = true
                binding.tvCountryCode.colorToText(R.color.black)
                binding.etPhoneNumber.hint =
                    viewModel.phoneNumberHint(countryCodeModel.length.toInt())
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

        viewModel.validationUscBeoeNumberPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        if (binding.lyUSCBEOENumber.isShown) {
                            binding.imageViewUSCBEOEPhoneError.visibility = View.VISIBLE
                            binding.errorTextUscBeoe.visibility = View.VISIBLE
                            binding.lyUSCBEOENumber.setBackgroundDrawable(R.drawable.edit_text_error_background)
                            binding.errorTextUscBeoe.text =
                                getString(R.string.error_mobile_num_not_valid)
                        }
                    } else {
                        binding.imageViewUSCBEOEPhoneError.visibility = View.GONE
                        binding.errorTextUscBeoe.visibility = View.GONE
                        binding.lyUSCBEOENumber.setBackgroundDrawable(R.drawable.edit_text_background)
                    }
                }
            })

        viewModel.validationBeneficiaryCnicPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilAddbeneficiaryCnicNicp.error = getString(R.string.error_cnic)
                    else {
                        binding.tilAddbeneficiaryCnicNicp.clearError()
                        binding.tilAddbeneficiaryCnicNicp.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationBeneficiaryMobileOperatorPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilBeneficiaryMobileOperator.error = getString(R.string.error_mobile_operator)
                    else {
                        binding.tilBeneficiaryMobileOperator.clearError()
                        binding.tilBeneficiaryMobileOperator.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationMobileOperatorPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilMobileOperator.error = getString(R.string.error_mobile_operator)
                    else {
                        binding.tilMobileOperator.clearError()
                        binding.tilMobileOperator.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationTransactionTypePassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.spinnerTransaction.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewTransactionTypeError.visibility = View.VISIBLE
                        binding.errorTextTransactionType.visibility = View.VISIBLE
                        binding.errorTextTransactionType.text = getString(R.string.error_transaction_type)
                    } else {
                        binding.spinnerTransaction.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewTransactionTypeError.visibility = View.GONE
                        binding.errorTextTransactionType.visibility = View.GONE
                    }
                }
            })

        viewModel.validationBeneficiaryAccountPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        if (binding.tilBeneficiaryAccount.isShown) {
                            binding.tilBeneficiaryAccount.error = getString(R.string.error_cnic)
                        }
                    } else {
                        binding.tilBeneficiaryAccount.clearError()
                        binding.tilBeneficiaryAccount.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationTransactionIdPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilTransactionid.error = getString(R.string.error_transaction_id)
                    else {
                        binding.tilTransactionid.clearError()
                        binding.tilTransactionid.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationRemittingEntityPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilRemitting.error = getString(R.string.error_remitting_entity)
                    else {
                        binding.tilRemitting.clearError()
                        binding.tilRemitting.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationTransactionAmountPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilTransactionamount.error = getString(R.string.error_transaction_amount)
                    else {
                        binding.tilTransactionamount.clearError()
                        binding.tilTransactionamount.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationRedemptionPartnerPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.spinnerRedemption.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewRedemptionPartnerError.visibility = View.VISIBLE
                        binding.errorTextRedemptionPartner.visibility = View.VISIBLE
                    } else {
                        binding.spinnerRedemption.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewRedemptionPartnerError.visibility = View.GONE
                        binding.errorTextRedemptionPartner.visibility = View.GONE
                    }
                }
            })

        viewModel.validationSelfAwardTypePassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.spinnerSelfAwardType.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewSelfAwardTypeError.visibility = View.VISIBLE
                        binding.errorTextSelfAward.visibility = View.VISIBLE
                    } else {
                        binding.spinnerSelfAwardType.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewSelfAwardTypeError.visibility = View.GONE
                        binding.errorTextSelfAward.visibility = View.GONE
                    }
                }
            })

        viewModel.validationSpecifyDetailsPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.etDetails.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewDetailsError.visibility = View.VISIBLE
                        binding.errorTextSpecifyDetails.visibility = View.VISIBLE
                    } else {
                        binding.etDetails.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewDetailsError.visibility = View.GONE
                        binding.errorTextSpecifyDetails.visibility = View.GONE
                    }
                }
            })

        viewModel.validationSpecifyOtherDetailsPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilDetails2.error = getString(R.string.error_enter_details)
                    else {
                        binding.tilDetails2.clearError()
                        binding.tilDetails2.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationBeneficiaryCountryPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.BeneficaryCountry.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewBeneficiaryCountry.visibility = View.VISIBLE
                        binding.errorTextBeneficiaryCountry.visibility = View.VISIBLE
                    } else {
                        binding.BeneficaryCountry.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewBeneficiaryCountry.visibility = View.GONE
                        binding.errorTextBeneficiaryCountry.visibility = View.GONE
                    }
                }
            })

        viewModel.validationRedemptionCountryPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        if (binding.NadraPassportCountry.isShown) {
                            binding.NadraPassportCountry.setBackgroundResource(R.drawable.edit_text_error_background)
                            binding.imageViewNadraPassportCountry.visibility = View.VISIBLE
                            binding.errorTextRedemptionCountry.visibility = View.VISIBLE
                        }
                    } else {
                        binding.NadraPassportCountry.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewNadraPassportCountry.visibility = View.GONE
                        binding.errorTextRedemptionCountry.visibility = View.GONE
                    }
                }
            })

        /*viewModel.validationBranchCenterPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.BranchCenter.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewBranchCenter.visibility = View.VISIBLE
                        binding.errorTextBranchCenter.visibility = View.VISIBLE
                    } else {
                        binding.BranchCenter.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewBranchCenter.visibility = View.GONE
                        binding.errorTextBranchCenter.visibility = View.GONE
                    }
                }
            })*/

        viewModel.validationBranchCenterPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilBranchCenter.error = getString(R.string.error_branch_center)
                    else {
                        binding.tilBranchCenter.clearError()
                        binding.tilBranchCenter.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationBeneficiaryCnicPointsPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilPointsbeneficiaryCnic.error = getString(R.string.error_cnic)
                    else {
                        binding.tilPointsbeneficiaryCnic.clearError()
                        binding.tilPointsbeneficiaryCnic.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationTransactionDatePassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        binding.etTransactionDate.setBackgroundResource(R.drawable.edit_text_error_background)
                        binding.imageViewTransactionDateError.visibility = View.VISIBLE
                        binding.errorTextTransactionDate.visibility = View.VISIBLE
                    } else {
                        binding.etTransactionDate.setBackgroundResource(R.drawable.edit_text_background)
                        binding.imageViewTransactionDateError.visibility = View.GONE
                        binding.errorTextTransactionDate.visibility = View.GONE
                    }
                }
            })

        viewModel.selfAwardType.observe(this, androidx.lifecycle.Observer {
            binding.vBuffer.visibility = View.GONE
            when (it) {
                getString(R.string.remittance_to_bank) -> {
                    makeIbanView()
                }
                getString(R.string.remittance_to_cnic) -> {
                    makeCnicView()
                }
                getString(R.string.remittance_to_passport) -> {
                    makePassportView()
                }
            }
            clearSelfAwardData()
        })

        viewModel.redemptionPartners.observe(this, {
            viewModel.clearValidations()
            if (it.contains("USC", true) || it.contains("BEOE", true))
                makeUSCBEOEView()
            else if (it.contains("PASSPORT", true) || it.contains("NADRA", true))
                makeNadraPassportView()
            else
                hideFurtherDetails()
            clearRedemptionData()
        })

        viewModel.validationSelfAwardCnicPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilBeneficiaryAccount.error = getString(R.string.error_cnic)
                    else {
                        binding.tilBeneficiaryAccount.clearError()
                        binding.tilBeneficiaryAccount.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationIbanPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        if (binding.tilBeneficiaryIban.isShown) {
                            binding.tilBeneficiaryIban.error =
                                getString(R.string.error_invalid_account_number)
                        }
                    } else {
                        binding.tilBeneficiaryIban.clearError()
                        binding.tilBeneficiaryIban.isErrorEnabled = false
                    }
                }
            })

        viewModel.validationPassportPassed.observe(
            this,
            { validationsPassed ->
                run {
                    if (!validationsPassed) {
                        if (binding.tilBeneficiaryPassport.isShown) {
                            binding.tilBeneficiaryPassport.error =
                                getString(R.string.error_passport_mumber)
                        }
                    } else {
                        binding.tilBeneficiaryPassport.clearError()
                        binding.tilBeneficiaryPassport.isErrorEnabled = false
                    }
                }
            })

        viewModel.observeBeneficiaryResponse()
            .observe(this, androidx.lifecycle.Observer { response ->
                when (response.status) {
                    Status.SUCCESS -> {
                        binding.spinnerSelectBene.adapter = context?.let {
                            oneLinkProgressDialog.hideProgressDialog()
                            response.data?.data?.let { userData ->
                                for (i in userData) {
                                    //if (i.nadraStatusCode == "A") {
                                    list.add(i)
                                    listSpinner.add(i.alias)
                                    //}
                                }
                                if (list.size > 0) {
                                    //binding.lyNoBeneficiary.visibility = View.GONE
                                    //binding.lyManagePoints.visibility = View.VISIBLE

                                } else {
                                    //binding.lyNoBeneficiary.visibility = View.VISIBLE
                                    //binding.lyManagePoints.visibility = View.GONE
                                }

                            }
                            ArrayAdapter(
                                it,
                                R.layout.custom_spinner_item,
                                listSpinner
                            )
                        } as SpinnerAdapter
                        binding.spinnerSelectBene.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // not in use
                                }

                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    if (listenerInitialized) {
                                        list[position]
                                        val data = list[position]
                                        selectedBeneficiary = data
                                        binding.tvSelectedBene.text = list[position].alias
                                        binding.tvSelectedBene.colorToText(R.color.pure_black)
                                        viewModel.selectedBene.postValue(data)


                                    } else {
                                        listenerInitialized = true
                                        binding.spinnerSelectBene.setSelection(-1)
                                    }
                                }
                            }
                    }
                    Status.LOADING -> {

                    }
                    Status.ERROR -> {

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
            UserData.getUser()?.country
        )
        when(viewModel.complaintType.value){

            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP ->{
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Mobile_Operator,
                    binding.etMobileOperator.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_type,
                    binding.tvTransaction.text.toString()
                )
            }

            COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY ->{

                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Nic_nicop,
                    binding.etAddbeneficiaryCnicnicop.text.toString().removeDashes()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Country_of_residence,
                    binding.BeneficaryCountry.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Mobile_Number,
                    binding.tvCountryCode.text.toString()+binding.etPhoneNumber.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Mobile_Operator,
                    binding.etBeneficiaryMobileOperator.text.toString()
                )

            }

            COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_Nic_nicop,
                    binding.etPointsbeneficiaryCnicNicp.text.toString().removeDashes()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Beneficiary_ID,
                    viewModel.selectedBene.value?.id
                )
            }

            COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_amount,
                    binding.etTransactionamount.text.toString().replace(",", "")
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_date,
                    binding.etTransactionDate.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Transaction_id,
                    binding.etTransactionid.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Remitting_entity,
                    binding.etRemitting.text.toString()
                )
                if (binding.etBeneficiaryAccount.text.toString() != "") {
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Beneficiary_Nic_Account,
                        binding.etBeneficiaryAccount.text.toString().removeDashes()
                    )
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.SELF_AWARD_TYPE,
                        "COC" //viewModel.transactionType.value,
                    )
                }
                if (binding.etBeneficiaryPassport.text.toString() != "") {
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Beneficiary_Nic_Account,
                        binding.etBeneficiaryPassport.text.toString()
                    )
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.SELF_AWARD_TYPE,
                        "PPT" //viewModel.transactionType.value,
                    )
                }
                if (binding.etBeneficiaryIban.text.toString() != "") {
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Beneficiary_Nic_Account,
                        binding.etBeneficiaryIban.text.toString()
                    )
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.SELF_AWARD_TYPE,
                        "ACC" //viewModel.transactionType.value,
                    )
                }
            }

            COMPLAINT_TYPE.REDEMPTION_ISSUES -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Comments,
                    binding.etDetails.text.toString()
                )
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Redemption_Partner,
                    binding.tvRedemption.text.toString()
                )
                if (binding.etUSCBEOENumber.text.toString() != "")
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Redemption_USC_BEOE_NUMBER,
                        "+92" + binding.etUSCBEOENumber.text.toString()
                    )
                if (binding.NadraPassportCountry.text.toString() != "")
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Redemption_NADRA_PASSPORT_COUNTRY,
                        binding.NadraPassportCountry.text.toString()
                    )
                if (binding.etBranchCenter.text.toString() != "")
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Redemption_BRANCH_CENTER,
                        binding.etBranchCenter.text.toString()
                    )
                /*if (binding.BranchCenter.text.toString() != "")
                    jsonObject.addProperty(
                        ComplaintRequestModelConstants.Redemption_BRANCH_CENTER,
                        binding.BranchCenter.text.toString()
                    )*/

            }

            COMPLAINT_TYPE.OTHERS -> {
                jsonObject.addProperty(
                    ComplaintRequestModelConstants.Comments,
                    binding.etDetails2.text.toString()
                )
            }
        }
        return jsonObject

    }

    private fun detailsValidationPassed():Boolean{
        val isBeneficiaryCnicValid: Boolean = viewModel.checkCnicValidation(binding.etAddbeneficiaryCnicnicop.text.toString())
        val isPhoneNumberValid: Boolean =
            viewModel.checkPhoneNumberValidation(binding.etPhoneNumber.text.toString(), 0)
        val isUscBeoeNumberValid: Boolean =
            viewModel.checkPhoneNumberValidation(binding.etUSCBEOENumber.text.toString(), 0)
        val isBeneficiaryMobileOperatorValid: Boolean =
            viewModel.checkMobileOperatorValidation(binding.etBeneficiaryMobileOperator.text.toString())
        val isMobileOperatorValid: Boolean = viewModel.checkMobileOperatorValidation(binding.etMobileOperator.text.toString())
        val isTransactionTypeValid: Boolean =
            viewModel.checkTransactionTypeValidation(binding.tvTransaction.text.toString())
        val isBeneficiaryAccountValid: Boolean =
            viewModel.checkCnicValidation(binding.etBeneficiaryAccount.text.toString())
        val isRemittingEntityValid: Boolean =
            viewModel.checkRemittingEntity(binding.etRemitting.text.toString())
        val isTransactionIdValid: Boolean =
            viewModel.checkTransactionId(binding.etTransactionid.text.toString())
        val isTransactionAmount: Boolean =
            viewModel.checkTransactionAmount(binding.etTransactionamount.text.toString())
        val isRedemptionPartnerValid: Boolean =
            viewModel.checkNotEmpty(binding.tvRedemption.text.toString())
        val isSelfAwardTypeValid: Boolean =
            viewModel.checkNotEmpty(binding.tvSelfAward.text.toString())
        val isSpecifyDetailsValid: Boolean =
            viewModel.checkSpecifyDetails(binding.etDetails.text.toString())
        val isSpecifyOtherDetailsValid: Boolean =
            viewModel.checkSpecifyDetails(binding.etDetails2.text.toString())
        val isBeneficiaryCountryValid: Boolean =
            viewModel.checkNotEmpty(binding.BeneficaryCountry.text.toString())
        val isRedemptionCountryValid: Boolean =
            viewModel.checkNotEmpty(binding.NadraPassportCountry.text.toString())
        val isRedemptionBranchCenterValid: Boolean =
            viewModel.checkBranchCenter(binding.etBranchCenter.text.toString())
        //viewModel.checkNotEmpty(binding.BranchCenter.text.toString())
        val isBeneficiaryCnicPointsValid: Boolean =
            viewModel.checkCnicValidation(binding.etPointsbeneficiaryCnicNicp.text.toString())
        val isTransactionDateValid: Boolean =
            viewModel.checkNotEmpty(binding.etTransactionDate.text.toString())
        val isSelfAwardIbanValid: Boolean =
            viewModel.checkIban(binding.etBeneficiaryIban.text.toString())
        val isSelfAwardPassportValid: Boolean =
            viewModel.checkPassportNumber(binding.etBeneficiaryPassport.text.toString())
        //var isFullNameValid: Boolean = viewModel.checkAliasValidation(alias.value!!)
        //var isEmailValid:Boolean = viewModel.checkEmailValidation(emailAddress.value!!)
        //var isCountryValid:Boolean = viewModel.checkCountryValidation(country)
        //var isDetailsValid:Boolean = viewModel.checkDetailsValidation(details.value!!)
        viewModel.validationBeneficiaryCnicPassed.value = isBeneficiaryCnicValid
        viewModel.validationBeneficiaryMobileOperatorPassed.value = isBeneficiaryMobileOperatorValid
        viewModel.validationPhoneNumberPassed.value = isPhoneNumberValid
        viewModel.validationUscBeoeNumberPassed.value = isUscBeoeNumberValid
        viewModel.validationMobileOperatorPassed.value = isMobileOperatorValid
        viewModel.validationTransactionTypePassed.value = isTransactionTypeValid
        viewModel.validationBeneficiaryAccountPassed.value = isBeneficiaryAccountValid
        viewModel.validationRemittingEntityPassed.value = isRemittingEntityValid
        viewModel.validationTransactionIdPassed.value = isTransactionIdValid
        viewModel.validationTransactionAmountPassed.value = isTransactionAmount
        viewModel.validationRedemptionPartnerPassed.value = isRedemptionPartnerValid
        viewModel.validationSelfAwardTypePassed.value = isSelfAwardTypeValid
        viewModel.validationSpecifyDetailsPassed.value = isSpecifyDetailsValid
        viewModel.validationSpecifyOtherDetailsPassed.value = isSpecifyOtherDetailsValid
        viewModel.validationBeneficiaryCountryPassed.value = isBeneficiaryCountryValid
        viewModel.validationRedemptionCountryPassed.value = isRedemptionCountryValid
        viewModel.validationBranchCenterPassed.value = isRedemptionBranchCenterValid
        viewModel.validationBeneficiaryCnicPointsPassed.value = isBeneficiaryCnicPointsValid
        viewModel.validationTransactionDatePassed.value = isTransactionDateValid
        viewModel.validationIbanPassed.value = isSelfAwardIbanValid
        viewModel.validationPassportPassed.value = isSelfAwardPassportValid

        when (viewModel.complaintType.value) {
            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP -> {
                return isMobileOperatorValid && isTransactionTypeValid
            }
            COMPLAINT_TYPE.UNABLE_TO_ADD_BENEFICIARY -> {
                return viewModel.checkCnicValidation(binding.etAddbeneficiaryCnicnicop.text.toString()) &&
                        binding.BeneficaryCountry.text.toString().isNotEmpty() &&
                        binding.etPhoneNumber.text.toString().isNotEmpty() &&
                        viewModel.checkPhoneNumberValidation(binding.etPhoneNumber.text.toString()) &&
                        binding.etBeneficiaryMobileOperator.text.toString().isNotEmpty()

            }
            COMPLAINT_TYPE.UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY ->{
                return viewModel.checkCnicValidation(binding.etPointsbeneficiaryCnicNicp.text.toString())

            }
            COMPLAINT_TYPE.UNABLE_TO_SELF_AWARDS_POINTS -> {
                return (isBeneficiaryAccountValid || isSelfAwardIbanValid || isSelfAwardPassportValid) &&  //binding.etBeneficiaryAccount.text.toString().isNotEmpty() &&
                        isSelfAwardTypeValid &&
                        binding.etTransactionDate.text.toString().isNotEmpty() &&
                        binding.etTransactionamount.text.toString().isNotEmpty() &&
                        ValidationUtils.isTransactionNoValid(binding.etTransactionid.text.toString()) &&    //binding.etTransactionid.text.toString().isNotEmpty() &&
                        binding.etRemitting.text.toString().isNotEmpty()

            }
            COMPLAINT_TYPE.REDEMPTION_ISSUES -> {
                val type: String = viewModel.redemptionPartners.value.toString()
                if ((type.contains("USC", true)
                            || type.contains("BEOE", true)
                            || type.contains("NADRA", true)
                            || type.contains("PASSPORT", true))
                ) {
                    return isSpecifyDetailsValid &&
                            ((isRedemptionCountryValid && isRedemptionBranchCenterValid) ||
                                    (isRedemptionBranchCenterValid && isUscBeoeNumberValid)) &&
                            isRedemptionPartnerValid
                }
                return isSpecifyDetailsValid && isRedemptionPartnerValid
            }
            COMPLAINT_TYPE.OTHERS -> {
                return isSpecifyOtherDetailsValid
            }
            else -> return false
        }
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCodeLength = countryCodeModel.length.toInt()
        viewModel.country.value = countryCodeModel.country
        viewModel.redemptionCountry.value = countryCodeModel.country
        binding.BeneficaryCountry.colorToText(R.color.black)
        binding.NadraPassportCountry.colorToText(R.color.black)
        viewModel.validationBeneficiaryCountryPassed.postValue(true)
        viewModel.validationRedemptionCountryPassed.postValue(true)
        binding.tvCountryCode.text = countryCodeModel.code
        binding.etPhoneNumber.isEnabled = true
        binding.tvCountryCode.colorToText(R.color.black)
        binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        /*binding.etPhoneNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
        fragmentHelper.onBack()
    }

    override fun onSelectBranchCenterListener(branchCenterModel: BranchCenterModel) {
        viewModel.redemptionBranchCenter.value = branchCenterModel.branchCenterName
        //binding.BranchCenter.colorToText(R.color.black)
        viewModel.validationBranchCenterPassed.postValue(true)
        fragmentHelper.onBack()
    }

    override fun onSelectBanksExchangeListener(banksAndExchangeModel: String) {
        viewModel.remittingEntity.value = banksAndExchangeModel
        viewModel.validationRemittingEntityPassed.postValue(true)
        fragmentHelper.onBack()
    }

    private fun openDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val cal = Calendar.getInstance()
        cal.get(Calendar.MONTH)
        cal.get(Calendar.YEAR)
        cal.get(Calendar.DAY_OF_MONTH)
        cal.add(Calendar.DAY_OF_MONTH, -3)
        val datePickerDialog = activity?.let {
            DatePickerDialog(
                it,
                { _, year, monthOfYear, dayOfMonth ->
                    c.set(year, monthOfYear, dayOfMonth)
                    viewModel.transactionDate.value =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString()

                    //viewModel.rawRemittanceDate.value = viewModel.rawDate
                    viewModel.transactionDate.value = viewModel.getDateInStringFormat(c)
                    viewModel.validationTransactionDatePassed.postValue(true)


                }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = 1633028400000L
        datePickerDialog?.datePicker?.maxDate = cal.timeInMillis
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }

    private fun makeIbanView() {
        binding.apply {
            tvBeneficiaryaccount.text = getString(R.string.beneficiary_account_number)
            tvBeneficiaryaccount.visibility = View.VISIBLE
            tilBeneficiaryAccount.visibility = View.GONE
            etBeneficiaryAccount.visibility = View.GONE
            tilBeneficiaryIban.visibility = View.VISIBLE
            etBeneficiaryIban.visibility = View.VISIBLE
            tilBeneficiaryPassport.visibility = View.GONE
            etBeneficiaryPassport.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
            binding.vBuffer.visibility = View.VISIBLE
            icHelpPassport.visibility = View.GONE
            //icHelpBankAccountNumber.visibility = View.VISIBLE
        }
    }

    private fun makeCnicView() {
        binding.apply {
            tvBeneficiaryaccount.text = getString(R.string.beneficiary_account_number_cnic)
            tvBeneficiaryaccount.visibility = View.VISIBLE
            tilBeneficiaryAccount.visibility = View.VISIBLE
            etBeneficiaryAccount.visibility = View.VISIBLE
            tilBeneficiaryIban.visibility = View.GONE
            etBeneficiaryIban.visibility = View.GONE
            tilBeneficiaryPassport.visibility = View.GONE
            etBeneficiaryPassport.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
            binding.vBuffer.visibility = View.VISIBLE
            icHelpPassport.visibility = View.GONE
            //icHelpBankAccountNumber.visibility = View.GONE
        }
    }

    private fun makePassportView() {
        binding.apply {
            tvBeneficiaryaccount.text = getString(R.string.beneficiary_passport_number)
            tvBeneficiaryaccount.visibility = View.VISIBLE
            tilBeneficiaryAccount.visibility = View.GONE
            etBeneficiaryAccount.visibility = View.GONE
            tilBeneficiaryIban.visibility = View.GONE
            etBeneficiaryIban.visibility = View.GONE
            tilBeneficiaryPassport.visibility = View.VISIBLE
            etBeneficiaryPassport.visibility = View.VISIBLE
            binding.vBuffer.visibility = View.VISIBLE
            icHelpPassport.visibility = View.VISIBLE
            //icHelpBankAccountNumber.visibility = View.GONE
        }
    }

    private fun clearSelfAwardData() {
        viewModel.selfAwardIban.postValue("")
        viewModel.cnicAccountNumber.postValue("")
        viewModel.selfAwardPassport.postValue("")
        viewModel.validationSelfAwardCnicPassed.postValue(true)
        viewModel.validationIbanPassed.postValue(true)
        viewModel.validationPassportPassed.postValue(true)
    }

    private fun clearRedemptionData() {
        viewModel.uscBeoeNumber.postValue("")
        viewModel.redemptionCountry.postValue("")
        viewModel.redemptionBranchCenter.postValue("")
    }

    private fun makeUSCBEOEView() {
        binding.apply {
            lyFurtherDetails.visibility = View.VISIBLE
            tvBranchCenter.visibility = View.VISIBLE
            //BranchCenter.visibility = View.VISIBLE
            tilBranchCenter.visibility = View.VISIBLE
            tvMobileNumber.visibility = View.VISIBLE
            lyUSCBEOENumber.visibility = View.VISIBLE
            tvNadraPassportCountry.visibility = View.GONE
            NadraPassportCountry.visibility = View.GONE
        }
    }

    private fun makeNadraPassportView() {
        binding.apply {
            lyFurtherDetails.visibility = View.VISIBLE
            tvBranchCenter.visibility = View.VISIBLE
            //BranchCenter.visibility = View.VISIBLE
            tilBranchCenter.visibility = View.VISIBLE
            tvNadraPassportCountry.visibility = View.VISIBLE
            NadraPassportCountry.visibility = View.VISIBLE
            tvMobileNumber.visibility = View.GONE
            lyUSCBEOENumber.visibility = View.GONE
        }
    }

    private fun hideFurtherDetails() {
        binding.apply {
            lyFurtherDetails.visibility = View.GONE
            tvBranchCenter.visibility = View.GONE
            //BranchCenter.visibility = View.GONE
            tilBranchCenter.visibility = View.GONE
            tvNadraPassportCountry.visibility = View.GONE
            NadraPassportCountry.visibility = View.GONE
            tvMobileNumber.visibility = View.GONE
            lyUSCBEOENumber.visibility = View.GONE
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            RegComplaintDetailsFragment()
    }
}