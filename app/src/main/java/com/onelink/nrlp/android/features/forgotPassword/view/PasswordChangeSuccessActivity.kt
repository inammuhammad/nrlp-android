package com.onelink.nrlp.android.features.forgotPassword.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.ActivityPasswordChangeSuccessBinding
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.PasswordChangeSuccessViewModel
import com.onelink.nrlp.android.features.login.view.LoginActivity
import kotlinx.android.synthetic.main.activity_password_change_success.*
import javax.inject.Inject


class PasswordChangeSuccessActivity :
    BaseActivity<ActivityPasswordChangeSuccessBinding, PasswordChangeSuccessViewModel>(
        PasswordChangeSuccessViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_password_change_success

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: PasswordChangeSuccessViewModel) {
        buttonDone.setOnClickListener {
            launchLoginActivity()
        }
    }

    companion object {
        fun newPasswordChangeSuccessIntent(context: Context): Intent {
            return Intent(context, PasswordChangeSuccessActivity::class.java)
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
