package com.nmbb.oplayer.ui.adapter;

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.*;
import com.nmbb.oplayer.R;
import com.nmbb.oplayer.ui.MainFragmentActivity;
import com.nmbb.oplayer.ui.MovieDetailsActivity;
import com.nmbb.oplayer.ui.VideoPlayerActivity;
import com.nmbb.oplayer.ui.adapter.popap.ActionItem;
import com.nmbb.oplayer.ui.exeptions.ParserParamsNoFoundException;
import com.nmbb.oplayer.ui.helper.constants.Constants;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.parsers.MyHitMainPageParser;
import com.nmbb.oplayer.ui.adapter.popap.QuickAction;
import com.nmbb.oplayer.ui.products.*;

public class BoxAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    Intent mReloadUiIntent;
    Intent mDismissDialog;

    ArrayList<Object> objects;
    private static ArrayList<Object> videoObjects = new ArrayList<Object>(){};
    private boolean mIsLongClick = false;
    MainFragmentActivity mMainFragmentActivity;
    private QuickAction mQuickAction;

    public BoxAdapter(Context context, ArrayList<Object> videos, MainFragmentActivity activity) {
        mMainFragmentActivity = activity;
        mReloadUiIntent = new Intent(Constants.INTENT_RELOAD_UI);
        mDismissDialog = new Intent(Constants.INTENT_DISMISS_DIALOG_AND_RELOAD_UI);
        ctx = context;
        objects = videos;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    boolean mIsAlreadyClicked = false;

    /**
     * ��������� �������� �� sd
     */
    private static final int VIDEO_FAVORITE = 2;

    /**
     * ��������� � ���. ����
     */
    private static final int VIDEO_ONLINE = 3;


    int mCurrentPosition = 0;

    private void setParamsQuickAction(final int position, final ImageView imageView) {
        mCurrentPosition = position;
        final Object o = getProduct(position);
        if(o instanceof Video){
            final Video video = (Video)o;
            LoggerUtil.log("adapter: " + video.getName());
            ActionItem favoriteItem;

            if(video.isFavorite()){
                favoriteItem = new ActionItem(VIDEO_FAVORITE, "", mMainFragmentActivity.getResources()
                        .getDrawable(R.drawable.added_bookmark));
            } else {
                favoriteItem = new ActionItem(VIDEO_FAVORITE, "", mMainFragmentActivity.getResources()
                        .getDrawable(R.drawable.removed_bookmark));
            }

            ActionItem filmOnlineItem = new ActionItem(VIDEO_ONLINE, "", mMainFragmentActivity.getResources()
                    .getDrawable(R.drawable.play_video));
            mQuickAction = new QuickAction(mMainFragmentActivity, QuickAction.HORIZONTAL);
            mQuickAction.addActionItem(favoriteItem);
            mQuickAction.addActionItem(filmOnlineItem);

            mQuickAction
                    .setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                        @Override
                        public void onItemClick(QuickAction quickAction, int pos,
                                                int actionId) {
                            if (actionId == VIDEO_FAVORITE) {
                                if(video.isFavorite()){
                                    video.setIsFavorite(false);
                                    mMainFragmentActivity.addRemoveVideoAtDataBase(false, video);
                                } else {
                                    video.setIsFavorite(true);
                                    mMainFragmentActivity.addRemoveVideoAtDataBase(true, video);
                                }

                                if((video).isFavorite()){
                                    imageView.setImageResource(R.drawable.added_bookmark);
                                } else{
                                    imageView.setImageResource(R.drawable.removed_bookmark);
                                }
                            } else {
                                if (actionId == VIDEO_ONLINE)
                                    LoggerUtil.log("position pos: " + mCurrentPosition);

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            mMainFragmentActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mMainFragmentActivity.onPreExecute();
                                                }
                                            });
                                            MyHitMainPageParser.getMoreInformationAboutVideo(video);
                                            String link = video.getVideoUrl();// = MyHitMainPageParser.getLinkToFlv(video.getUrl() + "/online");
                                            String linkHttp = video.getVideoHttpUrl();
                                            if ((link).length() > 0 || (linkHttp).length() > 0) {
                                                if(objects.size() > 0){
                                                    videoObjects.addAll(objects);
                                                }
                                                Intent intent = new Intent(ctx, VideoPlayerActivity.class);
                                                intent.putExtra("path", link);
                                                intent.putExtra("pathHttp", linkHttp);
                                                intent.putExtra("position", mCurrentPosition + "");
                                                ctx.startActivity(intent);
                                            } else {

                                                mMainFragmentActivity.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mMainFragmentActivity.onPostExecute();
                                                    }
                                                });
                                                ctx.sendBroadcast(new Intent(Constants.INTENT_DISMISS_DIALOG));
                                                Toast.makeText(ctx,"Проверьте соединение с интернет и повторите попытку.", Toast.LENGTH_LONG).show();
                                            }
                                        } catch (ParserParamsNoFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            }
                        }
                    });
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View view;
        view = lInflater.inflate(R.layout.listitem, parent, false);
        try{
            final Object o = getProduct(position);
            final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.mainlinearLayout);
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.relative_layout);
            final ProgressBar progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
            TextView filterName = (TextView) view.findViewById(R.id.filter_name);
            filterName.setVisibility(View.GONE);

            final WebView webView = (WebView) view.findViewById(R.id.webview);
            progressBar.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);

            if(o instanceof Video){
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        return true;
                    }
                });
                final Video v = (Video)o;
                relativeLayout.setVisibility(View.VISIBLE);
                webView.loadDataWithBaseURL(null, v.getHtml(), "text/html", "utf-8", null);
            } else if( o instanceof  Category) {
                relativeLayout.setVisibility(View.GONE);
                Category category = (Category) o;
                String selectedCategory = "";
                if(category.getCategoryName().equals(mMainFragmentActivity.mTytle.getText())){
                    selectedCategory = "--> ";
                }
                filterName.setText(selectedCategory + category.getCategoryName());
                filterName.setVisibility(View.VISIBLE);
                linearLayout.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.WRAP_CONTENT, android.widget.AbsListView.LayoutParams.WRAP_CONTENT));
                notifyDataSetChanged();
            }  else if( o instanceof  SubCategory) {
                relativeLayout.setVisibility(View.GONE);
                SubCategory category = (SubCategory) o;
                filterName.setText(category.getSubCategoryName());
                filterName.setVisibility(View.VISIBLE);
                linearLayout.setLayoutParams(new android.widget.AbsListView.LayoutParams(android.widget.AbsListView.LayoutParams.WRAP_CONTENT, android.widget.AbsListView.LayoutParams.WRAP_CONTENT));
            }

            linearLayout.setTag(position);

            webView.setTag(position);

            final ImageView view_popup = (ImageView) view.findViewById(R.id.view_popup_dialog);
            view_popup.setTag(position);

            if(o instanceof Video){
                if(((Video)o).isFavorite()){
                    view_popup.setImageResource(R.drawable.added_bookmark);
                } else{
                    view_popup.setImageResource(R.drawable.removed_bookmark);
                }
            }

            view_popup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setParamsQuickAction((Integer)v.getTag(), view_popup);
                    mQuickAction.show(v);
                    mQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);

                }
            });

            webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            webView.setHorizontalScrollbarOverlay(false);
            webView.setVerticalScrollbarOverlay(false);


            View.OnTouchListener listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try {
                        LoggerUtil.log("event : " + event.getAction());
                        if (event.getAction() == 0 && !mIsAlreadyClicked) {
                            mIsAlreadyClicked = true;
                        } else if (event.getAction() == 1 && mIsAlreadyClicked) {
                            mIsAlreadyClicked = false;
                            videoObjects = objects;
                            Object o = getProduct((Integer) v.getTag());
                            if (o instanceof Video && MainFragmentActivity.mGridView.isEnabled()) {
                                if (!mIsLongClick) {
                                    getProduct((Integer) v.getTag());
                                    LoggerUtil.log("clicked item " + v.getTag());
                                    Intent intent = new Intent(ctx, MovieDetailsActivity.class);
                                    videoObjects = objects;
                                    intent.putExtra("position", v.getTag().toString());
                                    ctx.startActivity(intent);
                                } else {
                                    mIsLongClick = false;
                                }
                            } else if (o instanceof Category) {
                                final Category category = (Category) o;
                                MyHitMainPageParser.mCurrentDownloadedPages = 1;
                                MyHitMainPageParser.mCurrentRequestLink = Constants.FIRST_REQUEST_LINK.replace("pol=", category.getUrl());
                                mMainFragmentActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mMainFragmentActivity.mTytle.setText(category.getCategoryName());
                                        mMainFragmentActivity.updateAdapter();
                                    }
                                });
                            } else if(o instanceof SubCategory){
                                final SubCategory subCategory = (SubCategory) o;
                                String searchQuery = subCategory.getSubCategoryName().split("\\|")[0].trim();
                                mMainFragmentActivity.mAutoComplete.setText(searchQuery);
                                mMainFragmentActivity.fillSearchDataInBackGround("http://5tv5.ru/index.php", searchQuery);
                            }
                            mIsAlreadyClicked = false;
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    return false;
                }
            };


            webView.setOnTouchListener(listener);

            linearLayout.setOnTouchListener(listener);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoggerUtil.log("tag : " + v.getTag());
                }
            });
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return view;
    }

    public ArrayList<Object> getCategoriesArray(){
        return mNewObjects;
    }

    private ArrayList<Object> mNewObjects = new ArrayList<Object>(){};

    public static Object getVideoObjectById(int position){
        return videoObjects.get(position);
    }

    Object getProduct(int position) {
        return getItem(position);
    }
}