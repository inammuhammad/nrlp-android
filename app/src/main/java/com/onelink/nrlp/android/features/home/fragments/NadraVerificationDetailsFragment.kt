package com.onelink.nrlp.android.features.home.fragments

import android.app.DatePickerDialog
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentNadraVerificationDetailsBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.NadraVerificationsSuccessActivity
import com.onelink.nrlp.android.features.redeem.fragments.REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.redeem.fragments.TAG_REDEMPTION_CREATE_DIALOG
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertCityDialogFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject


const val NADRA_VERIFICATION_DETAILS_SCREEN = 4

class NadraVerificationDetailsFragment :
    BaseFragment<HomeFragmentViewModel, FragmentNadraVerificationDetailsBinding>
    (HomeFragmentViewModel::class.java), SelectCityFragment.OnSelectCityListener,
    OneLinkAlertCityDialogFragment.OneLinkAlertDialogListeners{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    private var sharedViewModel: SharedViewModel? = null

    override fun getLayoutRes() = R.layout.fragment_nadra_verification_details

    override fun getTitle(): String = resources.getString(R.string.nadra_verification_title)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): HomeFragmentViewModel = ViewModelProvider(
        this, viewModelFactory
    ).get(HomeFragmentViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
        initTextWatchers()
        initObservers()
        initOnFocusChangeListeners()
        sharedViewModel?.maxProgress?.postValue(NADRA_VERIFICATION_DETAILS_SCREEN)
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            //showLogoutConfirmationDialog()
            if(viewModel.validationsPassed(
                    binding.etFullName.text.toString(),
                    binding.etMotherMaidenName.text.toString(),
                    binding.etCnicNicopIssuanceDate.text.toString())
            ) {
                oneLinkProgressDialog.showProgressDialog(context)
                viewModel.updateNadraDetails(
                    binding.etMotherMaidenName.text.toString(),
                    binding.tvPlaceOfBirth.text.toString(),
                    binding.etCnicNicopIssuanceDate.text.toString(),
                    binding.etFullName.text.toString()
                )
            }
        }
        binding.etCnicNicopIssuanceDate.setOnClickListener {
            openDatePickerDialog()
            hideKeyboard()
        }
        binding.tvPlaceOfBirth.setOnClickListener {
            fragmentHelper.addFragment(
                SelectCityFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
            validateFields()
        }

        binding.icHelpFullName.setOnClickListener {
            showGeneralAlertDialog(this,"Register",getString(R.string.help_full_name))
        }
    }

    private fun initOnFocusChangeListeners() {

        binding.etFullName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.isFullNameValidationPassed.postValue(
                    viewModel.checkFullNameValidation(
                        binding.etFullName.text.toString()
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

    private fun initTextWatchers() {

        binding.etFullName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
        })

        binding.etMotherMaidenName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }
        })

        binding.tvPlaceOfBirth.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateFields()
            }

        })
    }

    fun initObservers(){

        viewModel.isFullNameValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilFullName.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilFullName.clearError()
                    binding.tilFullName.isErrorEnabled = false
                }
            }
        })

        viewModel.isMotherMaidenNameValidationPassed.observe(this, androidx.lifecycle.Observer{ validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilMotherMaidenName.error = getString(R.string.error_not_valid_mother_name)
                else {
                    binding.tilMotherMaidenName.clearError()
                    binding.tilMotherMaidenName.isErrorEnabled = false
                }
            }
        })

        viewModel.observeUpdateNadraDetails().observe(
            this,
            androidx.lifecycle.Observer { response ->
                when (response.status){
                    Status.SUCCESS -> {
                        UserData.setUpdatedName(binding.etFullName.text.toString())
                        oneLinkProgressDialog.hideProgressDialog()
                        activity?.let { NadraVerificationsSuccessActivity.start(it) }
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
                    binding.etCnicNicopIssuanceDate.text = viewModel.getDateInStringFormat(c)
                    validateFields()
                }, year, month, day
            )
        }
        datePickerDialog?.datePicker?.minDate = -2208958096000L
        datePickerDialog?.datePicker?.maxDate = System.currentTimeMillis()
        datePickerDialog?.datePicker?.layoutDirection = View.LAYOUT_DIRECTION_LTR
        datePickerDialog?.show()
    }

    private fun validateFields(){
        var isValid = true
        binding.apply {
            if(etFullName.text.isNullOrEmpty() || etMotherMaidenName.text.isNullOrEmpty() ||
                   etCnicNicopIssuanceDate.text.isNullOrEmpty() || tvPlaceOfBirth.text.isNullOrEmpty() )
                isValid = false
            binding.btnConfirm.isEnabled=isValid

        }
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
            binding.tvPlaceOfBirth.text = citiesModel.city
            binding.tvPlaceOfBirth.colorToText(R.color.black)
            fragmentHelper.onBack()
        }
    }

    override fun onPositiveButtonClicked(targetCode: Int, city: String) {
        viewModel.placeOfBirth.value = city
        binding.tvPlaceOfBirth.text = city
        binding.tvPlaceOfBirth.colorToText(R.color.black)
        fragmentHelper.onBack()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as HomeActivity).enableSideMenuDrawer(true)
    }

    companion object {
        private const val TAG = "nadraVerificationDetails.fragment"
        @JvmStatic
        fun newInstance() = NadraVerificationDetailsFragment()
    }
}