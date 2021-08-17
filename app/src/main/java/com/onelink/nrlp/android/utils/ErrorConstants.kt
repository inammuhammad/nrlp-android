package com.onelink.nrlp.android.utils

@Suppress("unused")
class ErrorConstants {

    companion object {
        const val ERROR_CNIC = "Enter a valid CNIC/NICOP Number"
        const val NAME_NOT_VALID_ERROR_MSG = "Enter a valid name"
        const val ALIAS_NOT_VALID_ERROR_MSG =
            "Enter a valid alias including alphanumeric characters only"
        const val PHONE_NOT_VALID_ERROR_MSG = "Enter a valid mobile number"
        const val EMAIL_NOT_VALID_ERROR_MSG = "Enter a valid email address"
        const val PASSWORDS_NOT_MATCH_ERROR_MSG = "Passwords do not match"
        const val PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN = "Enter a valid password"
        const val PASSWORD_NOT_VALID_ERROR_MSG =
            "Your password must contain atleast 8 characters, including 1 small letter, 1 capital letter, 1 number and 1 special character"
    }
}