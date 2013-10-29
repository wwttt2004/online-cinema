package com.nmbb.oplayer.ui.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.nmbb.oplayer.ui.products.Video;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DBConnector {

    // Данные базы данных и таблиц
    private String DATABASE_NAME = "videoviewer.db";
    private static final int DATABASE_VERSION = 1;
    private String TABLE_NAME_FAVORITE;

    // Название столбцов
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "Name";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_IMAGE_URL = "ImageUrl";
    private static final String COLUMN_URL = "Url";
    private static final String COLUMN_ONLINE_WEB_URL = "OnlineWebUrl";
    private static final String COLUMN_VIDEO_URL = "OnlineVideoUrl";
    private static final String COLUMN_GENRE = "Genre";
    private static final String COLUMN_YEAR = "Year";
    private static final String COLUMN_COUNTRY = "Country";
    private static final String COLUMN_PRODUCED_BY = "ProducedBy";
    private static final String COLUMN_QUALITY_VIDEO = "QualityVideo";
    private static final String COLUMN_QUALITY_AUDIO = "QualityAudio";
    //    private static final String COLUMN_BITMAP = "Bitmap";
    private static final String COLUMN_CURRENT_TIME_POSITION = "CurrentTimePosition";
    private static final String COLUMN_TOTAL_TIME = "TotalTime";
    private static final String COLUMN_FAVORITE = "Favorite";
    private String[] ALL_COLUMN = { COLUMN_ID,
            COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IMAGE_URL, COLUMN_URL, COLUMN_ONLINE_WEB_URL, COLUMN_VIDEO_URL, COLUMN_GENRE,
            COLUMN_YEAR, COLUMN_COUNTRY, COLUMN_PRODUCED_BY, COLUMN_QUALITY_VIDEO, COLUMN_QUALITY_AUDIO, COLUMN_CURRENT_TIME_POSITION,
            COLUMN_TOTAL_TIME, COLUMN_FAVORITE};

    // Номера столбцов
    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_NAME = 1;
    private static final int NUM_COLUMN_DESCRIPTION = 2;
    private static final int NUM_COLUMN_IMAGE_URL = 3;
    private static final int NUM_COLUMN_URL = 4;
    private static final int NUM_COLUMN_ONLINE_WEB_URL = 5;
    private static final int NUM_COLUMN_VIDEO_URL = 6;
    private static final int NUM_COLUMN_GENRE = 7;
    private static final int NUM_COLUMN_YEAR = 8;
    private static final int NUM_COLUMN_COUNTRY = 9;
    private static final int NUM_COLUMN_PRODUCED_BY = 10;
    private static final int NUM_COLUMN_QUALITY_VIDEO = 11;
    private static final int NUM_COLUMN_QUALITY_AUDIO = 12;
    //    private static final int NUM_COLUMN_BITMAP = 13;
    private static final int NUM_COLUMN_CURRENT_TIME_POSITION = 13;
    private static final int NUM_COLUMN_TOTAL_TIME = 14;
    private static final int NUM_COLUMN_FAVORITE = 15;

    private SQLiteDatabase mDataBase;

    static DBConnector mRecentDB;
    static DBConnector mFavoriteDB;
    OpenHelper mOpenHelper;

//    public static DBConnector getFavoriteDB() {
//        return mFavoriteDB;
//    }
//
//    public static void setFavoriteDB(DBConnector favoriteDB) {
//        mFavoriteDB = favoriteDB;
//    }
//
//    public static DBConnector getRecentDB() {
//        return mRecentDB;
//    }
//
//    public static void setRecentDB(DBConnector recentDB) {
//        mRecentDB = recentDB;
//    }
//
//    public static void openDateBases() {
//        mFavoriteDB.openDB();
//        mRecentDB.openDB();
//    }
//
//    public static void closeDateBase(){
//        mFavoriteDB.closeDB();
//        mRecentDB.closeDB();
//    }

    public DBConnector(Context context, boolean isRecent) {
        TABLE_NAME_FAVORITE = isRecent ? "RecentVideos" : "FavoriteVideos";
        DATABASE_NAME = isRecent ? "recentvideo.db" : "favoritevideo.db";
        mOpenHelper = new OpenHelper(context);
        //mDataBase = mOpenHelper.getWritableDatabase();
    }

    public DBConnector openDB(){
        try{
//            if(this.isOpen())
            mDataBase = mOpenHelper.getWritableDatabase();
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return this;
    }

    public void closeDB(){
        try{
//            if(this.isOpen())
            mDataBase.close();
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean isOpen(){
        return mDataBase.isOpen();
    }

    public long insert(Video video) {
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_NAME, video.getName());
        cv.put(COLUMN_DESCRIPTION, video.getDescription());
        cv.put(COLUMN_IMAGE_URL, video.getImageUrl());
        cv.put(COLUMN_URL, video.getUrl());
        cv.put(COLUMN_ONLINE_WEB_URL, video.getOnlineWebUrl());
        cv.put(COLUMN_VIDEO_URL, video.getVideoUrl());
        cv.put(COLUMN_GENRE, video.getGenre());
        cv.put(COLUMN_YEAR, video.getYear());
        cv.put(COLUMN_COUNTRY, video.getCountry());
        cv.put(COLUMN_PRODUCED_BY, video.getProducedBy());
        cv.put(COLUMN_QUALITY_VIDEO, video.getQualityVideo());
        cv.put(COLUMN_QUALITY_AUDIO, video.getQualityAudio());

        cv.put(COLUMN_CURRENT_TIME_POSITION, video.getCurrentTimePosition());
        cv.put(COLUMN_TOTAL_TIME, video.getTotalTime());
        cv.put(COLUMN_FAVORITE, video.isFavorite() ? 1 : 0);

        long result =  mDataBase.insert(TABLE_NAME_FAVORITE, null, cv);
        return result;
    }

    byte [] getBitesFromBitmap(Bitmap bitmap){
        if(bitmap != null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        }
        return null;
    }

    public int update(Video video, long index) {
//        openDB();
        Video video1 = video;
        long index1 = index;
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, video.getName());
        cv.put(COLUMN_DESCRIPTION, video.getDescription());
        cv.put(COLUMN_IMAGE_URL, video.getImageUrl());
        cv.put(COLUMN_URL, video.getUrl());
        cv.put(COLUMN_ONLINE_WEB_URL, video.getOnlineWebUrl());
        cv.put(COLUMN_VIDEO_URL, video.getVideoUrl());
        cv.put(COLUMN_GENRE, video.getGenre());
        cv.put(COLUMN_YEAR, video.getYear());
        cv.put(COLUMN_COUNTRY, video.getCountry());
        cv.put(COLUMN_PRODUCED_BY, video.getProducedBy());
        cv.put(COLUMN_QUALITY_VIDEO, video.getQualityVideo());
        cv.put(COLUMN_QUALITY_AUDIO, video.getQualityAudio());
        cv.put(COLUMN_CURRENT_TIME_POSITION, video.getCurrentTimePosition());
        cv.put(COLUMN_TOTAL_TIME, video.getTotalTime());
        cv.put(COLUMN_FAVORITE, video.isFavorite() ? 1 : 0);
        int result = mDataBase.update(TABLE_NAME_FAVORITE, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(index) });
//        closeDB();
        return result;
    }

    public void deleteAll() {
//        openDB();
        mDataBase.delete(TABLE_NAME_FAVORITE, null, null);
//        closeDB();
    }

    public void delete(long id) {
//        openDB();
        mDataBase.delete(TABLE_NAME_FAVORITE, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
//        closeDB();
    }

    public Video select(long id) {
//        openDB();
        Cursor cursor = mDataBase.query(TABLE_NAME_FAVORITE, null, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, COLUMN_NAME);

        cursor.moveToFirst();
        String name = cursor.getString(NUM_COLUMN_NAME);
        String description = cursor.getString(NUM_COLUMN_DESCRIPTION);
        String imageUrl = cursor.getString(NUM_COLUMN_IMAGE_URL);
        String url = cursor.getString(NUM_COLUMN_URL);
        String onlineWebUrl = cursor.getString(NUM_COLUMN_ONLINE_WEB_URL);
        String videoUrl = cursor.getString(NUM_COLUMN_VIDEO_URL);
        String genre = cursor.getString(NUM_COLUMN_GENRE);
        String year = cursor.getString(NUM_COLUMN_YEAR);
        String country = cursor.getString(NUM_COLUMN_COUNTRY);
        String producedBy = cursor.getString(NUM_COLUMN_PRODUCED_BY);
        String qualityVideo = cursor.getString(NUM_COLUMN_QUALITY_VIDEO);
        String qualityAudio = cursor.getString(NUM_COLUMN_QUALITY_AUDIO);
        long currentPosition = cursor.getLong(NUM_COLUMN_CURRENT_TIME_POSITION);
        long totalTime = cursor.getLong(NUM_COLUMN_TOTAL_TIME);
        boolean isFavorite = (cursor.getInt(NUM_COLUMN_FAVORITE) > 0);
        Video video = new Video(id, name, description, imageUrl, url, onlineWebUrl, videoUrl, genre, year, country, producedBy, qualityVideo, qualityAudio, currentPosition, totalTime, isFavorite);
        cursor.close();
//        closeDB();
        return video;
    }

    Bitmap byteArrayToBitmap(byte [] bytes){
        if(bytes != null)
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return  null;
    }

    public ArrayList<Video> selectAllAll() {
//        openDB();
//        Cursor cursor = mDataBase.query(TABLE_NAME_FAVORITE, null, null, null, null, null, COLUMN_NAME);    //sort by name
        Cursor cursor = mDataBase.query(TABLE_NAME_FAVORITE, ALL_COLUMN, null, null, null, null, null);

        ArrayList<Video> arr = new ArrayList<Video>();
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            do {
                long id = cursor.getLong(NUM_COLUMN_ID);
                String name = cursor.getString(NUM_COLUMN_NAME);
                String description = cursor.getString(NUM_COLUMN_DESCRIPTION);
                String imageUrl = cursor.getString(NUM_COLUMN_IMAGE_URL);
                String url = cursor.getString(NUM_COLUMN_URL);
                String onlineWebUrl = cursor.getString(NUM_COLUMN_ONLINE_WEB_URL);
                String videoUrl = cursor.getString(NUM_COLUMN_VIDEO_URL);
                String genre = cursor.getString(NUM_COLUMN_GENRE);
                String year = cursor.getString(NUM_COLUMN_YEAR);
                String country = cursor.getString(NUM_COLUMN_COUNTRY);
                String producedBy = cursor.getString(NUM_COLUMN_PRODUCED_BY);
                String qualityVideo = cursor.getString(NUM_COLUMN_QUALITY_VIDEO);
                String qualityAudio = cursor.getString(NUM_COLUMN_QUALITY_AUDIO);
//                Bitmap bitmap = byteArrayToBitmap(mCursor.getBlob(NUM_COLUMN_BITMAP));
                long currentPosition = cursor.getLong(NUM_COLUMN_CURRENT_TIME_POSITION);
                long totalTime = cursor.getLong(NUM_COLUMN_TOTAL_TIME);
                boolean isFavorite = (cursor.getInt(NUM_COLUMN_FAVORITE) > 0);

                arr.add(new Video(id, name, description, imageUrl, url, onlineWebUrl, videoUrl, genre, year, country, producedBy, qualityVideo, qualityAudio, currentPosition, totalTime, isFavorite));
            } while (cursor.moveToNext());
        }
        cursor.close();
//        closeDB();
        return arr;
    }

    public long getVideoId(Video video) {
        Cursor cursor = mDataBase.query(TABLE_NAME_FAVORITE, null, null, null, null, null, COLUMN_NAME);

        byte[] b = new byte[]{-62, -96};
        String s = new String(b);

        String videoDescription = (video.getDescription().trim()).toLowerCase().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(s, "");
        String currentVideoDescription;

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) do {
            currentVideoDescription = (cursor.getString(NUM_COLUMN_DESCRIPTION).trim()).toLowerCase().replaceAll(" ", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(s, "");
            long id = cursor.getLong(NUM_COLUMN_ID);

            if (videoDescription.contains(currentVideoDescription) ||
                    currentVideoDescription.contains(videoDescription)){
                cursor.close();
                return id;
            }

        } while (cursor.moveToNext());
        cursor.close();
        return -1;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {              //todo
            String query = "CREATE TABLE " + TABLE_NAME_FAVORITE + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_URL + " INTEGER, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_ONLINE_WEB_URL + " TEXT, " +
                    COLUMN_VIDEO_URL + " TEXT, " +
                    COLUMN_GENRE + " TEXT, " +
                    COLUMN_YEAR + " TEXT, " +
                    COLUMN_COUNTRY + " TEXT, " +
                    COLUMN_PRODUCED_BY + " TEXT, " +
                    COLUMN_QUALITY_VIDEO + " TEXT, " +
                    COLUMN_QUALITY_AUDIO + " TEXT, " +
//                    COLUMN_BITMAP + " BLOB, " +
                    COLUMN_CURRENT_TIME_POSITION + " TEXT, " +
                    COLUMN_TOTAL_TIME + " TEXT, " +
                    COLUMN_FAVORITE + " INTEGER ); ";
            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITE);
            onCreate(db);
        }
    }
}