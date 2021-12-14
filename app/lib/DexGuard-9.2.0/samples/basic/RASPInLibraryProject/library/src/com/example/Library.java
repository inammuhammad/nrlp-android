/*
 * Sample application to illustrate processing with DexGuard.
 *
 * Copyright (c) 2012-2018 Guardsquare NV
 */
package com.example;

import android.content.Context;
import android.util.Log;
import com.guardsquare.dexguard.rasp.callback.DetectionReport;

/**
 * Sample class that returns a message.
 */
public class Library
{
    /**
     * Returns a message when the given integer is 20.
     */
    public String getMessage(int i) {
        return i == 20 ? "DexGuard RASP checks have been injected into the library of this sample." : "";
    }

    /**
     * This is an optional DexGuard RASP callback method. This signature must match.
     *
     * Any strings used in the callback will be automatically encrypted.
     *
     * @param detectionReport A detection report containing threat information.
     */
    public static void myCallback(DetectionReport detectionReport)
    {
        Log.i("HelloWorldActivity", "Threat detected");

        // The type of threat detected can be checked.
        if (detectionReport.isRunningInEmulator())
        {
            Log.i("HelloWorldActivity", "Emulator Detected");
        }
    }
}
