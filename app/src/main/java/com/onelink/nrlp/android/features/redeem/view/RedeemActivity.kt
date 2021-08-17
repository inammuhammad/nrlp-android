package com.onelink.nrlp.android.features.redeem.view

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityRedeemBinding
import com.onelink.nrlp.android.features.redeem.fragments.RedeemAgentConfirmationFragment
import com.onelink.nrlp.android.features.redeem.fragments.RedemptionPartnerFragment
import com.onelink.nrlp.android.features.redeem.viewmodels.RedeemActivityViewModel
import javax.inject.Inject

class RedeemActivity :
    BaseFragmentActivity<ActivityRedeemBinding, RedeemActivityViewModel>(RedeemActivityViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_redeem

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: RedeemActivityViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        binding.toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
                if (it !is RedeemAgentConfirmationFragment)
                    binding.toolbar.setLeftButtonVisible(true)
                else
                    binding.toolbar.setLeftButtonVisible(false)
            }
        }
        addFragment(
            RedemptionPartnerFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = true
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                if (it !is RedeemAgentConfirmationFragment) {
                    onBack()
                }
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        @Suppress("unused")
        fun newRedeemIntent(context: Context): Intent {
            return Intent(context, RedeemActivity::class.java)
        }
    }
}
