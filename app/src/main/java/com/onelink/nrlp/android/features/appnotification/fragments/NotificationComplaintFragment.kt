package com.onelink.nrlp.android.features.appnotification.fragments

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.databinding.FragmentNotificationComplaintBinding
import com.onelink.nrlp.android.features.appnotification.adapters.NotificationsListAdapter
import com.onelink.nrlp.android.features.appnotification.models.NotificationReadRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListRequestModel
import com.onelink.nrlp.android.features.appnotification.viewmodels.AppNotificationViewModel
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import java.lang.Exception
import javax.inject.Inject

class NotificationComplaintFragment :
    BaseFragment<AppNotificationViewModel, FragmentNotificationComplaintBinding>(AppNotificationViewModel::class.java) {

    private var pageNum = 1

    @Inject
    lateinit var oneLinkProgressDialog: OneLinkProgressDialog

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var adapter: NotificationsListAdapter

    override fun getLayoutRes() = R.layout.fragment_notification_complaint

    override fun onInject() {
        AndroidSupportInjection.inject(this)
    }

    override fun getViewM(): AppNotificationViewModel =
        ViewModelProvider(this, viewModelFactory).get(AppNotificationViewModel::class.java)

    override fun init(savedInstanceState: Bundle?) {
        super.init(savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.btnLoadMore.setOnSingleClickListener {
            pageNum += 1
            viewModel.getNotifications(
                NotificationsListRequestModel(
                    page = pageNum.toString(),
                    perPage = "5"
            )
            )
        }
    }

    private fun initObservers(){
        viewModel.getNotifications(NotificationsListRequestModel(
            page = pageNum.toString(),
            perPage = "5",
            //cnic = UserData.getUser()?.cnicNicop.toString(),
            //notificationType = "complaint"
        ))
        viewModel.observeNotifications().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val list = it.data.records
                        if (pageNum <= 1) {
                            adapter = NotificationsListAdapter(context, list, { notificationItem ->
                                Log.d(TAG, notificationItem.isReadFlag.toString())
                                try {
                                    if (notificationItem.isReadFlag!! == 0) {
                                        viewModel.markNotificationRead(
                                            NotificationReadRequestModel(notificationItem.id.toString())
                                        )
                                    }
                                } catch (e: Exception) {
                                }
                            }, { notificationDeleteItem ->
                                viewModel.deleteNotifications(
                                    NotificationReadRequestModel(
                                        notificationDeleteItem.id.toString()
                                    )
                                )
                                list.remove(notificationDeleteItem)
                                adapter.notifyDataSetChanged()
                            })
                            binding.rvNotificationsList.adapter = adapter
                        }
                        else{
                            adapter.addItems(list)
                            //adapter.notifyDataSetChanged()
                        }
                    }
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let { showGeneralErrorDialog(this, it) }
                }
            }
        })

        viewModel.observeNotificationRead().observe(this, Observer { response ->
            when(response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                }
                Status.LOADING -> {
                    oneLinkProgressDialog.showProgressDialog(activity)
                }
                Status.ERROR -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.error?.let { showGeneralErrorDialog(this, it) }
                }
            }
        })
    }

    companion object {
        val TAG = "notif"
        fun newInstance() = NotificationComplaintFragment()
    }
}