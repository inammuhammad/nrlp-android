package com.onelink.nrlp.android.features.contactus.adapter

import android.content.Context
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.databinding.ContactUsCellBinding
import com.onelink.nrlp.android.features.contactus.models.ContactDetailModel
import com.onelink.nrlp.android.features.contactus.view.ContactUsUtils
import com.onelink.nrlp.android.utils.UriConstants


class ContactUsAdapter(
    private val callModelObj: List<ContactDetailModel>, appContext: Context
) : RecyclerView.Adapter<ContactUsAdapter.ContactUsViewHolder>() {
    override fun getItemCount() = callModelObj.size
    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactUsViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), R.layout.contact_us_cell, parent, false
        )
    )

    init {
        context = appContext
    }

    class ContactUsViewHolder(val contactUsBinding: ContactUsCellBinding) :
        RecyclerView.ViewHolder(contactUsBinding.root)

    override fun onBindViewHolder(holder: ContactUsViewHolder, position: Int) {
        holder.contactUsBinding.contactUs = callModelObj[position]
        when (callModelObj[position].flag) {
            ContactUsUtils.CALL_IMG -> {
                context?.resources?.getString(R.string.call_us)
                holder.contactUsBinding.ivMenuItemImage.setImageResource(R.drawable.ic_phone)
            }
            ContactUsUtils.EMAIL_IMG -> {
                context?.resources?.getString(R.string.email_us)
                holder.contactUsBinding.ivMenuItemImage.setImageResource(R.drawable.ic_email)
            }
            ContactUsUtils.WEB_IMG -> {
                context?.resources?.getString(R.string.web_url)
                holder.contactUsBinding.ivMenuItemImage.setImageResource(R.drawable.ic_web_url)
            }
        }
        if (position == 0) {
            holder.contactUsBinding.contactDetail.text = UriConstants.Phone_NO
            holder.contactUsBinding.contactDetail.autoLinkMask = Linkify.PHONE_NUMBERS
        } else if (position == 1) {
            holder.contactUsBinding.contactDetail.text = UriConstants.Email
            holder.contactUsBinding.contactDetail.autoLinkMask = Linkify.EMAIL_ADDRESSES
        } else if (position == 2) {
            if (!UriConstants.Web.startsWith("http://") && !UriConstants.Web.startsWith("https://")) {
                holder.contactUsBinding.contactDetail.text = UriConstants.Web
                holder.contactUsBinding.contactDetail.autoLinkMask = Linkify.WEB_URLS
            }
        }
    }
}