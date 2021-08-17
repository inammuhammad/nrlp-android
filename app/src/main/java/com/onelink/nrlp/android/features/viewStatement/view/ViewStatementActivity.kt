package com.onelink.nrlp.android.features.viewStatement.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.BeneficiaryActivityBinding
import com.onelink.nrlp.android.features.viewStatement.fragments.LoyaltyStatementFragment
import com.onelink.nrlp.android.features.viewStatement.viewmodel.ViewStatementViewModel
import kotlinx.android.synthetic.main.beneficiary_activity.*
import javax.inject.Inject

class ViewStatementActivity :
    BaseFragmentActivity<BeneficiaryActivityBinding, ViewStatementViewModel>(ViewStatementViewModel::class.java){
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.beneficiary_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory


    override fun initViewModel(viewModel: ViewStatementViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                it.onResume()
            }
        }

        addFragment(
            LoyaltyStatementFragment.newInstance(),
            clearBackStack = false,
            addToBackStack = true
        )
    }

    companion object {
        fun newViewStatementIntent(context: Context): Intent {
            return Intent(context, ViewStatementActivity::class.java)
        }
    }
}