//package com.nmbb.oplayer.ui.sql;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import com.nmbb.oplayer.ui.products.Video;
//
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private String DATABASE_NAME = "videoviewer.db";
//    private static final int DATABASE_VERSION = 1;
//    private static String TABLE_NAME = "FavoriteVideos";
//
//    // Contacts Table Columns names
//    private static final String COLUMN_ID = "_id";
//    private static final String COLUMN_NAME = "Name";
//    private static final String COLUMN_DESCRIPTION = "Description";
//    private static final String COLUMN_IMAGE_URL = "ImageUrl";
//    private static final String COLUMN_URL = "Url";
//    private static final String COLUMN_ONLINE_WEB_URL = "OnlineWebUrl";
//    private static final String COLUMN_VIDEO_URL = "OnlineVideoUrl";
//    private static final String COLUMN_GENRE = "Genre";
//    private static final String COLUMN_YEAR = "Year";
//    private static final String COLUMN_COUNTRY = "Country";
//    private static final String COLUMN_PRODUCED_BY = "ProducedBy";
//    private static final String COLUMN_QUALITY_VIDEO = "QualityVideo";
//    private static final String COLUMN_QUALITY_AUDIO = "QualityAudio";
//    //    private static final String COLUMN_BITMAP = "Bitmap";
//    private static final String COLUMN_CURRENT_TIME_POSITION = "CurrentTimePosition";
//    private static final String COLUMN_TOTAL_TIME = "TotalTime";
//    private static final String COLUMN_FAVORITE = "Favorite";
//    private String[] ALL_COLUMN = { COLUMN_ID,
//            COLUMN_NAME, COLUMN_DESCRIPTION, COLUMN_IMAGE_URL, COLUMN_URL, COLUMN_ONLINE_WEB_URL, COLUMN_VIDEO_URL, COLUMN_GENRE,
//            COLUMN_YEAR, COLUMN_COUNTRY, COLUMN_PRODUCED_BY, COLUMN_QUALITY_VIDEO, COLUMN_QUALITY_AUDIO, COLUMN_CURRENT_TIME_POSITION,
//            COLUMN_TOTAL_TIME, COLUMN_FAVORITE};
//
//
//    private static final int NUM_COLUMN_ID = 0;
//    private static final int NUM_COLUMN_NAME = 1;
//    private static final int NUM_COLUMN_DESCRIPTION = 2;
//    private static final int NUM_COLUMN_IMAGE_URL = 3;
//    private static final int NUM_COLUMN_URL = 4;
//    private static final int NUM_COLUMN_ONLINE_WEB_URL = 5;
//    private static final int NUM_COLUMN_VIDEO_URL = 6;
//    private static final int NUM_COLUMN_GENRE = 7;
//    private static final int NUM_COLUMN_YEAR = 8;
//    private static final int NUM_COLUMN_COUNTRY = 9;
//    private static final int NUM_COLUMN_PRODUCED_BY = 10;
//    private static final int NUM_COLUMN_QUALITY_VIDEO = 11;
//    private static final int NUM_COLUMN_QUALITY_AUDIO = 12;
//    //    private static final int NUM_COLUMN_BITMAP = 13;
//    private static final int NUM_COLUMN_CURRENT_TIME_POSITION = 13;
//    private static final int NUM_COLUMN_TOTAL_TIME = 14;
//    private static final int NUM_COLUMN_FAVORITE = 15;
//
//    public DatabaseHandler(Context context, String dataBaseTableName) {
//        super(context, dataBaseTableName, null, DATABASE_VERSION);
//        TABLE_NAME = dataBaseTableName;
//    }
//
//    // Creating Tables
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String query = "CREATE TABLE " + TABLE_NAME + " (" +
//                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_NAME + " TEXT, " +
//                COLUMN_DESCRIPTION + " TEXT, " +
//                COLUMN_IMAGE_URL + " INTEGER, " +
//                COLUMN_URL + " TEXT, " +
//                COLUMN_ONLINE_WEB_URL + " TEXT, " +
//                COLUMN_VIDEO_URL + " TEXT, " +
//                COLUMN_GENRE + " TEXT, " +
//                COLUMN_YEAR + " TEXT, " +
//                COLUMN_COUNTRY + " TEXT, " +
//                COLUMN_PRODUCED_BY + " TEXT, " +
//                COLUMN_QUALITY_VIDEO + " TEXT, " +
//                COLUMN_QUALITY_AUDIO + " TEXT, " +
////                    COLUMN_BITMAP + " BLOB, " +
//                COLUMN_CURRENT_TIME_POSITION + " TEXT, " +
//                COLUMN_TOTAL_TIME + " TEXT, " +
//                COLUMN_FAVORITE + " INTEGER ); ";
//        db.execSQL(query);
//    }
//
//    // Upgrading database
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        onCreate(db);
//    }
//
//    /**
//     * All CRUD(Create, Read, Update, Delete) Operations
//     */
//
//    // Adding new contact
//    public void addVideo(Video video) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv=new ContentValues();
//        cv.put(COLUMN_NAME, video.getName());
//        cv.put(COLUMN_DESCRIPTION, video.getDescription());
//        cv.put(COLUMN_IMAGE_URL, video.getImageUrl());
//        cv.put(COLUMN_URL, video.getUrl());
//        cv.put(COLUMN_ONLINE_WEB_URL, video.getOnlineWebUrl());
//        cv.put(COLUMN_VIDEO_URL, video.getVideoUrl());
//        cv.put(COLUMN_GENRE, video.getGenre());
//        cv.put(COLUMN_YEAR, video.getYear());
//        cv.put(COLUMN_COUNTRY, video.getCountry());
//        cv.put(COLUMN_PRODUCED_BY, video.getProducedBy());
//        cv.put(COLUMN_QUALITY_VIDEO, video.getQualityVideo());
//        cv.put(COLUMN_QUALITY_AUDIO, video.getQualityAudio());
//
//        cv.put(COLUMN_CURRENT_TIME_POSITION, video.getCurrentTimePosition());
//        cv.put(COLUMN_TOTAL_TIME, video.getTotalTime());
//        cv.put(COLUMN_FAVORITE, video.isFavorite() ? 1 : 0);
//
//        db.insert(TABLE_NAME, null, cv);
//        db.close(); // Closing database connection
//    }
//
//    // Getting single contact
//    public Video getVideoById(long id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor mCursor = db.query(TABLE_NAME, ALL_COLUMN, COLUMN_ID + " = ?", new String[] { String.valueOf(id) }, null, null, null, null);
//
//        mCursor.moveToFirst();
//        String name = mCursor.getString(NUM_COLUMN_NAME);
//        String description = mCursor.getString(NUM_COLUMN_DESCRIPTION);
//        String imageUrl = mCursor.getString(NUM_COLUMN_IMAGE_URL);
//        String url = mCursor.getString(NUM_COLUMN_URL);
//        String onlineWebUrl = mCursor.getString(NUM_COLUMN_ONLINE_WEB_URL);
//        String videoUrl = mCursor.getString(NUM_COLUMN_VIDEO_URL);
//        String genre = mCursor.getString(NUM_COLUMN_GENRE);
//        String year = mCursor.getString(NUM_COLUMN_YEAR);
//        String country = mCursor.getString(NUM_COLUMN_COUNTRY);
//        String producedBy = mCursor.getString(NUM_COLUMN_PRODUCED_BY);
//        String qualityVideo = mCursor.getString(NUM_COLUMN_QUALITY_VIDEO);
//        String qualityAudio = mCursor.getString(NUM_COLUMN_QUALITY_AUDIO);
//        long currentPosition = mCursor.getLong(NUM_COLUMN_CURRENT_TIME_POSITION);
//        long totalTime = mCursor.getLong(NUM_COLUMN_TOTAL_TIME);
//        boolean isFavorite = (mCursor.getInt(NUM_COLUMN_FAVORITE) > 0);
//        Video video = new Video(id, name, description, imageUrl, url, onlineWebUrl, videoUrl, genre, year, country, producedBy, qualityVideo, qualityAudio, currentPosition, totalTime, isFavorite);
//        db.close();
//        return video;
//    }
//
//    // Getting All Contacts
//    public List<Video> getVideosList() {
//        List<Video> videosList = new ArrayList<Video>();
//        // Select All Query
//        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
//
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        // looping through all rows and adding to list
//        cursor.moveToFirst();
//        if (!cursor.isAfterLast()) {
//            do {
//                long id = cursor.getLong(NUM_COLUMN_ID);
//                String name = cursor.getString(NUM_COLUMN_NAME);
//                String description = cursor.getString(NUM_COLUMN_DESCRIPTION);
//                String imageUrl = cursor.getString(NUM_COLUMN_IMAGE_URL);
//                String url = cursor.getString(NUM_COLUMN_URL);
//                String onlineWebUrl = cursor.getString(NUM_COLUMN_ONLINE_WEB_URL);
//                String videoUrl = cursor.getString(NUM_COLUMN_VIDEO_URL);
//                String genre = cursor.getString(NUM_COLUMN_GENRE);
//                String year = cursor.getString(NUM_COLUMN_YEAR);
//                String country = cursor.getString(NUM_COLUMN_COUNTRY);
//                String producedBy = cursor.getString(NUM_COLUMN_PRODUCED_BY);
//                String qualityVideo = cursor.getString(NUM_COLUMN_QUALITY_VIDEO);
//                String qualityAudio = cursor.getString(NUM_COLUMN_QUALITY_AUDIO);
////                Bitmap bitmap = byteArrayToBitmap(mCursor.getBlob(NUM_COLUMN_BITMAP));
//                long currentPosition = cursor.getLong(NUM_COLUMN_CURRENT_TIME_POSITION);
//                long totalTime = cursor.getLong(NUM_COLUMN_TOTAL_TIME);
//                boolean isFavorite = (cursor.getInt(NUM_COLUMN_FAVORITE) > 0);
//
//                videosList.add(new Video(id, name, description, imageUrl, url, onlineWebUrl, videoUrl, genre, year, country, producedBy, qualityVideo, qualityAudio, currentPosition, totalTime, isFavorite));
//            } while (cursor.moveToNext());
//        }
//
//        db.close();
//
//        // return contact list
//        return videosList;
//    }
//
//    // Updating single contact
//    public int updateVideo(Video video, long index) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues cv = new ContentValues();
//        cv.put(COLUMN_NAME, video.getName());
//        cv.put(COLUMN_DESCRIPTION, video.getDescription());
//        cv.put(COLUMN_IMAGE_URL, video.getImageUrl());
//        cv.put(COLUMN_URL, video.getUrl());
//        cv.put(COLUMN_ONLINE_WEB_URL, video.getOnlineWebUrl());
//        cv.put(COLUMN_VIDEO_URL, video.getVideoUrl());
//        cv.put(COLUMN_GENRE, video.getGenre());
//        cv.put(COLUMN_YEAR, video.getYear());
//        cv.put(COLUMN_COUNTRY, video.getCountry());
//        cv.put(COLUMN_PRODUCED_BY, video.getProducedBy());
//        cv.put(COLUMN_QUALITY_VIDEO, video.getQualityVideo());
//        cv.put(COLUMN_QUALITY_AUDIO, video.getQualityAudio());
//        cv.put(COLUMN_CURRENT_TIME_POSITION, video.getCurrentTimePosition());
//        cv.put(COLUMN_TOTAL_TIME, video.getTotalTime());
//        cv.put(COLUMN_FAVORITE, video.isFavorite() ? 1 : 0);
//        int result = db.update(TABLE_NAME, cv, COLUMN_ID + " = ?", new String[] { String.valueOf(index) });
//        db.close();
//        return result;
//    }
//
//    // Deleting single contact
//    public void deleteVideoById(long id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
//        db.close();
//    }
//
//    // Getting contacts Count
//    public int getVideosCount() {
//        String countQuery = "SELECT  * FROM " + TABLE_NAME;
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();
//
//        // return count
//        return cursor.getCount();
//    }
//
//    public void deleteAllVideos() {
////        openDB();
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_NAME, null, null);
//        db.close();
////        closeDB();
//    }
//
//    public long getVideoId(Video video) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor mCursor = db.query(TABLE_NAME, null, null, null, null, null, COLUMN_NAME);
//
//        String videoDescription = video.getDescription();
//        mCursor.moveToFirst();
//        if (!mCursor.isAfterLast()) do {
//            long id = mCursor.getLong(NUM_COLUMN_ID);
//            if (videoDescription.equals(mCursor.getString(NUM_COLUMN_DESCRIPTION))){
//                db.close();
//                return id;
//            }
//        } while (mCursor.moveToNext());
//        return -1;
//    }
//}