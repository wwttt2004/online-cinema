package com.nmbb.oplayer.ui;

import android.app.AlertDialog;
import android.content.*;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.nmbb.oplayer.R;
import android.os.Bundle;
import android.view.View.OnClickListener;
import com.nmbb.oplayer.ui.adapter.BoxAdapter;
import com.nmbb.oplayer.ui.aplication.AndroidApplication;
import com.nmbb.oplayer.ui.helper.Utils.ActivityUtil;
import com.nmbb.oplayer.ui.helper.Utils.NonServerConnector;
import com.nmbb.oplayer.ui.helper.constants.ArrayVideoStatus;
import com.nmbb.oplayer.ui.helper.constants.Constants;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.constants.MenuItemConstants;
import com.nmbb.oplayer.ui.helper.parsers.MyHitMainPageParser;
import com.nmbb.oplayer.ui.helper.webloader.WebLoader;
import com.nmbb.oplayer.ui.products.Category;
import com.nmbb.oplayer.ui.products.SubCategory;
import com.nmbb.oplayer.ui.products.Video;
import com.nmbb.oplayer.ui.sql.DBConnector;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class MainFragmentActivity extends ActivityUtil implements OnClickListener, TextWatcher {

    public static LinearLayout mControlLayout;
    public static GridView mGridView;
    private boolean mIsContentAlreadyLoaded = true;
    static ArrayList<Object> videos = new ArrayList<Object>();
    static BoxAdapter boxAdapter;
    static private EditText mSearch;
    private AlertDialog.Builder mBuilder;
    private InputMethodManager mKeyboard;
    private GridView mListViewFilter;
    public static SlidingDrawer mSlidingDrawer;
    static Button mButtonSlide;
    private Button mMenuButton;
    //    GridView mNavigationGrid;
    static RelativeLayout mSearchLayout;
    static LinearLayout mMainControlLayout;
    ArrayList<Object> mCategories = new ArrayList<Object>();
    ArrayList<Object> mSubCategories = new ArrayList<Object>();
    BoxAdapter mCategoryAdapter;

    static ProgressBar mProgressBar;
    static TextView mTextProgressBar;
    DBConnector mFavoriteDB;
    DBConnector mRecentDB;

    public TextView mTytle;

    GridView mSearchAutoComplete;

    public EditText mAutoComplete;
    private ArrayList<String> mContacts = new ArrayList<String>();
    ArrayAdapter mArrayAdapter;
    private BoxAdapter searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggerUtil.log("start onCreate");
        new AndroidApplication();

        inputManager =
                (InputMethodManager) getApplicationContext().
                        getSystemService(Context.INPUT_METHOD_SERVICE);

        setContentView(R.layout.fragment_pager);
        new MyHitMainPageParser(this);

        initImageLoader();

        mTytle = (TextView) findViewById(R.id.textViewTitle);

        mSearchAutoComplete = (GridView) findViewById(R.id.search_results);
        mSearchAutoComplete.setVisibility(View.GONE);

        searchAdapter = new BoxAdapter(this, mSubCategories, MainFragmentActivity.this);


        mSearchAutoComplete.setAdapter(searchAdapter);

        mFavoriteDB = new DBConnector(getApplicationContext(), false);
        mRecentDB = new DBConnector(getApplicationContext(), true);

        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.panel_tools);
        mButtonSlide = (Button) findViewById(R.id.handle);
        mButtonSlide.setVisibility(View.GONE);
        mSlidingDrawer.setVisibility(View.GONE);

        mMenuButton = (Button) findViewById(R.id.button_menu);
        mMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSlidingDrawer.toggle();
//                if(mSlidingDrawer.isOpened()){
//                    mSlidingDrawer.animateClose();
//                } else {
//                    mSlidingDrawer.animateOpen();
//                }
            }
        });


        mAutoComplete = (EditText) findViewById(R.id.auto_complete);
        mAutoComplete.setVisibility(View.GONE);
        mAutoComplete.addTextChangedListener(this);

        mAutoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
                    String searchQuery = textView.getText().toString().trim();
                    fillSearchDataInBackGround("http://5tv5.ru/index.php", searchQuery);
                    return true;
                }
                return false;
            }
        });

//        mContacts.add("Рэмбо 1");
//        mContacts.add("Рэмбо 2");
//        mContacts.add("Рэмбо 3");
//        mContacts.add("Рэмбо 4");
//        mContacts.add("Рэмбо 5");

//        mAutoComplete.setAdapter(new ArrayAdapter(MainFragmentActivity.this,
//                android.R.layout.simple_dropdown_item_1line, mContacts));

        mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                mGridView.setEnabled(false);
                mSearch.setEnabled(false);
            }
        });

        mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                mGridView.setEnabled(true);
                mSearch.setEnabled(true);
            }
        });

        mControlLayout = (LinearLayout) findViewById(R.id.linearLayoutControl);
        mControlLayout.setVisibility(View.GONE);

        mMainControlLayout = (LinearLayout)findViewById(R.id.mainLinearLayoutControl);
        mMainControlLayout.setVisibility(View.GONE);

        mKeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        mProgressBar = (ProgressBar)findViewById(R.id.main_progress_bar);
        mTextProgressBar = (TextView) findViewById(R.id.text_progress_bar);

        mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Выйти из приложения?");
        mBuilder.setNegativeButton("Выйти", mDialogClickListener);
        mBuilder.setPositiveButton("Отмена", null);
        mBuilder.setCancelable(false);
        mSearch = (EditText)findViewById(R.id.search);
        mSearch.setOnTouchListener(mCurrentPageTouchListener);
        mSearchLayout = (RelativeLayout)findViewById(R.id.linearLayoutSearch);
        mSearchLayout.setVisibility(View.GONE);

        mGridView = (GridView) findViewById(R.id.gridView);
        mListViewFilter = (GridView) findViewById(R.id.list_view_filter);
        mListViewFilter.setOnTouchListener(mTouchListener);

        mCategoryAdapter = new BoxAdapter(this, mCategories, MainFragmentActivity.this);

        mListViewFilter.setAdapter(mCategoryAdapter);

//        mNavigationGrid = (GridView) findViewById(R.id.gridViewNavigation);


        boxAdapter = new BoxAdapter(this, videos, MainFragmentActivity.this);
        mGridView.setAdapter(boxAdapter);

        int columnsCount = (int)(getWidth()/600);
        mGridView.setNumColumns(columnsCount);
        mGridView.setOnScrollListener(scrollListener);
        mGridView.setOnTouchListener(listViewTouchListener);

        mGridView.setVisibility(View.VISIBLE);
        LoggerUtil.log("MyHitMainPageParser.getVideos().size() " + MyHitMainPageParser.getVideos().size());
        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
            mVideoArrayStatus = savedInstanceState.getInt("mVideoArrayStatus");
        } else {
            mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
        }
        updateAdapter();




//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                postData();
//            }
//        }).start();



    }

    @Override
    public void afterTextChanged(Editable arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoggerUtil.log("Text changed: " + mAutoComplete.getText());
//                mSubCategories.clear();
                boolean isAutoComplete = false;
                if(mAutoComplete.getText().length() > 2){
                    mContacts.clear();
                    String result = WebLoader.load("http://5tv5.ru/autocomplete.php?q=" + mAutoComplete.getText().toString(), "utf-8");
                    LoggerUtil.log("AUTOCOMPLETE RESULT: " + result);

                    if(result != null){
                        setupSearchArray(result.split("\\n"));
                    }

                    LoggerUtil.log("AUTOCOMPLETE RESULT: #### " + mContacts.size());
                    isAutoComplete = true;
                }

                final boolean finalIsAutoComplete = isAutoComplete;
                MainFragmentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!finalIsAutoComplete){
                            mListViewFilter.setVisibility(View.VISIBLE);
                            mSearchAutoComplete.setVisibility(View.GONE);
                        } else {
                            mListViewFilter.setVisibility(View.GONE);
                            mSearchAutoComplete.setVisibility(View.VISIBLE);
                            searchAdapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }).start();
    }

    private void setupSearchArray(String[] split) {
        mSubCategories.clear();
        for(String str : split){
            String[] splitted = str.split("\\|");
            String name = splitted.length == 3 ? (splitted[0] + " | " + splitted[2]) :
                    (splitted.length == 2 || splitted.length == 1 ? splitted[0] : "");
            if(name.length() > 0)
                mSubCategories.add(new SubCategory(name));
        }
    }

    float xStart = 0;
    float xEnd = 0;
    float yStart = 0;
    float yEnd = 0;

    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LoggerUtil.log("KEY_ACTION: " + event.getAction());
            switch (event.getAction()){
                case 0:
                    LoggerUtil.log("KEY_ACTION: DOWN");
                    if(xStart == 0)
                        xStart = event.getX();
                    if(yStart == 0)
                        yStart = event.getY();
                    break;
                case 1:
                    xEnd = event.getX();
                    yEnd = event.getY();

                    float deltaY = yEnd - yStart;

                    if(deltaY < 0){
                        deltaY = deltaY * (-1);
                    }

                    if(xEnd - xStart > mListViewFilter.getWidth() / 1.5 && deltaY < 100){
                        mSlidingDrawer.animateClose();
                    }
                    xStart = 0;
                    xEnd = 0;
                    yStart = 0;
                    yEnd = 0;
                    break;
                case 2:
                    LoggerUtil.log("KEY_ACTION: UP");
                    break;
            }
            return false;
        }
    };

    static ImageLoader mImageDownloader;

    private void initImageLoader(){
        mImageDownloader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        mImageDownloader.init(config);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    public void updateAdapter(){
        fillDataInBackGround(String.format(MyHitMainPageParser.mCurrentRequestLink, MyHitMainPageParser.mCurrentDownloadedPages), true);
        if(mSlidingDrawer.isShown()){
            mSlidingDrawer.animateClose();
            mGridView.setEnabled(true);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LoggerUtil.log("onConfigurationChanged");
        int columnsCount = (int)(getWidth()/600);
        mGridView.setNumColumns(columnsCount);
        super.onConfigurationChanged(newConfig);
    }

    View.OnTouchListener listViewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();
            switch(action & MotionEvent.ACTION_MASK){

                case MotionEvent.ACTION_MOVE:
                    LoggerUtil.log("scroll ACTION_MOVE ");
                    break;
                case MotionEvent.ACTION_DOWN:
                    LoggerUtil.log("scroll ACTION_DOWN ");
                    break;
                case MotionEvent.ACTION_CANCEL:
                    LoggerUtil.log("scroll ACTION_CANCEL ");
                    break;
                case MotionEvent.ACTION_UP:
                    LoggerUtil.log("scroll ACTION_UP ");
                    break;
            }
            return false;
        }
    };

    boolean isLoading;
    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {

        int currentFirstVisibleItem;
        boolean isOnScroll = false;
        int currentScrollIndex = 0;

        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            this.isOnScroll = true;
            currentFirstVisibleItem = firstVisibleItem;
//            LoggerUtil.log("new data: firstVisibleItem " + firstVisibleItem);
//            LoggerUtil.log("new data: visibleItemCount " + visibleItemCount);
//            LoggerUtil.log("new data: totalItemCount " + totalItemCount);
            currentScrollIndex = 0;
        }

        public void onScrollStateChanged(AbsListView view, int scrollState) {
            this.isScrollCompleted();
            isOnScroll = false;
        }

        private void isScrollCompleted() {
            if (currentFirstVisibleItem != 0 && !isOnScroll && currentScrollIndex > 1) {
                if(!isLoading){
                    isLoading = true;
                    MyHitMainPageParser.mCurrentDownloadedPages ++;
                    fillDataInBackGround(String.format(MyHitMainPageParser.mCurrentRequestLink, MyHitMainPageParser.mCurrentDownloadedPages));
                }
            }
            currentScrollIndex ++;
        }

    };

    private View.OnTouchListener mCurrentPageTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            v.requestFocus();
            mKeyboard.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
            ((EditText)v).selectAll();
            return false;
        }
    };

    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener(){
        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
            System.exit(0);
        }
    };

    private void fillDataInBackGround(String requestLink) {
        fillDataInBackGround(requestLink, false);
    }

    private void fillDataInBackGround(final String requestLink, final boolean isClearArrayList) {
        LoggerUtil.log("REQUEST LINK: " + requestLink);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fillData(requestLink, isClearArrayList);
                isLoading = false;
            }
        }).start();
    }

    public void fillSearchDataInBackGround(final String requestLink, final String searchQuery){
        LoggerUtil.log("REQUEST LINK: " + requestLink);
        new Thread(new Runnable() {
            @Override
            public void run() {
                fillData(requestLink, true, searchQuery);
                isLoading = false;
            }
        }).start();
    }

    public void onPreExecute() {
        LoggerUtil.log("################# preExecute");
        mControlLayout.setVisibility(View.GONE);
        mSlidingDrawer.setVisibility(View.GONE);
        mGridView.setVisibility(View.GONE);
        mSearchLayout.setVisibility(View.GONE);
        mMainControlLayout.setVisibility(View.GONE);
        if(mProgressBar.getVisibility() == View.GONE)
            mProgressBar.setVisibility(View.VISIBLE);
        if(mTextProgressBar.getVisibility() == View.GONE)
            mTextProgressBar.setVisibility(View.VISIBLE);
    }

    public void changeTab(){
        setFirstTab();
    }

    private void setFirstTab(){
        if(mSlidingDrawer.isOpened()){
            mSlidingDrawer.animateClose();
        }
        mGridView.setVisibility(View.GONE);
        videos.clear();
        boxAdapter.notifyDataSetChanged();
        if(mIsContentAlreadyLoaded){
            mSearchLayout.setVisibility(View.GONE);
            updateList();
        }
        notifyDataSetChanged();
        mGridView.setVisibility(View.VISIBLE);
    }

    public void onPostExecute(){
        LoggerUtil.log("############### postExecute");
        mSlidingDrawer.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.VISIBLE);
        dismissDialog();
    }

    private void dismissProgressDialog(boolean isShowToast){
        if(isShowToast){
            Toast.makeText(this,"Проверьте соединение с интернет и повторите попытку.", Toast.LENGTH_LONG).show();
            videos.clear();
            mIsContentAlreadyLoaded = false;
            mControlLayout.setVisibility(View.GONE);
            mMainControlLayout.setVisibility(View.GONE);
        }
        dismissDialog();
    }

    InputMethodManager inputManager;

    private void preExecuteOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onPreExecute();
            }
        });
    }

    private void notifyDataSetChangedOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    void fillData(String link, boolean  isClearArrayList) {
        fillData(link, isClearArrayList, null);
    }

    void fillData(final String link, final  boolean  isClearArrayList, final String searchQuery) {
        Thread request  = new Thread(new Runnable() {
            @Override
            public void run() {
                preExecuteOnUiThread();
                videos.clear();
                mIsDownloadStarted = true;
                LoggerUtil.log("start before : " + mIsDownloadStarted);
                try {
                    MyHitMainPageParser.setVideoArrayList(link, videos, isClearArrayList, searchQuery);
                    MyHitMainPageParser.setCategoriesCategoriesList(mCategories);

                    LoggerUtil.log("CATEGORIES SIZE: " + mCategories.size());
                    for (Object category : mCategories){
                        LoggerUtil.log("CATEGORY NAME: " + ((Category)category).getCategoryName());
                        LoggerUtil.log("CATEGORY URL: " + ((Category)category).getUrl());
                    }

                    postExecuteOnUiThread();
                } catch (Exception e) {
                    dismissDialog();
                    mIsContentAlreadyLoaded = false;
                    e.printStackTrace();
                }
                notifyDataSetChangedOnUiThread();
                mIsDownloadStarted = false;
                LoggerUtil.log("start after : " + mIsDownloadStarted);
            }
        });
        request.run();
    }

    private void postExecuteOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onPostExecute();
            }
        });
    }

    void dismissDialog(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mProgressBar.getVisibility() == View.VISIBLE)
                    mProgressBar.setVisibility(View.GONE);
                if(mTextProgressBar.getVisibility() == View.VISIBLE)
                    mTextProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setFirstTabOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFirstTab();
            }
        });
    }

    static public int mProgressInt = 0;

    void notifyDataSetChanged(){
        updateVideosArray();
        boxAdapter.notifyDataSetChanged();
    }

    private void dialogSetLoadInfo(int mInfoLoad) {
        mTextProgressBar.setText(mInfoLoad + "%");
    }

    @Override
    public void onBackPressed() {
        if(mProgressBar.getVisibility() == View.GONE){
            if(mSlidingDrawer.isOpened()) {
                mSlidingDrawer.animateClose();
            }else if(mSearchLayout.getVisibility() == View.VISIBLE){
                if(!MyHitMainPageParser.isSearchRequest()){
                    mControlLayout.setVisibility(View.VISIBLE);
                    mMainControlLayout.setVisibility(View.VISIBLE);
                }else {
                    mMainControlLayout.setVisibility(View.GONE);
                }
                mSearchLayout.setVisibility(View.GONE);
                if(!MyHitMainPageParser.isSearchRequest()){
                    mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
                }
            } else if(mGridView.getVisibility() == View.GONE){
                setFirstTabOnUiThread();
            }else if(mVideoArrayStatus != MenuItemConstants.ALL_VIDEO){
                MyHitMainPageParser.setIsSearchRequest(false);
                mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
                showAllVideosOnUiThread();
                setFirstTabOnUiThread();
                mControlLayout.setVisibility(View.VISIBLE);
                mMainControlLayout.setVisibility(View.VISIBLE);
            } else{
                mBuilder.show();
            }
        }else{
            mBuilder.show();
        }
    }

    private void showAllVideosOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mVideoArrayStatus == MenuItemConstants.ALL_VIDEO){
                    mControlLayout.setVisibility(View.VISIBLE);
                    mSlidingDrawer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onPause(){
        mRecentDB.closeDB();
        mFavoriteDB.closeDB();
        LoggerUtil.log("start onPause");
        unregisterReceiver(mReceiver);
        super.onPause();
    }

    IntentFilter mIntents = new IntentFilter();
    {
        mIntents.addAction(Constants.INTENT_RELOAD_UI);
        mIntents.addAction(Constants.INTENT_DISMISS_DIALOG_AND_RELOAD_UI);
        mIntents.addAction(Constants.INTENT_SCROLL_LIST_VIEW);
        mIntents.addAction(Constants.INTENT_DISMISS_DIALOG);
        mIntents.addAction(Constants.INTENT_CONNECTIVITY_CHANGE);
        mIntents.addAction(Constants.INTENT_DIALOG_CHANGE_PROGRESS);
        mIntents.addAction(Constants.INTENT_UPDATE_LISTVIEWS);
        mIntents.addAction(Constants.INTENT_SEARCH_FAIL);
    }

    @Override
    public void onResume(){
        LoggerUtil.log("start onResume : " + mIsDownloadStarted);
        super.onResume();
        mFavoriteDB.openDB();
        mRecentDB.openDB();
        MyHitMainPageParser.mMainFragmentActivity = MainFragmentActivity.this;
        mGridView.setNumColumns(((int)(getWidth()/600)));
        registerReceiver(mReceiver, mIntents);
        LoggerUtil.log("start onResume 1 : " + mIsDownloadStarted);
        if(mProgressInt == 100){
            dismissProgressDialogOnUiThread(false);
            mProgressInt = 0;
        }
        boxAdapter.notifyDataSetChanged();

        postExecuteOnUiThread();
    }

    private void updateList(){
        LoggerUtil.log("mVideoArrayStatus " + mVideoArrayStatus);
        if(mVideoArrayStatus == MenuItemConstants.ALL_VIDEO){
            videos.addAll(MyHitMainPageParser.getVideos());
        } else if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
            if(MyHitMainPageParser.getResentVideos().size() > 0){
                hiddenNavigationButton();
                videos.addAll(MyHitMainPageParser.getResentVideos());
            } else{
                videos.addAll(MyHitMainPageParser.getVideos());
            }
        } else if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE){
            if(MyHitMainPageParser.getFavoriteVideos().size() > 0){
                hiddenNavigationButton();
                videos.addAll(MyHitMainPageParser.getFavoriteVideos());
            } else{
                videos.addAll(MyHitMainPageParser.getVideos());
            }
        }
    }

    private void updateVideosArray(){
        try{
            if(videos.size() > 0){
                Video video;
                boolean isFavorite;
                long currentTimePosition;
                long index;
                for(Object object : videos){
                    if(!(object instanceof Video)) return;
                    isFavorite = false;
                    video = (Video) object;
                    if(mFavoriteDB.getVideoId(video) > -1)
                        isFavorite = true;
                    video.setIsFavorite(isFavorite);
                    currentTimePosition = 0;
                    index = mRecentDB.getVideoId(video);
                    if(mRecentDB.getVideoId(video) > -1)
                        currentTimePosition = mRecentDB.select(index).getCurrentTimePosition();
                    video.setCurrentTimePosition(currentTimePosition);
                }
            }
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean mIsDownloadStarted = false;

    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context arg0, Intent intent) {
            LoggerUtil.log(intent.getAction());
            LoggerUtil.log("Received: -mReloadUiIntent: " +intent.getAction() + "");
            if(intent.getAction().equals(Constants.INTENT_RELOAD_UI)){
                changeTab();
                onPostExecute();
            } else if(intent.getAction().equals(Constants.INTENT_DISMISS_DIALOG_AND_RELOAD_UI)){
                onPostExecute();
            } else if(intent.getAction().equals(Constants.INTENT_DISMISS_DIALOG)){
                dismissProgressDialogOnUiThread(true);
            } else if(intent.getAction().equals(Constants.INTENT_DIALOG_CHANGE_PROGRESS)){
                mProgressInt = intent.getIntExtra(Constants.EXTRA_DIALOG_PROGRESS, 0);
                dialogSetLoadInfoOnUiThread();
            } else if(intent.getAction().equals(Constants.INTENT_UPDATE_LISTVIEWS)){
                mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
                mGridView.setEnabled(true);
                setFirstTabOnUiThread();
            } else if(intent.getAction().equals(Constants.INTENT_SEARCH_FAIL)){
                mMessageToToast = "По вашему запросу ничего не найдено.";
                setToastMessageOnUiThread();
                showAllVideosOnUiThread();
            }
        }
    };

    private void setToastMessageOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainFragmentActivity.this, mMessageToToast, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dialogSetLoadInfoOnUiThread(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialogSetLoadInfo(mProgressInt);
            }
        });
    }

    private void dismissProgressDialogOnUiThread(final boolean isShowToast){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProgressDialog(isShowToast);
            }
        });
    }

    public static int mVideoArrayStatus = ArrayVideoStatus.ALL_VIDEOS;

    public void addRemoveVideoAtDataBase(boolean isAdd, Video video) {
        LoggerUtil.log("DB Favorites is Open : " + mFavoriteDB.isOpen());
        if(mFavoriteDB.isOpen()){
            long index = mFavoriteDB.getVideoId(video);
            if(index != -1){
                mFavoriteDB.delete(index);
            }
            if(isAdd){
                mFavoriteDB.insert(video);
            }
        }
    }

    String mMessageToToast = "";

    void hiddenNavigationButton(){
        mControlLayout.setVisibility(View.GONE);
    }

    public void setFavoriteFlag(Video video){
        if(mFavoriteDB.getVideoId(video) > -1){
            video.setIsFavorite(true);
        } else {
            video.setIsFavorite(false);
        }
    }

    public void setCurrentTimePosition(Video video){
        long index = mRecentDB.getVideoId(video);
        if(index > -1){
            video.setCurrentTimePosition(mRecentDB.select(index).getCurrentTimePosition());
        } else {
            video.setCurrentTimePosition(0);
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        state.putInt("mVideoArrayStatus", mVideoArrayStatus);
        super.onSaveInstanceState(state);
    }

    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://5tv5.ru/index.php");

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
            nameValuePairs.add(new BasicNameValuePair("whatresult", "1"));
            nameValuePairs.add(new BasicNameValuePair("ter", ""));
            nameValuePairs.add(new BasicNameValuePair("namerus", "blade"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "windows-1251"));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            String resp = readData(httppost, "windows-1251"); //convertStreamToString(response.getEntity().getContent());
//            resp = URLEncoder.encode(resp, HTTP.UTF_8);
            LoggerUtil.log("POST RESPONSE: " + resp.indexOf("Продолжение приключений Прекрасных"));

        } catch (ClientProtocolException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
            // TODO Auto-generated catch block
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String readData(HttpUriRequest uri, String encoding) throws Exception {
        return new String(readBytes(uri), encoding);
    }

    public byte [] readBytes(HttpUriRequest uri) throws Exception {
        HttpClient client =  getNewHttpClient();
        HttpEntity entity = null;
        HttpResponse response = client.execute(uri);
        Log.e("response.getStatusLine().getStatusCode()", ":" + response.getStatusLine());
        if (response.getStatusLine().getStatusCode() == 200) {
            entity = response.getEntity();
            return EntityUtils.toByteArray(entity);
        } else {
            return null;
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



    public HttpClient getNewHttpClient() throws Exception {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new NonServerConnector.MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
}
