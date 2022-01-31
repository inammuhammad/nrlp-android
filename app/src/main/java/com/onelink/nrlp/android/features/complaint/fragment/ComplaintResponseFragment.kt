package com.onelink.nrlp.android.features.complaint.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintResponseBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.ComplaintSharedViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ComplaintResponseFragment:
BaseFragment<ComplaintSharedViewModel, FragmentComplaintResponseBinding>(
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
    }

    private fun initListeners(){
        binding.btnDone.setOnClickListener {
            fragmentHelper.onBack()
        }
    }


    override fun getLayoutRes()= R.layout.fragment_complaint_response

    companion object {
        @JvmStatic
        fun newInstance() =
            ComplaintResponseFragment()
    }
}