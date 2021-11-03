package com.onelink.nrlp.android.utils

import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Debug
import android.os.Process.killProcess
import android.os.Process.myPid
import android.util.Log
import com.google.gson.Gson
import com.guardsquare.dexguard.runtime.detection.*
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.models.toBoolean
import com.onelink.nrlp.android.utils.network.NetworkConnectivity
import com.onelink.nrlp.android.utils.network.NetworkConnectivity.Companion.getLocalIpAddress
import com.scottyab.rootbeer.RootBeer
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Qazi Abubakar on 20/07/2020.
 */
object AppUtils {
    val hookOK = 1

    /*
    * To authenticate app installation
     */
    fun isValidInstallation(context: Context?) =
        //(BuildConfig.IS_DEBUG) ||
                (!isRooted(context)
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
                || checkAndInitializeTampering(context) || isDeviceRootedSupplementary(context)
                || isHookDetected(context)
    } ?: false

    fun <T : Any> getSHA256JsonString(model: T): String = hash256(Gson().toJson(model))

    fun hash256(jsonString: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(jsonString.toByteArray())
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }

    private fun checkAndInitializeTampering(context: Context?): Boolean {
        // We need a context for most methods.
        //val context: Context = this

        // You can pick your own value or values for OK,
        // to make the code less predictable.
        val OK = 101
        var apkChanged = 0
        var certificateChanged = 0
        var primaryDexChanged = 0

        // Let the DexGuard runtime library detect whether the apk has
        // been modified or repackaged in any way (with jar, zip,
        // jarsigner, zipalign, or any other tool), after DexGuard has
        // packaged it. The return value is the value of the optional
        // integer argument OK (default=0) if the apk is unchanged.
        try {
            apkChanged = TamperDetector.checkApk(context, OK)
        } catch (ex: Exception) {
            //currentFocus?.let { CommonMethods.showSnackbar(this, it, ex.message, "OK") }
        }

        // Let the DexGuard runtime library detect whether the apk has
        // been re-signed with a different certificate, after DexGuard has
        // packaged it.  The return value is the value of the optional
        // integer argument OK (default=0) if the certificate is still
        // the same.
        try {
            certificateChanged = CertificateChecker.checkCertificate(context, OK)
        } catch (ex: Exception) {
            //currentFocus?.let { CommonMethods.showSnackbar(this, it, ex.message, "OK") }
        }

        // Let the DexGuard runtime library determine whether the primary
        // DEX has been modified, after DexGuard has packaged the APK. The
        // return value is the value of the optional integer argument OK
        // (default=0) if the file is still the same.
        //
        // Any file or combination of files can be checked, other than the
        // APK META-INF directory and the files contained therein.
        try {
            primaryDexChanged = FileChecker(context).checkFile("classes.dex", OK)
        } catch (ex: Exception) {
            //currentFocus?.let { CommonMethods.showSnackbar(this, it, ex.message, "OK") }
        }

        //int rootDetector = RootDetector.isDeviceRooted(context,OK);
        if (apkChanged != OK && certificateChanged != OK && primaryDexChanged != OK) {
            return true
        }
        return false
    }

    fun isPackageExisted(targetPackage: String, context: Context?): Boolean {
        val pm: PackageManager = context!!.getPackageManager()
        try {
            val info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    val packageName = arrayListOf<String>(
        "com.devadvance.rootcloak",
        "com.devadvance.rootcloakplus",
        "com.noshufou.android.su",
        "com.thirdparty.superuser",
        "eu.chainfire.supersu",
        "com.koushikdutta.superuser",
        "com.zachspong.temprootremovejb",
        "com.ramdroid.appquarantine",
        "com.ashensoftware.xposedframework"
    )

    private fun isDeviceRootedSupplementary(context: Context?): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3() || checkRootMethod4(context)
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod4(context: Context?): Boolean {
        packageName.forEach {
            if (isPackageExisted(it, context)) {
                return true
            }
        }
        return false
    }

    private fun checkRootMethod2(): Boolean {
        val paths = arrayOf(
            "/system/app/Superuser.apk",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/app/Superuser.apk",
            "/system/xbin/which",
            "/sbin/su",
            "/system/bin/su",
            "/system/xbin/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/su",
            "/su/bin/su",
            "/system/su",
            "/system/bin/.ext/.su",
            "/system/usr/we-need-root/su-backup",
            "/system/xbin/mu"
        )
        for (path in paths) {
            if (File(path).exists()) return true
        }
        return false
    }


    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        return try {
            process = Runtime.getRuntime()
                .exec(arrayOf("/system/xbin/which", "su"))
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            bufferedReader.readLine() != null
        } catch (t: Throwable) {
            false
        } finally {
            process?.destroy()
        }
    }

    private fun isHookDetected(context: Context?): Boolean {
        if(HookDetector.isApplicationHooked(context, hookOK) != hookOK)
            return true
        return false
    }
}