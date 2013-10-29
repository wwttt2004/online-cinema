//package com.nmbb.oplayer.ui.helper.Utils;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import com.nmbb.oplayer.ui.helper.parsers.MyHitMainPageParser;
//import com.nmbb.oplayer.ui.products.Video;
//
//import java.io.InputStream;
//
///**
// * Created with IntelliJ IDEA.
// * User: Maxim
// * Date: 16.01.13
// * Time: 16:18
// * To change this template use File | Settings | File Templates.
// */
//public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//    ImageView bmImage;
//    ProgressBar mProgressBar;
//    Video mVideo = null;
//    Activity mActivity;
//
//    public DownloadImageTask(ImageView bmImage, Video video, ProgressBar progressBar, Activity activity) {
//        this.mProgressBar = progressBar;
//        this.mActivity = activity;
//        this.mVideo = video;
//        this.bmImage = bmImage;
//    }
//
//    public DownloadImageTask(ImageView bmImage, Video video) {
//        this.mProgressBar = null;
//        this.mActivity = null;
//        this.mVideo = video;
//        this.bmImage = bmImage;
//    }
//
//    protected Bitmap doInBackground(String... urls) {
//        if(this.mVideo != null && this.mVideo.getBitmap() == null){
////            if(mActivity != null){
////                mActivity.runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        mProgressBar.setVisibility(View.VISIBLE);
////                        bmImage.setVisibility(View.GONE);
////                    }
////                });
////            }
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                LoggerUtil.log("Error" + e.getMessage());
//                e.printStackTrace();
//            }
//            if(mActivity != null){
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressBar.setVisibility(View.GONE);
////                        bmImage.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//            if(mIcon11 == null)
//                return MyHitMainPageParser.getNoPhotoBitMap();
//            return mIcon11;
//        } else {
//            if(mActivity != null){
//                mActivity.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mProgressBar.setVisibility(View.GONE);
//                        bmImage.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//            return this.mVideo.getBitmap();
//        }
//    }
//
//    protected void onPostExecute(Bitmap result) {
//        if(this.mVideo.getBitmap() == null){
//            this.mVideo.setBitmap(result);
//        }
//        bmImage.setImageBitmap(result);
//    }
//}
