/*
 * Copyright (C) 2013 yixia.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nmbb.oplayer.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.adapter.BoxAdapter;
import com.nmbb.oplayer.ui.helper.WakeLocker;
import com.nmbb.oplayer.ui.products.Video;
import com.nmbb.oplayer.ui.sql.DBConnector;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideoPlayerActivity extends Activity implements OnInfoListener, OnBufferingUpdateListener {

    /**
     * TODO: Set the path variable to a streaming video URL or a local media file
     * path.
     */
    private String path = "";
    private Uri uri;
    private VideoView mVideoView;
    private boolean isStart;
    ProgressBar pb;
    TextView downloadRateView, loadRateView;
    private String mPosition;
    private String mHttpVideoPath;
    private String mRtmpVideoPath;

    private View mVolumeBrightnessLayout;
    private GestureDetector mGestureDetector;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private int mLayout = VideoView.VIDEO_LAYOUT_ZOOM;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private ImageView mOperationPercent;
    private ImageView mOperationBg;
    private MediaController mMediaController;
    private WakeLocker mWakeLock;
    private long mCurrentPosition;
    private Video mVideo;
    private DBConnector mRecentDB;
    private AlertDialog.Builder mExitDialog;
    private AlertDialog.Builder mChooseUrl;
    private AlertDialog.Builder mContinueDialog;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        if (!LibsChecker.checkVitamioLibs(this))
            return;

        setContentView(R.layout.videoview);

        Intent intent = getIntent();
//		mPath = "http://st5.kino-dom.tv/s/ae4f6b03c028f7bd272687d62e3d791b/moimiglazami/s01e03.flv";
//		mPath = "http://212.113.32.182:1935/tv/a2.stream/playlist.m3u8?Kan=2454f877272918f9a5391834597da586&ch=2";


        mWakeLock = new WakeLocker(getApplicationContext());
        mRecentDB = new DBConnector(this, true);

        mPosition = intent.getStringExtra("position");

        mVideo = (Video) BoxAdapter.getVideoObjectById(Integer.parseInt(mPosition));

        mHttpVideoPath = intent.getStringExtra("pathHttp");
        mRtmpVideoPath = intent.getStringExtra("path");

//        path = mRtmpVideoPath;

        mVideoView = (VideoView) findViewById(R.id.buffer);
        pb = (ProgressBar) findViewById(R.id.probar);

        mVolumeBrightnessLayout = findViewById(R.id.operation_volume_brightness);
        mOperationBg = (ImageView) findViewById(R.id.operation_bg);
        mOperationPercent = (ImageView) findViewById(R.id.operation_percent);

        downloadRateView = (TextView) findViewById(R.id.download_rate);
        loadRateView = (TextView) findViewById(R.id.load_rate);

        mExitDialog = new AlertDialog.Builder(this);
        mExitDialog.setTitle("Закончить просмотр видео?");
        mExitDialog.setNegativeButton("Да", mDialogClickListener);
        mExitDialog.setPositiveButton("Нет", null);
        mExitDialog.setCancelable(false);

        mChooseUrl = new AlertDialog.Builder(this);
        mChooseUrl.setTitle("Выбор ресурса");
        mChooseUrl.setNegativeButton("HTTP", mDialogPlayHttp);
        mChooseUrl.setPositiveButton("RTMP", mDialogPlayRtmp);
//        mChooseUrl.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if(isOnBackPressed){
//                    VideoPlayerActivity.this.finish();
//                } else {
//                    mChooseUrl.show();
//                }
//            }
//        });
        mChooseUrl.setCancelable(false);

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String time = df.format(mVideo.getCurrentTimePosition() - 10800000);

        mContinueDialog = new AlertDialog.Builder(this);
        mContinueDialog.setTitle("Вы уже смотрели это видео. Смотреть");
        mContinueDialog.setNegativeButton("c начала", mDialogGoToBeaginingClickListener);
        mContinueDialog.setPositiveButton("начиная с " + time, mDialogContinueClickListener);
        mContinueDialog.setCancelable(false);

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if(mHttpVideoPath != null && mHttpVideoPath.length() > 0 && mRtmpVideoPath != null && mRtmpVideoPath.length() > 0){
            mChooseUrl.show();
        } else if(mHttpVideoPath != null && mHttpVideoPath.length() > 0){
            path = mHttpVideoPath;
        } else if(mRtmpVideoPath != null && mRtmpVideoPath.length() > 0) {
            path = mRtmpVideoPath;
        }

        if (path.length() > 0) {
            setupCurrentPosition();
        }

    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        switch (what) {
            case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                    isStart = true;
                    pb.setVisibility(View.VISIBLE);
                    downloadRateView.setVisibility(View.VISIBLE);
                    loadRateView.setVisibility(View.VISIBLE);
                    mMediaController.disablePlayButton();
                }
                break;
            case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                if (isStart) {
                    mVideoView.start();
                    pb.setVisibility(View.GONE);
                    downloadRateView.setVisibility(View.GONE);
                    loadRateView.setVisibility(View.GONE);
                    mMediaController.enablePlayButton();
                }
                break;
            case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
                downloadRateView.setText("" + extra + "kb/s" + "  ");
                break;
        }
        return true;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        loadRateView.setText(percent + "%");
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

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
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
    protected void onResume() {
        super.onResume();
        mRecentDB.openDB();
        mWakeLock.acquire();
    }

    @Override
    protected void onPause() {
        if (mVideoView != null){
            mCurrentPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
        mWakeLock.release();
        setCurrentPosition();
        addVideoAtDataBase();
        mRecentDB.closeDB();
        super.onPause();
    }

    private boolean isOnBackPressed = false;
    @Override
    public void onBackPressed(){
        isOnBackPressed = true;
        mExitDialog.show();
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

    private void addVideoAtDataBase() {
        long index = mRecentDB.getVideoId(mVideo);
        if(index == -1){
            mRecentDB.insert(mVideo);
        } else {
            mRecentDB.update(mVideo, index);
        }
    }

    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            setCurrentPosition();
            VideoPlayerActivity.this.finish();
        }
    };

    private DialogInterface.OnClickListener mDialogPlayRtmp = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            path = mRtmpVideoPath;
            setupCurrentPosition();
        }
    };

    private DialogInterface.OnClickListener mDialogPlayHttp = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            path = mHttpVideoPath;
            setupCurrentPosition();
        }
    };

    private void setupCurrentPosition() {
        if(mVideo.getCurrentTimePosition() == 0){
            startVideoFromPosition(0);
        } else {
            mContinueDialog.show();
        }
    }

    private DialogInterface.OnClickListener mDialogGoToBeaginingClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startVideoFromPosition(0);
        }
    };

    private DialogInterface.OnClickListener mDialogContinueClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startVideoFromPosition(mVideo.getCurrentTimePosition());
        }
    };

    private void startVideoFromPosition(long currentPosition) {
        uri = Uri.parse(path);
        mVideoView.setVideoURI(uri);
        mMediaController = new MediaController(this);
        mVideoView.setMediaController(mMediaController);
        mVideoView.seekTo(currentPosition);
        mVideoView.requestFocus();

        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        mVideoView.setOnInfoListener(this);
        mVideoView.setOnBufferingUpdateListener(this);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });
    }
}
