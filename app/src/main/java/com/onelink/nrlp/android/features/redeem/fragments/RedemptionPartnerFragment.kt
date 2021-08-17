package com.onelink.nrlp.android.features.redeem.fragments

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentRedemptionpartnerBinding
import com.onelink.nrlp.android.features.faqs.adapter.RVAdapter
import com.onelink.nrlp.android.features.redeem.adapter.RedemPartnerAdapter
import com.onelink.nrlp.android.features.redeem.model.RedeemPartnerModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemSharedViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemtionFragmentPartnerViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class RedemptionPartnerFragment : BaseFragment<RedemtionFragmentPartnerViewModel, FragmentRedemptionpartnerBinding>(RedemtionFragmentPartnerViewModel::class.java), RedemPartnerAdapter.OnItemClickListener {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: RVAdapter

    private var redeemSharedViewModel: RedeemSharedViewModel? = null

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RedemptionPartnerFragment()
    }

    override fun getTitle(): String = resources.getString(R.string.redeem)
    override fun getLayoutRes() = R.layout.fragment_redemptionpartner
    override fun getViewM(): RedemtionFragmentPartnerViewModel =
        ViewModelProvider(this, viewModelFactory).get(RedemtionFragmentPartnerViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        activity?.let {
            redeemSharedViewModel = ViewModelProvider(it).get(RedeemSharedViewModel::class.java)
        }

        viewModel.getRedeemPartner()
        initObservers()
    }

    private fun initObservers() {
        viewModel.observerRedeemPartner().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val redeemPartnerList: List<RedeemPartnerModel> = it.data
                        binding.redemPartner.setHasFixedSize(true)
                        binding.redemPartner.adapter = RedemPartnerAdapter(redeemPartnerList, this)
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

    override fun onItemClicked(redeemPartnerModel: RedeemPartnerModel) {
        redeemSharedViewModel?.setRedeemPartnerModel(redeemPartnerModel)
        viewModel.addNextFragment(fragmentHelper)
        hideKeyboard()
    }
}