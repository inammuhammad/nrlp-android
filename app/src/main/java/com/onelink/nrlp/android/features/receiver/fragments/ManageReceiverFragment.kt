package com.onelink.nrlp.android.features.receiver.fragments

import android.content.Context
import android.os.Bundle
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
import com.onelink.nrlp.android.features.receiver.viewmodel.ManageReceiverViewModel
import com.onelink.nrlp.android.features.receiver.viewmodel.ReceiverSharedViewModel
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import javax.inject.Inject

class ManageReceiverFragment : BaseFragment<ManageReceiverViewModel, FragmentManageReceiverBinding>(
    ManageReceiverViewModel::class.java), BeneficiariesAdapter.ClickEventHandler{

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var receiverSharedViewModel: ReceiverSharedViewModel? = null

    override fun getLayoutRes() = R.layout.fragment_manage_receiver

    override fun getTitle(): String = resources.getString(R.string.terms_and_conditions)

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
        viewModel.observeAllBeneficiaries().observe(this, Observer { response ->
            binding.btnNext.isEnabled = true
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        if (it.data.size > 0) {
                            binding.lyNoBeneficiary.visibility = View.GONE
                            //binding.lyBeneficiariesList.visibility = View.VISIBLE
                            val beneficiariesList: List<BeneficiaryDetailsModel> = it.data
                            binding.rvBeneficiaries.setHasFixedSize(true)
                            binding.rvBeneficiaries.adapter =
                                BeneficiariesAdapter(context, beneficiariesList)
                            binding.btnNext.isEnabled = beneficiariesList.size < limit!!
                            //Toast.makeText(context, limit.toString(), Toast.LENGTH_LONG).show()
                        } else {
                            //binding.lyNoBeneficiary.visibility = View.VISIBLE
                            binding.lyBeneficiariesList.visibility = View.GONE
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
        viewModel.getAllBeneficiaries()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ManageReceiverFragment()
    }

    override fun onBeneficiarySelected(beneficiaryDetailsModel: BeneficiaryDetailsModel) {
        receiverSharedViewModel?.isDeleteBeneficiary?.postValue(true)
        receiverSharedViewModel?.beneficiaryDetails?.postValue(beneficiaryDetailsModel)
        fragmentHelper.addFragment(
            ReceiverDetailsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

}