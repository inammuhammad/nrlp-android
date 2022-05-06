package com.onelink.nrlp.android.features.appnotification.fragments

//this is a type of notification, not a specific fragment for the parent activity
//types required by client: complaints, activity & announcements

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

    companion object {
        fun newInstance() = NotificationActivityFragment()
    }
}