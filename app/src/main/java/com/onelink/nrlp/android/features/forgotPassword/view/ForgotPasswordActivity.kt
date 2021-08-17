package com.onelink.nrlp.android.features.forgotPassword.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ForgotPasswordActivityBinding
import com.onelink.nrlp.android.features.forgotPassword.fragments.ForgotPasswordFragment
import com.onelink.nrlp.android.features.forgotPassword.viewmodel.ForgotPasswordViewModel
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ForgotPasswordActivity :
    BaseFragmentActivity<ForgotPasswordActivityBinding, ForgotPasswordViewModel>(
        ForgotPasswordViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.forgot_password_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ForgotPasswordViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it.isAdded) binding.toolbar.setTitle(it.getTitle())
            }
        }

        addFragment(
            ForgotPasswordFragment.newInstance(), clearBackStack = false, addToBackStack = true
        )
    }

    companion object {
        fun newForgotPasswordIntent(context: Context): Intent {
            return Intent(context, ForgotPasswordActivity::class.java)
        }
    }
}
