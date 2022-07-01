package com.onelink.nrlp.android.utils

import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.R

class Constants {
    companion object {
        val DOMAIN = LukaKeRakk.gdm()
        val BASE_URL = LukaKeRakk.ure38()
        const val BENEFICIARY = "Beneficiary"
        const val REMITTER = "Remitter"

        // Comment date checksum replaced(DD-MM-YY) + checksum replaced
        // 00-00-00 w73v34zmazcrlz1li8v3iplfdoawrjz7ggsutsin ***** QA checksum *****
        // 08-06-2022 nqsuyvjyu2xagcdz8jz0cexf6mn36hg7z58dyp12 2.3
        // 08-06-2022 kvpmpr0mb8i3xa90ifppu9yj08ktib9quupnz94m 2.4
        // 22-06-2022 ys7jjeixu43f49y07xij81b34uhxo63q3j1o5r02 2.5
        // 29-06-2022 kvpmpr0mb8i3xa90ifppu9yj08ktib9quupnz94m 2.6
        const val checkSum =
            "w73v34zmazcrlz1li8v3iplfdoawrjz7ggsutsin" //"ramind4gtozrzwd5ntq5l44lqbf036qnspf7a3yk"
        const val InternationalPassport = "International Passport"
        const val PakistaniPassport = "Pakistani Passport"

        const val INTENT_KEY_ACCOUNT_TYPE = "account_type"
        const val SPINNER_ACCOUNT_TYPE_HINT = "Account Type"
        const val SPINNER_BENEFICIARY_HINT = "Beneficiary Relation"
        const val SPINNER_PASSPORT_TYPE_HINT = "Passport Type"
        const val SOMETHING_WENT_WRONG = "Something unexpected happened. Please try again."
        const val PLAY_STORE_NRLP_URL = "https://play.google.com/store/apps/details?id=com.onelink.sohnidharti.app"
    }
}

object ErrorCodesConstants {
    const val NO_INTERNET_CONNECTION = "CONNECTION-ERROR"
    const val SERVER_ERROR = "SERVER-ERROR"
    const val SESSION_EXPIRED = "GEN-ERR-08"
    const val PROFILE_VERIFICATION_FAILED = "GEN-ERR-53"
    const val UNVERIFIED_DEVICE = "AUTH-LOG-02"
    const val UNSUCCESSFUL_TRANSACTION_FETCH = "AUTH-VRN-06"
    const val INCORRECT_INFORMATION = "PU-VA-01"
    const val ATTEMPTS_EXCEEDED = "PU-VA-99"
    const val NEW_VERSION = "GEN-ERR-19"
    const val REMITTANCE_OLDER_DATE = "AUTH-SA-21"
    const val REMITTANCE_SAME_DATE = "AUTH-SA-20"
    const val REMITTANCE_RDA_TRANSACTION = "AUTH-SA-19"
    const val SELF_AWARD_NOT_ELIGIBLE = "AUTH-SA-22"
    const val SELF_AWARD_NOT_ALLOWED = "AUTH-SA-23"
}


object IntentConstants {
    const val TRANSACTION_ID = "transaction_id"
    const val PARTNER_NAME = "partner_name"
    const val REDEEM_POINTS = "redeem_points"
    const val PSID = "psid"
    const val LOGIN_CREDENTIALS_PARCELABLE = "login_credentials_parcelable"
    const val IS_FROM_SPLASH = "is_from_splash"
    const val SELF_AWARD_VERIFY_OTP_Message = "self_award_verify_otp_message"
    const val AUTH_ID = "auth_id"
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
    const val RC_NEW_VERSION = 1023
    const val TAG_NEW_VERSION = "new_version_available"
    const val REMITTANCE_DIALOG = 1030
    const val SELF_AWARD_DIALOG = 1031
    const val TAG_UNABLE_TO_SELF_AWARD = "unable_to_self_award"
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

object UserTypeValue{
    const val Remitter=0
    const val Beneficiary=1
}

object SelfAwardRequestConstants{
    const val Amount="amount"
    const val Reference_NO="reference_no"
    const val Beneficiary_NIC_NICOP="beneficiary_nic_nicop"
    const val Beneficiary_ACCOUNT_NUMBER="beneficiary_account_number"
    const val Self_Award_Row_ID="sp_respone_row_id"
    const val Transaction_Date="transaction_date"
    const val Transaction_TYPE="transaction_type"
}

object ComplaintRequestModelConstants{
    const val Registered="c_registered"
    const val User_type="c_user_type"
    const val Complaint_type_id="c_complaint_type_id"
    const val Nic_nicop="c_nic_nicop"
    const val Beneficiary_Nic_nicop="c_beneficiary_nic_nicop"
    const val Beneficiary_Nic_Account="c_beneficiary_nic_nicop"
    const val Name="c_name"
    const val Mobile_no="c_mobile_no"
    const val Email="c_email"
    const val Country_of_residence="c_country_of_residence"
    const val Beneficiary_Country_of_residence="c_beneficiary_country_of_residence"
    const val Comments="c_comments"
    const val Mobile_Operator="c_mobile_operator_name"
    const val Beneficiary_Mobile_Operator="c_beneficiary_mobile_operator_name"
    const val Beneficiary_Mobile_Number="c_beneficiary_mobile_no"
    const val Transaction_type="c_transaction_type"
    const val Transaction_id="c_transaction_id"
    const val Transaction_amount="c_transaction_amount"
    const val Transaction_date="c_transaction_date"
    const val Remitting_entity="c_remitting_entity"
    const val Redemption_Partner="c_redemption_partners"
}

object RequestModelConstants{
    const val Mother_name="mother_maiden_name"
}

object UnregisteredComplaintTypes{
    const val UNABLE_TO_REGISTER= R.string.unable_to_register
    const val UNABLE_TO_RECEIVE_REGISTRATION_CODE=R.string.unable_to_registration_code
    const val UNABLE_TO_RECEIVE_OTP=R.string.unable_to_otp
    const val OTHERS=R.string.others

}

object RegisteredComplaintTypes{
    const val UNABLE_TO_RECEIVE_OTP=R.string.unable_to_otp
    const val UNABLE_TO_ADD_BENEFICIARY=R.string.unable_to_add_beneficiary

    const val UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY = R.string.unable_to_transfer_points

    const val UNABLE_TO_SELF_AWARDS_POINTS = R.string.unable_to_self_award_points

    const val REDEMPTION_ISSUES = R.string.redemption_issues

    const val OTHERS = R.string.others
}

object COMPLAINT_TYPE {

    const val UNABLE_TO_REGISTER = 1

    const val UNABLE_TO_RECEIVE_REGISTRATION_CODE = 2

    const val UNABLE_TO_RECEIVE_OTP = 3

    const val UNABLE_TO_ADD_BENEFICIARY = 4

    const val UNABLE_TO_TRANSFER_POINTS_TO_BENEFICIARY = 5

    const val UNABLE_TO_SELF_AWARDS_POINTS = 6

    const val REDEMPTION_ISSUES = 7

    const val OTHERS = 8

}

object ViewStatementConstants {
    const val CREDIT = "Credit"
    const val DEBIT = "Debit"
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