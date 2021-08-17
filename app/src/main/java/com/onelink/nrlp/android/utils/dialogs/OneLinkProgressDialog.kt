package com.onelink.nrlp.android.utils.dialogs

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.onelink.nrlp.android.R
import javax.inject.Inject

/**
 * Created by Umar Javed.
 */

class OneLinkProgressDialog @Inject constructor() {
    private var dialog: Dialog? = null

    @SuppressLint("InflateParams")
    fun showProgressDialog(context: Context?) {
        if (isContextFinishing(context)) {
            return
        }

        if (dialog == null && context != null) {
            dialog = Dialog(context)
        }

        val inflate = LayoutInflater.from(context).inflate(R.layout.layout_loading, null)
        dialog?.apply {
            setContentView(inflate)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setDimAmount(0.3F)
        }?.show()
        dialog?.show()
    }

    fun hideProgressDialog() {
        dialog?.let {
            if (isContextFinishing(it.context)) {
                return
            } else {
                it.dismiss()
            }
        } ?: run {
            return
        }

        dialog = null
    }

    private fun isContextFinishing(context: Context?): Boolean {
        return context is Activity && context.isFinishing
    }
}
