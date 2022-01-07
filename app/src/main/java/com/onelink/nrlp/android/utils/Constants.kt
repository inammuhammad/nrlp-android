package com.onelink.nrlp.android.utils

import com.onelink.nrlp.android.BuildConfig

class Constants {
    companion object {
        val DOMAIN = LukaKeRakk.gdm()
        val BASE_URL = LukaKeRakk.ure38()
        const val BENEFICIARY = "Beneficiary"
        const val REMITTER = "Remitter"

        // Comment date checksum replaced(DD-MM-YY) + checksum replaced
        // 29-12-21 69ylzdfq2uem9fdkbl713m7nl9taggbna3fq06q7
        const val checkSum = "w73v34zmazcrlz1li8v3iplfdoawrjz7ggsutsin"

        const val InternationalPassport = "International Passport"
        const val PakistaniPassport = "Pakistani Passport"

        const val INTENT_KEY_ACCOUNT_TYPE = "account_type"
        const val SPINNER_ACCOUNT_TYPE_HINT = "Account Type"
        const val SPINNER_BENEFICIARY_HINT = "Beneficiary Relation"
        const val SPINNER_PASSPORT_TYPE_HINT = "Passport Type"
        const val SOMETHING_WENT_WRONG = "Something unexpected happened. Please try again."
    }
}

object ErrorCodesConstants {
    const val NO_INTERNET_CONNECTION = "CONNECTION-ERROR"
    const val SERVER_ERROR = "SERVER-ERROR"
    const val SESSION_EXPIRED = "GEN-ERR-08"
    const val UNVERIFIED_DEVICE = "AUTH-LOG-02"
    const val UNSUCCESSFUL_TRANSACTION_FETCH = "AUTH-VRN-06"
}


object IntentConstants {
    const val TRANSACTION_ID = "transaction_id"
    const val PARTNER_NAME = "partner_name"
    const val REDEEM_POINTS = "redeem_points"
    const val PSID = "psid"
    const val LOGIN_CREDENTIALS_PARCELABLE = "login_credentials_parcelable"
    const val IS_FROM_SPLASH = "is_from_splash"
    const val SELF_AWARD_VERIFY_OTP_Message = "self_award_verify_otp_message"
}

object LoyaltyCardConstants {
    const val BRONZE = "bronze"
    const val PLATINUM = "platinum"
    const val GOLD = "gold"
    const val SILVER = "silver"
}

object ErrorDialogConstants {
    const val RC_NO_INTERNET_CONNECTION_DIALOG = 1010
    const val TAG_NO_INTERNET_CONNECTION_DIALOG = "no_internet_connection"
    const val RC_GENERAL_SERVER_ERROR_DIALOG_CODE = 1011
    const val TAG_GENERAL_SERVER_ERROR_DIALOG_CODE = "general_server_error"
    const val RC_SESSION_EXPIRED = 1012
    const val TAG_SESSION_EXPIRED = "session_expired"
}

object HeaderConstants {
    const val CLIENT_ID = "x-ibm-client-id"
    const val CLIENT_SECRET = "x-ibm-client-secret"
    const val CONTENT_TYPE = "content-type"
    const val ACCEPT = "accept"
    const val APPLICATION_JSON = "application/json"
    const val AUTHORIZATION = "authorization"
    const val BEARER = "Bearer "
    const val DEVICE_ID = "device_id"
    const val APPLICATION_VERSION = "application_version"
    const val RANDOM_KEY = "random_key"
    const val SESSION_KEY = "session_key"
    const val DEVICE_NAME = "device_name"
    const val OS_VERSION = "os_version"
    const val DEVICE_TYPE = "device_type"
}

object PlayStoreConstants {
    const val VENDING = "com.android.vending"
    const val FEEDBACK = "com.google.android.feedback"
}

object UriConstants {
    const val Phone_NO = "+92 21 111 116 757"
    const val Email = "support@nrlp.com.pk"
    const val Web = "www.NRLP.com.pk"
    const val ABOUT_NRLP_URL = "https://1link.net.pk/sohni-dharti/#1633123604420-798e6f70-2ecf"
}

object Sp {
    init {
        System.loadLibrary("see")
    }

    val AK = if (BuildConfig.IS_DEBUG) ak().dc() else pak().dc()
    val BK = if (BuildConfig.IS_DEBUG) bk().dc() else pbk().dc()
    val CK = if (BuildConfig.IS_DEBUG) ck().dc() else pck().dc()
    val DK = if (BuildConfig.IS_DEBUG) dk().dc() else pdk().dc()

    private external fun ak(): String

    private external fun bk(): String

    private external fun ck(): String

    private external fun dk(): String

    private external fun pak(): String

    private external fun pbk(): String

    private external fun pck(): String

    private external fun pdk(): String


}