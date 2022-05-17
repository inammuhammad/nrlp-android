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
import com.onelink.nrlp.android.features.select.city.model.CitiesModel
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import com.onelink.nrlp.android.utils.toFormattedDate
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationsListAdapter(
    private val context: Context?,
    private val notificationsList: ArrayList<NotificationListItemModel>,
    private val listener: (NotificationListItemModel) -> Unit,
    private val deleteListener: (NotificationListItemModel) -> Unit
) : RecyclerView.Adapter<NotificationsListAdapter.NotificationsListViewHolder>() {

    fun addItems(newList: ArrayList<NotificationListItemModel>){
        notificationsList.addAll(newList)
        this.notifyDataSetChanged()
    }

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
            tvDateTime.text = formattedDate(notificationsList[position].notificationDateTime.toString())
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

    private fun formattedDate(rawDate: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        val dateString: Date = dateFormat.parse(rawDate) ?: Date()
        return (SimpleDateFormat("d MMM").format(dateString) + " at " + SimpleDateFormat("hh:mm aaa").format(dateString))
    }
}