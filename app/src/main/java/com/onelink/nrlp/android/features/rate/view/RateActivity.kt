package com.onelink.nrlp.android.features.rate.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityRateBinding
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.rate.fragments.TransactionRatingFragment
import com.onelink.nrlp.android.features.rate.viewmodels.RateViewModel
import com.onelink.nrlp.android.features.receiver.fragments.AddRemittanceReceiverFragment
import com.onelink.nrlp.android.features.redeem.view.RedeemSuccessActivity
import com.onelink.nrlp.android.utils.IntentConstants
import com.onelink.nrlp.android.utils.TransactionTypeConstants
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

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            when(transactionType) {
                TransactionTypeConstants.REGISTRATION -> {
                    launchLoginActivity()
                }
                else -> {
                    finish()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onBackPressed() {
        when(transactionType) {
            TransactionTypeConstants.REGISTRATION -> {
                launchLoginActivity()
            }
            else -> {
                finish()
            }
        }
    }
    
    private fun initView(){
        addFragment(
            TransactionRatingFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    private fun launchLoginActivity() {
        val intent = LoginActivity.newLoginIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    companion object {
        fun newRateIntent(context: Context): Intent {
            return Intent(context, RateActivity::class.java)
        }
    }

}