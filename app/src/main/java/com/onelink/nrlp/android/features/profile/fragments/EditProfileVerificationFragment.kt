package com.onelink.nrlp.android.features.profile.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentUpdateProfileValidationBinding
import com.onelink.nrlp.android.features.profile.enabled
import com.onelink.nrlp.android.features.profile.viewmodel.EditProfileVerificationViewModel
import com.onelink.nrlp.android.features.profile.viewmodel.ProfileSharedViewModel
import com.onelink.nrlp.android.utils.RequestModelConstants
import com.onelink.nrlp.android.utils.dialogs.OneLinkAlertDialogsFragment
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class EditProfileVerificationFragment :
    BaseFragment<EditProfileVerificationViewModel,FragmentUpdateProfileValidationBinding>(
        EditProfileVerificationViewModel::class.java
    ), OneLinkAlertDialogsFragment.OneLinkAlertDialogListeners{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    private lateinit var profileSharedViewModel: ProfileSharedViewModel

    override fun getTitle() = resources.getString(R.string.verification)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): EditProfileVerificationViewModel =
        ViewModelProvider(this,viewModelFactory).get(EditProfileVerificationViewModel::class.java)

    override fun getLayoutRes()= R.layout.fragment_update_profile_validation

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel=viewModel
        activity?.let {
            profileSharedViewModel = ViewModelProvider(it).get(ProfileSharedViewModel::class.java)
        }
        initObserver()
        initListeners()
    }

    private fun initListeners(){
        binding.btnNext.setOnSingleClickListener {
            if(viewModel.isValidationsPassed
                    (binding.etMothername.text.toString())
            ){
                viewModel.checkNADRAverification(getJsonObject())
            }
        }
    }

    private fun initObserver(){
        viewModel.observeProfileValidation().observe(this, { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    profileSharedViewModel.validationSuccessful.postValue(true)
                    profileSharedViewModel.motherMaidenName.postValue(binding.etMothername.text.toString())
                    fragmentHelper.onBack()
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                        profileSharedViewModel.validationSuccessful.postValue(false)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })

        viewModel.motherNameValidationPassed.observe(this,{ validationPassed->
            if(!validationPassed){
                binding.tilMothername.error=resources.getString(R.string.provide_necessary_information)
            }else{
                binding.tilMothername.clearError()
                binding.tilMothername.isErrorEnabled=false
            }
        })

        viewModel.motherMaidenNameNotEmpty.observe(this,{ validationPassed->
            binding.btnNext.isEnabled = validationPassed
        })
    }

    private fun getJsonObject():JsonObject{
        val jsonObject=JsonObject()
        jsonObject.addProperty(
            RequestModelConstants.Mother_name,
            binding.etMothername.text.toString()
        )
        return jsonObject
    }

    companion object{
        @JvmStatic
        fun newInstance() = EditProfileVerificationFragment()
    }
}