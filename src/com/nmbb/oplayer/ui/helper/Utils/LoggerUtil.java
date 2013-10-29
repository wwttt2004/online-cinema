package com.nmbb.oplayer.ui.helper.Utils;

import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: maxim
 * Date: 11.01.13
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
public class LoggerUtil {
    private static final String TAG = "-------------------->";
    private static boolean D = true;

    public static void log(String message){
        if(D)
            Log.i(TAG, message);
    }

    public static void log_d(String tag, String message){
        if(D)
            Log.d(tag, message);
    }
}
