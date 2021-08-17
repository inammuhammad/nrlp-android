package com.onelink.nrlp.android.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText


/**
 * Created by Qazi Abubakar
 */
open class OneLinkEditText : AppCompatEditText, View.OnTouchListener {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(
        context: Context, attrs: AttributeSet?
    ) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context, attrs: AttributeSet?, defStyle: Int
    ) : super(context, attrs, defStyle) {
        init()
/*
        if (LocaleManager.getLanguagePref(context) == LocaleManager.ENGLISH) {
            this.textDirection = View.TEXT_DIRECTION_RTL
        } else {
            this.textDirection = View.TEXT_DIRECTION_LTR
        }*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        this.isLongClickable = false
        this.setTextIsSelectable(false)
        this.setOnTouchListener(this)
        this.textDirection = View.TEXT_DIRECTION_LTR
        this.setRawInputType(this.inputType and EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE.inv())
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        this.requestFocus()
        if (!this.text.isNullOrEmpty()) event?.let {
            this.setSelection(
                getOffsetForPosition(
                    it.x, it.y
                )
            )
        }
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(
            this, InputMethodManager.SHOW_IMPLICIT
        )
        return true
    }

}