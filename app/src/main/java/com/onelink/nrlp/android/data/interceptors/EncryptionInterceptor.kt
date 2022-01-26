package com.onelink.nrlp.android.data.interceptors

import android.content.Context
import android.os.Build
import android.widget.Toast
import com.google.gson.Gson
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.utils.*
import okhttp3.*
import okio.Buffer
import org.json.JSONObject

var key1 = ""
var key2 = ""

class EncryptionInterceptor(val context: Context) : Interceptor {

    val whiteListNumber1 = "4330110434978"
    val whiteListNumber2 = "4330110436970"
    val whiteListNumber3 = "4330110435979"

    companion object {
        private const val PAYLOAD_HASH = "hash"
        private const val ENCRYPTION_KEY = "encryption_key"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val buffer = Buffer()
        request.body()?.writeTo(buffer)
        val oldPayload = buffer.readUtf8()
        return chain.proceed(getEncryptedRequest(request, oldPayload))
    }

    private fun getEncryptedRequest(request: Request, oldPayload: String): Request {
        var body = request.body()
        var keyIV = Pair(UniqueDeviceID.getUniqueId()?.take(32), UserData.finalEncryptionIV)
        try {
            key1 = UniqueDeviceID.getUniqueId()?.take(32).toString()
            key2 = UserData.finalEncryptionIV.toString()
            if(key2 == "null")
                key2 = ""
        }catch (e: Exception){}

        if (oldPayload.isNotEmpty()) {
            val jsonPayload = JSONObject(oldPayload)
            keyIV = getKeyIVPair(jsonPayload)
            var newPayload = getEncryptedPayload(jsonPayload, keyIV)//jsonPayload
            if( oldPayload.contains(whiteListNumber1, true) ||
                oldPayload.contains(whiteListNumber2, true) ||
                oldPayload.contains(whiteListNumber3, true)){
                newPayload = JSONObject(oldPayload)
            }
            body = RequestBody.create(
                MediaType.get(HeaderConstants.APPLICATION_JSON),
                newPayload.toString()
            )
        }
        return getRequestWithHeaders(request, body, keyIV)
    }


    private fun getRequestWithHeaders(
        request: Request,
        body: RequestBody?,
        keyIV: Pair<String?, String?>
    ): Request {

        val headers = request.headers()
        val encryptedRequest = request.newBuilder()

        addOrRemoveHeader(
            encryptedRequest,
            HeaderConstants.DEVICE_ID,
            UniqueDeviceID.getUniqueId() ?: "", headers, keyIV
        )

        addOrRemoveHeader(
            encryptedRequest,
            HeaderConstants.APPLICATION_VERSION,
            Constants.checkSum , headers, keyIV
        )

        addOrRemoveHeader(
            encryptedRequest,
            HeaderConstants.OS_VERSION,
            android.os.Build.VERSION.RELEASE, headers, keyIV
        )

        addOrRemoveHeader(
            encryptedRequest,
            HeaderConstants.DEVICE_TYPE,
            "Android", headers, keyIV
        )

        addOrRemoveHeader(
            encryptedRequest,
            HeaderConstants.DEVICE_NAME,
            android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL, headers, keyIV
        )

        return encryptedRequest
            .method(request.method(), body)
            .build()

    }

    private fun addOrRemoveHeader(
        encryptedRequest: Request.Builder,
        headerName: String,
        headerValue: String,
        headers: Headers,
        keyIV: Pair<String?, String?>
    ) {

        if (headers.get(headerName) != null) {
            if (headers.get(headerName)?.isEmpty()!!) {
                encryptedRequest.removeHeader(headerName)
            }
        } else {
            encryptedRequest.header(
                headerName,
                AESEncryptionHelper.encrypt(
                    headerValue,
                    keyIV.first ?: "",
                    keyIV.second ?: ""
                )
            )
        }
    }


    private fun getEncryptedPayload(
        jsonPayload: JSONObject,
        keyIV: Pair<String?, String?>
    ): JSONObject {
        val encryptedJsonPayload = getFieldsEncryptedPayload(jsonPayload, keyIV)
        jsonPayload.put(
            PAYLOAD_HASH, AESEncryptionHelper.encrypt(
                AppUtils.hash256(encryptedJsonPayload.toString()),
                key1, key2
                //keyIV.first ?: "", keyIV.second ?: ""
            )
        )
        return jsonPayload
    }

    private fun getKeyIVPair(jsonPayload: JSONObject): Pair<String?, String?> {
        var keyIV = Pair(UniqueDeviceID.getUniqueId()?.take(32), UserData.finalEncryptionIV)

        if (jsonPayload.has(ENCRYPTION_KEY) && jsonPayload.getString(ENCRYPTION_KEY).isNotEmpty()) {
            val secretKey = jsonPayload.getString(ENCRYPTION_KEY)
            keyIV = Pair(secretKey.take(32), UserData.finalEncryptionIV)
            key1 = secretKey.take(32)
            key2 = UserData.finalEncryptionIV.toString()
            jsonPayload.remove(ENCRYPTION_KEY)
        }
        return keyIV
    }

    private fun getFieldsEncryptedPayload(
        jsonPayload: JSONObject,
        keyIV: Pair<String?, String?>
    ): JSONObject {
        jsonPayload.keys().forEach { key ->
            enumValues<EncryptionFields>().forEach {
                if (it.value == key) {
                    jsonPayload.put(
                        key,
                        AESEncryptionHelper.encrypt(
                            jsonPayload.getString(key),
                            keyIV.first ?: "",
                            keyIV.second ?: ""
                        )
                    )
                }
            }
        }
        return jsonPayload
    }


    private fun getSortedPayload(jsonString: String): String {
        var map: Map<String, Any> = HashMap()
        map = (Gson().fromJson(jsonString, map.javaClass)).toSortedMap()
        return JSONObject(map).toString()
    }

    enum class EncryptionFields(val value: String) {
        OTP("otp"),
        EMAIL("email"),
        NIC("nic_nicop"),
        PASSWORD("password"),
        AGENT_CODE("agent_code"),
        NEW_PASSWORD("new_password"),
        OLD_PASSWORD("old_password"),
        REFERENCE_NUMBER("reference_no"),
        BENEFICIARY_NIC("beneficiary_nic_nicop"),
        REGISTRATION_CODE("registration_code"),
        RESIDENT_ID("resident_id"),
        PASSPORT_ID("passport_id"),
        REDEEM_AMOUNT("amount"),
        POINTS("points"),
        MOTHER_MAIDEN_NAME("mother_maiden_name"),
        PLACE_OF_BIRTH("place_of_birth"),
        CNIC_NICOP_ISSUANCE_DATE("cnic_nicop_issuance_date"),
        SP_RESPONE_ROW_ID("sp_respone_row_id")
    }
}