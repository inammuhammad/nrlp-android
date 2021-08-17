package com.onelink.nrlp.android.features.uuid.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.UuidActivityBinding
import com.onelink.nrlp.android.features.uuid.viewmodel.UUIDActivityViewModel
import com.onelink.nrlp.android.features.uuid.viewmodel.UUIDSharedViewModel
import com.onelink.nrlp.android.models.LoginCredentials
import com.onelink.nrlp.android.utils.IntentConstants
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class UUIDActivity :
    BaseFragmentActivity<UuidActivityBinding, UUIDActivityViewModel>(
        UUIDActivityViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var loginCredentials: LoginCredentials

    private lateinit var uuidSharedViewModel: UUIDSharedViewModel

    override fun getLayoutRes() = R.layout.uuid_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: UUIDActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }
        val temp =
            intent.extras?.getParcelable<LoginCredentials>(IntentConstants.LOGIN_CREDENTIALS_PARCELABLE)
        loginCredentials = temp as LoginCredentials
        uuidSharedViewModel = ViewModelProvider(this).get(UUIDSharedViewModel::class.java)
        uuidSharedViewModel.setLoginCredentialsModel(loginCredentials)

        addFragment(
            UUIDOtpAuthentication.newInstance(), clearBackStack = false, addToBackStack = true
        )
    }

    companion object {
        fun newUUIDIntent(context: Context): Intent {
            return Intent(context, UUIDActivity::class.java)
        }
    }
}
