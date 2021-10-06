/*
 * Sample library to illustrate asset encryption with DexGuard.
 *
 * Copyright (c) 2012-2018 Guardsquare NV
 */
package com.example;

import android.app.*;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.*;

public class LibraryAssetFragment extends Fragment {

    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        try {
            Activity activity = getActivity();

            // Open the message asset. DexGuard will encrypt the file for us.
            InputStream stream = activity.getAssets().open("messageLibrary.txt");

            // Read the message from the stream.
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String message = reader.readLine();
            reader.close();

            // Display the message.
            TextView view = new TextView(activity);
            view.setText(message);
            view.setGravity(Gravity.CENTER);

            return view;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
