package com.onelink.nrlp.android.features.rate.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityRateBinding
import com.onelink.nrlp.android.features.rate.fragments.TransactionRatingFragment
import com.onelink.nrlp.android.features.rate.viewmodels.RateViewModel
import com.onelink.nrlp.android.features.receiver.fragments.AddRemittanceReceiverFragment
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.utils.IntentConstants
import javax.inject.Inject

class RateActivity : BaseFragmentActivity<ActivityRateBinding, RateViewModel>(RateViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_rate

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    var transactionId: String? = ""
    var transactionType: String? = ""

    override fun initViewModel(viewModel: RateViewModel) {
        transactionId = intent.extras?.get(IntentConstants.TRANSACTION_ID) as String? ?: ""
        transactionType = intent.extras?.get(IntentConstants.TRANSACTION_TYPE) as String? ?: ""
        initView()
    }

    override fun onBackPressed() {
        finish()
    }
    
    private fun initView(){
        addFragment(
            TransactionRatingFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    companion object {
        fun newRateIntent(context: Context): Intent {
            return Intent(context, RateActivity::class.java)
        }
    }

}