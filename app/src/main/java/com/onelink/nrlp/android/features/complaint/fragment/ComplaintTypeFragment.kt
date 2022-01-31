package com.onelink.nrlp.android.features.complaint.fragment

import android.os.Bundle
import android.provider.SyncStateContract
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintTypeBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintSharedViewModel
import com.onelink.nrlp.android.utils.Constants
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ComplaintTypeFragment :
    BaseFragment<ComplaintSharedViewModel, FragmentComplaintTypeBinding>(
        ComplaintSharedViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): ComplaintSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(ComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initListeners()
        initViews()
    }

    private fun initListeners(){
        binding.btnNext.setOnClickListener {
            if (viewModel.radioValidationPassed(binding.radio.checkedRadioButtonId)){
                viewModel.gotoComplaintDetailsFragment(resources, fragmentHelper,binding.radio.checkedRadioButtonId)
            }
        }
    }

    private fun initViews(){
        if(viewModel.userType.value.equals(Constants.REMITTER)){
            binding.unableToRegistrationCode.visibility= View.GONE
        }
        else{
            binding.unableToOtp.visibility=View.GONE
        }
    }

    override fun getLayoutRes()= R.layout.fragment_complaint_type

    companion object {
        @JvmStatic
        fun newInstance() =
            ComplaintTypeFragment ()
    }
}