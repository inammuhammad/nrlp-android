package com.onelink.nrlp.android.features.home.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentNadraVerificationDetailsBinding
import com.onelink.nrlp.android.databinding.FragmentNadraVerificationRequiredBinding
import com.onelink.nrlp.android.features.home.view.HomeActivity
import com.onelink.nrlp.android.features.home.view.NadraVerificationsSuccessActivity
import com.onelink.nrlp.android.features.login.view.LoginFragment
import com.onelink.nrlp.android.features.register.view.RegisterSuccessActivity
import com.onelink.nrlp.android.features.register.viewmodel.SharedViewModel
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.features.select.city.view.SelectCityFragment
import com.onelink.nrlp.android.utils.ValidationUtils
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.toSpanned
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

const val NADRA_VERIFICATION_DETAILS_SCREEN = 4

class NadraVerificationDetailsFragment :
    BaseFragment<HomeFragmentViewModel, FragmentNadraVerificationDetailsBinding>
    (HomeFragmentViewModel::class.java), SelectCityFragment.OnSelectCityListener{

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
        sharedViewModel?.maxProgress?.postValue(NADRA_VERIFICATION_DETAILS_SCREEN)
    }

    private fun initListeners() {
        binding.btnConfirm.setOnClickListener {
            //showLogoutConfirmationDialog()
            oneLinkProgressDialog.showProgressDialog(context)
            viewModel.updateNadraDetails(
                binding.etMotherMaidenName.text.toString(),
                binding.tvPlaceOfBirth.text.toString(),
                binding.etCnicNicopIssuanceDate.text.toString(),
                binding.etFullName.text.toString()
            )
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
    }

    fun initObservers(){
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
            binding.btnConfirm.isEnabled = isValid
        }
    }

    companion object {
        private const val TAG = "nadraVerificationDetails.fragment"
        @JvmStatic
        fun newInstance() = NadraVerificationDetailsFragment()
    }

    override fun onSelectCityListener(citiesModel: CitiesModel) {
        viewModel.placeOfBirth.value = citiesModel.city
        binding.tvPlaceOfBirth.text = citiesModel.city
        validateFields()
        binding.tvPlaceOfBirth.colorToText(R.color.black)
        fragmentHelper.onBack()
    }

}