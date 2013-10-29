package com.nmbb.oplayer.ui.helper;

import android.content.Context;
import android.os.PowerManager;
import android.util.Log;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: maxim
 * Date: 15.10.12
 * Time: 12:00
 * To change this template use File | Settings | File Templates.
 */
public class WakeLocker {
    private PowerManager mPowerManager = null;
    private PowerManager.WakeLock mWakeLock = null;
    private static final String TAG = "###################### " + WakeLocker.class.getSimpleName();
    private Context mContext;

    public WakeLocker(Context ctx){
        mContext = ctx;
    }

    public void acquire(){
        mPowerManager = (PowerManager) mContext
                .getSystemService(
                        mContext.POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                        | PowerManager.ON_AFTER_RELEASE, TAG);
        mWakeLock.acquire();
        Log.i(TAG, "wake lock acquire");
    }

    public void release(){
        if(mWakeLock != null && mWakeLock.isHeld()){
            Log.i(TAG, "wake lock released");
            mWakeLock.release();
        }
    }
}
