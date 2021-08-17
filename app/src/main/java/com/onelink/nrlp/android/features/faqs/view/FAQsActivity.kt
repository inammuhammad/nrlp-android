package com.onelink.nrlp.android.features.faqs.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.FaqsActivityBinding
import com.onelink.nrlp.android.features.faqs.viewmodel.FaqsViewModel
import javax.inject.Inject

class FAQsActivity :
    BaseFragmentActivity<FaqsActivityBinding, FaqsViewModel>(FaqsViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.faqs_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: FaqsViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }
        addFragment(FAQsFragment.newInstance(), clearBackStack = true, addToBackStack = true)
    }

    companion object {
        fun newFaqIntent(context: Context): Intent {
            return Intent(context, FAQsActivity::class.java)
        }
    }
}