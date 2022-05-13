package com.onelink.nrlp.android.common.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.AttrRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.onelink.nrlp.android.R
import kotlinx.android.synthetic.main.layout_custom_toolbar.view.*

class CustomToolbar : ConstraintLayout {
    private lateinit var view: View

    constructor(context: Context) : super(context) {
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initLayout()
    }

    constructor(
        context: Context, attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)


    private fun initLayout() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_custom_toolbar, this)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }

    fun setLeftButton(drawable: Drawable?) {
        ivLeft.setImageDrawable(drawable)
    }

    fun setLeftButtonVisible(visible: Boolean) {
        if (visible) ivLeft.visibility = View.VISIBLE
        else ivLeft.visibility = View.GONE
    }

    fun setRightButtonVisible(visible: Boolean) {
        if (visible) tvRight.visibility = View.VISIBLE
        else tvRight.visibility = View.GONE
    }

    fun setNotificationCountVisible(visible: Boolean) {
        if (visible) tvNotificationCount.visibility = View.VISIBLE
        else tvNotificationCount.visibility = View.GONE
    }

    fun setLeftButtonClickListener(onClickListener: OnClickListener) {
        ivLeft.setOnClickListener(onClickListener)
    }

    fun setRightButtonClickListener(onClickListener: OnClickListener) {
        tvRight.setOnClickListener(onClickListener)
    }

    fun showBorderView(boolean: Boolean) {
        if (boolean)
            borderView.visibility = View.VISIBLE
        else
            borderView.visibility = View.GONE
    }
}
