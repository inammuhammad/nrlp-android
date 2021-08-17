package com.onelink.nrlp.android.features.login.helper

import android.annotation.SuppressLint
import android.text.Editable
import android.text.Selection
import com.onelink.nrlp.android.databinding.LoginFragmentBinding
import java.util.regex.Pattern

@SuppressLint("SetTextI18n")
object CnicTextHelper {

    fun afterTextChange(s: Editable?, binding: LoginFragmentBinding) {
        val regex1 = "^\\d{13,}$"
        val regex2 = "^\\d{5}-\\d{8,}$"
        val regex3 = "^[0-9-]{15}$"
        val regex4 = "^\\d{5}-\\d{7}-\\d$"
        val regex5 = "^\\d{12}-\\d"
        val inputString = s.toString()
        when {
            Pattern.matches(regex1, inputString) -> {
                binding.etCnic.setText(
                    inputString.substring(0, 5) + "-" + inputString.substring(
                        5, 12
                    ) + inputString.substring(12, 13)
                )
                binding.etCnic.setSelection(15)
            }
            Pattern.matches(regex2, inputString) -> {
                binding.etCnic.setText(
                    inputString.substring(0, 13) + "-" + inputString.substring(
                        13, 14
                    )
                )
                binding.etCnic.setSelection(15)
            }
            Pattern.matches(regex3, inputString) && !Pattern.matches(
                regex4, inputString
            ) -> {
                val newS = inputString.replace("-".toRegex(), "")
                binding.etCnic.setText(
                    newS.substring(0, 5) + "-" + newS.substring(
                        5, 12
                    ) + newS.substring(12, 13)
                )

                Selection.setSelection(binding.etCnic.text, 15)
            }
            Pattern.matches(regex5, inputString) -> {
                binding.etCnic.setText(
                    inputString.substring(
                        0, 5
                    ) + "-" + inputString.substring(5)
                )
                binding.etCnic.setSelection(inputString.length + 1)
            }
        }
    }

    fun onTextChange(
        s: CharSequence?,
        before: Int,
        count: Int,
        binding: LoginFragmentBinding
    ) {
        val inputString = s.toString()
        val editTextEditable: Editable? = binding.etCnic.text
        val editTextString = editTextEditable.toString()
        if (inputString.isNotEmpty() && editTextString.isNotEmpty() && before < count) {
            val regex1 = "^\\d{5}$"
            val regex2 = "^\\d{5}-\\d{7}$"
            val regex3 = "^\\d{5,12}$"
            when {
                Pattern.matches(regex1, inputString) || Pattern.matches(regex2, inputString) -> {
                    binding.etCnic.setText("$inputString-")
                    binding.etCnic.setSelection(inputString.length + 1)
                }
                Pattern.matches(regex3, inputString) -> {
                    binding.etCnic.setText(
                        inputString.substring(
                            0, 5
                        ) + "-" + inputString.substring(5)
                    )
                    binding.etCnic.setSelection(inputString.length + 1)
                }
            }
        }
    }
}