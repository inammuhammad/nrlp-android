package com.onelink.nrlp.android.features.nrlpBenefits.fragments

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.NrlpBenfitsFragmentBinding
import com.onelink.nrlp.android.features.nrlpBenefits.adapter.NrlpBenefitsAdapter
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsFragmentViewModel
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsSharedViewModel
import com.onelink.nrlp.android.utils.base64ToBitmap
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

class NrlpBenefitsFragment :
    BaseFragment<NrlpBenefitsFragmentViewModel, NrlpBenfitsFragmentBinding>(
        NrlpBenefitsFragmentViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var nrlpBenefitsSharedViewModel: NrlpBenefitsSharedViewModel

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog
    override fun getLayoutRes() = R.layout.nrlp_benfits_fragment

    override fun getTitle() = getString(R.string.view_nrlp_benefits)

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): NrlpBenefitsFragmentViewModel =
        ViewModelProvider(this, viewModelFactory).get(NrlpBenefitsFragmentViewModel::class.java)


    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            nrlpBenefitsSharedViewModel =
                ViewModelProvider(it).get(NrlpBenefitsSharedViewModel::class.java)
        }

        initObservers()
    }

    private fun initObservers() {
        nrlpBenefitsSharedViewModel.redemptionPartnerModel.observe(this, Observer {
            viewModel.getNrlpPartnerBenefits(it.id)
            val bitmap = it.imgSrc.base64ToBitmap()
            if (bitmap != null)
                binding.ivTitle.setImageBitmap(bitmap)
            else
                binding.ivTitle.setImageDrawable(context?.let { context ->
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_benefits_placeholder
                    )
                })
        })

        viewModel.observeNrlpPartnerBenefits().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val redemptionPartnersList = it.data.partnerCatalogs
                        binding.rvBenefits.setHasFixedSize(true)
                        binding.rvBenefits.adapter = NrlpBenefitsAdapter(redemptionPartnersList)
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

    companion object {
        @JvmStatic
        fun newInstance() = NrlpBenefitsFragment()
    }
}
