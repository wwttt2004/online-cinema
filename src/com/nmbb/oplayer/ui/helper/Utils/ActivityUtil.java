package com.nmbb.oplayer.ui.helper.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import com.nmbb.oplayer.ui.helper.webloader.WebLoader;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 19.01.13
 * Time: 11:16
 * To change this template use File | Settings | File Templates.
 */
public class ActivityUtil extends Activity {

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        NetworkUtil.setUpTrafficInfoInBackground();
    }

    @Override
    protected void onPause(){
        NetworkUtil.mIsStopPrint = true;
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!NetworkUtil.mIsAlreadyStarted)
            NetworkUtil.setUpTrafficInfoInBackground();
    }

    public void setDefaultOrientation(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);

        int mNaturalOrientation;
        if((isNaturalOrientation() && getWidth() > getHeight()) || (!isNaturalOrientation() && getHeight() > getWidth())){
            LoggerUtil.log("Natural Orientation is landscape");
            mNaturalOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            LoggerUtil.log("Natural Orientation is portrait");
            mNaturalOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }

        setRequestedOrientation(mNaturalOrientation);
    }

    public boolean isLandscapeOrientation(){
        return (getWidth() > getHeight());
    }

    public boolean isPortraitOrientation(){
        return !isLandscapeOrientation();
    }

    private long getWidthHeight(boolean isWidth){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return (isWidth ? size.x : size.y);
    }

    public long getHeight(){
        return getWidthHeight(false);
    }

    public long getWidth(){
        return getWidthHeight(true);
    }

    public boolean isNaturalOrientation(){
        Display display;
        display = getWindow().getWindowManager().getDefaultDisplay();
        int rotation = display.getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
                return true;
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                return false;
            default:
                break;
        }
        return false;
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String isInternet(){
        return WebLoader.load("http://google.com");
    }
}
