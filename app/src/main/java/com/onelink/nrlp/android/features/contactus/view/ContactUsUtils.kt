package com.onelink.nrlp.android.features.contactus.view

import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.features.contactus.models.ContactDetailModel

class ContactUsUtils {
    companion object {
        const val CALL_IMG = 1
        const val EMAIL_IMG = 2
        const val WEB_IMG = 3
        fun contactUsList(): MutableList<ContactDetailModel> {
            val contactDetailModel = mutableListOf<ContactDetailModel>()
            contactDetailModel.add(
                ContactDetailModel(
                    R.drawable.ic_phone,
                    R.string.call_us,
                    R.string.call_no,
                    CALL_IMG
                )
            )
            contactDetailModel.add(
                ContactDetailModel(
                    R.drawable.ic_email,
                    R.string.email_us,
                    R.string.email_add,
                    EMAIL_IMG
                )
            )
            contactDetailModel.add(
                ContactDetailModel(
                    R.drawable.ic_web_url,
                    R.string.web_url,
                    R.string.web_address,
                    WEB_IMG
                )
            )
            return contactDetailModel
        }
    }
}