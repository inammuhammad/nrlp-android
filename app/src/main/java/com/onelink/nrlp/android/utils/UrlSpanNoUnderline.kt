package com.onelink.nrlp.android.utils

import android.text.TextPaint
import android.text.style.URLSpan

internal class UrlSpanNoUnderline(src: URLSpan) : URLSpan(src.url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}