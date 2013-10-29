//package com.nmbb.oplayer.ui;
//
//import android.app.AlertDialog;
//import android.content.*;
//import android.content.res.Configuration;
//import android.graphics.Color;
//import android.text.InputFilter;
//import android.text.Spanned;
//import android.view.*;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.*;
//import com.nmbb.oplayer.R;
//import android.os.Bundle;
//import android.view.View.OnClickListener;
//import com.nmbb.oplayer.ui.adapter.BoxAdapter;
//import com.nmbb.oplayer.ui.adapter.NavigationAdapter;
//import com.nmbb.oplayer.ui.helper.Utils.ActivityUtil;
//import com.nmbb.oplayer.ui.helper.constants.ArrayVideoStatus;
//import com.nmbb.oplayer.ui.helper.constants.Constants;
//import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
//import com.nmbb.oplayer.ui.helper.Utils.NetworkUtil;
//import com.nmbb.oplayer.ui.helper.WakeLocker;
//import com.nmbb.oplayer.ui.helper.constants.MenuItemConstants;
//import com.nmbb.oplayer.ui.helper.parsers.MyHitMainPageParser;
//import com.nmbb.oplayer.ui.products.Video;
//import com.nmbb.oplayer.ui.search.SearchKeywordsConverter;
//import com.nmbb.oplayer.ui.sql.FavoriteVideoDataSource;
//import com.nmbb.oplayer.ui.sql.RecentVideoDataSource;
//import com.nmbb.oplayer.ui.sql.ResentVideosSql;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainFragmentActivity extends ActivityUtil implements OnClickListener {
//
//    RecentVideoDataSource mDateBase;
//    FavoriteVideoDataSource mFavoriteDateBase;
//    public static LinearLayout mControlLayout;
//    public static GridView mGridView;
//    private static Button mBtnPrev;
//    private boolean mIsContentAlreadyLoaded = true;
//    private boolean mIsRequestAlreadyStarted = false;
//    static ArrayList<Object> videos = new ArrayList<Object>();
//    static BoxAdapter boxAdapter;
//    static private EditText mSearch;
//    private static Button mBtnSearch;
//    private AlertDialog.Builder mBuilder;
//    private NetworkUtil mNetworkUtil;
//    private InputMethodManager mKeyboard;
//    private GridView mListViewFilter;
//    //    private TransparentPanel mTransparentPanel;
////    private LinearLayout mFilterLinearLayout;
//    public static SlidingDrawer mSlidingDrawer;
//    static Button mButtonSlide;
//    GridView mNavigationGrid;
//    NavigationAdapter mNavigationAdapter;
//    static RelativeLayout mSearchLayout;
//    static LinearLayout mMainControlLayout;
//
//    public ArrayList<Object> filterItems = new ArrayList<Object>(){};
//    private BoxAdapter boxAdapterFilter;
//    ArrayList<String> mNavigationNumbersArray = new ArrayList<String>(){};
//    static ProgressBar mProgressBar;
//    static TextView mTextProgressBar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        LoggerUtil.log("start onCreate");
//
//        inputManager =
//                (InputMethodManager) getApplicationContext().
//                        getSystemService(Context.INPUT_METHOD_SERVICE);
//
//        setContentView(R.layout.fragment_pager);
//        new MyHitMainPageParser(this);
//
////        mTransparentPanel = (TransparentPanel) findViewById(R.id.activity_transparent_panel);
////        mFilterLinearLayout = (LinearLayout) findViewById(R.id.content);
//
//        mSlidingDrawer = (SlidingDrawer) findViewById(R.id.panel_tools);
//        mButtonSlide = (Button) findViewById(R.id.handle);
//        mSlidingDrawer.setVisibility(View.GONE);
//
//        mSlidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
//            @Override
//            public void onDrawerOpened() {
//                setEnableForAllItemsOfControlLayout(false);
//                mGridView.setEnabled(false);
//                mSearch.setEnabled(false);
////                boxAdapterFilter.notifyDataSetChanged();
//            }
//        });
//
//        mSlidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
//            @Override
//            public void onDrawerClosed() {
//                setNavigationButtonsEnabled();
//                mGridView.setEnabled(true);
//                mSearch.setEnabled(true);
//            }
//        });
//
//        mDateBase = new RecentVideoDataSource(this);
//        mFavoriteDateBase = new FavoriteVideoDataSource(this);
//
//        mControlLayout = (LinearLayout) findViewById(R.id.linearLayoutControl);
//        mControlLayout.setVisibility(View.GONE);
//
//        mMainControlLayout = (LinearLayout)findViewById(R.id.mainLinearLayoutControl);
//        mMainControlLayout.setVisibility(View.GONE);
//
//        mKeyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        mNetworkUtil = new NetworkUtil(MainFragmentActivity.this);
//
//        mProgressBar = (ProgressBar)findViewById(R.id.main_progress_bar);
//        mTextProgressBar = (TextView) findViewById(R.id.text_progress_bar);
//
////        dialog = new ProgressDialog(this);
////        dialog.setMessage("Идет загрузка...");
////        dialog.setIndeterminate(true);
////        dialog.setCancelable(false);
//
//        mBuilder = new AlertDialog.Builder(this);
//        mBuilder.setTitle("Выйти из приложения?");
//        mBuilder.setNegativeButton("Выйти", mDialogClickListener);
//        mBuilder.setPositiveButton("Отмена", null);
//        mBuilder.setCancelable(false);
//
//        mWakeLock = new WakeLocker(getApplicationContext());
//        mBtnSearch = (Button)findViewById(R.id.button_search);
////        mBtnSearch.setVisibility(View.GONE);
//        mSearch = (EditText)findViewById(R.id.search);
////        mSearch.setVisibility(View.GONE);
//        mSearch.setOnTouchListener(mCurrentPageTouchListener);
//        mSearch.setOnEditorActionListener(mSearchEditorActionListener);
//        mBtnSearch.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doSearchRequest();
////                mIsRequestAlreadyStarted = true;
////                mSearch.selectAll();
////                hideKeyboard();
////                String searchLink = "http://my-hit.ru/index.php?module=search&func=view&result_orderby=score&result_order_asc=0&search_string=" +
////                        SearchKeywordsConverter.getConvertedKeyword(mSearch.getText().toString()) +
////                        "&submit=%C8%F1%EA%E0%F2%FC";
////                LoggerUtil.log("convert: " + searchLink);
////                fillSearchDataInBackGround(searchLink);
//            }
//        });
//        mSearchLayout = (RelativeLayout)findViewById(R.id.linearLayoutSearch);
//        mSearchLayout.setVisibility(View.GONE);
//
//        mGridView = (GridView) findViewById(R.id.gridView);
//        mListViewFilter = (GridView) findViewById(R.id.list_view_filter);
//        mBtnPrev = (Button) findViewById(R.id.btn_prev);
//        mBtnPrev.setEnabled(false);
////        mBtnPrev.setVisibility(View.GONE);
//        mBtnPrev.setText("Назад");
//        mBtnPrev.setTextColor(Color.parseColor("#ffffff"));
//        mBtnNext = (Button) findViewById(R.id.btn_next);
////        mBtnNext.setVisibility(View.GONE);
//        mBtnNext.setText("Вперёд");
//        mBtnNext.setTextColor(Color.parseColor("#ffffff"));
//
//        mNavigationGrid = (GridView) findViewById(R.id.gridViewNavigation);
//        mNavigationAdapter = new NavigationAdapter(getApplicationContext(), mNavigationNumbersArray, MainFragmentActivity.this);
//        mNavigationGrid.setAdapter(mNavigationAdapter);
////        setUpNavigationArray(MyHitMainPageParser.getFirstPage());
//
//        int columnsCount = (int)(getWidth()/400);
//        mGridView.setNumColumns(columnsCount);
//        mGridView.setOnScrollListener(scrollListener);
//        mGridView.setOnTouchListener(listViewTouchListener);
//        mBtnNext.setOnClickListener(mNextClickListener);
//
//        mBtnPrev.setOnClickListener(mPrevClickListener);
//
//        boxAdapter = new BoxAdapter(this, videos, MainFragmentActivity.this);
//        boxAdapterFilter = new BoxAdapter(this, filterItems, MainFragmentActivity.this);
//        mGridView.setAdapter(boxAdapter);
//        mListViewFilter.setAdapter(boxAdapterFilter);
//
////        boxAdapter.notifyDataSetChanged();
//        hideKeyboard();
//
//        mGridView.setVisibility(View.VISIBLE);
//        LoggerUtil.log("MyHitMainPageParser.getVideos().size() " + MyHitMainPageParser.getVideos().size());
//        if(savedInstanceState != null && !savedInstanceState.isEmpty()){
//            mVideoArrayStatus = savedInstanceState.getInt("mVideoArrayStatus");
//        } else {
//            mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//        }
//        updateAdapter();
//
//        MyHitMainPageParser.mMainFragmentActivity = MainFragmentActivity.this;
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_SEARCH
//                && (event.getFlags() & KeyEvent.FLAG_LONG_PRESS) == KeyEvent.FLAG_LONG_PRESS) {
//            return true;
//        } else if(keyCode == KeyEvent.KEYCODE_SEARCH){
////            MyHitMainPageParser.setIsSearchRequest(true);
//            mVideoArrayStatus = MenuItemConstants.SEARCH;
//            showSearchOnUiThread();
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
////    @Override
////    public boolean onSearchRequested() {
////        MyHitMainPageParser.setIsSearchRequest(true);
////        mVideoArrayStatus = MenuItemConstants.SEARCH;
////        showSearchOnUiThread();
////        return false;
////    }
//
//    private void setUpNavigationArray(int firstItemNumber) {
//        if(firstItemNumber <= 0){
//            firstItemNumber = 1;
//        }
//
//        MyHitMainPageParser.setFirstPage(firstItemNumber);
//        mNavigationNumbersArray.clear();
//        mNavigationButtonsCount = ((int)getWidth() - 85*2) / (90);
//        mNavigationGrid.setNumColumns(mNavigationButtonsCount);
//
//        for(int currentItemNumber = firstItemNumber; currentItemNumber < firstItemNumber + mNavigationButtonsCount; currentItemNumber ++){
//            mNavigationNumbersArray.add(currentItemNumber + "");
//            if(currentItemNumber == Integer.parseInt(MyHitMainPageParser.getTotalPagesCount())){
//                break;
//            }
//        }
//        setNavigationButtonsEnabled();
//        mNavigationAdapter.notifyDataSetChanged();
//    }
//
//    SlidingDrawer.OnDrawerScrollListener mDrawerScrollListener = new SlidingDrawer.OnDrawerScrollListener() {
//        @Override
//        public void onScrollStarted() {
//            notifyChanged();
//        }
//
//        @Override
//        public void onScrollEnded() {
//            if(mSlidingDrawer.isOpened()){
//                mGridView.setEnabled(true);
//            } else {
//                mGridView.setEnabled(false);
//            }
//        }
//    };
//
//    private void updateAdapter(){
//        if(MyHitMainPageParser.getVideos().size() > 0){
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    setFirstTab();
//                }
//            });
//        } else {
//            String link = MyHitMainPageParser.getLastLink();
//            fillDataInBackGround(link.length() ==  0 ? Constants.FIRST_REQUEST_LINK : link);
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        LoggerUtil.log("onConfigurationChanged");
//        int columnsCount = (int)(getWidth()/400);
//        mGridView.setNumColumns(columnsCount);
//        setUpNavigationArray(MyHitMainPageParser.getFirstPage());
//        super.onConfigurationChanged(newConfig);
//    }
//
//    View.OnTouchListener listViewTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            final int action = event.getAction();
//            switch(action & MotionEvent.ACTION_MASK){
//
//                case MotionEvent.ACTION_MOVE:
//                    LoggerUtil.log("scroll ACTION_MOVE ");
//                    break;
//                case MotionEvent.ACTION_DOWN:
//                    LoggerUtil.log("scroll ACTION_DOWN ");
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                    LoggerUtil.log("scroll ACTION_CANCEL ");
//                    break;
//                case MotionEvent.ACTION_UP:
//                    LoggerUtil.log("scroll ACTION_UP ");
//                    break;
//            }
//            return false;
//        }
//    };
//
//    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(AbsListView view, int scrollState) {
//            LoggerUtil.log("scroll scrollState " + scrollState);
//        }
//
//        @Override
//        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//            LoggerUtil.log("scroll firstVisibleItem " + firstVisibleItem);
//            GridView list = (GridView) view;
//            View v = list.getChildAt(0);
////            LoggerUtil.log("scroll firstVisibleItem" + view.getX());
//            if(firstVisibleItem == 0){
//                LoggerUtil.log("scroll начало списка");
//            } else if(firstVisibleItem + visibleItemCount == totalItemCount - 1){
//                LoggerUtil.log("scroll конец списка");
//            }
//        }
//    };
//
//    private View.OnTouchListener mCurrentPageTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            v.requestFocus();
//            mKeyboard.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
//            ((EditText)v).selectAll();
//            return false;
//        }
//    };
//
//    private OnClickListener mUpdateListViewListener = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(!mIsContentAlreadyLoaded){
//                fillDataInBackGround(MyHitMainPageParser.getLastLink());
//                mIsContentAlreadyLoaded = true;
//            }
//        }
//    };
//
//    private DialogInterface.OnClickListener mDialogClickListener = new DialogInterface.OnClickListener(){
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            finish();
//            System.exit(0);
//        }
//    };
//
//    private void fillDataInBackGround(final String requestLink) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                fillData(requestLink);
//            }
//        }).start();
//    }
//
//    private void fillSearchDataInBackGround(final String requestLink) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                fillSearchData(requestLink);
//            }
//        }).start();
//    }
//
//    InputFilter mFilter = new InputFilter() {
//        public CharSequence filter(CharSequence source, int start, int end,
//                                   Spanned dest, int dstart, int dend) {
//            for (int i = start; i < end; i++) {
//                if (!Character.isDigit(source.charAt(i))) {
//                    return "";
//                }
//            }
//            return null;
//        }
//    };
//
//    public static void onPreExecute() {
//        LoggerUtil.log("################# preExecute");
//        mControlLayout.setVisibility(View.GONE);
//        mSlidingDrawer.setVisibility(View.GONE);
//        mGridView.setVisibility(View.GONE);
////        mSearch.setVisibility(View.GONE);
////        mBtnSearch.setVisibility(View.GONE);
//        mSearchLayout.setVisibility(View.GONE);
//        mMainControlLayout.setVisibility(View.GONE);
//        if(mProgressBar.getVisibility() == View.GONE)
//            mProgressBar.setVisibility(View.VISIBLE);
//        if(mTextProgressBar.getVisibility() == View.GONE)
//            mTextProgressBar.setVisibility(View.VISIBLE);
//        mGridView.smoothScrollToPosition(0);
////        dialog.show();
//    }
//
//    public void changeTab(){
//        setFirstTab();
//    }
//
//    private void setFirstTab(){
//        if(mSlidingDrawer.isOpened()){
//            mSlidingDrawer.animateClose();
//        }
//        mGridView.setVisibility(View.GONE);
////        videos.clear();
////        boxAdapter.notifyDataSetChanged();            // todo test
////        mGridView.smoothScrollToPosition(0, 0);
//        if(mIsContentAlreadyLoaded){
////            mSearch.setVisibility(View.GONE);
////            mBtnSearch.setVisibility(View.GONE);
//            mSearchLayout.setVisibility(View.GONE);
//            setControlPanel();
//            updateList();
//        }
//        notifyDataSetChanged();
//        mGridView.setVisibility(View.VISIBLE);
//    }
//
//    public void onPostExecute(){
//        LoggerUtil.log("############### postExecute");
////        mControlLayout.setVisibility(View.VISIBLE);
//        mSlidingDrawer.setVisibility(View.VISIBLE);
////        if(mVideoArrayStatus == MenuItemConstants.ALL_VIDEO)
////            mMainControlLayout.setVisibility(View.VISIBLE);
//        setControlPanel();
////        notifyDataSetChanged();
//        mGridView.setVisibility(View.VISIBLE);
//        dismissDialog();
//    }
//
//    private void dismissProgressDialog(boolean isShowToast){
//        if(isShowToast){
//            Toast.makeText(this,"Проверьте соединение с интернет и повторите попытку.", Toast.LENGTH_LONG).show();
////            videos.clear();   // todo test
//            mIsContentAlreadyLoaded = false;
//            //notifyDataSetChanged();
//            mControlLayout.setVisibility(View.GONE);
//            mMainControlLayout.setVisibility(View.GONE);
////            mBtnNext.setVisibility(View.GONE);
////            mBtnPrev.setVisibility(View.GONE);
//        }
//        dismissDialog();
//    }
//
//    void doSearchRequest(){if(!mIsRequestAlreadyStarted){
//        mIsRequestAlreadyStarted = true;
//        String keyWords = mSearch.getText().toString();
//        if(keyWords.trim().length() < 1){
//            mIsRequestAlreadyStarted = false;
//            return;
//        }
//        mSearch.selectAll();
//        hideKeyboard();
//        String searchLink = "http://my-hit.ru/index.php?module=search&func=view&result_orderby=score&result_order_asc=0&search_string=" +
//                SearchKeywordsConverter.getConvertedKeyword(keyWords) +
//                "&submit=%C8%F1%EA%E0%F2%FC";
//        LoggerUtil.log("convert: " + searchLink);
//        fillSearchDataInBackGround(searchLink);
//    }
//
//    }
//
//    TextView.OnEditorActionListener mSearchEditorActionListener = new TextView.OnEditorActionListener() {
//        @Override
//        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//            doSearchRequest();
////            if(!mIsRequestAlreadyStarted){
////                mIsRequestAlreadyStarted = true;
////                ((EditText)textView).selectAll();
////                hideKeyboard();
////                String keyWords = textView.getText().toString();
////                if(keyWords.trim().length() < 1){
////                    mIsRequestAlreadyStarted = false;
////                    return true;
////                }
////                String searchLink = "http://my-hit.ru/index.php?module=search&func=view&result_orderby=score&result_order_asc=0&search_string=" +
////                        SearchKeywordsConverter.getConvertedKeyword(keyWords) +
////                        "&submit=%C8%F1%EA%E0%F2%FC";
////                LoggerUtil.log("convert: " + searchLink);
////                fillSearchDataInBackGround(searchLink);
////            }
//            return true;
//        }
//    };
//
//    private static Button mBtnNext;
//    OnClickListener mPrevClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            doRequest(MyHitMainPageParser.getFirstPage() - mNavigationButtonsCount, 0);
////            int pageNumber = MyHitMainPageParser.getFirstPage() - mNavigationButtonsCount;
////            NavigationAdapter.mSelectedPosition = 0;
////            fillDataInBackGround(MyHitMainPageParser.getLastLink() + "/" + (pageNumber > 0 ? pageNumber : 1));
////            setUpNavigationArray(pageNumber);
//        }
//    };
//
//    int mNavigationButtonsCount = 0;
//
//    OnClickListener mNextClickListener = new OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            doRequest(MyHitMainPageParser.getFirstPage() + mNavigationButtonsCount, 0);
////            fillDataInBackGround(MyHitMainPageParser.getLastLink() + "/" + (MyHitMainPageParser.getFirstPage() + mNavigationButtonsCount));
////            NavigationAdapter.mSelectedPosition = 0;
////            setUpNavigationArray(MyHitMainPageParser.getFirstPage() + mNavigationButtonsCount);
//        }
//    };
//
//    public void doRequest(int goToPosition, int selectedPosition, int firstPosition){
//        NavigationAdapter.mSelectedPosition = selectedPosition;
//        fillDataInBackGround(MyHitMainPageParser.getLastLink() + "/" + (goToPosition > 0 ? goToPosition : 1));
//        setUpNavigationArray(firstPosition < 1 ? goToPosition : firstPosition);
//    }
//
//    public void doRequest(int goToPosition, int selectedPosition){
//        doRequest(goToPosition, selectedPosition, 0);
//    }
//
//    public void doRequest(String link){
//        if(mSlidingDrawer.isOpened())
//            mSlidingDrawer.animateClose();
//        MyHitMainPageParser.setLastLink("");
//        NavigationAdapter.mSelectedPosition = 0;
//        fillDataInBackGround(link);
//        setUpNavigationArray(0);
//    }
//
//    InputMethodManager inputManager;
//
//    private void preExecuteOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                onPreExecute();
//            }
//        });
//    }
//
//    private void notifyDataSetChangedOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                notifyDataSetChanged();
//            }
//        });
//    }
//
//    private void setControlPanelOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                setControlPanel();
//                mIsContentAlreadyLoaded = true;
//                notifyDataSetChanged();
//            }
//        });
//    }
//
//    void setNavigationButtonsEnabled(){
//        if(MyHitMainPageParser.getFirstPage() == 1){
//            if(mBtnPrev.isEnabled())
//                mBtnPrev.setEnabled(false);
//        } else {
//            if(!mBtnPrev.isEnabled())
//                mBtnPrev.setEnabled(true);
//        }
//        if(!mBtnNext.isEnabled()){
//            mBtnNext.setEnabled(true);
//        }
//        if(MyHitMainPageParser.getFirstPage() + mNavigationButtonsCount > Integer.parseInt(MyHitMainPageParser.getTotalPagesCount())){
//            if(mBtnNext.isEnabled())
//                mBtnNext.setEnabled(false);
//        }
//    }
//
//    void fillData(final String link) { //todo
//        Thread request  = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                preExecuteOnUiThread();
////                videos.clear();    // todo test
////                notifyDataSetChangedOnUiThread();
//                mIsDownloadStarted = true;
//                LoggerUtil.log("start before : " + mIsDownloadStarted);
//                try {
//                    MyHitMainPageParser.setVideoArrayList(link, videos);
//                    setControlPanelOnUiThread();
//                    postExecuteOnUiThread();
//                } catch (Exception e) {
//                    dismissDialog();
//                    notifyDataSetChangedOnUiThread();
//                    mIsContentAlreadyLoaded = false;
//                    e.printStackTrace();
//                }
//                mIsRequestAlreadyStarted = false;
//                mIsDownloadStarted = false;
//                LoggerUtil.log("start after : " + mIsDownloadStarted);
//            }
//        });
//        request.run();
//    }
//
//    private void postExecuteOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                onPostExecute();
//            }
//        });
//    }
//
//    void dismissDialog(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(mProgressBar.getVisibility() == View.VISIBLE)
//                    mProgressBar.setVisibility(View.GONE);
//                if(mTextProgressBar.getVisibility() == View.VISIBLE)
//                    mTextProgressBar.setVisibility(View.GONE);
//            }
//        });
//    }
//
//    private void setFirstTabOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                setFirstTab();
//            }
//        });
//    }
//
//    void fillSearchData(final String link) {
//        Thread request  = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                preExecuteOnUiThread();
////                videos.clear();   // todo test
////                notifyDataSetChangedOnUiThread();
//                try {
//                    MyHitMainPageParser.setSearchArrayList(link, videos);
//                    setControlPanelOnUiThread();     //todo
//                    postExecuteOnUiThread();
//                    setFirstTabOnUiThread();
//                } catch (Exception e) {
//                    dismissDialog();
//                    mIsContentAlreadyLoaded = false;
//                    e.printStackTrace();
//                }
//                mIsRequestAlreadyStarted = false;
//            }
//        });
//        request.run();
//    }
//
//    static public int mProgressInt = 0;
//
//    void notifyDataSetChanged(){
//        boxAdapter.notifyDataSetChanged();
//        setUpNavigationArray(MyHitMainPageParser.getFirstPage());
//        notifyChanged();
//        try{
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    void notifyFilterDataSetChanged(){
//        boxAdapterFilter.notifyDataSetChanged();
//        try{
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    void showSearch(){
////        mSearch.setVisibility(View.VISIBLE);
////        mBtnSearch.setVisibility(View.VISIBLE);
//        mSearchLayout.setVisibility(View.VISIBLE);
//        mMainControlLayout.setVisibility(View.VISIBLE);
//        mControlLayout.setVisibility(View.GONE);
//        mSearch.requestFocus();
//        mKeyboard.showSoftInput(mSearch, InputMethodManager.SHOW_IMPLICIT);
//        mSearch.selectAll();
//        if(MyHitMainPageParser.getVideoSearchList().size() > 0){
//            MyHitMainPageParser.setIsSearchRequest(true);
////            videos.clear();      // todo test
//            videos.addAll(MyHitMainPageParser.getVideoSearchList());
//            mGridView.setVisibility(View.GONE);
//            mGridView.smoothScrollToPosition(0);
//            notifyDataSetChangedOnUiThread();
//            mGridView.setVisibility(View.VISIBLE);
//            mControlLayout.setVisibility(View.GONE);
//        }
//    }
//
//    public static void setControlPanel(){
//        if(MyHitMainPageParser.isSearchRequest() ||
//                mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE ||
//                mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
//            mControlLayout.setVisibility(View.GONE);
//            mMainControlLayout.setVisibility(View.GONE);
////            mBtnPrev.setVisibility(View.GONE);
////            mBtnNext.setVisibility(View.GONE);
//        } else {
//            mControlLayout.setVisibility(View.VISIBLE);
//            mMainControlLayout.setVisibility(View.VISIBLE);
////            mBtnPrev.setVisibility(View.VISIBLE);
////            mBtnNext.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void hideKeyboard(){
//        try{
//            if(inputManager != null)
//                inputManager.hideSoftInputFromWindow(
//                        MainFragmentActivity.this.getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    private void dialogSetLoadInfo(int mInfoLoad) {
////        dialog.setMessage("Идет загрузка..." + mInfoLoad + "%");
//        mTextProgressBar.setText(mInfoLoad + "%");
//    }
//
//    @Override
//    public void onBackPressed() {
//        if(mSlidingDrawer.isOpened()) {
//            mSlidingDrawer.animateClose();
//        }else if(mSearchLayout.getVisibility() == View.VISIBLE){
////            mSearch.setVisibility(View.GONE);
////            mBtnSearch.setVisibility(View.GONE);
//            if(!MyHitMainPageParser.isSearchRequest()){
//                mControlLayout.setVisibility(View.VISIBLE);
//                mMainControlLayout.setVisibility(View.VISIBLE);
//            }else {
//                mMainControlLayout.setVisibility(View.GONE);
//            }
//            mSearchLayout.setVisibility(View.GONE);
//            if(!MyHitMainPageParser.isSearchRequest()){
//                mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//            }
////            updateList();
////            MyHitMainPageParser.setIsSearchRequest(false);
////            mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
////            showAllVideosOnUiThread();
////            setFirstTabOnUiThread();
//        } else if(mGridView.getVisibility() == View.GONE){
//            setFirstTabOnUiThread();
//        }else if(mVideoArrayStatus != MenuItemConstants.ALL_VIDEO){
//            MyHitMainPageParser.setIsSearchRequest(false);
//            mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//            showAllVideosOnUiThread();
//            setFirstTabOnUiThread();
//            mControlLayout.setVisibility(View.VISIBLE);
//            mMainControlLayout.setVisibility(View.VISIBLE);
//        }else{
//            mBuilder.show();
//        }
//    }
//
//    private void showAllVideosOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(mVideoArrayStatus == MenuItemConstants.ALL_VIDEO){
//                    mControlLayout.setVisibility(View.VISIBLE);
////                    mBtnPrev.setVisibility(View.VISIBLE);
////                    mBtnNext.setVisibility(View.VISIBLE);
//                    mSlidingDrawer.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//    }
//
//    void showVideo(){
//        hideKeyboard();
//        setFirstTab();
//    }
//
//    private WakeLocker mWakeLock;
//
//    @Override
//    public void onPause(){
//        super.onPause();
//        mDateBase.close();
//        mFavoriteDateBase.close();
//        LoggerUtil.log("start onPause");
//        unregisterReceiver(mReceiver);
//    }
//
//    IntentFilter mIntents = new IntentFilter();
//    {
//        mIntents.addAction(Constants.INTENT_RELOAD_UI);
//        mIntents.addAction(Constants.INTENT_DISMISS_DIALOG_AND_RELOAD_UI);
//        mIntents.addAction(Constants.INTENT_SCROLL_LIST_VIEW);
//        mIntents.addAction(Constants.INTENT_DISMISS_DIALOG);
//        mIntents.addAction(Constants.INTENT_CONNECTIVITY_CHANGE);
//        mIntents.addAction(Constants.INTENT_DIALOG_CHANGE_PROGRESS);
//        mIntents.addAction(Constants.INTENT_UPDATE_LISTVIEWS);
//        mIntents.addAction(Constants.INTENT_SEARCH_FAIL);
//    }
//
//    @Override
//    public void onResume(){
//        LoggerUtil.log("start onResume : " + mIsDownloadStarted);
//        super.onResume();
//        mGridView.setNumColumns(((int)(getWidth()/400)));
//        mDateBase.open();
//        mFavoriteDateBase.open();
//        MyHitMainPageParser.mFavoriteDateBase = mFavoriteDateBase;
//        registerReceiver(mReceiver, mIntents);
//        LoggerUtil.log("start onResume 1 : " + mIsDownloadStarted);
//        if(mProgressInt == 100){
//            dismissProgressDialogOnUiThread(false);
//            mProgressInt = 0;
//        }
//        if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
//            showResentListAtBackground(false);
//        } else if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE){
//            showResentListAtBackground(true);
//        }
//        boxAdapter.notifyDataSetChanged();
//
////        updateList();
//
//        postExecuteOnUiThread();
////        setFirstTabOnUiThread();
//    }
//
//    void updateList(){
//        LoggerUtil.log("mVideoArrayStatus " + mVideoArrayStatus);
//        if(mVideoArrayStatus == MenuItemConstants.ALL_VIDEO){
//            videos.addAll(MyHitMainPageParser.getVideos());
//        } else if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
//            if(MyHitMainPageParser.getResentVideos().size() > 0){
//                hiddenNavigationButton();
//                videos.addAll(MyHitMainPageParser.getResentVideos());
//            } else{
//                videos.addAll(MyHitMainPageParser.getVideos());
//            }
//        } else if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE){
//            if(MyHitMainPageParser.getFavoriteVideos().size() > 0){
//                hiddenNavigationButton();
//                videos.addAll(MyHitMainPageParser.getFavoriteVideos());
//            } else{
//                videos.addAll(MyHitMainPageParser.getVideos());
//            }
//        } else if(mVideoArrayStatus == MenuItemConstants.SEARCH){
//            videos.addAll(MyHitMainPageParser.getVideoSearchList());
////            mSearch.setVisibility(View.VISIBLE);
////            mBtnSearch.setVisibility(View.VISIBLE);
//            mSearchLayout.setVisibility(View.VISIBLE);
//            mControlLayout.setVisibility(View.GONE);
//        }
//    }
//
//    void setEnableForAllItemsOfControlLayout(boolean isEnabled){
//        mBtnNext.setEnabled(isEnabled);
//        mBtnPrev.setEnabled(isEnabled);
//    }
//
//    private boolean mIsDownloadStarted = false;
//
//    private BroadcastReceiver mReceiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context arg0, Intent intent) {
//            LoggerUtil.log(intent.getAction());
//            LoggerUtil.log("Received: -mReloadUiIntent: " +intent.getAction() + "");
//            if(intent.getAction().equals(Constants.INTENT_RELOAD_UI)){
//                changeTab();
//                onPostExecute();
//            } else if(intent.getAction().equals(Constants.INTENT_DISMISS_DIALOG_AND_RELOAD_UI)){
//                onPostExecute();
//            } else if(intent.getAction().equals(Constants.INTENT_SCROLL_LIST_VIEW)){
//                int position = intent.getIntExtra(Constants.EXTRA_SCROLL_LIST_VIEW, 0);
//                if(position == 1){
////                    filterItems.clear();      // todo clear
//                    filterItems.addAll(boxAdapterFilter.getCategoriesArray());
//                }
//                notifyFilterDataSetChanged();
//            } else if(intent.getAction().equals(Constants.INTENT_DISMISS_DIALOG)){
//                dismissProgressDialogOnUiThread(true);
//            } else if(intent.getAction().equals(Constants.INTENT_DIALOG_CHANGE_PROGRESS)){
//                mProgressInt = intent.getIntExtra(Constants.EXTRA_DIALOG_PROGRESS, 0);
//                dialogSetLoadInfoOnUiThread();
//            } else if(intent.getAction().equals(Constants.INTENT_UPDATE_LISTVIEWS)){
//                mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//                mGridView.setEnabled(true);
//                setFirstTabOnUiThread();
//            } else if(intent.getAction().equals(Constants.INTENT_SEARCH_FAIL)){
//                mMessageToToast = "По вашему запросу ничего не найдено.";
//                setToastMessageOnUiThread();
//                showAllVideosOnUiThread();
//            }
//        }
//    };
//
//    private void setToastMessageOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(MainFragmentActivity.this, mMessageToToast, Toast.LENGTH_LONG).show();
//            }
//        });
//    }
//
//    private void dialogSetLoadInfoOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                dialogSetLoadInfo(mProgressInt);
//            }
//        });
//    }
//
//    private void dismissProgressDialogOnUiThread(final boolean isShowToast){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                dismissProgressDialog(isShowToast);
//            }
//        });
//    }
//
//    void notifyChanged(){
//        try{
//            if(filterItems == null || filterItems.size() < 5){
////                if(filterItems != null){
////                    filterItems.clear();     // todo test
////                }
//                filterItems.addAll(MyHitMainPageParser.getCategories());
//                filterNotifyChangedOnUiThread();
//            }
//        } catch(Exception ex){
//            ex.printStackTrace();
//        }
//    }
//
//    private void filterNotifyChangedOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                boxAdapterFilter.notifyDataSetChanged();
//            }
//        });
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//        menu.add(Menu.NONE, MenuItemConstants.SEARCH, Menu.NONE, "Поиск");
//
//        menu.add(Menu.NONE, MenuItemConstants.REFRESH, Menu.NONE, "Обновить");
//
////        SubMenu sm = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Видео");
//
//        SubMenu sm = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Загрузить");
//        sm.add(Menu.NONE, MenuItemConstants.ALL_VIDEO, Menu.NONE, "Весь список");
//        sm.add(Menu.NONE, MenuItemConstants.DOWNLOAD_FAVORITE, Menu.NONE, "Избранное");
//
////        SubMenu sm1 = sm.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Последние просмотренные") ;
//        sm.add(Menu.NONE, MenuItemConstants.DOWNLOAD_RECENT, Menu.NONE, "Просмотренное");
////        sm1.add(Menu.NONE, MenuItemConstants.CLEAR_RECENT, Menu.NONE, "Очистить список");
//
//
//        SubMenu sm1 = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Очистить") ;
//        sm1.add(Menu.NONE, MenuItemConstants.CLEAR_FAVORITE, Menu.NONE, "Избранное");
//        sm1.add(Menu.NONE, MenuItemConstants.CLEAR_RECENT, Menu.NONE, "Просмотренное");
//////        sm1 = sm.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Избранное видео") ;
////        menu.add(Menu.NONE, MenuItemConstants.DOWNLOAD_FAVORITE, Menu.NONE, "Избранное");
////        sm1.add(Menu.NONE, MenuItemConstants.CLEAR_FAVORITE, Menu.NONE, "Очистить список");
////        sm1.add(Menu.NONE, MenuItemConstants.CANCEL, Menu.NONE, "Отмена");
//
////       sm = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Обновить");
////        menu.add(Menu.NONE, MenuItemConstants.REFRESH, Menu.NONE, "Обновить");
////        sm.add(Menu.NONE, MenuItemConstants.CANCEL, Menu.NONE, "Отмена");
//
//        //get a SubMenu reference
////        sm = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Навигация");
////        //add menu items to the submenu
////        sm.add(Menu.NONE, MenuItemConstants.NAVIGATION_BUTTONS, Menu.NONE, "Кнопки навигации");
////        sm.add(Menu.NONE, MenuItemConstants.NAVIGATION_AUTO_DOWNLOAD, Menu.NONE, "Автоподгрузка видео");
//
//        //it is better to use final variables for IDs than constant values
//        //menu.add(Menu.NONE,1,Menu.NONE,"Exit");
//
//        //the menu option text is defined as constant String
////        sm = menu.addSubMenu(Menu.NONE, MenuItemConstants.PARENT_MENU, Menu.NONE, "Выход");
////        sm.add(Menu.NONE, MenuItemConstants.MINIMIZE, Menu.NONE, "Свернуть");
//        menu.add(Menu.NONE, MenuItemConstants.EXIT, Menu.NONE, "Выход");
////        sm.add(Menu.NONE, MenuItemConstants.CANCEL, Menu.NONE, "Отмена");
//
//        return true;
//    }
//
//    ArrayList<Object> mResentVideos = new ArrayList<Object>(){};
//    ArrayList<Object> mFavoriteVideos = new ArrayList<Object>(){};
//
//    public static int mVideoArrayStatus = ArrayVideoStatus.ALL_VIDEOS;
//
//    private void showSearchOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                showSearch();
//            }
//        });
//    }
//
//    boolean mIsFilterShow = false;
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item)
//    {
//        switch(item.getItemId()){              //todo
//            case MenuItemConstants.REFRESH :
//                String link = MyHitMainPageParser.getLastLink() + "/" + (MyHitMainPageParser.getCurrentPage().equals("0") ? "1" : MyHitMainPageParser.getCurrentPage());
//                fillDataInBackGround(link.length() ==  0 ? Constants.FIRST_REQUEST_LINK : link);
//                break;
////            case MenuItemConstants.NAVIGATION_BUTTONS :
////                showNavigationButton();
////                break;
////            case MenuItemConstants.NAVIGATION_AUTO_DOWNLOAD :
////                hiddenNavigationButton();
////                break;
//            case MenuItemConstants.SEARCH :
////                MyHitMainPageParser.setIsSearchRequest(true);
//                mVideoArrayStatus = MenuItemConstants.SEARCH;
//                showSearchOnUiThread();
//                break;
////            case MenuItemConstants.FILTER :
////                filter();
////                break;
////            case MenuItemConstants.MINIMIZE:
////                this.finish();
////                break;
//            case MenuItemConstants.EXIT:
//                this.finish();
//                System.exit(0);
//                break;
//            case MenuItemConstants.DOWNLOAD_RECENT:
//                showResentListAtBackground(false);
//                break;
//            case MenuItemConstants.CLEAR_RECENT:
//                List<ResentVideosSql> recentVideos = mDateBase.getAllVideos();
//                for(ResentVideosSql videosSql : recentVideos){
//                    mDateBase.deleteVideo(videosSql);
//                }
//                if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
//                    showResentListAtBackground(false);
//                }
//                mMessageToToast = "Список \"Последние просмотренные\" очищен";
//                setToastMessageOnUiThread();
//                break;
//            case MenuItemConstants.ALL_VIDEO:
//                MyHitMainPageParser.setIsSearchRequest(false);
//                mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//                showVideo();
//                break;
//            case MenuItemConstants.DOWNLOAD_FAVORITE:
//                showResentListAtBackground(true);
//                break;
//            case MenuItemConstants.CLEAR_FAVORITE:
////                MyHitMainPageParser.setIsSearchRequest(false);
//                List<ResentVideosSql> favoriteVideos = mFavoriteDateBase.getAllVideos();
//                for(ResentVideosSql videosSql : favoriteVideos){
//                    mFavoriteDateBase.deleteVideo(videosSql);
//                }
//                if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE){
//                    showResentListAtBackground(true);
//                }
//                mMessageToToast = "Список \"Избранное видео\" очищен";
//                setToastMessageOnUiThread();
//                break;
//        }
//        return false;
//    }
//
////    private void filter() {
////        if(mFilterLinearLayout.getVisibility() == View.VISIBLE){
////            mGridView.setEnabled(true);
////        }else{
////            if(filterItems == null || filterItems.size() != 5){
////                if(filterItems != null)
////                    filterItems.clear();
////                filterItems.addAll(MyHitMainPageParser.getCategories());
////                filterNotifyChangedOnUiThread();
////            }
////
////            mGridView.setEnabled(false);
////        }
////    }
//
//    private void hiddenNavigationButtonOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                hiddenNavigationButton();
//            }
//        });
//    }
//
//    public void showResentListAtBackground(final boolean isFavorites) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    mProgressInt = 0;
//                    boolean isShownNavigationPanel = (mControlLayout.getVisibility() == View.VISIBLE);
//                    preExecuteOnUiThread();
////                    mFavoriteDateBase.open();
//                    List<ResentVideosSql> videos1 =  isFavorites ? mFavoriteDateBase.getAllVideos() : mDateBase.getAllVideos();
//
//                    Video currentVideo = null;
//                    int videosCount = videos1.size();
//                    int currentIndex = 0;
//                    String videoData = "";
//                    if(videosCount > 0){
//                        if(isFavorites){
//                            mFavoriteVideos.clear();
//                            mVideoArrayStatus = MenuItemConstants.DOWNLOAD_FAVORITE;
//                        }
//                        else {
//                            mResentVideos.clear();
//                            mVideoArrayStatus = MenuItemConstants.DOWNLOAD_RECENT;
//                        }
//                        MyHitMainPageParser.setIsSearchRequest(false);
//
//                        hiddenNavigationButtonOnUiThread();
//                        for(currentIndex = videosCount - 1; currentIndex >=0; currentIndex --){
//                            mProgressInt += 100/videosCount;
//                            dialogSetLoadInfoOnUiThread();
//                            videoData = videos1.get(currentIndex).getVideo();
//                            LoggerUtil.log(" videoData: " + videoData);
//                            currentVideo = new Video(videoData);
//                            if(isFavorites){
//                                mFavoriteVideos.add(currentVideo);
//                                MyHitMainPageParser.setFavoriteVideos(mFavoriteVideos);
//                            }else{
//                                mResentVideos.add(currentVideo);
//                                MyHitMainPageParser.setResentVideos(mResentVideos);
//                            }
//                        }
//                        if(isFavorites){
//                            MyHitMainPageParser.setFavoriteVideos(mFavoriteVideos);
//                        }else{
//                            MyHitMainPageParser.setResentVideos(mResentVideos);
//                        }
//                        mProgressInt = 100;
//                        dialogSetLoadInfoOnUiThread();
//                        videos.clear();   // todo tets
////                        videos = (isFavorites ? mFavoriteVideos : mResentVideos);
//                        videos.addAll(isFavorites ? mFavoriteVideos : mResentVideos);
//                        notifyDataSetChangedOnUiThread();
//                    } else {
//                        //todo add if resent/favorites is empty
//                        mMessageToToast = "Cписок " + (isFavorites ? "\"Избранное видео\"" : "\"Последние просмотренные\"") + " пуст.";
//                        setToastMessageOnUiThread();
//                        if(mVideoArrayStatus == MenuItemConstants.DOWNLOAD_FAVORITE ||
//                                mVideoArrayStatus == MenuItemConstants.DOWNLOAD_RECENT){
//                            MyHitMainPageParser.setIsSearchRequest(false);
//                            mVideoArrayStatus = MenuItemConstants.ALL_VIDEO;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showVideo();
//                                    for(Object o : videos){
//                                        if(o instanceof Video){
//                                            ((Video) o).setIsFavorite(false);
//                                        }
//                                    }
//                                    notifyDataSetChanged();
//                                }
//                            });
//                            showNavigationButtonsOnUiThread();
//                        } else if(isShownNavigationPanel)
//                            showNavigationButtonsOnUiThread();
//                    }
//                    dismissProgressDialogOnUiThread(false);
////                runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        mSlidingDrawer.setVisibility(View.VISIBLE);
////                    }
////                });
//                    postExecuteOnUiThread();
//                }catch(Exception ex){
//                    ex.printStackTrace();
//                }
//
//            }}).start();
//    }
//
//    public void addRemoveVideoAtDataBase(boolean isAdd, Video mVideo) {
//        String videoData = mVideo.getSimpleString().split(";;;")[0];
////        LoggerUtil.log("video1: " + videoData);
//
//        LoggerUtil.log("video1: " + videoData);
//        for(ResentVideosSql video : mFavoriteDateBase.getAllVideos()){
//            LoggerUtil.log("video1: " + video.getVideo());
//            if(video.getVideo().split(";;;")[0].equals(videoData)){
////                isVideoAlreadyExists = true;
//                mFavoriteDateBase.deleteVideo(video);
//                break;
//            }
//        }
//        if(isAdd){
//            mFavoriteDateBase.createVideo(getVideoData(mVideo));
//        }
//    }
//
//    private String getVideoData(Video mVideo) {
//        String videoData = mVideo.getSimpleString();
//        LoggerUtil.log("video1: " + videoData);
//        long currentTime;
//        String existedVideoData = "";
//        for(ResentVideosSql videoSql : mDateBase.getAllVideos()){
//            existedVideoData = videoSql.getVideo();
//            if(existedVideoData.split(";;;")[0].equals(videoData.split(";;;")[0])){
//                String data = existedVideoData.split(";;;")[0] + existedVideoData.split(";;;")[1] + videoData.split(";;;")[0];
//                videoSql.setVideo(data);
//                currentTime = Long.parseLong(existedVideoData.split(";;;")[1]);
//                if(currentTime > 0 && mVideo.getCurrentTimePosition() == 0){
//                    videoData = data;
//                }
//                break;
//            }
//        }
//        return videoData;
//    }
//
//    private void showNavigationButtonsOnUiThread(){
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mMainControlLayout.setVisibility(View.VISIBLE);
//                mControlLayout.setVisibility(View.VISIBLE);
////                mBtnNext.setVisibility(View.VISIBLE);
////                mBtnPrev.setVisibility(View.VISIBLE);
//            }
//        });
//    }
//
//    String mMessageToToast = "";
//
//    void hiddenNavigationButton(){
//        mControlLayout.setVisibility(View.GONE);
////        mBtnNext.setVisibility(View.GONE);
////        mBtnPrev.setVisibility(View.GONE);
//    }
//
//    void showNavigationButton(){
//        if(videos != null && videos.size() > 0){
//            mControlLayout.setVisibility(View.VISIBLE);
////            mBtnNext.setVisibility(View.VISIBLE);
////            mBtnPrev.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        //To change body of implemented methods use File | Settings | File Templates.
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle state){
//        state.putInt("mVideoArrayStatus", mVideoArrayStatus);
//        super.onSaveInstanceState(state);
//    }
//}
