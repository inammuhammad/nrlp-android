package com.onelink.nrlp.android.features.register.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.ActivityRegisterContainerBinding
import com.onelink.nrlp.android.features.login.view.LoginActivity
import com.onelink.nrlp.android.features.rate.view.RateActivity
import com.onelink.nrlp.android.features.register.viewmodel.RegisterViewModel
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.IntentConstants
import com.onelink.nrlp.android.utils.TransactionTypeConstants
import kotlinx.android.synthetic.main.fragment_register_success.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

class RegisterSuccessActivity :
    BaseActivity<ActivityRegisterContainerBinding, RegisterViewModel>(RegisterViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_register_success

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: RegisterViewModel) {
        val accountType = intent.extras?.get(Constants.INTENT_KEY_ACCOUNT_TYPE) as String
        if (accountType == Constants.BENEFICIARY.toLowerCase(Locale.getDefault())) {
            textViewRegisterSuccessMsg.text = getString(R.string.register_success_msg_beneficiary)
        } else if (accountType == Constants.REMITTER.toLowerCase(Locale.getDefault())) {
            textViewRegisterSuccessMsg.text = getString(R.string.register_success_msg_remitter)
        }
        buttonDone.setOnClickListener {
            startActivity(
                RateActivity.newRateIntent(this)
                    .putExtra(IntentConstants.TRANSACTION_TYPE, TransactionTypeConstants.REGISTRATION)
            )
            //launchLoginActivity()
        }
    }

    companion object {
        fun newRegisterSuccessIntent(context: Context): Intent {
            return Intent(context, RegisterSuccessActivity::class.java)
        }
    }

    override fun onBackPressed() {
        launchLoginActivity()
        finish()
    }

    private fun launchLoginActivity() {
        val intent = LoginActivity.newLoginIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
