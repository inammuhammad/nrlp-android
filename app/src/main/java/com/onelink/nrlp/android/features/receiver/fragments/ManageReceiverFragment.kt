package com.onelink.nrlp.android.features.receiver.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentManageReceiverBinding
import com.onelink.nrlp.android.features.beneficiary.adapter.BeneficiariesAdapter
import com.onelink.nrlp.android.features.beneficiary.fragments.BeneficiaryDetailsFragment
import com.onelink.nrlp.android.features.beneficiary.fragments.ManageBeneficiaryFragment
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiarySharedViewModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.ManageBeneficiaryViewModel
import com.onelink.nrlp.android.features.receiver.adapter.ReceiversAdapter
import com.onelink.nrlp.android.features.receiver.models.ReceiverDetailsModel
import com.onelink.nrlp.android.features.receiver.viewmodel.ManageReceiverViewModel
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import javax.inject.Inject

class ManageReceiverFragment : BaseFragment<ManageReceiverViewModel, FragmentManageReceiverBinding>(
    ManageReceiverViewModel::class.java), ReceiversAdapter.ClickEventHandler{

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var receiverSharedViewModel: ReceiverSharedViewModel? = null

    override fun getLayoutRes() = R.layout.fragment_manage_receiver

    override fun getTitle(): String =
        context?.resources?.getString(R.string.remittance_receiver_manager).toString() //resources.getString(R.string.terms_and_conditions)

    override fun getViewM(): ManageReceiverViewModel =
        ViewModelProvider(this, viewModelFactory).get(ManageReceiverViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        activity?.let {
            receiverSharedViewModel =
                ViewModelProvider(it).get(ReceiverSharedViewModel::class.java)
        }
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        val sharedPref = activity?.getSharedPreferences("beneficiarySp", Context.MODE_PRIVATE)
        val limit = sharedPref?.getInt("no_of_beneficiaries_allowed", 0)
        binding.btnNext.isEnabled = true
        viewModel.observeAllReceivers().observe(this, Observer { response ->
            //Log.d("list", it.data.toString())
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data.let {
                        if (it != null) {
                            if (it.data.size > 0){
                                binding.lyNoReceiver.visibility = View.GONE
                                binding.lyReceiversList.visibility = View.VISIBLE
                                val receiversList: List<ReceiverDetailsModel> = it.data
                                binding.rvReceivers.setHasFixedSize(true)
                                binding.rvReceivers.adapter =
                                    ReceiversAdapter(context, receiversList)
                            }
                            else {
                                binding.lyNoReceiver.visibility = View.VISIBLE
                                binding.lyReceiversList.visibility = View.GONE
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let {
                        showGeneralErrorDialog(this, it)
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
            }
        })
        /*viewModel.observeAllBeneficiaries().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        if (it.data.size > 0) {
                            //binding.lyNoBeneficiary.visibility = View.GONE
                            //binding.lyBeneficiariesList.visibility = View.VISIBLE
                            val beneficiariesList: List<BeneficiaryDetailsModel> = it.data
                            binding.rvBeneficiaries.setHasFixedSize(true)
                            binding.rvBeneficiaries.adapter =
                                BeneficiariesAdapter(context, beneficiariesList)
                            //binding.btnNext.isEnabled = beneficiariesList.size < limit!!
                            //Toast.makeText(context, limit.toString(), Toast.LENGTH_LONG).show()
                        } else {
                            //binding.lyNoBeneficiary.visibility = View.VISIBLE
                            binding.lyBeneficiariesList.visibility = View.GONE
                        }
                    }
                }
            }
        })*/
    }

    private fun initListeners() {
        binding.btnNext.setOnClickListener {
            receiverSharedViewModel?.isDeleteBeneficiary?.postValue(false)
            fragmentHelper.addFragment(
                ReceiverTypeFragment
                    .newInstance(),
                clearBackStack = false,
                addToBackStack = true
            )
        }
    }

    override fun refresh() {
        super.refresh()
        viewModel.getAllReceivers()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ManageReceiverFragment()
    }

    override fun onReceiverSelected(receiverDetailsModel: ReceiverDetailsModel) {
        receiverSharedViewModel?.isDeleteBeneficiary?.postValue(true)
        receiverSharedViewModel?.receiverDetails?.postValue(receiverDetailsModel)
        fragmentHelper.addFragment(
            ReceiverDetailsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

}