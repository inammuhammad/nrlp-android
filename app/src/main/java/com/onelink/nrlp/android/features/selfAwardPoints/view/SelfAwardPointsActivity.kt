package com.onelink.nrlp.android.features.selfAwardPoints.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.SelfAwardPointsActivityBinding
import com.onelink.nrlp.android.features.selfAwardPoints.viewmodel.SelfAwardPointsViewModel
import kotlinx.android.synthetic.main.manage_points_activity.*
import javax.inject.Inject

class SelfAwardPointsActivity :
    BaseFragmentActivity<SelfAwardPointsActivityBinding, SelfAwardPointsViewModel>(
        SelfAwardPointsViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.self_award_points_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: SelfAwardPointsViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }

        addFragment(
            SelfAwardPointsFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = true
        )
    }

    companion object {
        fun newSelfAwardPointsIntent(context: Context): Intent {
            return Intent(context, SelfAwardPointsActivity::class.java)
        }
    }

}