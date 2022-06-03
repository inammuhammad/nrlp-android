package com.onelink.nrlp.android.features.appnotification.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.databinding.FragmentNotificationComplaintBinding
import com.onelink.nrlp.android.features.appnotification.viewmodels.AppNotificationViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NotificationActivityFragment :
    BaseFragment<AppNotificationViewModel, FragmentNotificationComplaintBinding>(AppNotificationViewModel::class.java) {

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getLayoutRes() = R.layout.fragment_notification_complaint

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): AppNotificationViewModel =
        ViewModelProvider(this, viewModelFactory).get(AppNotificationViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        binding.ivComingSoon.visibility = View.VISIBLE
        binding.tvComingSoon.visibility = View.VISIBLE
        binding.rvNotificationsList.visibility = View.GONE
        binding.btnLoadMore.visibility = View.GONE
        super.init(savedInstanceState)
    }

    companion object {
        fun newInstance() = NotificationActivityFragment()
    }
}