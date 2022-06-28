package com.onelink.nrlp.android.features.selfAwardPoints.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.SelfAwardPointsFragmentBinding
import com.onelink.nrlp.android.features.redeem.fragments.REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TAG_REDEMPTION_CREATE_DIALOG_FBR
import com.onelink.nrlp.android.features.register.fragments.BENEFICIARY_FLOW_SCREENS
import com.onelink.nrlp.android.features.register.fragments.REMITTER_FLOW_SCREENS
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsSharedViewModel
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.utils.SelfAwardRequestConstants
import com.onelink.nrlp.android.utils.cleanNicNumber
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_edit_profile.*
import kotlinx.android.synthetic.main.fragment_receiver_details.*
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject


class SelfAwardPointsFragment :
    BaseFragment<SelfAwardPointsFragmentViewModel, SelfAwardPointsFragmentBinding>(
        SelfAwardPointsFragmentViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    private var listenerInitialized: Boolean = false
//
//    private var selectedBeneficiary = BeneficiaryDetailsModel(-1, BigInteger.ONE, "12312", 0, "")

    private var selfAwardPointSharedViewModel: SelfAwardPointsSharedViewModel? = null

    private var listenerInitialized: Boolean = false

    override fun getLayoutRes() = R.layout.self_award_points_fragment

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String {
        return resources.getString(R.string.self_award_points)
    }

    override fun getViewM(): SelfAwardPointsFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(SelfAwardPointsFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        activity?.let {
            selfAwardPointSharedViewModel =
                ViewModelProvider(it).get(SelfAwardPointsSharedViewModel::class.java)
        }

        binding.spinnerSelectTransactionType.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.selfAwardTransactionTypes)
            )
        } as SpinnerAdapter
        binding.spinnerSelectTransactionType.onItemSelectedListener =
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
                        viewModel.transactionType.postValue(resources.getStringArray(R.array.selfAwardTransactionTypes)[position])
                    } else {
                        listenerInitialized = true
                        binding.spinnerSelectTransactionType.setSelection(-1)
                    }
                }
            }

        //showWarningDialog(getString(R.string.self_award_warning))
        showGeneralAlertDialog(this,"SelfAward",getString(R.string.self_award_warning))

        initListeners()
        initObservers()
        initFocusChangeListener()
    }

    private fun initListeners() {
        binding.lyRemittanceDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog()
        }

        binding.btnNextAccount.setOnSingleClickListener {
            var isValidData = false
            var transactionTypeBE = ""
            var beneficiaryType = ""
            var userNumber = ""
            when (viewModel.transactionType.value) {
                getString(R.string.remittance_to_bank) -> {
                    isValidData = validateIbanFields()
                    transactionTypeBE = "ACC"
                    beneficiaryType = SelfAwardRequestConstants.Beneficiary_ACCOUNT_NUMBER
                    userNumber = binding.etAccountNumber.text.toString()
                }
                getString(R.string.remittance_to_passport) -> {
                    isValidData = validatePassportData()
                    transactionTypeBE = "PPT"
                    beneficiaryType = SelfAwardRequestConstants.Beneficiary_PASSPORT_NUMBER
                    userNumber = binding.etPassportNumber.text.toString()
                }
            }
            if (isValidData) {
                val selfAwardPointsRequest = JsonObject()

                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Amount,
                    binding.etRemittanceAmount.text.toString().replace(",", ""),
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Reference_NO,
                    binding.etRefNo.text.toString(),
                )
                selfAwardPointsRequest.addProperty(
                    beneficiaryType,
                    userNumber
                )
                /*selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Beneficiary_ACCOUNT_NUMBER,
                    binding.etAccountNumber.text.toString()
                )*/
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Transaction_Date,
                    viewModel.remittanceDate.value,
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Transaction_TYPE,
                    transactionTypeBE //viewModel.transactionType.value,
                )

                selfAwardPointSharedViewModel?.setSelfAwardPointsFlowDataModel(
                    selfAwardPointsRequest
                )
                viewModel.verifySafeAwardValidTransaction(selfAwardPointsRequest)
            }
        }

        binding.btnNext.setOnSingleClickListener {
            if (validateCnicFields()) {
                val selfAwardPointsRequest = JsonObject()

                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Amount,
                    binding.etRemittanceAmount.text.toString().replace(",", ""),
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Reference_NO,
                    binding.etRefNo.text.toString(),
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Beneficiary_NIC_NICOP,
                    binding.etCnicAccountNumber.text.toString().replace("-", ""),
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Transaction_Date,
                    viewModel.remittanceDate.value,
                )
                selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Transaction_TYPE,
                    "COC" //viewModel.transactionType.value,
                )

                selfAwardPointSharedViewModel?.setSelfAwardPointsFlowDataModel(
                    selfAwardPointsRequest
                )
                viewModel.verifySafeAwardValidTransaction(selfAwardPointsRequest)
            }
        }
        binding.spinnerLy.setOnClickListener {
            binding.spinnerSelectTransactionType.performClick()
        }
        binding.icHelpTransaction.setOnClickListener {
            //showWarningDialog(getString(R.string.transaction_eligibity_for_self_award))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.enter_remittance_transaction_num_msg_self_award))
        }
        binding.icHelpAmount.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_amount_help))
            showGeneralAlertDialog(this, "SelfAward", getString(R.string.remittance_amount_help))
        }
        binding.icHelpDate.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_date_help))
            showGeneralAlertDialog(this, "SelfAward", getString(R.string.remittance_date_help))
        }
        binding.icHelpCnicAccountNumber.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_date_help))
            showGeneralAlertDialog(
                this,
                "SelfAward",
                getString(R.string.remittance_account_cnic_number_help)
            )
        }
        binding.icHelpPassportNumber.setOnClickListener {
            showGeneralAlertDialog(this, "SelfAward", getString(R.string.beneficiary_passport_help))
        }

        binding.etCnicAccountNumber.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    binding.etCnicAccountNumber.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12) +
                                inputString.substring(12, 13)
                    )
                    binding.etCnicAccountNumber.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    binding.etCnicAccountNumber.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    binding.etCnicAccountNumber.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    binding.etCnicAccountNumber.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(binding.etCnicAccountNumber.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    binding.etCnicAccountNumber.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.etCnicAccountNumber.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.etCnicAccountNumber.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = binding.etCnicAccountNumber.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    //viewModel.cnicNotEmpty.postValue(true)
                    when {
                        Pattern.matches(regex1, inputString)
                                || Pattern.matches(regex2, inputString) -> {
                            binding.etCnicAccountNumber.setText("$inputString-")
                            binding.etCnicAccountNumber.setSelection(inputString.length + 1)
                        }
                        Pattern.matches(regex3, inputString) -> {
                            binding.etCnicAccountNumber.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            binding.etCnicAccountNumber.setSelection(inputString.length + 1)
                        }
                    }
                }
                binding.etCnicAccountNumber.addTextChangedListener(this)
            }
        })
    }

    private fun validateCnicFields(): Boolean {
        return viewModel.validReferenceNumber.value!! && viewModel.validTransactionAmount.value!! && viewModel.validCnicAccountNumber.value!!
    }

    private fun validateIbanFields(): Boolean {
        return viewModel.validReferenceNumber.value!! && viewModel.validTransactionAmount.value!! && viewModel.validIbanAccountNumber.value!!
    }

    private fun validatePassportData(): Boolean {
        return viewModel.validReferenceNumber.value!! && viewModel.validTransactionAmount.value!! && viewModel.validPassportNumber.value!!
    }

    private fun initObservers() {
        viewModel.observeSafeAwardValidTransaction().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        selfAwardPointSharedViewModel?.setSelfAwardRowIdModel(it.rowID)
                        fragmentHelper.addFragment(
                            SelfAwardPointsOTPFragment.newInstance(),
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

        viewModel.transactionType.observe(this, Observer {
            binding.apply {
                tvRemittanceTransactionType.text = it
                tvRemittanceTransactionType.colorToText(R.color.black)
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
                clearFields()
            }
        })

        viewModel.validReferenceNumber.observe(this, Observer { validationPassed ->
            run {
                if (!validationPassed)
                    binding.tilRefNo.error = getString(R.string.error_invalid_reference_number)
                else {
                    binding.tilRefNo.clearError()
                    binding.tilRefNo.isErrorEnabled = false
                }
            }
        })

        viewModel.validTransactionAmount.observe(this, Observer { validationPassed ->
            run {
                if (!validationPassed)
                    binding.tilRemittanceAmount.error = getString(R.string.error_transaction_amount)
                else {
                    binding.tilRemittanceAmount.clearError()
                    binding.tilRemittanceAmount.isErrorEnabled = false
                }
            }
        })

        viewModel.validCnicAccountNumber.observe(this, Observer { validationPassed ->
            run {
                if (!validationPassed)
                    binding.tilCnicAccountNumber.error = getString(R.string.error_cnic)
                else {
                    binding.tilCnicAccountNumber.clearError()
                    binding.tilCnicAccountNumber.isErrorEnabled = false
                }
            }
        })

        viewModel.validIbanAccountNumber.observe(this, Observer { validationPassed ->
            run {
                if (!validationPassed)
                    binding.tilAccountNumber.error =
                        getString(R.string.error_invalid_account_number)
                else {
                    binding.tilAccountNumber.clearError()
                    binding.tilAccountNumber.isErrorEnabled = false
                }
            }
        })

        viewModel.validPassportNumber.observe(this, Observer { validationPassed ->
            run {
                if (!validationPassed)
                    binding.tilPassportNumber.error = getString(R.string.error_passport_mumber)
                else {
                    binding.tilPassportNumber.clearError()
                    binding.tilPassportNumber.isErrorEnabled = false
                }
            }
        })

    }

    private fun initFocusChangeListener() {
        binding.etRefNo.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validReferenceNumber
            }
        }

        binding.etRemittanceAmount.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validTransactionAmount
            }
        }

        binding.etCnicAccountNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validCnicAccountNumber
            }
        }

        binding.etAccountNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validIbanAccountNumber
            }
        }
        binding.etPassportNumber.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validPassportNumber
            }
        }
        viewModel.validCnicAccountNumber.postValue(true)
        viewModel.validReferenceNumber.postValue(true)
        viewModel.validTransactionAmount.postValue(true)
        viewModel.validIbanAccountNumber.postValue(true)
        viewModel.validPassportNumber.postValue(true)
    }

    private fun clearFields() {
        viewModel.accountIbanNumber.postValue("")
        viewModel.passportNumber.postValue("")
        viewModel.validPassportNumber.postValue(true)
        viewModel.validIbanAccountNumber.postValue(true)

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
                        viewModel.rawDate =
                                dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year.toString()

                        viewModel.rawRemittanceDate.value = viewModel.rawDate
                        viewModel.remittanceDate.value = viewModel.getDateInStringFormat(c)


                    }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = MILLIS_MINIMUM_DATE
        datePickerDialog?.datePicker?.maxDate = cal.timeInMillis
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }

    private fun makeIbanView(){
        binding.apply {
            tvCnicAccountNumber.visibility = View.VISIBLE
            icHelpCnicAccountNumber.visibility = View.VISIBLE
            tvCnicAccountNumber.text = getString(R.string.beneficiary_account_number)
            tilCnicAccountNumber.visibility = View.GONE
            tilPassportNumber.visibility = View.GONE
            tilAccountNumber.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            btnNextAccount.visibility = View.VISIBLE
            icHelpPassportNumber.visibility = View.GONE
            //icHelpBankAccountNumber.visibility = View.VISIBLE
        }
    }

    private fun makeCnicView(){
        binding.apply {
            tvCnicAccountNumber.visibility = View.VISIBLE
            icHelpCnicAccountNumber.visibility = View.VISIBLE
            tvCnicAccountNumber.text = getString(R.string.beneficiary_account_number_cnic)
            tilCnicAccountNumber.visibility = View.VISIBLE
            tilAccountNumber.visibility = View.GONE
            tilPassportNumber.visibility = View.GONE
            btnNext.visibility = View.VISIBLE
            btnNextAccount.visibility = View.GONE
            icHelpPassportNumber.visibility = View.GONE
            //icHelpBankAccountNumber.visibility = View.GONE
        }
    }

    private fun makePassportView() {
        binding.apply {
            tvCnicAccountNumber.visibility = View.VISIBLE
            icHelpCnicAccountNumber.visibility = View.VISIBLE
            tvCnicAccountNumber.text = getString(R.string.beneficiary_passport_number)
            tilCnicAccountNumber.visibility = View.GONE
            tilAccountNumber.visibility = View.GONE
            tilPassportNumber.visibility = View.VISIBLE
            btnNext.visibility = View.GONE
            btnNextAccount.visibility = View.VISIBLE
            icHelpPassportNumber.visibility = View.VISIBLE
            //icHelpBankAccountNumber.visibility = View.GONE
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() = SelfAwardPointsFragment()
        const val MILLIS_MINIMUM_DATE = 1633028400000L
    }
}