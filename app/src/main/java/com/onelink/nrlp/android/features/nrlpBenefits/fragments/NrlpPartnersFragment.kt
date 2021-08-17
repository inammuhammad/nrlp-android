package com.onelink.nrlp.android.features.nrlpBenefits.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.NrlpPartnersFragmentBinding
import com.onelink.nrlp.android.features.nrlpBenefits.adapter.NrlpPartnersTilesAdapter
import com.onelink.nrlp.android.features.nrlpBenefits.model.RedemptionPartnerModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsSharedViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpPartnersFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

class NrlpPartnersFragment :
    BaseFragment<NrlpPartnersFragmentViewModel, NrlpPartnersFragmentBinding>(NrlpPartnersFragmentViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    private lateinit var nrlpBenefitsSharedViewModel: NrlpBenefitsSharedViewModel

    override fun getLayoutRes() = R.layout.nrlp_partners_fragment

    override fun getTitle() = getString(R.string.view_nrlp_benefits)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): NrlpPartnersFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(NrlpPartnersFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        viewModel.getNrlpBenefits()
        activity?.let {
            nrlpBenefitsSharedViewModel = ViewModelProvider(it).get(NrlpBenefitsSharedViewModel::class.java)
        }

        viewModel.observeNrlpBenefits().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val redemptionPartnerModel: List<RedemptionPartnerModel> = it.data.redemptionPartners
                        val nrlpPartnersTilesAdapter = NrlpPartnersTilesAdapter(activity, redemptionPartnerModel, ::onNrlpTileClicked)
                        binding.homeTilesGrid.isExpanded = true
                        binding.homeTilesGrid.adapter = nrlpPartnersTilesAdapter
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

    private fun onNrlpTileClicked(redemptionPartnerModel: RedemptionPartnerModel) {
        nrlpBenefitsSharedViewModel.redemptionPartnerModel.postValue(redemptionPartnerModel)

        fragmentHelper.addFragment(
            NrlpBenefitsFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = NrlpPartnersFragment()
    }
}
