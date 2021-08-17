package com.onelink.nrlp.android.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.URLSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class TextViewNoUnderline @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        setSpannableFactory(Factory.instance)
    }

    private class Factory : Spannable.Factory() {
        override fun newSpannable(source: CharSequence): Spannable {
            return SpannableNoUnderline(source)
        }

        companion object {
            val instance = Factory()
        }
    }

    private class SpannableNoUnderline(source: CharSequence?) :
        SpannableString(source) {
        override fun setSpan(
            what: Any,
            start: Int,
            end: Int,
            flags: Int
        ) {
            super.setSpan(
                if (what is URLSpan) UrlSpanNoUnderline(what) else what,
                start,
                end,
                flags
            )
        }
    }
}