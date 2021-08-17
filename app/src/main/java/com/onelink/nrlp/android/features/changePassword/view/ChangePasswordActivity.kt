package com.onelink.nrlp.android.features.changePassword.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ChangepasswordActivityBinding
import com.onelink.nrlp.android.features.changePassword.viewmodel.ChangePasswordViewModel
import javax.inject.Inject

class ChangePasswordActivity :
    BaseFragmentActivity<ChangepasswordActivityBinding, ChangePasswordViewModel>(
        ChangePasswordViewModel::class.java
    ) {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.changepassword_activity


    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ChangePasswordViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())

            }
        }
        addFragment(
            ChangePasswordFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = true
        )
    }

    companion object {
        fun newChangePasswordIntent(context: Context): Intent {
            return Intent(context, ChangePasswordActivity::class.java)
        }
    }


}