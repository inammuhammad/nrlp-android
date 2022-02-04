package com.onelink.nrlp.android.features.complaint.fragment.unregistered

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintUserBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class UserTypeComplaintFragment :
    BaseFragment<UnRegComplaintSharedViewModel,FragmentComplaintUserBinding>(
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
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initListeners()
    }

    override fun getLayoutRes()= R.layout.fragment_complaint_user

    private fun initListeners(){
        binding.btnNext.setOnClickListener{
            if (viewModel.radioValidationPassed(binding.radio.checkedRadioButtonId)){
                viewModel.gotoComplaintTypeFragment(resources, fragmentHelper,binding.radio.checkedRadioButtonId)
            }

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserTypeComplaintFragment ()
    }
}