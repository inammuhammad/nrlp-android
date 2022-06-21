package com.onelink.nrlp.android.features.appnotification.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.core.BaseFragment
import com.onelink.nrlp.android.core.Status
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.databinding.FragmentNotificationComplaintBinding
import com.onelink.nrlp.android.features.appnotification.adapters.NotificationsListAdapter
import com.onelink.nrlp.android.features.appnotification.models.NotificationReadRequestModel
import com.onelink.nrlp.android.features.appnotification.models.NotificationsListRequestModel
import com.onelink.nrlp.android.features.appnotification.viewmodels.AppNotificationViewModel
import com.onelink.nrlp.android.utils.cleanNicNumber
import com.onelink.nrlp.android.utils.dialogs.OneLinkProgressDialog
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class NotificationComplaintFragment :
    BaseFragment<AppNotificationViewModel, FragmentNotificationComplaintBinding>(
        AppNotificationViewModel::class.java
    ) {

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
        binding.ivComingSoon.visibility = View.GONE
        binding.tvComingSoon.visibility = View.GONE
        binding.rvNotificationsList.visibility = View.VISIBLE
        binding.btnLoadMore.visibility = View.VISIBLE
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.btnLoadMore.setOnSingleClickListener {
            pageNum += 1
            viewModel.getNotifications(
                NotificationsListRequestModel(
                    page = pageNum.toString(),
                    perPage = "10",
                    cnic = UserData.getUser()?.cnicNicop.toString().cleanNicNumber(),
                )
            )
        }

        binding.rvNotificationsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (::adapter.isInitialized)
                    if (adapter.getDeleteBtnState())
                        adapter.hideDeleteButton(true)
            }
        })

        binding.rvNotificationsList.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (::adapter.isInitialized)
                        adapter.hideDeleteButton(true)
                }
            }
            return@OnTouchListener false
        })

        /*binding.rvNotificationsList.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(::adapter.isInitialized)
                            adapter.hideDeleteButton(true)
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })*/

    }

    private fun initObservers() {
        viewModel.getNotifications(
            NotificationsListRequestModel(
                page = pageNum.toString(),
                perPage = "10",
                cnic = UserData.getUser()?.cnicNicop.toString().cleanNicNumber(),
                //notificationType = "complaint"
            )
        )
        viewModel.observeNotifications().observe(this, Observer { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    oneLinkProgressDialog.hideProgressDialog()
                    response.data?.let {
                        val list = it.data.records
                        binding.btnLoadMore.isEnabled = list.size > 9
                        if (pageNum <= 1) {
                            adapter = NotificationsListAdapter(context, list, { notificationItem ->
                                try {
                                    adapter.hideDeleteButton(true)
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
                        } else {
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
            when (response.status) {
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

    override fun onPause() {
        super.onPause()
        if (::adapter.isInitialized)
            adapter.hideDeleteButton(true)
    }

    companion object {
        val TAG = "notif"
        fun newInstance() = NotificationComplaintFragment()
    }
}