//package com.nmbb.oplayer.ui.sql;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
//
///**
//* Created with IntelliJ IDEA.
//* User: Maxim
//* Date: 2/9/13
//* Time: 2:05 PM
//* To change this template use File | Settings | File Templates.
//*/
//public class MySQLiteFavoriteHelper extends SQLiteOpenHelper {
//    public static final String TABLE_VIDEOS = "videos_favorite";
//    public static final String COLUMN_ID = "_id_favorite";
//    public static final String COLUMN_VIDEO = "video_favorite";
//
//    private static final String DATABASE_NAME = "favoritesvideos.db";
//    private static final int DATABASE_VERSION = 1;
//
//    // Database creation sql statement
//    private static final String DATABASE_CREATE = "create table "
//            + TABLE_VIDEOS + "(" + COLUMN_ID
//            + " integer primary key autoincrement, " + COLUMN_VIDEO
//            + " text not null);";
//
//    public MySQLiteFavoriteHelper(Context context) {
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
