package com.onelink.nrlp.android.utils

import java.text.DecimalFormat

/**
 * Created by Qazi Abubakar
 */

fun Int.toFormattedAmount(): String {
    val formatter = DecimalFormat("#,###,###")
    return formatter.format(this)
}