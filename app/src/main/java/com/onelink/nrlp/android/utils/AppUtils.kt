package com.onelink.nrlp.android.utils

import android.content.Context
import android.os.Build
import android.os.Debug
import com.google.gson.Gson
import com.guardsquare.dexguard.runtime.detection.RootDetector
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.models.toBoolean
import com.onelink.nrlp.android.utils.network.NetworkConnectivity
import com.onelink.nrlp.android.utils.network.NetworkConnectivity.Companion.getLocalIpAddress
import com.scottyab.rootbeer.RootBeer
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Qazi Abubakar on 20/07/2020.
 */
object AppUtils {

    /*
    * To authenticate app installation
     */
    fun isValidInstallation(context: Context?) = (BuildConfig.IS_DEBUG)
            || (!isRooted(context)
            && !isEmulator()
            && !Debug.isDebuggerConnected())

    //&& isFromPlayStore()
    //&& SgTils.isValidApk(OneLinkApplication.getInstance())

    /*
    * it takes all officially installed packages from android system.
    * after getting installerPackages, it check whether application package exist in them or not
    * if the build is in debug, it will return true
     */

    @Suppress("unused")
    fun isFromPlayStore(): Boolean {
        if (BuildConfig.IS_DEBUG) return true
        val validInstallers: List<String> = ArrayList(
            listOf(
                PlayStoreConstants.VENDING,
                PlayStoreConstants.FEEDBACK
            )
        )
        val installer = BuildConfig.APPLICATION_ID
        return validInstallers.contains(installer)
    }

    /*
    * gets fingerprint and build details and compare whether its an emulator or not
     */
    private fun isEmulator() =
        (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")

    private fun isRooted(context: Context?) = context?.let {
        RootBeer(it).isRootedWithBusyBoxCheck || RootDetector.isDeviceRooted(context).toBoolean()
    } ?: false

    fun <T : Any> getSHA256JsonString(model: T): String = hash256(Gson().toJson(model))

    fun hash256(jsonString: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(jsonString.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}