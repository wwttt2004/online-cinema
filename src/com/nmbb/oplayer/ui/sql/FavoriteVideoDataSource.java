//package com.nmbb.oplayer.ui.sql;
//
///**
//* Created with IntelliJ IDEA.
//* User: Maxim
//* Date: 2/9/13
//* Time: 2:23 PM
//* To change this template use File | Settings | File Templates.
//*/
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.SQLException;
//import android.database.sqlite.SQLiteDatabase;
//
//public class FavoriteVideoDataSource {
//    // Database fields
//    private SQLiteDatabase database;
//    private MySQLiteFavoriteHelper dbHelper;
//    private String[] allColumns = { MySQLiteFavoriteHelper.COLUMN_ID,
//            MySQLiteFavoriteHelper.COLUMN_VIDEO};
//
//    public FavoriteVideoDataSource(Context context) {
//        dbHelper = new MySQLiteFavoriteHelper(context);
//    }
//
//    public void open() throws SQLException {
//        database = dbHelper.getWritableDatabase();
//    }
//
//    public void close() {
//        dbHelper.close();
//    }
//
//    public ResentVideosSql createVideo(String Video) {
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteFavoriteHelper.COLUMN_VIDEO, Video);
//        long insertId = database.insert(MySQLiteFavoriteHelper.TABLE_VIDEOS, null,
//                values);
//        Cursor cursor = database.query(MySQLiteFavoriteHelper.TABLE_VIDEOS,
//                allColumns, MySQLiteFavoriteHelper.COLUMN_ID + " = " + insertId, null,
//                null, null, null);
//
//        cursor.moveToFirst();
//        ResentVideosSql newVideo = cursorToVideo(cursor);
//        cursor.close();
//        return newVideo;
//    }
//
//    public void deleteVideo(ResentVideosSql video) {
//        long id = video.getId();
//        System.out.println("Video deleted with id: " + id);
//        database.delete(MySQLiteFavoriteHelper.TABLE_VIDEOS, MySQLiteFavoriteHelper.COLUMN_ID
//                + " = " + id, null);
//    }
//
//    public List<ResentVideosSql> getAllVideos() {
//        List<ResentVideosSql> mVideos = new ArrayList<ResentVideosSql>();
//
//        Cursor cursor = database.query(MySQLiteFavoriteHelper.TABLE_VIDEOS,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            ResentVideosSql video = cursorToVideo(cursor);
//            mVideos.add(video);
//            cursor.moveToNext();
//        }
//        // Make sure to close the cursor
//        cursor.close();
//        return mVideos;
//    }
//
//    private ResentVideosSql cursorToVideo(Cursor cursor) {
//        ResentVideosSql video = new ResentVideosSql();
//        video.setId(cursor.getLong(0));
//        video.setVideo(cursor.getString(1));
//        return video;
//    }
//}
