package com.onelink.nrlp.android.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * Created by Qazi Abubakar
 */

fun BigDecimal.toFormattedAmount(): String {
    val formatter = DecimalFormat("#,###,###.##")
    return formatter.format(this)
}

fun BigDecimal.roundOff(): BigDecimal {
    return this.setScale(0, RoundingMode.FLOOR)
}