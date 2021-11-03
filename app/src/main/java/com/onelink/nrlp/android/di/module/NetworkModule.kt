package com.onelink.nrlp.android.di.module

import android.content.Context
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.onelink.nrlp.android.BuildConfig
import com.onelink.nrlp.android.data.ServiceGateway
import com.onelink.nrlp.android.data.interceptors.AuthenticationInterceptor
import com.onelink.nrlp.android.data.interceptors.EncryptionInterceptor
import com.onelink.nrlp.android.data.interceptors.NetworkConnectionInterceptor
import com.onelink.nrlp.android.utils.Constants
import com.onelink.nrlp.android.utils.Sp
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * Created by Umar Javed.
 */

@Module
class NetworkModule {

    @Named("DOMAIN")
    @Provides
    internal fun getDomain() = Constants.DOMAIN

    @Named("BASE_URL")
    @Provides
    internal fun getBaseUrlCore() = Constants.BASE_URL

    @Provides
    @Singleton
    fun provideGson(): Gson =
        GsonBuilder().setLenient().create()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivityInterceptor(context: Context) =
        NetworkConnectionInterceptor(context)

    @Provides
    @Singleton
    fun provideAuthenticationInterceptor(context: Context) = AuthenticationInterceptor(context)

    @Provides
    @Singleton
    fun provideEncryptionInterceptor(context: Context) = EncryptionInterceptor(context)


    @Provides
    @Singleton
    fun provideChuckInterceptor(context: Context) = ChuckInterceptor(context)

    @Provides
    @Singleton
    fun provideCertificatePinner(@Named("DOMAIN") domain: String): CertificatePinner {
        return CertificatePinner.Builder()
            .add(
                domain,
                Sp.AK,
                Sp.BK,
                Sp.CK,
                Sp.DK
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(
        authenticationInterceptor: AuthenticationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkConnectionInterceptor: NetworkConnectionInterceptor,
        chuckInterceptor: ChuckInterceptor,
        certificatePinner: CertificatePinner,
        encryptionInterceptor: EncryptionInterceptor
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.certificatePinner(certificatePinner)
        client.addInterceptor(authenticationInterceptor)
        client.addInterceptor(encryptionInterceptor)
        if (BuildConfig.IS_DEBUG) client.addInterceptor(loggingInterceptor)
        client.addInterceptor(networkConnectionInterceptor)
        //client.addInterceptor(chuckInterceptor)
        client.connectTimeout(10, TimeUnit.SECONDS)
        client.readTimeout(30, TimeUnit.SECONDS)
        return client.build()
    }

    @Provides
    @Singleton
    fun provideAPIGateway(
        gson: Gson,
        okHttpClient: OkHttpClient,
        @Named("BASE_URL") baseUrl: String
    ): ServiceGateway {
        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .build()
            .create(ServiceGateway::class.java)
    }
}