/*
 * Sample application to illustrate processing with DexGuard.
 *
 * Copyright (c) 2012-2018 Guardsquare NV
 */
package com.example;

import com.example.jni.NativeSecret;

/**
 * Sample class that loads a native library and provides a native method.
 */
public class Library
{
    /**
     * Returns the secret string "Hello, world!".
     */
    public String getMessage() {
        return new NativeSecret().getMessage();
    }
}
