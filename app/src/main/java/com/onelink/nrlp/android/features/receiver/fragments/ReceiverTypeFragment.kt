package com.onelink.nrlp.android.features.receiver.fragments

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentReceiverTypeBinding
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ReceiverTypeFragment : BaseFragment<ReceiverSharedViewModel, FragmentReceiverTypeBinding>(
    ReceiverSharedViewModel::class.java){

    override fun getTitle(): String = "Choose Receiver Type"

    override fun getLayoutRes() = R.layout.fragment_receiver_type

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): ReceiverSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(ReceiverSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
    }

    private fun initListeners(){
        binding.btnNext.setOnClickListener {
            viewModel.receiverType.postValue(binding.rdReceiverType.checkedRadioButtonId)
            viewModel.isDeleteBeneficiary.postValue(false)
            fragmentHelper.addFragment(
                ReceiverDetailsFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiverTypeFragment()
    }
}