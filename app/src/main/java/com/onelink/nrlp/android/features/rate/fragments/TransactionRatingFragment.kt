package com.onelink.nrlp.android.features.rate.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.JsonObject
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentSelfAwardRatingBinding
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.rate.model.RateRedemptionRequestModel
import com.onelink.nrlp.android.features.rate.view.RateActivity
import com.onelink.nrlp.android.features.rate.viewmodels.RateViewModel
import com.onelink.nrlp.android.utils.LukaKeRakk
import com.onelink.nrlp.android.utils.TransactionTypeConstants
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import org.json.JSONObject
import javax.inject.Inject

class TransactionRatingFragment : BaseFragment<RateViewModel, FragmentSelfAwardRatingBinding>
    (RateViewModel::class.java) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var transactionId: String? = ""
    private var transactionType: String? = ""

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getLayoutRes() = R.layout.fragment_self_award_rating

    override fun getViewM(): RateViewModel =
        ViewModelProvider(this, viewModelFactory).get(RateViewModel::class.java)

    override fun init(savedInstanceState: Bundle?){
        super.init(savedInstanceState)
        binding.lifecycleOwner = this
        transactionId = (activity as RateActivity).transactionId
        transactionType = (activity as RateActivity).transactionType
        setHeading(transactionType)
        initListeners()
        initObservers()
    }

    private fun initListeners(){
        //binding.rgRating.setOnCheckedChangeListener { radioGroup, i -> makeRatingCall(radioGroup, i) }
        binding.ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                // Called when the user swipes the RatingBar
                makeRatingCall(rating.toString())
            }
    }

    private fun initObservers(){
        viewModel.observeRateRedemption().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    when(transactionType) {
                        TransactionTypeConstants.REGISTRATION -> {
                            launchLoginActivity()
                        }
                        else -> {
                            activity?.finish()
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

    private fun makeRatingCall(rating: String) {
        val jsonObject= JsonObject()
        when(transactionType){
            TransactionTypeConstants.TRANSFER_POINTS -> {
                jsonObject.addProperty(
                    TransactionTypeConstants.TRANSACTION_TYPE,
                    transactionType
                )
                jsonObject.addProperty(
                    TransactionTypeConstants.COMMENTS,
                    rating
                )
            }
            TransactionTypeConstants.REGISTRATION -> {
                jsonObject.addProperty(
                    TransactionTypeConstants.TRANSACTION_TYPE,
                    transactionType
                )
                jsonObject.addProperty(
                    TransactionTypeConstants.COMMENTS,
                    rating
                )
                jsonObject.addProperty(
                    "encryption_key",
                    LukaKeRakk.kcth()
                )
            }
            else -> {
                jsonObject.addProperty(
                    TransactionTypeConstants.TRANSACTION_ID,
                    transactionId
                )
                jsonObject.addProperty(
                    TransactionTypeConstants.COMMENTS,
                    rating
                )
            }
        }
        viewModel.rateRedemption(jsonObject)
    }

    /*private fun makeRatingCall(group: RadioGroup, index: Int){
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
    }*/

    private fun launchLoginActivity() {
        val intent = context?.let { LoginActivity.newLoginIntent(it) }
        intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setHeading(type: String?) {
        var title = ""
        title = when(transactionType){
            TransactionTypeConstants.TRANSFER_POINTS -> {
                getString(R.string.rate_transfer_experience)
            }
            TransactionTypeConstants.REGISTRATION -> {
                getString(R.string.rate_registration_experience)
            }
            else -> {
                getString(R.string.rate_redemption_experience)
            }
        }
        binding.tvRateExperience.text = title
    }

    companion object {
        @JvmStatic
        fun newInstance() = TransactionRatingFragment()
    }

}