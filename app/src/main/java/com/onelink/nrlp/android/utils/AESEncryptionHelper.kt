package com.onelink.nrlp.android.utils

import android.util.Base64
import java.lang.Exception
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object AESEncryptionHelper {

    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS7Padding"
    private const val LENGTH_ERROR = "SecretKey length is not 32 chars"

    fun encrypt(str: String, secretKey: String, initializationVector: String): String {
        if (str.isEmpty() || secretKey.isEmpty() || initializationVector.isEmpty()) {
            return str
        }
        val encrypted =
            cipher(Cipher.ENCRYPT_MODE, secretKey, initializationVector).doFinal(
                str.toByteArray(
                    Charsets.UTF_8
                )
            )
        return Base64.encodeToString(encrypted, Base64.NO_WRAP or Base64.URL_SAFE)
    }

    fun decrypt(str: String, secretKey: String, initializationVector: String): String {
        if (str.isEmpty() || secretKey.isEmpty() || initializationVector.isEmpty()) {
            return str
        }
        val byteStr =
            Base64.decode(str.toByteArray(Charsets.UTF_8), Base64.DEFAULT)
        try {
            return String(
                cipher(Cipher.DECRYPT_MODE, secretKey, initializationVector).doFinal(
                    byteStr
                )
            )
        }catch (e: Exception){}
        return str
    }

    private fun cipher(opMode: Int, secretKey: String, initializationVector: String): Cipher {
        if (secretKey.length != 32) throw RuntimeException(LENGTH_ERROR)
        val c = Cipher.getInstance(TRANSFORMATION)
        val sk = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGORITHM)
        val iv = IvParameterSpec(initializationVector.toByteArray(Charsets.UTF_8))
        c.init(opMode, sk, iv)
        return c
    }
}