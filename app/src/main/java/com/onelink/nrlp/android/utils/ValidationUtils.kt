package com.onelink.nrlp.android.utils

import java.util.regex.Pattern

const val REGISTRATION_CODE_LENGTH = 4
const val AGENT_CODE_LENGTH = 6

object ValidationUtils {

    private const val STRING_CNIC_VALIDATOR_REGEX = "^[0-9]{5}-[0-9]{7}-[0-9]$"

    // const val REGEX_STRING_NOT_EMPTY = "(\\S)+"
    private const val STRING_PASSWORD_VALIDATOR_REGEX =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[\\\\/%§\"&“|`´}{°><:.,;#+')(@_\$\"!?*=^-]).{8,}\$"
    private const val STRING_FULL_NAME_VALIDATOR_REGEX = "^[a-zA-Z ]*\$"
    private const val ALIAS_VALIDATOR_REGEX = "^[a-zA-Z0-9 ]{1,50}\$"
    private const val AGENT_CODE_VALIDATOR_REGEX = "^[a-zA-Z0-9 ]{6}\$"
    private const val STRING_EMAIL_VALIDATOR_REGEX =
        "(^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)+$)|^$"
    private const val PHONE_NUMBER_VALIDATOR_REGEX = "^\\+?[1-9]\\d+$"

    fun isValidAmount(value: String): Boolean {
        if (value.isEmpty()) {
            return false
        }
        return try {
            value.replace(",", "").toDouble() >= 1
        } catch (e: NumberFormatException) {
            false
        }
    }

    @Suppress("unused")
    fun isValidRegistrationCode(value: String): Boolean {
        return value.length == REGISTRATION_CODE_LENGTH
    }

    @Suppress("unused")
    fun isAliasValid(value: String): Boolean {
        return Pattern.matches(ALIAS_VALIDATOR_REGEX, value)
    }

    fun isCNICValid(cnic: String): Boolean {
        return Pattern.matches(STRING_CNIC_VALIDATOR_REGEX, cnic) && !cnic.removeDashes()
            .allCharactersSame()
    }

    fun isValidAgentCode(agentCode: String): Boolean {
        return Pattern.matches(AGENT_CODE_VALIDATOR_REGEX, agentCode)
    }

    fun isValidAgentCodeLength(agentCode: String): Boolean {
        return agentCode.length == AGENT_CODE_LENGTH
    }

    fun isPhoneNumberValid(phoneNumber: String, length: Int?): Boolean {
        return phoneNumber.length == length && Pattern.matches(
            PHONE_NUMBER_VALIDATOR_REGEX,
            phoneNumber
        )
    }

    fun isPasswordValid(pass: String): Boolean {
        return Pattern.matches(STRING_PASSWORD_VALIDATOR_REGEX, pass)
    }

    fun isPasswordLengthValid(pass: String): Boolean {
        return pass.length >= 8
    }

    fun isNameValid(name: String): Boolean {
        return Pattern.matches(STRING_FULL_NAME_VALIDATOR_REGEX, name)
    }

    fun isSpinnerNotEmpty(selected: String, default: String): Boolean {
        return selected != default
    }

    fun arePointsValid(points: String): Boolean {
        return points.isNotEmpty() && points.length < 5
    }

    fun isEmailValid(emailAddress: String) = emailAddress.isEmpty() || Pattern.matches(
        STRING_EMAIL_VALIDATOR_REGEX,
        emailAddress
    )
}