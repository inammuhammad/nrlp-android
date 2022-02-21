package com.onelink.nrlp.android.features.receiver.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentComplaintTypeBinding
import com.onelink.nrlp.android.databinding.FragmentReceiverSuccessBinding
import com.onelink.nrlp.android.features.complaint.viewmodel.RegComplaintSharedViewModel
import com.onelink.nrlp.android.features.receiver.view.ReceiverActivity
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverDetailsViewModel
import com.onelink.nrlp.android.features.select.country.view.SelectCountryFragment
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ReceiverSuccessFragment :
        BaseFragment<RegComplaintSharedViewModel, FragmentReceiverSuccessBinding>(
        RegComplaintSharedViewModel::class.java
    ){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_receiver_success

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getTitle() = resources.getString(R.string.remittance_receiver_manager)

    override fun getViewM(): RegComplaintSharedViewModel =
        ViewModelProvider(requireActivity(), viewModelFactory).get(RegComplaintSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        initListeners()
    }

    private fun initListeners(){
        binding.buttonNext.setOnSingleClickListener {
            if((activity as ReceiverActivity).isFromHome)
                (activity as ReceiverActivity).finish()
            else {
                fragmentHelper.addFragment(
                    ManageReceiverFragment.newInstance(),
                    clearBackStack = true,
                    addToBackStack = false
                )
            }
        }
    }

    companion object{
        @JvmStatic
        fun newInstance() = ReceiverSuccessFragment()
    }

}