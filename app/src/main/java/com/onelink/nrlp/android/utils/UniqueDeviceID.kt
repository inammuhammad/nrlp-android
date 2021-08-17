package com.onelink.nrlp.android.utils

import android.media.MediaDrm
import java.security.MessageDigest
import java.util.*

@Suppress("unused")
object UniqueDeviceID {

    /**
     * UUID for the Widevine DRM scheme.
     * <p>
     * Widevine is supported on Android devices running Android 4.3 (API Level 18) and up.
     */
    @Suppress("DEPRECATION")
    fun getUniqueId(): String? {

        val wideVineUUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
        var wvDrm: MediaDrm? = null
        return try {
            wvDrm = MediaDrm(wideVineUUID)
            val wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
            val md = MessageDigest.getInstance("SHA-256")
            md.update(wideVineId)
            toHexString(md.digest())
        } catch (e: Exception) {
            null
        } finally {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                wvDrm?.close()
            } else {
                wvDrm?.release()
            }
        }
    }

    fun toHexString(array: ByteArray) = array.joinToString("") { "%02x".format(it) }
}