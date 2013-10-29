//package com.nmbb.oplayer.ui.sql;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
//
///**
// * Created with IntelliJ IDEA.
// * User: Maxim
// * Date: 2/9/13
// * Time: 2:05 PM
// * To change this template use File | Settings | File Templates.
// */
//public class MySQLiteHelper extends SQLiteOpenHelper {
//    public static final String TABLE_VIDEOS = "videos";
//
//    private static final String DATABASE_NAME = "resentvideos.db";
//    private static final int DATABASE_VERSION = 1;
//
//    public static final String COLUMN_ID = "_id";
//    public static final String COLUMN_NAME = "Name";
//    public static final String COLUMN_DESCRIPTION = "Description";
//    public static final String COLUMN_IMAGE_URL = "ImageUrl";
//    public static final String COLUMN_URL = "Url";
//    public static final String COLUMN_ONLINE_WEB_URL = "OnlineWebUrl";
//    public static final String COLUMN_VIDEO_URL = "OnlineVideoUrl";
//    public static final String COLUMN_GENRE = "Genre";
//    public static final String COLUMN_YEAR = "Year";
//    public static final String COLUMN_COUNTRY = "Country";
//    public static final String COLUMN_PRODUCED_BY = "ProducedBy";
//    public static final String COLUMN_QUALITY_VIDEO = "QualityVideo";
//    public static final String COLUMN_QUALITY_AUDIO = "QualityAudio";
//    public static final String COLUMN_CURRENT_TIME_POSITION = "CurrentTimePosition";
//    public static final String COLUMN_TOTAL_TIME = "TotalTime";
//    public static final String COLUMN_FAVORITE = "Favorite";
//    public static final String[] ALL_COLUMN = { COLUMN_ID,
//            COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IMAGE_URL, COLUMN_URL, COLUMN_ONLINE_WEB_URL, COLUMN_VIDEO_URL, COLUMN_GENRE,
//            COLUMN_YEAR, COLUMN_COUNTRY, COLUMN_PRODUCED_BY, COLUMN_QUALITY_VIDEO, COLUMN_QUALITY_AUDIO, COLUMN_CURRENT_TIME_POSITION,
//            COLUMN_TOTAL_TIME, COLUMN_FAVORITE};
//
//    public static final int NUM_COLUMN_ID = 0;
//    public static final int NUM_COLUMN_NAME = 1;
//    public static final int NUM_COLUMN_DESCRIPTION = 2;
//    public static final int NUM_COLUMN_IMAGE_URL = 3;
//    public static final int NUM_COLUMN_URL = 4;
//    public static final int NUM_COLUMN_ONLINE_WEB_URL = 5;
//    public static final int NUM_COLUMN_VIDEO_URL = 6;
//    public static final int NUM_COLUMN_GENRE = 7;
//    public static final int NUM_COLUMN_YEAR = 8;
//    public static final int NUM_COLUMN_COUNTRY = 9;
//    public static final int NUM_COLUMN_PRODUCED_BY = 10;
//    public static final int NUM_COLUMN_QUALITY_VIDEO = 11;
//    public static final int NUM_COLUMN_QUALITY_AUDIO = 12;
//    public static final int NUM_COLUMN_CURRENT_TIME_POSITION = 13;
//    public static final int NUM_COLUMN_TOTAL_TIME = 14;
//    public static final int NUM_COLUMN_FAVORITE = 15;
//
//    // Database creation sql statement
//    private static final String DATABASE_CREATE = "create table " +
//            TABLE_VIDEOS + "("+
//            COLUMN_ID + " integer primary key autoincrement, " +
//            COLUMN_NAME + " TEXT, " +
//            COLUMN_DESCRIPTION + " TEXT, " +
//            COLUMN_IMAGE_URL + " INTEGER, " +
//            COLUMN_URL + " TEXT, " +
//            COLUMN_ONLINE_WEB_URL + " TEXT, " +
//            COLUMN_VIDEO_URL + " TEXT, " +
//            COLUMN_GENRE + " TEXT, " +
//            COLUMN_YEAR + " TEXT, " +
//            COLUMN_COUNTRY + " TEXT, " +
//            COLUMN_PRODUCED_BY + " TEXT, " +
//            COLUMN_QUALITY_VIDEO + " TEXT, " +
//            COLUMN_QUALITY_AUDIO + " TEXT, " +
//            COLUMN_CURRENT_TIME_POSITION + " LONG, " +
//            COLUMN_TOTAL_TIME + " LONG, " +
//            COLUMN_FAVORITE + " INTEGER ); ";
//
//    public MySQLiteHelper(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase database) {
//        database.execSQL(DATABASE_CREATE);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        LoggerUtil.log(MySQLiteHelper.class.getName() +
//                "Upgrading database from version " + oldVersion + " to "
//                + newVersion + ", which will destroy all old data");
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
//        onCreate(db);
//    }
//}
