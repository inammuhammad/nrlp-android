package com.onelink.nrlp.android.features.rate.fragments

import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentSelfAwardRatingBinding
import com.onelink.nrlp.android.features.rate.model.RateRedemptionRequestModel
import com.onelink.nrlp.android.features.rate.view.RateActivity
import com.onelink.nrlp.android.features.rate.viewmodels.RateViewModel
import com.onelink.nrlp.android.features.redeem.viewmodels.RedemptionSLICPolicyViewModel
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsFragmentViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class TransactionRatingFragment : BaseFragment<RateViewModel, FragmentSelfAwardRatingBinding>
    (RateViewModel::class.java) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var transactionId = ""

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_self_award_rating

    override fun getViewM(): RateViewModel =
        ViewModelProvider(this,viewModelFactory).get(RateViewModel::class.java)

    override fun init(savedInstanceState: Bundle?){
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        transactionId = (activity as RateActivity).transactionId
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        binding.rgRating.setOnCheckedChangeListener { radioGroup, i -> makeRatingCall(radioGroup, i) }
    }

    private fun initObservers(){
        viewModel.observeRateRedemption().observe(this, Observer { response ->
            when(response.status){
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    activity?.finish()
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

    private fun makeRatingCall(group: RadioGroup, index: Int){
        Log.d("index", index.toString())
        var comments = ""
        when(index) {
            R.id.radioButtonGood -> {
                //Log.d("index", index.toString())
                comments = getString(R.string.good)
            }
            R.id.radioButtonSatisfactory -> {
                //Log.d("index", index.toString())
                comments = getString(R.string.satisfied)
            }
            R.id.radioButtonUnsatisfactory -> {
                //Log.d("index", index.toString())
                comments = getString(R.string.unsatisfied)
            }
        }
        viewModel.rateRedemption(RateRedemptionRequestModel(transactionId, comments))
    }

    companion object {
        @JvmStatic
        fun newInstance() = TransactionRatingFragment()
    }

}