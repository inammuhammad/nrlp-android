package com.onelink.nrlp.android.features.profile.view

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseActivity
import com.onelink.nrlp.android.databinding.FragmentProfileUpdateSuccessBinding
import com.onelink.nrlp.android.features.profile.viewmodel.ProfileUpdateSuccessViewModel
import kotlinx.android.synthetic.main.fragment_register_success.*
import javax.inject.Inject

class ProfileUpdateSuccessActivity :
    BaseActivity<FragmentProfileUpdateSuccessBinding, ProfileUpdateSuccessViewModel>(
        ProfileUpdateSuccessViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_profile_update_success

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ProfileUpdateSuccessViewModel) {
        buttonDone.setOnClickListener {
            finish()
        }
    }

    companion object {
        fun newProfileUpdateSuccessIntent(context: Context): Intent {
            return Intent(context, ProfileUpdateSuccessActivity::class.java)
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
