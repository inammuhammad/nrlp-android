package com.onelink.nrlp.android.features.selfAwardPoints.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Spanned
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.SelfAwardPointsFragmentBinding
import com.onelink.nrlp.android.features.redeem.fragments.REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TAG_REDEMPTION_CREATE_DIALOG_FBR
import com.onelink.nrlp.android.features.selfAwardPoints.model.SelfAwardPointsRequest
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsFragmentViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsSharedViewModel
import com.onelink.nrlp.android.features.viewStatement.fragments.AdvancedLoyaltyStatementFragment
import com.onelink.nrlp.android.utils.SelfAwardRequestConstants
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import java.util.*
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

        //showWarningDialog(getString(R.string.self_award_warning))
        //showGeneralAlertDialog(this,"SelfAward",getString(R.string.self_award_warning))

        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.lyRemittanceDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog()
        }

        binding.btnNext.setOnSingleClickListener {
            val selfAwardPointsRequest=JsonObject()

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
                binding.etCnicAccountNumber.text.toString(),
            )
            selfAwardPointsRequest.addProperty(
                    SelfAwardRequestConstants.Transaction_Date,
            viewModel.remittanceDate.value,
            )

            selfAwardPointSharedViewModel?.setSelfAwardPointsFlowDataModel(selfAwardPointsRequest)
            viewModel.verifySafeAwardValidTransaction(selfAwardPointsRequest)
        }
        binding.icHelpTransaction.setOnClickListener {
            //showWarningDialog(getString(R.string.transaction_eligibity_for_self_award))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.enter_remittance_transaction_num_msg_self_award))
        }
        binding.icHelpAmount.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_amount_help))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.remittance_amount_help))
        }
        binding.icHelpDate.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_date_help))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.remittance_date_help))
        }
        binding.icHelpCnicAccountNumber.setOnClickListener {
            //showWarningDialog(getString(R.string.remittance_date_help))
            showGeneralAlertDialog(this,"SelfAward",getString(R.string.remittance_account_cnic_number_help))
        }
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

                        viewModel.rawRemittanceDate.value = viewModel.rawDate
                        viewModel.remittanceDate.value = viewModel.getDateInStringFormat(c)


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
        fun newInstance() = SelfAwardPointsFragment()
    }
}