package com.onelink.nrlp.android.features.managePoints.view

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ManagePointsActivityBinding
import com.onelink.nrlp.android.features.managePoints.viewmodel.ManagePointsViewModel
import kotlinx.android.synthetic.main.manage_points_activity.*
import javax.inject.Inject

class ManagePointsActivity :
    BaseFragmentActivity<ManagePointsActivityBinding, ManagePointsViewModel>(
        ManagePointsViewModel::class.java
    ) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.manage_points_activity

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: ManagePointsViewModel) {
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        toolbar.showBorderView(true)
        supportFragmentManager.addOnBackStackChangedListener {
            val fragment = getCurrentFragment() as BaseFragment<*, *>?
            fragment?.let {
                binding.toolbar.setTitle(it.getTitle())
            }
        }

        addFragment(
            ManagePointsFragment.newInstance(),
            clearBackStack = true,
            addToBackStack = true
        )
    }

    companion object {
        fun newManagePointsIntent(context: Context): Intent {
            return Intent(context, ManagePointsActivity::class.java)
        }
    }

}