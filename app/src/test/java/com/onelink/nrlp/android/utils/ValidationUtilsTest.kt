package com.onelink.nrlp.android.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidationUtilsTest {

    companion object {

        private const val CNIC_VALID = "34310-5603816-1"
        private const val CNIC_INVALID = "3431056038161"
        private const val PHONE_VALID = "3318415906"
        private const val PHONE_INVALID = "+92-3318415906"
        private const val AGENT_CODE_VALID = "a67ss7"
        private const val AGENT_CODE_INVALID = "asdfg"
        private const val PASSWORD_VALID = "ssSS@@11"
        private const val PASSWORD_INVALID = "1234568"
        private const val NAME_VALID = "Ali"
        private const val NAME_INVALID = "Ali007"
        private const val POINTS_VALID = "1234"
        private const val POINTS_INVALID = "12345"
        private const val EMAIL_VALID = "test@gmail.com"
        private const val EMAIL_INVALID = "test@gmailcom"

    }

    @Test
    fun cnicValidationSuccess() = assertTrue(ValidationUtils.isCNICValid(CNIC_VALID))

    @Test
    fun cnicValidationFailed() = assertFalse(ValidationUtils.isCNICValid(CNIC_INVALID))

    @Test
    fun phoneValidationSuccess() = assertTrue(
        ValidationUtils.isPhoneNumberValid(
            PHONE_VALID,
            PHONE_VALID.length
        )
    )

    @Test
    fun phoneValidationFailed() = assertFalse(
        ValidationUtils.isPhoneNumberValid(
            PHONE_INVALID,
            PHONE_INVALID.length
        )
    )

    @Test
    fun agentCodeValidationSuccess() = assertTrue(
        ValidationUtils.isValidAgentCode(
            AGENT_CODE_VALID
        )
    )

    @Test
    fun agentCodeValidationFailed() = assertFalse(
        ValidationUtils.isValidAgentCode(
            AGENT_CODE_INVALID
        )
    )

    @Test
    fun agentCodeLengthSuccess() = assertTrue(
        ValidationUtils.isValidAgentCodeLength(
            AGENT_CODE_VALID
        )
    )

    @Test
    fun agentCodeLengthFailed() = assertFalse(
        ValidationUtils.isValidAgentCodeLength(
            AGENT_CODE_INVALID
        )
    )

    @Test
    fun passwordValidationSuccess() = assertTrue(
        ValidationUtils.isPasswordValid(
            PASSWORD_VALID
        )
    )


    @Test
    fun passwordValidationFailed() = assertFalse(
        ValidationUtils.isPasswordValid(
            PASSWORD_INVALID
        )
    )

    @Test
    fun passwordLengthSuccess() = assertTrue(
        ValidationUtils.isPasswordLengthValid(
            PASSWORD_VALID
        )
    )

    @Test
    fun passwordLengthFailed() = assertFalse(
        ValidationUtils.isPasswordLengthValid(
            PASSWORD_INVALID
        )
    )

    @Test
    fun nameValidationSuccess() = assertTrue(
        ValidationUtils.isNameValid(
            NAME_VALID
        )
    )

    @Test
    fun nameValidationFailed() = assertFalse(
        ValidationUtils.isNameValid(
            NAME_INVALID
        )
    )

    @Test
    fun pointsValidationSuccess() = assertTrue(
        ValidationUtils.arePointsValid(
            POINTS_VALID
        )
    )

    @Test
    fun pointsValidationFailed() = assertFalse(
        ValidationUtils.arePointsValid(
            POINTS_INVALID
        )
    )

    @Test
    fun emailValidationSuccess() = assertTrue(
        ValidationUtils.isEmailValid(
            EMAIL_VALID
        )
    )

    @Test
    fun emailValidationFailed() = assertFalse(
        ValidationUtils.isEmailValid(
            EMAIL_INVALID
        )
    )

}