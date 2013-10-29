package com.nmbb.oplayer.ui.products;

import android.graphics.Bitmap;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Video implements Serializable {

    private long mId = -1;
    private String mName = "";
    private String mDescription = "";
    private String mImageUrl = "";
    private String mUrl = "";
    private String mOnlineWebUrl = "";
    private String mVideoUrl = "";
    private String mGenre = "";
    private String mYear = "";
    private String mCountry = "";
    private String mProducedBy = "";
    private String mQualityVideo = "";
    private String mQualityAudio = "";
    private  Bitmap mBitmap = null;
    private long mCurrentTimePosition = 0;
    private long mTotalTime = 0;
    private boolean mIsFavorite = false;
    private String mBigImageUrl = "";
    private String mFullDescription = "";
    private String mHtml = "";
    private String mHtmlVideoDetails = "";
    private String mVideoHttpUrl = "";

    public Video(Long id, String name, String description, String imageUrl,
                 String url, String onlineWebUrl, String videoUrl, String genre,
                 String year, String country, String producedBy, String qualityVideo,
                 String qualityAudio, long currentTimePosition,
                 long totalTime, boolean isFavorite){
        mId = id;
        mName = name;
        mDescription = description;
        mImageUrl= imageUrl;
        mUrl = url;
        mOnlineWebUrl = onlineWebUrl;
        mVideoUrl = videoUrl;
        mGenre = genre;
        mYear = year;
        mCountry = country;
        mProducedBy = producedBy;
        mQualityVideo = qualityVideo;
        mQualityAudio = qualityAudio;
        mCurrentTimePosition = currentTimePosition;
        mTotalTime = totalTime;
        mIsFavorite = isFavorite;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public boolean isFavorite() {
        return mIsFavorite;
    }

    public void setIsFavorite(boolean mIsFavorite) {
        this.mIsFavorite = mIsFavorite;
    }

    public Video(){}

//    public Video(String name, String description, String imageUrl, String url) {
//        setName(name);
//        setDescription(description);
//        setImageUrl(imageUrl);
//        setUrl(url);
//    }


    public long getTotalTime() {
        return mTotalTime;
    }

    public void setTotalTime(long mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public long getCurrentTimePosition() {
        return mCurrentTimePosition;
    }

    public void setCurrentTimePosition(long mCurrentTimePosition) {
        this.mCurrentTimePosition = mCurrentTimePosition;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap mBitmap) {
        this.mBitmap = mBitmap;
    }

    public String getQualityVideo() {
        return mQualityVideo;
    }

    public void setQualityVideo(String mQualityVideo) {
        this.mQualityVideo = mQualityVideo;
    }

    public String getQualityAudio() {
        return mQualityAudio;
    }

    public void setQualityAudio(String mQualityAudio) {
        this.mQualityAudio = mQualityAudio;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String mGenre) {
        this.mGenre = mGenre;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String mYear) {
        this.mYear = mYear;
    }

    public String getCountry() {
        return mCountry;
    }

    public void setCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getProducedBy() {
        return mProducedBy;
    }

    public void setProducedBy(String mProducedBy) {
        this.mProducedBy = mProducedBy;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getOnlineWebUrl() {
        return mOnlineWebUrl;
    }

    public void setOnlineWebUrl(String mOnlineWebUrl) {
        this.mOnlineWebUrl = mOnlineWebUrl;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public void setVideoHttpUrl(String mVideoUrl) {
        this.mVideoHttpUrl = mVideoUrl;
    }

    public String getVideoHttpUrl() {
        return mVideoHttpUrl;
    }

    public String getMetaData(){
        return (
                (this.getGenre().length() == 0 ? "" : this.getGenre()) +
                        (this.getProducedBy().length() == 0 ? "" : "\nРежисер: " + this.getProducedBy()) +
                        (this.getCountry().length() == 0 ? "" : this.getCountry()) +
                        (this.getYear().length() == 0 ? "" : "\nГод: " + this.getYear()) +
                        (this.getQualityVideo().length() == 0 ? "" : "\nКачество: " + this.getQualityVideo()) +
                        (this.getQualityAudio().length() == 0 ? "" : "\nЗвук: " + this.getQualityAudio())
//                        + (this.getTotalTime() > 0 ? ("\nПросмотрено " + getDate(this.getCurrentTimePosition()) + " из "+ getDate(this.getTotalTime())) : "")
        );
    }

    private static String getDate(long time){
        DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
        return formatter.format(time);
    }

    public String getSimpleString(){
        return (
                mName + ":::" +
                        mDescription + ":::" +
                        mImageUrl + ":::" +
                        mUrl + ":::" +
                        mOnlineWebUrl + ":::" +
                        mGenre + ":::" +
                        mYear + ":::" +
                        mCountry + ":::" +
                        mProducedBy + ":::" +
                        mQualityVideo + ":::" +
//                        mCurrentTimePosition + ":::" +
                        mQualityAudio + ";;;" +
                        mCurrentTimePosition + ";;;" +
                        mIsFavorite
        );
    }

    public Video(String allData){
        String data = allData.split(";;;")[0];
        try{
            mCurrentTimePosition = Long.parseLong(allData.split(";;;")[1]);
        } catch (Exception ex){
            mCurrentTimePosition = 0;
        }
        try{
            mIsFavorite = Boolean.parseBoolean(allData.split(";;;")[2]);
        } catch (Exception ex){
            mIsFavorite = false;
        }
        LoggerUtil.log("video: \"" + data);

        String[] videoData = data.split(":::");
        int videosCount = videoData.length;

        int i = 0;
        for(String str : videoData){
            LoggerUtil.log("video: " + i + " " + str);
            i++;
        }

        LoggerUtil.log("video count: " + videosCount);

        if(videosCount == 11 || videosCount == 10){
            mName = videoData[0];
            mDescription = videoData[1];
            mImageUrl = videoData[2];
            mUrl = videoData[3];
            mOnlineWebUrl = videoData[4];
            mGenre = videoData[5];
            mYear = videoData[6];
            mCountry = videoData[7];
            mProducedBy = videoData[8];
            mQualityVideo = videoData[9];
//            mCurrentTimePosition = Long.parseLong(videoData[10]);
            if(videosCount == 11)
                mQualityAudio = videoData[10];
        } else if(videosCount == 4){
            mName = videoData[0];
            mDescription = videoData[1];
            mImageUrl = videoData[2];
            mUrl = videoData[3];
        }
    }

    public void setBigImageUrl(String mBigImageUrl) {
        this.mBigImageUrl = mBigImageUrl;
    }

    public String getBigImageUrl() {
        return mBigImageUrl;
    }


    public String getFullDescription() {
        return mFullDescription;
    }

    public void setFullDescription(String fullDescription){
        this.mFullDescription = fullDescription;
    }

    public void setHtml(String html) {
        this.mHtml = html;
    }

    public String getHtml() {
        return mHtml;
    }

    public void setHtmlVideoDetails(String html) {
        this.mHtmlVideoDetails = html;
    }

    public String getHtmlVideoDetails() {
        return mHtmlVideoDetails;
    }
}