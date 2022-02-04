package com.onelink.nrlp.android.features.complaint.fragment.unregistered

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintResponseBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.UnRegComplaintSharedViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class UnregComplaintResponseFragment() :
BaseFragment<UnRegComplaintSharedViewModel, FragmentComplaintResponseBinding>(
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
        var complaint=resources.getString(R.string.complaint1)
        complaint=complaint+" " +viewModel.complaintId.value
        binding.tvComplaint1.setText(complaint)
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
            UnregComplaintResponseFragment()
    }
}