package com.onelink.nrlp.android.features.complaint.fragment.unregistered

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintTypeBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import com.onelink.nrlp.android.utils.Constants
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UnregComplaintTypeFragment :
    BaseFragment<UnRegComplaintSharedViewModel, FragmentComplaintTypeBinding>(
        UnRegComplaintSharedViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle(): String = getString(R.string.complaint)

    override fun getViewM(): UnRegComplaintSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(UnRegComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
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
        binding.lyUnregComplaintType.visibility=View.VISIBLE
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
            UnregComplaintTypeFragment ()
    }
}