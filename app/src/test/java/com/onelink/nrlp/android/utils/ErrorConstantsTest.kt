package com.onelink.nrlp.android.utils

import org.junit.Assert.*
import org.junit.Test

class ErrorConstantsTest {

    companion object {
        private const val ERROR_CNIC = "Enter a valid CNIC/NICOP Number"
        private const val NAME_NOT_VALID_ERROR_MSG = "Enter a valid name"
        private const val ALIAS_NOT_VALID_ERROR_MSG =
            "Enter a valid alias including alphanumeric characters only"
        private const val PHONE_NOT_VALID_ERROR_MSG = "Enter a valid mobile number"
        private const val EMAIL_NOT_VALID_ERROR_MSG = "Enter a valid email address"
        private const val PASSWORDS_NOT_MATCH_ERROR_MSG = "Passwords do not match"
        private const val PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN = "Enter a valid password"
        private const val PASSWORD_NOT_VALID_ERROR_MSG =
            "Your password must contain atleast 8 characters, including 1 small letter, 1 capital letter, 1 number and 1 special character"
    }

    @Test
    fun errorCnicTest() {
        assertNotNull(ErrorConstants.ERROR_CNIC)
        assertEquals(ERROR_CNIC, ErrorConstants.ERROR_CNIC)
        assertTrue(ERROR_CNIC == ErrorConstants.ERROR_CNIC)
    }

    @Test
    fun nameNotValidErrorMsgTest() {
        assertNotNull(ErrorConstants.NAME_NOT_VALID_ERROR_MSG)
        assertEquals(NAME_NOT_VALID_ERROR_MSG, ErrorConstants.NAME_NOT_VALID_ERROR_MSG)
        assertTrue(NAME_NOT_VALID_ERROR_MSG == ErrorConstants.NAME_NOT_VALID_ERROR_MSG)
    }

    @Test
    fun aliasNotValidErrorMsgTest() {
        assertNotNull(ErrorConstants.ALIAS_NOT_VALID_ERROR_MSG)
        assertEquals(ALIAS_NOT_VALID_ERROR_MSG, ErrorConstants.ALIAS_NOT_VALID_ERROR_MSG)
        assertTrue(ALIAS_NOT_VALID_ERROR_MSG == ErrorConstants.ALIAS_NOT_VALID_ERROR_MSG)
    }


    @Test
    fun phoneNotValidErrorMsgTest() {
        assertNotNull(ErrorConstants.PHONE_NOT_VALID_ERROR_MSG)
        assertEquals(PHONE_NOT_VALID_ERROR_MSG, ErrorConstants.PHONE_NOT_VALID_ERROR_MSG)
        assertTrue(PHONE_NOT_VALID_ERROR_MSG == ErrorConstants.PHONE_NOT_VALID_ERROR_MSG)
    }


    @Test
    fun invalidEmailMsgTest() {
        assertNotNull(ErrorConstants.EMAIL_NOT_VALID_ERROR_MSG)
        assertEquals(EMAIL_NOT_VALID_ERROR_MSG, ErrorConstants.EMAIL_NOT_VALID_ERROR_MSG)
        assertTrue(EMAIL_NOT_VALID_ERROR_MSG == ErrorConstants.EMAIL_NOT_VALID_ERROR_MSG)
    }


    @Test
    fun passwordNotMatchedErrorMsgTest() {
        assertNotNull(ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG)
        assertEquals(PASSWORDS_NOT_MATCH_ERROR_MSG, ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG)
        assertTrue(PASSWORDS_NOT_MATCH_ERROR_MSG == ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG)
    }


    @Test
    fun invalidPasswordErrorMsgTest() {
        assertNotNull(ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN)
        assertEquals(PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN, ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN)
        assertTrue(PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN == ErrorConstants.PASSWORDS_NOT_MATCH_ERROR_MSG_LOGIN)
    }


    @Test
    fun passwordLengthErrorMsgTest() {
        assertNotNull(ErrorConstants.PASSWORD_NOT_VALID_ERROR_MSG)
        assertEquals(PASSWORD_NOT_VALID_ERROR_MSG, ErrorConstants.PASSWORD_NOT_VALID_ERROR_MSG)
        assertTrue(PASSWORD_NOT_VALID_ERROR_MSG == ErrorConstants.PASSWORD_NOT_VALID_ERROR_MSG)
    }
}