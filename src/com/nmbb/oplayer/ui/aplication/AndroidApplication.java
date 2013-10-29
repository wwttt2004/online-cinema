package com.nmbb.oplayer.ui.aplication;

import android.app.Application;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nmbb.oplayer.ui.sql.DatabaseHandler;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 3/6/13
 * Time: 8:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class AndroidApplication extends Application {

    static AndroidApplication mInstance;

//    DatabaseHandler mDbConnectorFavorite;
//    DatabaseHandler mDbConnectorRecent;

    public AndroidApplication(){
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        this.mDbConnectorFavorite = new DatabaseHandler(getApplicationContext(), "FavoriteVideos");
//        this.mDbConnectorRecent = new DatabaseHandler(getApplicationContext(), "RecentVideos");
        initImageDownloader();
        mInstance = this;
    }

    private ImageLoader mImageDownloader;

    private void initImageDownloader(){
        mImageDownloader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        mImageDownloader.init(config);
    }

    public static AndroidApplication getInstance(){
        return mInstance;
    }

    public ImageLoader getImageLoader(){
        return this.mImageDownloader;
    }

//    public DatabaseHandler getFavoriteDb(){
//        return this.mDbConnectorFavorite;
//    }
//
//    public DatabaseHandler getRecentDb(){
//        return this.mDbConnectorRecent;
//    }
}