package com.onelink.nrlp.android.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.onelink.nrlp.android.widgets.OneLinkEditText
import java.util.*

/**
 * Created by Qazi Abubakar
 */

fun EditText.setCapitalizeTextWatcher() {
    val textWatcher: TextWatcher = object : TextWatcher {
        var mStart = 0
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
            // before text change
        }

        override fun onTextChanged(
            s: CharSequence,
            start: Int,
            before: Int,
            count: Int
        ) {
            mStart = start + count
        }

        override fun afterTextChanged(s: Editable) {
            val input = s.toString()
            val capitalizedText: String
            capitalizedText = if (input.isEmpty()) input else input.substring(0, 1)
                .toUpperCase(Locale.getDefault()) + input.substring(1)
            if (capitalizedText != this@setCapitalizeTextWatcher.text.toString()) {
                this@setCapitalizeTextWatcher.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        // before text change
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        // on Text Change
                    }

                    override fun afterTextChanged(s: Editable) {
                        this@setCapitalizeTextWatcher.setSelection(mStart)
                        this@setCapitalizeTextWatcher.removeTextChangedListener(this)
                    }
                })
                this@setCapitalizeTextWatcher.setText(capitalizedText)
            }
        }
    }
    this@setCapitalizeTextWatcher.addTextChangedListener(textWatcher)
}

fun OneLinkEditText.colorToText(@ColorRes color: Int) {
    this.setTextColor(
        ContextCompat.getColor(
            context,
            color
        )
    )
}

fun TextView.colorToText(@ColorRes color: Int) {
    this.setTextColor(
        ContextCompat.getColor(
            context,
            color
        )
    )
}
