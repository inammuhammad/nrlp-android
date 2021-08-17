package com.onelink.nrlp.android.features.contactus.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ContactUsActivityBinding
import com.onelink.nrlp.android.features.contactus.fragments.ContactUsFragment
import com.onelink.nrlp.android.features.contactus.viewmodel.ContactUsActivityViewModel
import javax.inject.Inject

class ContactUsActivity :
    BaseFragmentActivity<ContactUsActivityBinding, ContactUsActivityViewModel>(
        ContactUsActivityViewModel::class.java
    ) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.contact_us_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ContactUsActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }
        addFragment(ContactUsFragment.newInstance(), clearBackStack = true, addToBackStack = true)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ContactUsActivity::class.java)
        }
    }
}