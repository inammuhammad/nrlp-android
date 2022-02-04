package com.onelink.nrlp.android.features.complaint.fragment.registered

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentComplaintTypeBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.RegComplaintSharedViewModel
import com.onelink.nrlp.android.utils.Constants
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject

class RegComplaintTypeFragment:
    BaseFragment<RegComplaintSharedViewModel, FragmentComplaintTypeBinding>(
        RegComplaintSharedViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): RegComplaintSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(RegComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
        initViews()
    }

    private fun initListeners(){
        binding.btnNext.setOnClickListener {
            if(viewModel.radioValidationPassed(binding.radio2.checkedRadioButtonId)){
                viewModel.gotoComplaintDetailsFragment(resources, fragmentHelper,binding.radio2.checkedRadioButtonId)
            }
        }
    }

    private fun initViews(){
        binding.lyRegComplaintType.visibility=View.VISIBLE
        UserData.getUser()?.let {
            if (it.accountType==Constants.BENEFICIARY.toLowerCase(Locale.getDefault())){
                viewModel.userType.postValue(Constants.BENEFICIARY)
                binding.unableToAddBeneficiary.visibility=View.GONE
                binding.unableToTransferPoints.visibility=View.GONE
                binding.unableToSelfAward.visibility=View.GONE
            }
            else{
                viewModel.userType.postValue(Constants.REMITTER)
            }
        }

    }

    override fun getLayoutRes()= R.layout.fragment_complaint_type

    companion object {
        @JvmStatic
        fun newInstance() =
            RegComplaintTypeFragment ()
    }
}