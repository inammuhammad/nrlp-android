package com.onelink.nrlp.android.features.beneficiary.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentManageBeneficiaryBinding
import com.onelink.nrlp.android.features.beneficiary.adapter.BeneficiariesAdapter
import com.onelink.nrlp.android.models.BeneficiaryDetailsModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.BeneficiarySharedViewModel
import com.onelink.nrlp.android.features.beneficiary.viewmodel.ManageBeneficiaryViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class ManageBeneficiaryFragment : BaseFragment<ManageBeneficiaryViewModel, FragmentManageBeneficiaryBinding>(ManageBeneficiaryViewModel::class.java), BeneficiariesAdapter.ClickEventHandler {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var beneficiarySharedViewModel: BeneficiarySharedViewModel? = null

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_manage_beneficiary

    override fun getTitle(): String = resources.getString(R.string.manage_beneficiary_title)

    override fun getViewM(): ManageBeneficiaryViewModel =
        ViewModelProvider(this, viewModelFactory).get(ManageBeneficiaryViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        activity?.let {
            beneficiarySharedViewModel =
                ViewModelProvider(it).get(BeneficiarySharedViewModel::class.java)
        }
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.observeAllBeneficiaries().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        if (it.data.size > 0) {
                            binding.lyNoBeneficiary.visibility = View.GONE
                            binding.lyBeneficiariesList.visibility = View.VISIBLE
                            val beneficiariesList: List<BeneficiaryDetailsModel> = it.data
                            binding.rvBeneficiaries.setHasFixedSize(true)
                            binding.rvBeneficiaries.adapter =
                                BeneficiariesAdapter(context, beneficiariesList)
                            binding.btnNext.isEnabled = beneficiariesList.size < 3
                        } else {
                            binding.lyNoBeneficiary.visibility = View.VISIBLE
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
            beneficiarySharedViewModel?.isDeleteBeneficiary?.postValue(false)
            fragmentHelper.addFragment(
                BeneficiaryDetailsFragment.newInstance(),
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
            ManageBeneficiaryFragment()
    }

    override fun onBeneficiarySelected(beneficiaryDetailsModel: BeneficiaryDetailsModel) {
        beneficiarySharedViewModel?.isDeleteBeneficiary?.postValue(true)
        beneficiarySharedViewModel?.beneficiaryDetails?.postValue(beneficiaryDetailsModel)
        fragmentHelper.addFragment(
            BeneficiaryDetailsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }
}