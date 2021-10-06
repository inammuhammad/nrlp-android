/*
 * Sample application to illustrate SSL pinning with DexGuard.
 *
 * Copyright (c) 2012-2018 Guardsquare NV
 */
package com.example;

import java.io.IOException;
import java.net.URL;
import java.security.KeyStore;
import com.example.TrustStoreFactory;

import javax.net.ssl.HttpsURLConnection;

import android.content.Context;

import com.guardsquare.dexguard.runtime.net.SSLPinner;

/**
 * This sample illustrates how to perform certificate pinning using the javax.net APIs.
 *
 * This HttpsURLConnection factory creates connections with certificate pinning.
 * The trusted certificates are provided in a trust store.
 *
 * @see TrustStoreFactory
 */
public class PinnedCertificateHttpsURLConnectionFactory
{
    private final Context   context;
    private final SSLPinner sslPinner;

    public PinnedCertificateHttpsURLConnectionFactory(Context context)
    throws Exception
    {
        this.context   = context;
        // Initialize the ssl pinner instance with our trust store
        // as source of certificate authorities and trust material.
        this.sslPinner = new SSLPinner(new TrustStoreFactory(context).createTrustStore());
    }

    public HttpsURLConnection createHttpsURLConnection(String urlString)
    throws IOException
    {
        // Create the https URL connection.
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        if (!sslPinner.pinHttpsURLConnection(urlConnection))
        {
            throw new RuntimeException("SSL pinning attempt failed, application probably hooked.");
        }

        return urlConnection;
    }
}
