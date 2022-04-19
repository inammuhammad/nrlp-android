package com.onelink.nrlp.android.features.receiver.fragments

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentReceiverTypeBinding
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.utils.colorToText
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class ReceiverTypeFragment : BaseFragment<ReceiverSharedViewModel, FragmentReceiverTypeBinding>(
    ReceiverSharedViewModel::class.java){

    override fun getTitle(): String = resources.getString(R.string.remittance_receiver_manager)

    override fun getLayoutRes() = R.layout.fragment_receiver_type

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    private var listenerInitialized: Boolean = false

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): ReceiverSharedViewModel =
        ViewModelProvider(requireActivity(),viewModelFactory).get(ReceiverSharedViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        binding.spinnerSelectReceiverType.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.custom_spinner_item,
                resources.getStringArray(R.array.selfAwardTransactionTypes)
            )
        } as SpinnerAdapter
        binding.spinnerSelectReceiverType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // on nothing selected
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (listenerInitialized) {
                        viewModel.receiverType.postValue(resources.getStringArray(R.array.selfAwardTransactionTypes)[position])
                    } else {
                        listenerInitialized = true
                        binding.spinnerSelectReceiverType.setSelection(-1)
                    }
                }
            }
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.btnNext.setOnClickListener {
            //viewModel.receiverType.postValue(binding.rdReceiverType.checkedRadioButtonId)
            viewModel.isDeleteBeneficiary.postValue(false)
            fragmentHelper.addFragment(
                ReceiverDetailsFragment.newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
        binding.spinnerLy.setOnClickListener {
            binding.spinnerSelectReceiverType.performClick()
        }
    }

    private fun initObservers() {
        viewModel.receiverType.observe(this, Observer {
            binding.apply{
                tvRemittanceReceiverType.text = it
                tvRemittanceReceiverType.colorToText(R.color.black)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ReceiverTypeFragment()
    }
}