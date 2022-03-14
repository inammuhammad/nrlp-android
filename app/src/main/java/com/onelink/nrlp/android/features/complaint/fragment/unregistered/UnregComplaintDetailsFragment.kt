package com.onelink.nrlp.android.features.complaint.fragment.unregistered

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentUnregComplaintDetailsBinding
import com.onelink.nrlp.android.features.complaint.view.UnregComplaintActivity
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import com.onelink.nrlp.android.features.select.country.model.CountryCodeModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.*
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import java.util.*
import java.util.regex.Pattern
import javax.inject.Inject

class UnregComplaintDetailsFragment :
    BaseFragment<UnRegComplaintSharedViewModel, FragmentUnregComplaintDetailsBinding>(
        UnRegComplaintSharedViewModel::class.java
    ), SelectCountryFragment.OnSelectCountryListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    private var countryCodeLength: Int? = 10
    private var tvCountryClicked: Boolean = false
    lateinit var details:String
    private val UNREGISTERED:Int=0
    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): UnRegComplaintSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(UnRegComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.resourceProvider(resources)
        initOnFocusChangeListeners()
        initObservers()
        initListeners()
        initViews()
    }

    private fun initViews(){
        when(viewModel.getComplaintType()){

            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_OTP -> {
                binding.tvDetails.visibility=View.GONE
                binding.tilDetails.visibility=View.GONE
                binding.tilCnic.visibility=View.GONE
                binding.tvCnic.visibility=View.GONE
                binding.tilMobileOperator.visibility=View.VISIBLE
                binding.tvMobileOperator.visibility=View.VISIBLE
            }

            COMPLAINT_TYPE.UNABLE_TO_RECEIVE_REGISTRATION_CODE -> {
                binding.tilMobileOperator.visibility=View.VISIBLE
                binding.tvMobileOperator.visibility=View.VISIBLE
                binding.tilName.visibility=View.GONE
                binding.tvName.visibility=View.GONE
                binding.tilCnic.visibility=View.GONE
                binding.tvCnic.visibility=View.GONE
                binding.tilDetails.visibility=View.GONE
                binding.tvDetails.visibility=View.GONE
            }

        }

    }
    private fun initListeners(){
        binding.btnNext.setOnSingleClickListener {

            if(binding.etDetails.visibility==View.VISIBLE)
            {
                details=binding.etDetails.text.toString()
            }
            else
            {
                details=""
            }

            if(viewModel.detailsValidationsPassed(
                    phoneNumber = binding.etPhoneNumber.text.toString(),
                    phoneNumberLength = countryCodeLength,
                    country = binding.tvCountry.text.toString()
                )){
                oneLinkProgressDialog.showProgressDialog(context)

                viewModel.makeComplainCall(getComplaintRequestJsonObject())
            }

        }
        binding.tvCountry.setOnSingleClickListener {
            tvCountryClicked = true
            fragmentHelper.addFragment(
                SelectCountryFragment.newInstance(getUserType()),
                clearBackStack = false,
                addToBackStack = true
            )
            hideKeyboard()
        }
    }

    private fun initOnFocusChangeListeners() {
        binding.etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
                binding.etName.clearFocus()
                true
            } else false
        }

        binding.etCnic.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationCnicPassed.postValue(
                    viewModel.checkCnicValidation(
                        binding.etCnic.text.toString()
                    )
                )
            }
        }

        binding.etName.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationAliasPassed.postValue(
                    viewModel.checkAliasValidation(
                        binding.etName.text.toString()
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
        binding.etEmail.setOnFocusChangeListener { _, b ->
            when (b) {
                false -> viewModel.validationEmailPassed.postValue(
                    viewModel.checkEmailValidation(
                        binding.etEmail.text.toString()
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
        binding.etCnic.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                hideKeyboard()
                binding.etCnic.clearFocus()
                true
            } else false
        }
        (activity as UnregComplaintActivity).selectedCountry.observe(this, androidx.lifecycle.Observer { countryCodeModel ->
            viewModel.country.value = countryCodeModel.country
            binding.tvCountry.colorToText(R.color.black)
            binding.tvCountryCode.text = countryCodeModel.code
            binding.etPhoneNumber.isEnabled = true
            binding.tvCountryCode.colorToText(R.color.black)
            binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        })
        binding.etCnic.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12) +
                                inputString.substring(12, 13)
                    )
                    binding.etCnic.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    binding.etCnic.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    binding.etCnic.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(binding.etCnic.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    binding.etCnic.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.etCnic.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                binding.etCnic.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = binding.etCnic.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    viewModel.cnicNumberNotEmpty.postValue(true)
                    when {
                        Pattern.matches(regex1, inputString)
                                || Pattern.matches(regex2, inputString) -> {
                            binding.etCnic.setText("$inputString-")
                            binding.etCnic.setSelection(inputString.length + 1)
                        }
                        Pattern.matches(regex3, inputString) -> {
                            binding.etCnic.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            binding.etCnic.setSelection(inputString.length + 1)
                        }
                    }
                }
                binding.etCnic.addTextChangedListener(this)
            }
        })


        viewModel.validationAliasPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilName.error = getString(R.string.error_not_valid_name)
                else {
                    binding.tilName.clearError()
                    binding.tilName.isErrorEnabled = false
                }
            }
        })

        viewModel.validationEmailPassed.observe(this, { validationsPassed ->
            run {
                if (!validationsPassed)
                    binding.tilEmail.error =
                        getString(R.string.error_email_not_valid)
                else {
                    binding.tilEmail.clearError()
                    binding.tilEmail.isErrorEnabled = false
                }
            }
        })

        viewModel.validationCnicPassed.observe(
            this,
             { validationsPassed ->
                run {
                    if (!validationsPassed)
                        binding.tilCnic.error = getString(R.string.error_cnic)
                    else {
                        binding.tilCnic.clearError()
                        binding.tilCnic.isErrorEnabled = false
                    }
                }
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
    }
    private fun getUserType(): String {
        val selectedType = viewModel.userType.value.toString()
        if(selectedType.contains(resources.getString(R.string.select_account_type), true))
        {
            return "beneficiary"
        }
        return selectedType.toLowerCase()
    }

    override fun onSelectCountryListener(countryCodeModel: CountryCodeModel) {
        countryCodeLength = countryCodeModel.length.toInt()
        viewModel.country.value = countryCodeModel.country
        binding.tvCountry.colorToText(R.color.black)
        binding.tvCountryCode.text = countryCodeModel.code
        binding.etPhoneNumber.isEnabled = true
        binding.tvCountryCode.colorToText(R.color.black)
        binding.etPhoneNumber.hint = viewModel.phoneNumberHint(countryCodeModel.length.toInt())
        /*binding.etPhoneNumber.filters =
            arrayOf(InputFilter.LengthFilter(countryCodeModel.length.toInt()))*/
        fragmentHelper.onBack()
    }

    private fun getComplaintRequestJsonObject() : JsonObject
    {
        val requestModel = JsonObject()
        requestModel.addProperty(
            ComplaintRequestModelConstants.Registered,UNREGISTERED
        )
        requestModel.addProperty(
            ComplaintRequestModelConstants.User_type,
            viewModel.userType.value.toString().toLowerCase(Locale.getDefault())
        )
        requestModel.addProperty(
            ComplaintRequestModelConstants.Complaint_type_id,
            viewModel.complaintTypeIndex.value!!.toInt()
        )
        requestModel.addProperty(
            ComplaintRequestModelConstants.Country_of_residence,
            binding.tvCountry.text.toString()
        )
        requestModel.addProperty(
            ComplaintRequestModelConstants.Mobile_no,
            binding.tvCountryCode.text.toString() + binding.etPhoneNumber.text.toString(),
        )
        requestModel.addProperty(
            ComplaintRequestModelConstants.Email,
            binding.etEmail.text.toString()
        )


        if(viewModel.complaintText.value!!.toLowerCase(Locale.getDefault()).
            equals(resources.getString(UnregisteredComplaintTypes.UNABLE_TO_RECEIVE_REGISTRATION_CODE),
                true))
            {
                requestModel.addProperty(
                    ComplaintRequestModelConstants.Mobile_Operator,
                    binding.etMobileOperator.text.toString()
                )
            }
        else if(viewModel.complaintText.value!!.toLowerCase(Locale.getDefault()).
            equals(resources.getString(UnregisteredComplaintTypes.UNABLE_TO_RECEIVE_OTP),
                true)){
            requestModel.addProperty(
                ComplaintRequestModelConstants.Mobile_Operator,
                binding.etMobileOperator.text.toString()
            )
            requestModel.addProperty(
                ComplaintRequestModelConstants.Name,
                binding.etName.text.toString()
            )
        }
        else{
            requestModel.addProperty(
                ComplaintRequestModelConstants.Nic_nicop,
                binding.etCnic.text.toString().removeDashes()
            )
            requestModel.addProperty(
                ComplaintRequestModelConstants.Comments,
                binding.etDetails.text.toString()
            )
            requestModel.addProperty(
                ComplaintRequestModelConstants.Name,
                binding.etName.text.toString()
            )
        }
        return requestModel

    }

    override fun getLayoutRes()= R.layout.fragment_unreg_complaint_details

    companion object {
        @JvmStatic
        fun newInstance() =
            UnregComplaintDetailsFragment ()
    }
}