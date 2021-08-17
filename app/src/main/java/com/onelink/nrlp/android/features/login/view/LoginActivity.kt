package com.onelink.nrlp.android.features.login.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.LoginActivityBinding
import com.onelink.nrlp.android.features.login.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity :
    BaseFragmentActivity<LoginActivityBinding, LoginViewModel>(LoginViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.login_activity


    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: LoginViewModel) {
        viewModel.clearUserData()
        addFragment(LoginFragment.newInstance(), clearBackStack = true, addToBackStack = true)
    }

    companion object {
        fun newLoginIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }

        fun start(activity: Activity) {
            activity.startActivity(Intent(activity, LoginActivity::class.java))
            activity.finishAffinity()
        }
    }
}
