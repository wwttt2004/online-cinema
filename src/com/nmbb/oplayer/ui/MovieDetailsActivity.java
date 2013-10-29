package com.nmbb.oplayer.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.adapter.BoxAdapter;
import com.nmbb.oplayer.ui.exeptions.ParserParamsNoFoundException;
import com.nmbb.oplayer.ui.helper.Utils.ActivityUtil;
import com.nmbb.oplayer.ui.helper.constants.Constants;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.parsers.MyHitMainPageParser;
import com.nmbb.oplayer.ui.products.Video;
import com.nmbb.oplayer.ui.sql.DBConnector;
///**
// * Created with IntelliJ IDEA.
// * User: Maxim
// * Date: 13.01.13
// * Time: 1:35
// * To change this template use File | Settings | File Templates.
// */

public class MovieDetailsActivity extends ActivityUtil {

    private Video mVideo;
    String position;
    Button mBtFavorite;
    private DBConnector mFavoriteDB;



    LinearLayout mLinearLayout;
    ProgressBar mMainProgressBar;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsStopProcess = false;
        mFavoriteDB = new DBConnector(this, false);

        setContentView(R.layout.movedetails);

        webView = (WebView)findViewById(R.id.webview);

        mMainProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mMainProgressBar.setVisibility(View.GONE);
        mLinearLayout = (LinearLayout) findViewById(R.id.main_layout);
        Intent intent = getIntent();
        position = intent.getStringExtra("position");
        try{
            Object object = BoxAdapter.getVideoObjectById(Integer.parseInt(position));
            if(object != null && object instanceof Video){
                mVideo = (Video) object;
                getMoreInFormationInBackGround();
                Button mBtnOnline = (Button) findViewById(R.id.button_online);

                mBtnOnline.setOnClickListener(mOnlineClickListener);

                mBtFavorite = (Button) findViewById(R.id.button_favorite);

                mBtFavorite.setOnClickListener(mFavoriteListener);

            } else {
                LoggerUtil.log("error: object " + (object == null ? "is null" : "not Video"));
                this.finish();
            }
        } catch (Exception ex){
            ex.printStackTrace();
            this.finish();
        }
    }

    View.OnClickListener mFavoriteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!mVideo.isFavorite()){
                mVideo.setIsFavorite(true);
                (v).setBackgroundResource(R.drawable.added_bookmark);
                mFavoriteDB.insert(mVideo);
            } else {
                mVideo.setIsFavorite(false);
                long index = mFavoriteDB.getVideoId(mVideo);
                if(index != -1){
                    mFavoriteDB.delete(index);
                }
                (v).setBackgroundResource(R.drawable.removed_bookmark);
            }
        }
    };

    void initFavoriteButton(){
        if(mVideo.isFavorite()){
            mBtFavorite.setBackgroundResource(R.drawable.added_bookmark);
        }
    }

    View.OnClickListener mOnlineClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showProgressBarOnUiThread();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String link = mVideo.getVideoUrl();
                            String lintHttp = mVideo.getVideoHttpUrl();
                            hideProgressBarOnUiThread();
                            if(link.length() > 0 || lintHttp.length() > 0) {
                                Intent intent = new Intent(MovieDetailsActivity.this, VideoPlayerActivity.class);
                                intent.putExtra("path", link);
                                intent.putExtra("pathHttp", lintHttp);
                                intent.putExtra("position", position);
                                MovieDetailsActivity.this.finish();
                                MovieDetailsActivity.this.startActivity(intent);
                            }
                            else{
                                MovieDetailsActivity.this.sendBroadcast(new Intent(Constants.INTENT_DISMISS_DIALOG));
                            }
                        }
                    }).run();
                }
            }).start();
        }
    };

    private void hideProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMainProgressBar.setVisibility(View.VISIBLE);
                mLinearLayout.setVisibility(View.GONE);
            }
        });
    }


    private void dismissProgressDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MovieDetailsActivity.this,"Проверьте соединение с интернет и повторите попытку.", Toast.LENGTH_LONG).show();
                mMainProgressBar.setVisibility(View.GONE);
                mLinearLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            if(intent.getAction().equals(Constants.INTENT_DISMISS_DIALOG)){
                dismissProgressDialog();
            }
        }
    };

    @Override
    public void onResume(){
        super.onResume();
        mIsStopProcess = false;
        mFavoriteDB.openDB();
        initFavoriteButton();
        registerReceiver(mReceiver, mIntents);
    }

    @Override
    public void onPause(){
        super.onPause();
        mIsStopProcess = true;
        mFavoriteDB.closeDB();
        unregisterReceiver(mReceiver);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if(event.getPointerCount() > 1) {
            LoggerUtil.log("Multitouch detected!");
            return true;
        }
        else
            return super.onTouchEvent(event);
    }

    IntentFilter mIntents = new IntentFilter();
    {
        mIntents.addAction(Constants.INTENT_DISMISS_DIALOG);
    }

    boolean mIsStopProcess =  false;

    public void getMoreInFormationInBackGround() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient(){
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return true;
                        }
                    });
                    webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    webView.setHorizontalScrollbarOverlay(false);
                    webView.setVerticalScrollbarOverlay(false);

                    final String pleaseWaite = "<html>\n" +
                            " <head> </head> \n" +
                            " <body width=\"%s\">\n" +
                            "<div style=\"margin:0 auto;width: %s;\">\n" +
                            "  <p>Пожалуйста, подождите...</p>\n" +
                            "\t<div style=\"margin:0 auto;width: %s;\">\n" +
                            "  \t\t<progress max=\"100\" value=\"%s\">\n" +
                            "  \t\t</progress>\n" +
                            "\t</div>\n" +
                            " </body>\n" +
                            "</html>";

                    final String error = "<html>\n" +
                            " <head> </head> \n" +
                            " <body width=\"%s\">\n" +
                            "<div style=\"margin:0 auto;width: %s;\">\n" +
                            "  <p>Пожалуйста, проверте соединение с интренетом и повторите попытку</p>\n" +
                            "\t</div>\n" +
                            " </body>\n" +
                            "</html>";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int value = 0;
                            long start = System.currentTimeMillis();
                            while (mVideo.getHtmlVideoDetails().length() == 0){
                                if(mIsStopProcess) return;
                                if(System.currentTimeMillis() - start > 60000){
                                    fillingWebView(webView, String.format(error, "100%", "70%"));
                                    return;
                                }
                                value = value > 100 ? 0 : value;
                                fillingWebView(webView, String.format(pleaseWaite, "100%", "70%", "90%", value + ""));
                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                value ++;
                            }
                            fillingWebView(webView, mVideo.getHtmlVideoDetails());
                        }
                    }).start();
                    MyHitMainPageParser.getMoreInformationAboutVideo(mVideo);
                } catch (ParserParamsNoFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void fillingWebView(final WebView webView, final String data){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
            }
        });
    }
}
