package com.onelink.nrlp.android.features.profile

import android.content.Context
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.onelink.nrlp.android.R

/**
 * Created by Qazi Abubakar
 */

fun TextView.disabled(context: Context) {
    this.isEnabled = false
    this.setTextColor(ContextCompat.getColor(context, R.color.disabled_tv))
}

fun TextView.enabled(context: Context) {
    this.isEnabled = true
    this.setTextColor(ContextCompat.getColor(context, R.color.black))
}

