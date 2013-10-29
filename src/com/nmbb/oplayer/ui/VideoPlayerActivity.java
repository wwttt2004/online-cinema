/*
 * Copyright (C) 2011 VOV IO (http://vov.io/)
 */

package com.nmbb.oplayer.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.adapter.BoxAdapter;
import com.nmbb.oplayer.ui.helper.Utils.ActivityUtil;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.Utils.NetworkUtil;
import com.nmbb.oplayer.ui.helper.WakeLocker;
import com.nmbb.oplayer.ui.products.Video;
import com.nmbb.oplayer.ui.sql.DBConnector;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageView;

public class VideoPlayerActivity extends ActivityUtil implements OnCompletionListener {

    private String mPath;
    private String mTitle;
    private VideoView mVideoView;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private GestureDetector mGestureDetector;
    private MediaController mMediaController;
    private WakeLocker mWakeLock;
    private AlertDialog.Builder mExitDialog;
    private AlertDialog.Builder mChooseUrl;
    private AlertDialog.Builder mContinueDialog;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressLayout;
    private TextView mTextProgressBar;
    private TextView mTextTraffic;

    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setCurrentPosition();
            VideoPlayerActivity.this.finish();
        }
    };


    private DialogInterface.OnClickListener mDialogContinueClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startVideoFromCurrentPosition();
        }
    };

    private DialogInterface.OnClickListener mDialogGoToBeaginingClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mVideoView.start();
        }
    };

    String mHttpVideoPath;
    String mRtmpVideoPath;

    private DialogInterface.OnClickListener mDialogPlayRtmp = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPath = mRtmpVideoPath;
            setupVideo();
        }
    };

    private DialogInterface.OnClickListener mDialogPlayHttp = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            mPath = mHttpVideoPath;
            setupVideo();
        }
    };

    private boolean mIsPrintLog = false;
    private DBConnector mRecentDB;
    Video mVideo;
    private int mProgressValue = 0;

    private boolean mIsFirstLaunch = true;
    private String mPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState);
            mCurrentPosition = savedInstanceState.getLong("mCurrentPosition");
        }
        else {
            mCurrentPosition = 0;
        }

        mRecentDB = new DBConnector(this, true);

        LoggerUtil.log("start onCreate");

        mExitDialog = new AlertDialog.Builder(this);
        mExitDialog.setTitle("Закончить просмотр видео?");
        mExitDialog.setNegativeButton("Закончить", mDialogClickListener);
        mExitDialog.setPositiveButton("Продолжить", null);
        mExitDialog.setCancelable(false);

        mContinueDialog = new AlertDialog.Builder(this);
        mContinueDialog.setTitle("Смотреть видео");
        mContinueDialog.setNegativeButton("c начала", mDialogGoToBeaginingClickListener);
        mContinueDialog.setPositiveButton("с места последней остановки", mDialogContinueClickListener);
        mContinueDialog.setCancelable(false);

        mChooseUrl = new AlertDialog.Builder(this);
        mChooseUrl.setTitle("Выбор ресурса");
        mChooseUrl.setNegativeButton("HTTP", mDialogPlayHttp);
        mChooseUrl.setPositiveButton("RTMP", mDialogPlayRtmp);
        mChooseUrl.setCancelable(false);

        mWakeLock = new WakeLocker(getApplicationContext());

        if (!LibsChecker.checkVitamioLibs(this, getClass().getName(), R.string.init_decoders, R.raw.libarm))
            return;

        Intent intent = getIntent();
//		mPath = "http://st5.kino-dom.tv/s/ae4f6b03c028f7bd272687d62e3d791b/moimiglazami/s01e03.flv";
//		mPath = "http://212.113.32.182:1935/tv/a2.stream/playlist.m3u8?Kan=2454f877272918f9a5391834597da586&ch=2";


        mPosition = intent.getStringExtra("position");
        LoggerUtil.log("Position: " + mPosition);

        mHttpVideoPath = intent.getStringExtra("pathHttp");
        mRtmpVideoPath = intent.getStringExtra("path");

        if(mHttpVideoPath != null && mHttpVideoPath.length() > 0 && mRtmpVideoPath != null && mRtmpVideoPath.length() > 0){
            mChooseUrl.show();
        } else if((mHttpVideoPath != null && mHttpVideoPath.length() > 0) || (mRtmpVideoPath != null && mRtmpVideoPath.length() > 0)) {
            mPath = mHttpVideoPath != null && mHttpVideoPath.length() > 0 ? mHttpVideoPath : mRtmpVideoPath;
            setupVideo();
        }
    }

    private void setupVideo(){
//        mPath = intent.getStringExtra("path");
        //rtmp://95.31.25.113:1935/17/mp4:for/44691/video.mp4&amp
        LoggerUtil.log("#######################" + mPath);
//        mPath = "rtmp://95.31.25.113:1935/17/mp4:for/43950/video.mp4";

//        mPath = "http://vk.com/video_ext.php?oid=-34746612&id=164771764&hash=3757dbbff7c85696&hd=2";

        try{
            mVideo = (Video)BoxAdapter.getVideoObjectById(Integer.parseInt(mPosition));

            if(mVideo == null){
                LoggerUtil.log(this.getClass().getSimpleName() + ": mVideo == null");
                this.finish();
                return;
            }
//            mCurrentPosition = mVideo.getCurrentTimePosition();
            if((mCurrentPosition = mVideo.getCurrentTimePosition()) == 0) mIsShowContinueDialog = false;
            initVideoLayout();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    mIsPrintLog = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mIsPrintLog = true;
                    logBufferingInBackGround();
                }
            }).start();
//            }  else{
//                mContinueDialog.show();
//            }

        } catch (Exception ex){
            ex.printStackTrace();
            this.finish();
        }
    }

    private void addVideoAtDataBase() {
        long index = mRecentDB.getVideoId(mVideo);
        if(index == -1){
            mRecentDB.insert(mVideo);
        } else {
            mRecentDB.update(mVideo, index);
        }
    }

    void initVideoLayout(){
        setContentView(R.layout.videoview);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.setVisibility(View.GONE);
        mTextProgressBar = (TextView)findViewById(R.id.text_progress_bar);

        mTextTraffic = (TextView) findViewById(R.id.text_traffic);

        mProgressLayout = (LinearLayout) findViewById(R.id.progress_layout);
        mProgressLayout.setVisibility(View.GONE);

        mVideoView = (VideoView) findViewById(R.id.surface_view);
        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mVideoView.setVideoURI(Uri.parse(mPath), mVideo.getName());

        mVideoView.setOnCompletionListener(this);

        mMediaController = new MediaController(this);

        mVideoView.setMediaController(mMediaController);
        mVideoView.requestFocus();

        mGestureDetector = new GestureDetector(this, new MyGestureListener());
    }

    boolean mIsShowContinueDialog = true;

    boolean mIsAutoStop = false;

    private void logBufferingInBackGround() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mVideoView == null) return;
                while(mIsPrintLog){
                    mProgressValue = mVideoView.getBufferPercentage();
                    if(mProgressValue != mPrevProgressValue){
                        mPrevProgressValue = mProgressValue;
                        if(mProgressValue < 30){
                            showProgressBarOnUiThread();
                            if(mVideoView.isPlaying()) {mVideoView.pause(); mIsAutoStop = true;}
                        } else if(mProgressValue > 90){
                            hiddenProgressDialogOnUiThread();
                            if(!mVideoView.isPlaying() && mIsAutoStop) {mVideoView.start(); mIsAutoStop = false;}
                            if(mIsShowContinueDialog){
                                mVideoView.pause();
                                mIsShowContinueDialog = false;
                                VideoPlayerActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        LoggerUtil.log("mCurrent position sec: " + mCurrentPosition);
                                        mContinueDialog.show();
                                    }
                                });
                            }
                        }
                        if(mProgressBar.getVisibility() == View.VISIBLE){
                            setProgressOnUiThread();
                        }
                    }
                    start = start == -1 ? System.currentTimeMillis() : start;
                    if(System.currentTimeMillis() - start > 1000){
                        setTrafficOnUiThread();
                        start = System.currentTimeMillis();
                    }
                }
            }
        });
        thread.start();
    }

    long start = -1;

    private void showProgressBarOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressBar.getVisibility() == View.GONE){
                    mProgressBar.setVisibility(View.VISIBLE);
                    mMediaController.setVisibility(View.GONE);
                }
                if(mProgressLayout.getVisibility() == View.GONE){
                    mProgressLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void hiddenProgressDialogOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressBar.getVisibility() == View.VISIBLE){
                    mProgressBar.setVisibility(View.GONE);
                    mMediaController.setVisibility(View.VISIBLE);
                }
                if(mProgressLayout.getVisibility() == View.VISIBLE){
                    mProgressLayout.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setProgressOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextProgressBar.setText(mProgressValue+"%");
            }
        });
    }

    private void setTrafficOnUiThread() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextTraffic.setText((int)(NetworkUtil.mRXTrafficPerSecond / 1024 ) > 1 ? (int)(NetworkUtil.mRXTrafficPerSecond / 1024 ) + " KB/s" :
                        ((int)(NetworkUtil.mRXTrafficPerSecond / 1024 / 1024) > 1 ? (int)(NetworkUtil.mRXTrafficPerSecond / 1024 / 1024) + " MB/s" : NetworkUtil.mRXTrafficPerSecond + " B/s"));
            }
        });
    }

    int mPrevProgressValue = -1;

    private long mCurrentPosition;

    @Override
    protected void onPause() {
        mIsPrintLog = false;
        if (mVideoView != null){
            mCurrentPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
        mIsPrintLog = false;
        mWakeLock.release();
        setCurrentPosition();
        addVideoAtDataBase();
        mRecentDB.closeDB();
        super.onPause();
    }

    private void setCurrentPosition(){
        try{
            if(mVideoView != null){
                mVideo.setCurrentTimePosition(mVideoView.getCurrentPosition());
                mVideo.setTotalTime(mVideoView.getDuration());
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRecentDB.openDB();
        LoggerUtil.log("onResume ");
        mIsPrintLog = true;
        logBufferingInBackGround();
//        startVideoFromCurrentPosition();
        mWakeLock.acquire();
    }

    private void startVideoFromCurrentPosition(){
        if (mVideoView != null){
//            mVideoView.pause();
            mVideoView.seekTo(mCurrentPosition);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event))
            return true;

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                endGesture();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;

        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    private class MyGestureListener extends SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mLayout == VideoView.VIDEO_LAYOUT_ZOOM)
                mLayout = VideoView.VIDEO_LAYOUT_ORIGIN;
            else
                mLayout++;
            if (mVideoView != null)
                mVideoView.setVideoLayout(mLayout, 0);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();

            if (mOldX > windowWidth * 4.0 / 5)
                onVolumeSlide((mOldY - y) / windowHeight);
            else if (mOldX < windowWidth / 5.0)
                onBrightnessSlide((mOldY - y) / windowHeight);

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mVolumeBrightnessLayout.setVisibility(View.GONE);
        }
    };

    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            mOperationBg.setImageResource(R.drawable.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            mOperationBg.setImageResource(R.drawable.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mVideoView != null){
            mLayout = VideoView.VIDEO_LAYOUT_SCALE;
            mVideoView.setVideoLayout(mLayout, 0);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed(){
        mExitDialog.show();
    }

    @Override
    public void onCompletion(MediaPlayer player) {
//        mVideo.setCurrentTimePosition(0);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putLong("mCurrentPosition", mCurrentPosition);
        super.onSaveInstanceState(savedInstanceState);
    }
}
