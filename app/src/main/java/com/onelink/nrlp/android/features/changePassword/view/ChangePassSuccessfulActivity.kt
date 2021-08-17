package com.onelink.nrlp.android.features.changePassword.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.ChangeSuccessActivityBinding
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordSuccessViewModel
import com.onelink.nrlp.android.features.login.view.LoginActivity
import javax.inject.Inject

class ChangePassSuccessfulActivity :
    BaseActivity<ChangeSuccessActivityBinding, ChangePasswordSuccessViewModel>(
        ChangePasswordSuccessViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.change_success_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory


    companion object {
        fun newRegisterSuccessIntent(context: Context): Intent {
            return Intent(context, ChangePassSuccessfulActivity::class.java)
        }
    }

    override fun onBackPressed() {
        launchLoginActivity()
    }

    private fun launchLoginActivity() {
        val intent = LoginActivity.newLoginIntent(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun initViewModel(viewModel: ChangePasswordSuccessViewModel) {
        binding.buttonDone.setOnClickListener {
            launchLoginActivity()
        }
    }
}
