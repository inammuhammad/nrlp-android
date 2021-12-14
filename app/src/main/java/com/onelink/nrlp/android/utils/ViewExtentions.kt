package com.onelink.nrlp.android.utils

import android.view.View
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

fun ConstraintLayout.setBackgroundDrawable(@DrawableRes res: Int) {
    background = ContextCompat.getDrawable(
        context,
        res
    )
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}
