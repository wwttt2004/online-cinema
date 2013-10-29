package com.nmbb.oplayer.ui.helper.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.os.Binder;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 21.01.13
 * Time: 0:32
 * To change this template use File | Settings | File Templates.
 */
public class NetworkUtil {

    private Activity mActivity;

    public NetworkUtil(Activity activity){
        mActivity = activity;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static long mRXTrafficPerSecond;
    public static long mTXTrafficPerSecond;
    public static boolean mIsStopPrint;
    public static boolean mIsAlreadyStarted;

    public static void setUpTrafficInfoInBackground() {
        new Thread (new Runnable() {
            @Override
            public void run() {
                long currentRXTraffic;
                long currentTXTraffic;
                long mPrevRX = TrafficStats.getUidRxBytes(Binder.getCallingUid());
                long mPrevTX = TrafficStats.getUidTxBytes(Binder.getCallingUid());
                mIsStopPrint = false;
                mIsAlreadyStarted = true;
                while (!mIsStopPrint){
                    currentRXTraffic = TrafficStats.getUidRxBytes(Binder.getCallingUid());
                    currentTXTraffic = TrafficStats.getUidTxBytes(Binder.getCallingUid());
                    if((mRXTrafficPerSecond = (currentRXTraffic - mPrevRX)*10) > 0 || (mTXTrafficPerSecond = (currentTXTraffic - mPrevTX)*10) > 0) {
                        mPrevRX = currentRXTraffic;
                        mPrevTX = currentTXTraffic;
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mIsAlreadyStarted = false;
            }
        }).start();
    }
}
