package com.onelink.nrlp.android.features.appnotification.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.ItemPushNotificationBinding
import com.onelink.nrlp.android.features.appnotification.models.NotificationListItemModel
import com.onelink.nrlp.android.utils.setOnSingleClickListener

class NotificationsListAdapter(
    private val context: Context?,
    private val notificationsList: ArrayList<NotificationListItemModel>,
    private val listener: (NotificationListItemModel) -> Unit,
    private val deleteListener: (NotificationListItemModel) -> Unit
) : RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder>() {

    override fun getItemCount(): Int = notificationsList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = NotificationsListViewHolder (
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_push_notification,
            parent, false
        )
    )

    class NotificationsListViewHolder(val notificationItemBinding: ItemPushNotificationBinding) :
            RecyclerView.ViewHolder(notificationItemBinding.root)

    override fun onBindViewHolder(holder: NotificationsListViewHolder, position: Int) {
        holder.notificationItemBinding.notif = notificationsList[position]
        holder.notificationItemBinding.apply {
            tvNotificationDetails.text = notificationsList[position].notificationMessage
            lyNotificationItem.setOnSingleClickListener {
                listener(notificationsList[position])
                notificationsList[position].isReadFlag = 1
                notificationReadColour(lyNotificationItem)
            }
            tvNotificationDetails.setOnSingleClickListener {
                listener(notificationsList[position])
                notificationsList[position].isReadFlag = 1
                notificationReadColour(lyNotificationItem)
            }
            ivDotMenu.setOnSingleClickListener {
                if(ivDelete.visibility == View.GONE)
                    ivDelete.visibility = View.VISIBLE
                else
                    ivDelete.visibility = View.GONE
            }
            ivDelete.setOnSingleClickListener {
                ivDelete.visibility = View.GONE
                deleteListener(notificationsList[position])
            }
            if(notificationsList[position].isReadFlag == 1)
                notificationReadColour(lyNotificationItem)
            else
                notificationUnReadColour(lyNotificationItem)
        }
    }

    private fun notificationReadColour(view: View) {
        context?.let { ContextCompat.getColor(it, R.color.disabled_tv) }?.let {
            view.setBackgroundColor(
                it
            )
        }
    }

    private fun notificationUnReadColour(view: View) {
        context?.let { ContextCompat.getColor(it, R.color.colorAccent) }?.let {
            view.setBackgroundColor(
                it
            )
        }
    }
}