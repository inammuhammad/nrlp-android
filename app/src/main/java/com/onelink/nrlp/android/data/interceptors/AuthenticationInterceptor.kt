package com.onelink.nrlp.android.data.interceptors

import android.content.Context
import android.util.Log
import com.onelink.nrlp.android.data.local.UserData
import com.onelink.nrlp.android.utils.HeaderConstants
import com.onelink.nrlp.android.utils.LukaKeRakk
import com.onelink.nrlp.android.utils.UniqueDeviceID
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject

class AuthenticationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .header(HeaderConstants.CLIENT_ID, LukaKeRakk.shusi())
            .header(HeaderConstants.CLIENT_SECRET, LukaKeRakk.susu())
            .header(HeaderConstants.CONTENT_TYPE, HeaderConstants.APPLICATION_JSON)
            .header(HeaderConstants.ACCEPT, HeaderConstants.APPLICATION_JSON)
            .header(
                HeaderConstants.AUTHORIZATION,
                HeaderConstants.BEARER + (UserData.getUser()?.token ?: "")
            )

        if ((UserData.getUser()?.sessionKey ?: "").isNotEmpty()) {
            request.header(HeaderConstants.SESSION_KEY, UserData.getUser()?.sessionKey ?: "")
        } else {
            if ((UserData.identityKey ?: "").isNotEmpty()) {
                request.header(HeaderConstants.RANDOM_KEY, UserData.identityKey ?: "")
            }
        }
        request.method(original.method(), original.body())

        return chain.proceed(request.build())
    }
}
