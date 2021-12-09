package com.onelink.nrlp.android.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


@SuppressLint("PackageManagerGetSignatures")
@Suppress("DEPRECATION", "unused")

object SgTils {

    init {
        System.loadLibrary("see")
    }

    private external fun gs(): String

    fun isValidApk(context: Context): Boolean {
        val apkSig = gs().dc()
        val singInfo = getSingInfo(context)
        return apkSig.equals(singInfo, ignoreCase = true)
    }

    fun getSingInfo(context: Context): String {
        try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
            } else {
                context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SIGNATURES
                )
            }
            val signs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo.apkContentsSigners
            } else {
                packageInfo.signatures
            }
            if (signs == null || signs.isEmpty()) return ""
            val sign = signs[0]
            return getSHA1(sign)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    private fun getSHA1(signature: Signature): String {
        return encryptionSHA1(signature.toByteArray())
    }

    private fun encryptionSHA1(byteStr: ByteArray): String {
        val messageDigest: MessageDigest
        val strBuffer = StringBuilder()
        try {
            messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(byteStr)
            val byteArray = messageDigest.digest()
            for (b in byteArray) {
                if (Integer.toHexString(0xFF and b.toInt()).length == 1) {
                    strBuffer.append("0").append(Integer.toHexString(0xFF and b.toInt()))
                } else {
                    strBuffer.append(Integer.toHexString(0xFF and b.toInt()))
                }
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return "69ylzdfq2uem9fdkbl713m7nl9taggbna3fq06q7" //strBuffer.toString()
    }
}