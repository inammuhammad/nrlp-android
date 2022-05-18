package com.onelink.nrlp.android.utils.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.Selection
import android.text.Spanned
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.onelink.nrlp.android.R
import com.onelink.nrlp.android.utils.ValidationUtils
import kotlinx.android.synthetic.main.dgip_dialog.*
import kotlinx.android.synthetic.main.dgip_dialog.eTCnicNumber
import java.util.regex.Pattern


class OneLinkAlertDGIPDialogFragment : DialogFragment() {

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
                OneLinkAlertDGIPDialogFragment {
            val oneLinkAlertDialogsFragment = OneLinkAlertDGIPDialogFragment()
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
        private val instance = OneLinkAlertDGIPDialogFragment()
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
        return inflater.inflate(R.layout.dgip_dialog, container)
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
        initTextWatchers()
        updateView()
    }

    private fun updateView() {
        ivDialog?.setImageResource(drawable)
        tvDialogTitle?.text = title
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

            btnPositive?.setOnClickListener {
                /*oneLinkAlertDialogListeners.onPositiveButtonClicked(targetRequestCode)*/
                if(!eTCnicNumber.text.isNullOrBlank() && eTCnicNumber.text.toString().length == 15
                    && ValidationUtils.checkRepeats(eTCnicNumber.text.toString())) {
                    tilCnicNicop.clearError()
                    if(!eTMobile.text.isNullOrBlank() && ValidationUtils.checkRepeats(eTMobile.text.toString())
                        && !eTMobile.text.toString().contains("+", true)) {
                        tilMobileNo.clearError()
                        oneLinkAlertDialogListeners.onConfirmButtonCLicked(targetRequestCode,
                            eTCnicNumber.text.toString(),
                            eTMobile.text.toString(),
                            eTEmail.text.toString())
                        dismiss()
                    }
                    else {
                        tilMobileNo.error = "Please enter valid Mobile No"
                    }
                }
                else {
                    tilCnicNicop.error = "Please enter valid CNIC"
                }
            }

            btnNegative?.setOnClickListener {
                oneLinkAlertDialogListeners.onNegativeButtonClicked(targetRequestCode)
                dismiss()
            }
        }
    }

    private fun initTextWatchers() {
        eTCnicNumber.addTextChangedListener(object : TextWatcher {
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                val regex1 = "^\\d{13,}$"
                val regex2 = "^\\d{5}-\\d{8,}$"
                val regex3 = "^[0-9-]{15}$"
                val regex4 = "^\\d{5}-\\d{7}-\\d$"
                val regex5 = "^\\d{12}-\\d"
                val inputString = s.toString()
                if (Pattern.matches(regex1, inputString)) {
                    eTCnicNumber.setText(
                        inputString.substring(0, 5) + "-" + inputString.substring(5, 12)
                                +
                                inputString.substring(12, 13)
                    )
                    eTCnicNumber.setSelection(15)
                } else if (Pattern.matches(regex2, inputString)) {
                    eTCnicNumber.setText(
                        inputString.substring(0, 13) + "-" + inputString.substring(
                            13,
                            14
                        )
                    )
                    eTCnicNumber.setSelection(15)
                } else if (Pattern.matches(regex3, inputString) && !Pattern.matches(
                        regex4,
                        inputString
                    )
                ) {
                    val newS = inputString.replace("-".toRegex(), "")
                    eTCnicNumber.setText(
                        newS.substring(0, 5) + "-" + newS.substring(
                            5,
                            12
                        ) + newS.substring(12, 13)
                    )

                    Selection.setSelection(eTCnicNumber.text, 15)
                } else if (Pattern.matches(regex5, inputString)) {
                    eTCnicNumber.setText(
                        inputString.substring(
                            0,
                            5
                        ) + "-" + inputString.substring(5)
                    )
                    eTCnicNumber.setSelection(inputString.length + 1)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                eTCnicNumber.removeTextChangedListener(this)
                val inputString = s.toString()
                val editTextEditable: Editable? = eTCnicNumber.text
                val editTextString = editTextEditable.toString()
                if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
                    val regex1 = "^\\d{5}$"
                    val regex2 = "^\\d{5}-\\d{7}$"
                    val regex3 = "^\\d{5,12}$"
                    when {
                        Pattern.matches(regex1, inputString) ||
                                Pattern.matches(regex2, inputString) -> {
                            eTCnicNumber.setText("$inputString-")
                            eTCnicNumber.setSelection(inputString.length + 1)
                        }

                        Pattern.matches(regex3, inputString) -> {
                            eTCnicNumber.setText(
                                inputString.substring(
                                    0,
                                    5
                                ) + "-" + inputString.substring(5)
                            )
                            eTCnicNumber.setSelection(inputString.length + 1)
                        }
                    }
                }
                eTCnicNumber.addTextChangedListener(this)
            }
        })

    }

    interface OneLinkAlertDialogListeners {
        fun onNeutralButtonClicked(targetCode: Int)
        fun onPositiveButtonClicked(targetCode: Int)
        fun onNegativeButtonClicked(targetCode: Int)
        fun onConfirmButtonCLicked(targetCode: Int,cnic: String, mobileNo: String,email: String)
    }
}