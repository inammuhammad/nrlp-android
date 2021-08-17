package com.onelink.nrlp.android.widgets

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.textfield.TextInputLayout
import com.onelink.nrlp.android.R

class OneLinkTextInputLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    private var customHint: CharSequence = ""

    init {
        isHintEnabled = false
    }

    override fun setError(error: CharSequence?) {
        super.setError(error)

        if (error.isNullOrEmpty()) {
            hint = customHint
            editText?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        } else {
            customHint = hint.toString()
            hint = null
            editText?.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_edit_text_error,
                0
            )
            editText?.setPadding(editText?.paddingStart!!, 0, editText?.paddingStart!!, 0)

            context?.let {
                val tf = ResourcesCompat.getFont(context, R.font.hp_simplified_rg)
                this.typeface = tf
            }
        }
    }

    fun clearError() {
        error = null
    }
}