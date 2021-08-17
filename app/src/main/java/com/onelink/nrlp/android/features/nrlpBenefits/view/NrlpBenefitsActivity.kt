package com.onelink.nrlp.android.features.nrlpBenefits.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.NrlpBenfitsActivityBinding
import com.onelink.nrlp.android.features.nrlpBenefits.fragments.NrlpPartnersFragment
import com.onelink.nrlp.android.features.nrlpBenefits.viewmodel.NrlpBenefitsViewModel
import javax.inject.Inject

class NrlpBenefitsActivity :
    BaseFragmentActivity<NrlpBenfitsActivityBinding, NrlpBenefitsViewModel>(NrlpBenefitsViewModel::class.java){
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.nrlp_benfits_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory


    override fun initViewModel(viewModel: NrlpBenefitsViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        binding.toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }

        addFragment(
            NrlpPartnersFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    companion object {
        fun newViewStatementIntent(context: Context): Intent {
            return Intent(context, NrlpBenefitsActivity::class.java)
        }
    }
}