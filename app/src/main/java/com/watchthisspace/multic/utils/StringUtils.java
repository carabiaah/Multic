package com.watchthisspace.multic.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class StringUtils {

    public static String readFile(Context context, String path) {
        String result = null;

        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            result = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}