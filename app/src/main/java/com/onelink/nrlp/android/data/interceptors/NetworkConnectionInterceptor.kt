package com.onelink.nrlp.android.data.interceptors

import android.content.Context
import com.onelink.nrlp.android.utils.network.NetworkConnectivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by Umar Javed
 */

class NetworkConnectionInterceptor(val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if(!NetworkConnectivity.isNetworkAvailable(context = context)){
            throw NoConnectivityException()
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

}

class NoConnectivityException : IOException() {
    override fun getLocalizedMessage(): String? {
        return "No Internet Connection"
    }
}