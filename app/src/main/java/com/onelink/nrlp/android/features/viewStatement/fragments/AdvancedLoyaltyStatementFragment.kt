package com.onelink.nrlp.android.features.viewStatement.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentAdvancedLoyaltyStatementBinding
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.view.StatementGeneratedActivity
import com.onelink.nrlp.android.features.viewStatement.viewmodel.AdvancedLoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_advanced_loyalty_statement.*
import java.util.*
import javax.inject.Inject

const val FROM_DATE = "from_date"
const val TO_DATE = "to_date"

@Suppress("NAME_SHADOWING")
class AdvancedLoyaltyStatementFragment :
    BaseFragment<AdvancedLoyaltyStatementFragmentViewModel, FragmentAdvancedLoyaltyStatementBinding>(
        AdvancedLoyaltyStatementFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun getLayoutRes() = R.layout.fragment_advanced_loyalty_statement

    override fun getTitle(): String = resources.getString(R.string.view_more_transactions)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): AdvancedLoyaltyStatementFragmentViewModel =
        ViewModelProvider(
            this,
            viewModelFactory
        ).get(AdvancedLoyaltyStatementFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        UserData.getUser()?.let {
            viewModel.emailAddress.value = it.email ?: ""
        }
        initListeners()
        initObservers()
    }

    private fun initObservers() {
        viewModel.isEmailValidationPassed.observe(this, androidx.lifecycle.Observer {
            run {
                tilEmailAddress.error =
                    if (it) null else resources.getString(R.string.error_email_not_valid)
            }
        })
        viewModel.observeDetailedStatement().observe(this, androidx.lifecycle.Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data.let {
                        /*activity?.let {
                            activity?.startActivity(StatementGeneratedActivity.createIntent(it))
                            activity?.finish()
                        }*/
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
        }
        )

    }

    private fun initListeners() {
        lyFromDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog(FROM_DATE)
        }
        lyToDate.setOnClickListener {
            hideKeyboard()
            openDatePickerDialog(TO_DATE)
        }

        btnNext.setOnClickListener {
            //if (viewModel.validationsPassed(etEmailAddress.text.toString())) {
                viewModel.getDetailedStatements(
                    DetailedStatementRequestModel(
                        email = etEmailAddress.text.toString(),
                        fromDate = viewModel.getDateInApiFormat(viewModel.rawFromDate.value.toString()),
                        toDate = viewModel.getDateInApiFormat(viewModel.rawToDate.value.toString())
                    )
                )
            //}
        }
    }

    private fun openDatePickerDialog(type: String) {
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
                    when (type) {
                        FROM_DATE -> {
                            viewModel.rawFromDate.value = viewModel.rawDate
                            viewModel.fromDate.value =
                                viewModel.getDateInStringFormat(c)
                        }
                        TO_DATE -> {
                            viewModel.rawToDate.value = viewModel.rawDate
                            viewModel.toDate.value =
                                viewModel.getDateInStringFormat(c)
                        }
                    }

                }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = MILLIS_MINIMUM_DATE
        datePickerDialog?.datePicker?.maxDate = System.currentTimeMillis()
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() = AdvancedLoyaltyStatementFragment()
        const val MILLIS_MINIMUM_DATE = 1577818800000L
    }
}
