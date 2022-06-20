package com.onelink.nrlp.android.features.viewStatement.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentAdvancedLoyaltyStatementBinding
import com.onelink.nrlp.android.features.viewStatement.models.DetailedStatementRequestModel
import com.onelink.nrlp.android.features.viewStatement.viewmodel.AdvancedLoyaltyStatementFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_advanced_loyalty_statement.*
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
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
                        val pdfData = it?.byteStream()
                        if (pdfData != null) {
                            context?.let { it1 ->
                                openPDFContent(
                                    it1,
                                    pdfData,
                                    "SDRP-Statement.pdf"
                                )
                            }
                        }
                        /* if (pdfData != null) {

                             try {
                                 context?.openFileOutput("statement.pdf", Context.MODE_PRIVATE)
                                     .use { output ->
                                     output?.write(pdfData.readBytes())
                                 }

                             } catch (e: IOException) {
                                 e.printStackTrace()
                             }
                         }*/
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
            if (isDateValid()) {
                viewModel.getDetailedStatements(
                    DetailedStatementRequestModel(
                        email = etEmailAddress.text.toString(),
                        fromDate = viewModel.getDateInApiFormat(viewModel.rawFromDate.value.toString()),
                        toDate = viewModel.getDateInApiFormat(viewModel.rawToDate.value.toString())
                    )
                )
            }
        }
    }

    private fun openDatePickerDialog(type: String) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val cal = Calendar.getInstance()
        cal.get(Calendar.MONTH)
        cal.get(Calendar.YEAR)
        cal.get(Calendar.DAY_OF_MONTH)
        cal.add(Calendar.YEAR, -1)
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

    private fun openPDFContent(context: Context, inputStream: InputStream, fileName: String) {
        val filePath = context.externalCacheDir?.absolutePath ?: context.cacheDir.absolutePath
        val fileNameExtension =
            if (!fileName?.isNullOrEmpty()) fileName else context.getString(R.string.app_name) + ".pdf"
        val file = inputStream.saveToFile(filePath, fileNameExtension)

        val uri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + ".provider", file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, "application/pdf")
        }
        context.startActivity(Intent.createChooser(intent, "Select app"))
    }

    private fun InputStream.saveToFile(filePath: String, fileName: String): File = use { input ->
        val file = File(filePath, fileName)
        file.outputStream().use { output ->
            input.copyTo(output)
        }
        input.close()
        file
    }

    private fun isDateValid(): Boolean {
        var days = 0L
        try {
            val startDate = viewModel.rawFromDate.value
            val endDate = viewModel.rawToDate.value
            val sdf: SimpleDateFormat = SimpleDateFormat("dd/M/yyyy", Locale.US)
            val startDateParsed = sdf.parse(startDate)
            val endDateParsed = sdf.parse(endDate)
            days = getDiffYears(startDateParsed, endDateParsed)

        } catch (e: Exception) {

        }
        return days <= 365
    }

    fun getDiffYears(first: Date?, last: Date?): Long {
        val diff: Long = last?.time?.minus(first?.time!!) ?: 1633028400000L
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        return hours / 24
    }

    fun getDiffMonths(first: Date?, last: Date?): Int {
        val a = getCalendar(first)
        val b = getCalendar(last)
        var diff = b[MONTH] - a[MONTH]
        if (a[DATE] > b[DATE]) {
            diff--
        }
        return diff
    }

    fun getCalendar(date: Date?): Calendar {
        val cal = Calendar.getInstance(Locale.US)
        cal.time = date
        return cal
    }

    companion object {
        @JvmStatic
        fun newInstance() = AdvancedLoyaltyStatementFragment()
        const val MILLIS_MINIMUM_DATE = 1633028400000L //1577818800000L
    }
}
