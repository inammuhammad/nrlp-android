package com.onelink.nrlp.android.utils

import org.junit.Test

import org.junit.Assert.*

class AESEncryptionHelperTest {

    companion object {
        private const val ENCRYPTED_TEXT = "7SB1V97oF2L8AX05r0YUCw=="
        private const val DECRYPTED_TEXT = "1Link"
        private const val SECRET_KEY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
        private const val IV = "BBBBBBBBBBBBBBBB"
    }

    @Test
    fun textEncryption_validation_success() {
        val result = AESEncryptionHelper.encrypt(DECRYPTED_TEXT, SECRET_KEY, IV)
        assertEquals(result, ENCRYPTED_TEXT)
    }

    @Test
    fun textDecryption_validation_success() {
        val result = AESEncryptionHelper.decrypt(ENCRYPTED_TEXT, SECRET_KEY, IV)
        assertEquals(result, DECRYPTED_TEXT)
    }
}