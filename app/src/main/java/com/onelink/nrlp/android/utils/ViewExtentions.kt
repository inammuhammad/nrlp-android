package com.onelink.nrlp.android.utils

import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

fun ConstraintLayout.setBackgroundDrawable(@DrawableRes res: Int) {
    background = ContextCompat.getDrawable(
        context,
        res
    )
}