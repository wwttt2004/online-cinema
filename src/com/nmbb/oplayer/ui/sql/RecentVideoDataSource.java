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
//import com.nmbb.oplayer.ui.products.Video;
//
//public class RecentVideoDataSource {
//    // Database fields
//    private SQLiteDatabase database;
//    private MySQLiteHelper dbHelper;
//    private String[] allColumns = MySQLiteHelper.ALL_COLUMN;
//
//    public RecentVideoDataSource(Context context) {
//        dbHelper = new MySQLiteHelper(context);
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
//    public long createVideo(Video video) {
//        ContentValues values = new ContentValues();
//        values.put(MySQLiteHelper.COLUMN_NAME, video.getName());
//        values.put(MySQLiteHelper.COLUMN_COUNTRY, video.getCountry());
//        values.put(MySQLiteHelper.COLUMN_CURRENT_TIME_POSITION, video.getCurrentTimePosition());
//        values.put(MySQLiteHelper.COLUMN_DESCRIPTION, video.getDescription());
//        values.put(MySQLiteHelper.COLUMN_FAVORITE, (video.isFavorite() ? 1 : 0));
//        values.put(MySQLiteHelper.COLUMN_GENRE, video.getGenre());
//        values.put(MySQLiteHelper.COLUMN_IMAGE_URL, video.getImageUrl());
//        values.put(MySQLiteHelper.COLUMN_ONLINE_WEB_URL, video.getOnlineWebUrl());
//        values.put(MySQLiteHelper.COLUMN_PRODUCED_BY, video.getProducedBy());
//        values.put(MySQLiteHelper.COLUMN_QUALITY_AUDIO, video.getQualityAudio());
//        values.put(MySQLiteHelper.COLUMN_QUALITY_VIDEO, video.getQualityVideo());
//        values.put(MySQLiteHelper.COLUMN_TOTAL_TIME, video.getTotalTime());
//        values.put(MySQLiteHelper.COLUMN_URL, video.getUrl());
//        values.put(MySQLiteHelper.COLUMN_VIDEO_URL, video.getVideoUrl());
//        values.put(MySQLiteHelper.COLUMN_YEAR, video.getYear());
//
//        return database.insert(MySQLiteHelper.TABLE_VIDEOS, null, values);
////        long insertId = database.insert(MySQLiteHelper.TABLE_VIDEOS, null, values);
////        Cursor cursor = database.query(MySQLiteHelper.TABLE_VIDEOS,
////                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
////                null, null, null);
////
////        cursor.moveToFirst();
////        ResentVideosSql newVideo = cursorToVideo(cursor);
////        cursor.close();
////        return newVideo;
//    }
//
//    public boolean deleteVideo(Video video) {
//        Cursor cursor = database.query(MySQLiteHelper.TABLE_VIDEOS,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        Video videoDb;
//        long index = -1;
//        while (!cursor.isAfterLast()) {
//            videoDb = cursorToVideo(cursor);
//            if(video.getDescription().equals(videoDb.getDescription())){
//                index = cursor.getLong(MySQLiteHelper.NUM_COLUMN_ID);
//                break;
//            }
//            cursor.moveToNext();
//        }
//        cursor.close();
//        if(index == -1)
//            return false;
//        System.out.println("Video deleted with id: " + index);
//        database.delete(MySQLiteHelper.TABLE_VIDEOS, MySQLiteHelper.COLUMN_ID + " = " + index, null);
//        return true;
//    }
//
//    public int getVideosCount(){
//        return getAllVideos().size();
//    }
//
//    public List<Video> getAllVideos() {
//        List<Video> mVideos = new ArrayList<Video>();
//        Cursor cursor = database.query(MySQLiteHelper.TABLE_VIDEOS,
//                allColumns, null, null, null, null, null);
//
//        cursor.moveToFirst();
//        Video video;
//        while (!cursor.isAfterLast()) {
//            video = cursorToVideo(cursor);
//            mVideos.add(video);
//            cursor.moveToNext();
//        }
//        // Make sure to close the cursor
//        cursor.close();
//        return mVideos;
//    }
//
//    private Video cursorToVideo(Cursor cursor) {
//        Video video = new Video();
//        video.setName(cursor.getString(MySQLiteHelper.NUM_COLUMN_NAME));
//        video.setDescription(cursor.getString(MySQLiteHelper.NUM_COLUMN_DESCRIPTION));
//        video.setImageUrl(cursor.getString(MySQLiteHelper.NUM_COLUMN_IMAGE_URL));
//        video.setIsFavorite(cursor.getInt(MySQLiteHelper.NUM_COLUMN_FAVORITE) > 0);
//        video.setCurrentTimePosition(cursor.getLong(MySQLiteHelper.NUM_COLUMN_CURRENT_TIME_POSITION));
//        video.setUrl(cursor.getString(MySQLiteHelper.NUM_COLUMN_URL));
//        video.setOnlineWebUrl(cursor.getString(MySQLiteHelper.NUM_COLUMN_ONLINE_WEB_URL));
//        video.setVideoUrl(cursor.getString(MySQLiteHelper.NUM_COLUMN_VIDEO_URL));
//        video.setGenre(cursor.getString(MySQLiteHelper.NUM_COLUMN_GENRE));
//        video.setYear(cursor.getString(MySQLiteHelper.NUM_COLUMN_YEAR));
//        video.setProducedBy(cursor.getString(MySQLiteHelper.NUM_COLUMN_PRODUCED_BY));
//        video.setCountry(cursor.getString(MySQLiteHelper.NUM_COLUMN_COUNTRY));
//        video.setQualityVideo(cursor.getString(MySQLiteHelper.NUM_COLUMN_QUALITY_VIDEO));
//        video.setQualityAudio(cursor.getString(MySQLiteHelper.NUM_COLUMN_QUALITY_AUDIO));
//        video.setTotalTime(cursor.getLong(MySQLiteHelper.NUM_COLUMN_TOTAL_TIME));
//        return video;
//    }
//}
