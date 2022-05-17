package com.onelink.nrlp.android.features.appnotification

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragmentActivity
import com.onelink.nrlp.android.databinding.ActivityInAppNotificationBinding
import com.onelink.nrlp.android.features.appnotification.adapters.ViewPagerAdapter
import com.onelink.nrlp.android.features.appnotification.fragments.NotificationComplaintFragment
import com.onelink.nrlp.android.features.appnotification.viewmodels.AppNotificationViewModel
import javax.inject.Inject

class InAppNotificationActivity:
    BaseFragmentActivity<ActivityInAppNotificationBinding, AppNotificationViewModel>(AppNotificationViewModel::class.java) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.activity_in_app_notification

    override fun getVMFactory(): ViewModelProvider.Factory = viewModelFactory

    override fun initViewModel(viewModel: AppNotificationViewModel) {
        binding.toolbar.showBorderView(true)
        binding.toolbar.titleWithDrawableVisible(true)
        binding.toolbar.setLeftButtonClickListener(View.OnClickListener { onBack() })
        initView()
    }

    override fun onBackPressed() {
        finish()
    }

    private fun initView() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(NotificationComplaintFragment(), getString(R.string.complaint))
        //adapter.addFragment(NotificationComplaintFragment(), getString(R.string.active))
        //adapter.addFragment(NotificationComplaintFragment(), getString(R.string.home_title))
        binding.viewpager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewpager)
        binding.tabs.setSelectedTabIndicatorColor(Color.parseColor("#014D27"))

    }

    companion object {
        fun newNotificationActivityIntent(context: Context): Intent {
            return Intent(context, InAppNotificationActivity::class.java)
        }
    }
}