package com.onelink.nrlp.android.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Spanned
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.setOnSingleClickListener
import kotlinx.android.synthetic.main.custom_dialog.*

const val ARG_PARAM_IS_ALERT = "IS_ALERT"
const val ARG_PARAM_DRAWABLE = "DRAWABLE"
const val ARG_PARAM_TITLE = "TITLE"
const val ARG_PARAM_MESSAGE = "MESSAGE"
const val ARG_PARAM_NEUTRAL_BUTTON_TEXT = "NEUTRAL_BUTTON_TEXT"
const val ARG_PARAM_POSITIVE_BUTTON_TEXT = "POSITIVE_BUTTON_TEXT"
const val ARG_PARAM_NEGATIVE_BUTTON_TEXT = "NEGATIVE_BUTTON_TEXT"

class OneLinkAlertDialogsFragment : DialogFragment() {

    companion object {
        fun newInstance(
            isAlertOnly: Boolean,
            drawable: Int,
            title: String,
            message: Spanned,
            neutralButtonText: String = "",
            positiveButtonText: String = "",
            negativeButtonText: String = ""
        ):
            OneLinkAlertDialogsFragment {
            val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM_IS_ALERT, isAlertOnly)
            args.putInt(ARG_PARAM_DRAWABLE, drawable)
            args.putString(ARG_PARAM_TITLE, title)
            args.putCharSequence(ARG_PARAM_MESSAGE, message)
            args.putString(ARG_PARAM_NEUTRAL_BUTTON_TEXT, neutralButtonText)
            args.putString(ARG_PARAM_POSITIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(ARG_PARAM_NEGATIVE_BUTTON_TEXT, negativeButtonText)
            oneLinkAlertDialogsFragment.arguments = args
            return oneLinkAlertDialogsFragment
        }

        fun newInstanceForFBR(
            isAlertOnly: Boolean,
            drawable: Int,
            title: String,
            message: String,
            neutralButtonText: String = "",
            positiveButtonText: String = "",
            negativeButtonText: String = ""
        ):
                OneLinkAlertDialogsFragment {
            val oneLinkAlertDialogsFragment = OneLinkAlertDialogsFragment()
            val args = Bundle()
            args.putBoolean(ARG_PARAM_IS_ALERT, isAlertOnly)
            args.putInt(ARG_PARAM_DRAWABLE, drawable)
            args.putString(ARG_PARAM_TITLE, title)
            args.putCharSequence(ARG_PARAM_MESSAGE, message)
            args.putString(ARG_PARAM_NEUTRAL_BUTTON_TEXT, neutralButtonText)
            args.putString(ARG_PARAM_POSITIVE_BUTTON_TEXT, positiveButtonText)
            args.putString(ARG_PARAM_NEGATIVE_BUTTON_TEXT, negativeButtonText)
            oneLinkAlertDialogsFragment.arguments = args
            return oneLinkAlertDialogsFragment
        }

    }


    class Builder {
        private val instance = OneLinkAlertDialogsFragment()
        private val args = Bundle()

        fun setTargetFragment(fragment: Fragment, requestCode: Int) = apply {
            instance.setTargetFragment(
                fragment, requestCode
            )
        }

        fun setCancelable(cancelable: Boolean) = apply {
            instance.isCancelable = cancelable
        }

        fun setIsAlertOnly(isAlertOnly: Boolean) = apply {
            args.putBoolean(ARG_PARAM_IS_ALERT, isAlertOnly)
        }

        fun setDrawable(drawable: Int) = apply {
            args.putInt(ARG_PARAM_DRAWABLE, drawable)
        }

        fun setTitle(title: String) = apply {
            args.putString(ARG_PARAM_TITLE, title)
        }

        fun setMessage(message: Spanned) = apply {
            args.putCharSequence(ARG_PARAM_MESSAGE, message)
        }

        fun setNeutralButtonText(neutralButtonText: String) = apply {
            args.putString(ARG_PARAM_NEUTRAL_BUTTON_TEXT, neutralButtonText)
        }

        fun setPositiveButtonText(positiveButtonText: String) = apply {
            args.putString(ARG_PARAM_POSITIVE_BUTTON_TEXT, positiveButtonText)
        }

        fun setNegativeButtonText(negativeButtonText: String) = apply {
            args.putString(ARG_PARAM_NEGATIVE_BUTTON_TEXT, negativeButtonText)
        }

        fun build() = instance.apply { arguments = args }

        fun show(fm: FragmentManager, tag: String) {
            instance.apply { arguments = args }.show(fm, tag)
        }
    }

    private var isAlertOnly = false
    private var drawable = -1
    private var title = ""
    private var message: CharSequence = ""
    private var neutralButtonText = ""
    private var positiveButtonText = ""
    private var negativeButtonText = ""

    private lateinit var oneLinkAlertDialogListeners: OneLinkAlertDialogListeners

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.custom_dialog, container)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            window?.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        oneLinkAlertDialogListeners =
            if (targetFragment != null) targetFragment as OneLinkAlertDialogListeners
            else activity as OneLinkAlertDialogListeners

        val args: Bundle? = arguments

        isAlertOnly = args?.getBoolean(ARG_PARAM_IS_ALERT) ?: false
        drawable = args?.getInt(ARG_PARAM_DRAWABLE) ?: -1
        title = args?.getString(ARG_PARAM_TITLE) ?: ""
        message = args?.getCharSequence(ARG_PARAM_MESSAGE) ?: ""

        if (isAlertOnly) {
            neutralButtonText = args?.getString(ARG_PARAM_NEUTRAL_BUTTON_TEXT) ?: ""
        } else {
            positiveButtonText = args?.getString(ARG_PARAM_POSITIVE_BUTTON_TEXT) ?: ""
            negativeButtonText = args?.getString(ARG_PARAM_NEGATIVE_BUTTON_TEXT) ?: ""
        }

        updateView()
    }

    private fun updateView() {
        ivDialog?.setImageResource(drawable)
        if (drawable == 0)
            ivDialog.visibility = View.GONE
        else
            ivDialog.visibility = View.VISIBLE
        tvDialogTitle?.text = title
        tvDialogTitle.movementMethod = ScrollingMovementMethod.getInstance();
        tvDialogSubTitle?.text = message

        if (isAlertOnly) {
            lytPosNegButtons?.visibility = View.GONE
            btnNeutral?.visibility = View.VISIBLE
            btnNeutral?.text = neutralButtonText

            btnNeutral?.setOnClickListener {
                oneLinkAlertDialogListeners.onNeutralButtonClicked(targetRequestCode)
                dismiss()
            }
        } else {
            btnNeutral?.visibility = View.GONE
            lytPosNegButtons?.visibility = View.VISIBLE
            btnPositive?.text = positiveButtonText
            btnNegative?.text = negativeButtonText

            btnPositive?.setOnSingleClickListener {
                oneLinkAlertDialogListeners.onPositiveButtonClicked(targetRequestCode)
                dismiss()
            }

            btnNegative?.setOnClickListener {
                oneLinkAlertDialogListeners.onNegativeButtonClicked(targetRequestCode)
                dismiss()
            }
        }
    }

    interface OneLinkAlertDialogListeners {
        fun onNeutralButtonClicked(targetCode: Int)
        fun onPositiveButtonClicked(targetCode: Int)
        fun onNegativeButtonClicked(targetCode: Int)
    }
}
