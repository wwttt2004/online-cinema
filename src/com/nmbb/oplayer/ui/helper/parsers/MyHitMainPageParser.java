package com.nmbb.oplayer.ui.helper.parsers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import com.nmbb.oplayer.ui.MainFragmentActivity;
import com.nmbb.oplayer.ui.products.Category;
import com.nmbb.oplayer.ui.products.Video;
import com.nmbb.oplayer.ui.helper.constants.Constants;
import com.nmbb.oplayer.ui.exeptions.ParserParamsNoFoundException;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.webloader.WebLoader;
//import com.nmbb.oplayer.ui.sql.FavoriteVideoDataSource;
//import com.nmbb.oplayer.ui.sql.ResentVideosSql;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Maxim
 * Date: 11.01.13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */

public class MyHitMainPageParser {

    private static String mResponse;
    private static ArrayList<Object> mCategories = null;
    private static ArrayList<Object> mVideos = new ArrayList<Object>();
    private static ArrayList<Object> mResentVideos = new ArrayList<Object>();
    private static ArrayList<Object> mFavoriteVideos = new ArrayList<Object>();
    private static boolean mIsSearchRequest = false;
    public static MainFragmentActivity mMainFragmentActivity;
    public static int mCurrentDownloadedPages = 1;
    public static String mCurrentRequestLink = Constants.FIRST_REQUEST_LINK;

    public static ArrayList<Object> getFavoriteVideos() {
        Video video;
        for(Object o : mFavoriteVideos){
            video = (Video) o;
            mMainFragmentActivity.setFavoriteFlag(video);
            mMainFragmentActivity.setCurrentTimePosition(video);
        }
        return mFavoriteVideos;
    }

    public static void setFavoriteVideos(ArrayList<Object> mFavoriteVideos) {
        MyHitMainPageParser.mFavoriteVideos.addAll(mFavoriteVideos);
    }

    public static ArrayList<Object> getResentVideos() {
        Video video;
        for(Object o : mResentVideos){
            video = (Video) o;
            mMainFragmentActivity.setFavoriteFlag(video);
            mMainFragmentActivity.setCurrentTimePosition(video);
        }
        return mResentVideos;
    }

    public static void setResentVideos(ArrayList<Object> mResentVideos) {
        MyHitMainPageParser.mResentVideos.addAll(mResentVideos);
    }

    public static boolean isSearchRequest() {
        return mIsSearchRequest;
    }

    public static void setIsSearchRequest(boolean isSearch) {
        mIsSearchRequest = isSearch;
    }

    private static int mStart = -1;

    private static String mLastLink = "";

    private static Context mContext;
    private static Bitmap mIcon = null;

    public MyHitMainPageParser(Context context){
        mContext = context;
        new WebLoader(mContext);
    }

    public static String getLastLink() {
        return mLastLink;
    }

    static int mFirstPage = 1;

    static public int getFirstPage() {
        return mFirstPage;
    }

    static public void setFirstPage(int firstPage) {
        mFirstPage = firstPage;
    }

    public static void setVideoArrayList(String link, ArrayList<Object> videos) throws Exception {
        setVideoArrayList(link, videos, true, null);
    }

    private static String mFullResponse;

    public static void setVideoArrayList(String link, ArrayList<Object> videos, boolean isRewriteVideos) throws Exception {
        setVideoArrayList(link, videos, isRewriteVideos, null);
    }

    public static void setVideoArrayList(String link, ArrayList<Object> videos, boolean isRewriteVideos, String searchQuery) throws Exception {
        mIsSearchRequest = false;
        int progress = 0;
        Intent intent;
        intent = new Intent(Constants.INTENT_DIALOG_CHANGE_PROGRESS);
        intent.putExtra(Constants.EXTRA_DIALOG_PROGRESS, progress);
        mContext.sendBroadcast(intent);

        if(mLastLink.length() == 0)
            mLastLink = link;
        LoggerUtil.log(link);
        videos.clear();
        mFullResponse = mResponse = (searchQuery == null ? WebLoader.load(link) : WebLoader.loadSearchRequest(link, searchQuery));
        Video video;
        mStart = mResponse.indexOf("<table><tbody><tr class=today>");

        while (mStart != -1){
            mResponse = mResponse.substring(mStart);
            video = getSetUppedVideoObject();
            if(video == null)
                continue;
            videos.add(video);
            progress += Constants.PROGRESS;
            progress = progress > 100 ? 100 : progress;
            intent.putExtra(Constants.EXTRA_DIALOG_PROGRESS, progress);
            mContext.sendBroadcast(intent);

            mStart = mResponse.indexOf("<table><tbody><tr class=today>");
        }

        mStart = -1;
        if(isRewriteVideos){
            mVideos.clear();
        }
        mVideos.addAll(videos);
        videos.clear();
        intent.putExtra(Constants.EXTRA_DIALOG_PROGRESS, progress);
        if(videos.size() == 0){
            videos.addAll(getVideos());
        }
        mContext.sendBroadcast(intent);
    }

    private static Video getSetUppedVideoObject() throws ParserParamsNoFoundException {
        Video video = new Video();

        String html = "<html><head></head><body>" +

                getField("", false, "<table><tbody><tr class=today>", false, "</td></tr></tbody></table>", "", true).
                        replace("=smallposters", "=http://5tv5.ru/smallposters").
                        replace("<img src=1_files/favadd_16.gif border=0> В закладки</a>", "") +
                "</td></tr></tbody></table></body></html>";

        mProductDetailsResponse = html;
        String firstTd = getField1("", false, "<td valign=top width=1%>", false, "</td>", "", true);
        String ratings = getField1("", false, "<div", false, "</div>", "", true);
        html = html.replace(ratings, "").
                replace("</div>", "").
                replace("<table>", "<table width=\"100%\">").
                replace("<tr><td colspan=3><img src=1_files/hr.gif width=627 height=1></td></tr>", "").
                replace(firstTd, "").replace("</td>", "").
                replace("<td style=font-size:8pt;valign=top width=10%>", "<td width=\"10%\" style=\"vertical-align: top;\">");

        LoggerUtil.log("HTML: ratings: " + ratings);
        video.setHtml(html);

        mProductDetailsResponse = video.getHtml();
        video.setUrl(getUrl());
        video.setDescription(getDescription());
        mProductDetailsResponse = video.getHtml();
        video.setName(getName());
        LoggerUtil.log("video html: " + video.getHtml());

        return video;
    }

    private static String getDescription() throws ParserParamsNoFoundException {
        return getField1("", false, "Описание:", false, "</p>", "");
    }

    private static String getName() throws ParserParamsNoFoundException {
        return getField1("", false, "<img alt=\"", true, "\"", "");
    }

    private static String getUrl() throws ParserParamsNoFoundException {
        return convertUrl(getField1("", false, "href=\"", true, "\" >", "", true));
    }

    private static String convertUrl(String url){
        byte [] bytes = url.getBytes();
        ArrayList<Byte> newBytes = new ArrayList<Byte>();

        byte[] space = new byte[]{37, 50, 48};

        for (byte aByte : bytes) {
            if ((aByte + "").contains("-62")) {
                newBytes.add(space[0]);
                continue;
            } else if ((aByte + "").contains("-96")) {
                newBytes.add(space[1]);
                newBytes.add(space[2]);
                continue;
            }
            newBytes.add(aByte);
        }

        bytes = new byte[newBytes.size()];

        for(int currentByteIndex = 0; currentByteIndex < bytes.length; currentByteIndex ++){
            bytes[currentByteIndex] = newBytes.get(currentByteIndex);
        }
        return new String(bytes);
    }

    private static String getField(String param1, boolean isAddLengthOfParam1, String param2, boolean isAddLengthOfParam2, String param3, String param4) throws ParserParamsNoFoundException {
        return getField(param1, isAddLengthOfParam1, param2, isAddLengthOfParam2, param3, param4, false);
    }

    private static String getField(String param1, boolean isAddLengthOfParam1, String param2, boolean isAddLengthOfParam2, String param3, String param4, boolean returnHtml) throws ParserParamsNoFoundException {
        int start;
        if(mStart != -1){
            if(param4.length() > 0)
                mStart = mResponse.indexOf(param4) + param4.length();
            else
                mStart = -1;
        }
        if(param1.length() > 0){
            start = mResponse.indexOf(param1);
            if(start < 0 || (mStart != -1 && mStart != (-1 + param4.length()) && start > mStart)){
                return "";
            }
            if (isAddLengthOfParam1){
                start = start + param1.length();
            }
            mResponse = mResponse.substring(start);
        }

        if(param2.length() == 0)
            start = 0;
        else
            start = mResponse.indexOf(param2);
        if(start < 0 || (mStart != -1 && start > mStart)){
            return "";
        }
        if (isAddLengthOfParam2){
            start = start + param2.length();
        }
        mResponse = mResponse.substring(start);

        int end = mResponse.indexOf(param3);

        String htmlString = mResponse.substring(0, end).trim();

        mResponse = mResponse.substring(end);

        return returnHtml ? htmlString : Html.fromHtml(htmlString).toString();
    }

    private static String getField1(String param1, boolean isAddLengthOfParam1, String param2, boolean isAddLengthOfParam2, String param3, String param4) throws ParserParamsNoFoundException {
        return getField1(param1, isAddLengthOfParam1, param2, isAddLengthOfParam2, param3, param4, false);
    }

    private static String getField1(String param1, boolean isAddLengthOfParam1, String param2, boolean isAddLengthOfParam2, String param3, String param4, boolean returnHtml) throws ParserParamsNoFoundException {
        int start;
        if(mStart != -1){
            if(param4.length() > 0)
                mStart = mProductDetailsResponse.indexOf(param4) + param4.length();
            else
                mStart = -1;
        }
        if(param1.length() > 0){
            start = mProductDetailsResponse.indexOf(param1);
            if(start < 0 || (mStart != -1 && mStart != (-1 + param4.length()) && start > mStart)){
                return "";
            }
            if (isAddLengthOfParam1){
                start = start + param1.length();
            }
            mProductDetailsResponse = mProductDetailsResponse.substring(start);
        }

        start = mProductDetailsResponse.indexOf(param2);
        if(start < 0 || (mStart != -1 && start > mStart)){
            return "";
        }
        if (isAddLengthOfParam2){
            start = start + param2.length();
        }
        mProductDetailsResponse = mProductDetailsResponse.substring(start);

        String htmlString = mProductDetailsResponse.substring(0, mProductDetailsResponse.indexOf(param3)).trim();

        return returnHtml ? htmlString : Html.fromHtml(htmlString).toString();
    }


    public static String getLinkToFlv(String link)  throws ParserParamsNoFoundException {
        mResponse = null;
        mResponse = WebLoader.load(link);
        if(mResponse != null)
            return getUrlToFlvVideo();
        return null;
    }

    public static ArrayList<Object> getCategories(){
        return mCategories;
    }

    public static ArrayList<Object> getVideos(){
        Video video;
        for(Object o : mVideos){
            video = (Video) o;
            mMainFragmentActivity.setFavoriteFlag(video);
            mMainFragmentActivity.setCurrentTimePosition(video);
        }
        return mVideos;
    }

    private static String getUrlToFlvVideo() {
        try {
            mStart = -1;
            return getField("scaling: 'fit',", true, "url: '", true, "'", "<td style=font-size:8pt;valign=top width=10%><a target=_blank href=");
        } catch (ParserParamsNoFoundException e) {
            return null;
        }
    }

    public static void setLastLink(String value) {
        mLastLink = value;
    }

    static String mProductDetailsResponse;

    public static void getMoreInformationAboutVideo(Video video) throws ParserParamsNoFoundException {
        video.setVideoUrl("");
        String response = WebLoader.load(video.getUrl());

        mProductDetailsResponse = response;
        if(video.getFullDescription().length() == 0) {
            video.setFullDescription(getFullDescription());
            mProductDetailsResponse = response;
            video.setHtmlVideoDetails(getHtmlVideoDetails(video));
            LoggerUtil.log("HTML: " + video.getHtmlVideoDetails());

            LoggerUtil.log("param product details: " + video.getFullDescription());
        }

        mProductDetailsResponse = response;
        video.setVideoHttpUrl(getVideoHttpUrl());
        mProductDetailsResponse = response;
        video.setVideoUrl(getVideoUrl());
        LoggerUtil.log("param product details: " + video.getVideoHttpUrl());
        LoggerUtil.log("param product details: " + video.getVideoUrl());
    }

    private static String getHtmlVideoDetails(Video video) throws ParserParamsNoFoundException {

        String style = "<style type=\"text/css\">\n" +
                "h2 {\n" +
                "\tfont-family: Georgia, \"Times New Roman\", Times, serif;\n" +
                "\tfont-size: 12px;\n" +
                "\tfont-weight: normal;\n" +
                "\tcolor: #000000;\n" +
                "\tmargin: 0 0 1px 0;\n" +
                "}</style>";
        String title = getField1("", false, "<p style=\"margin-top: 1px; margin-bottom: 5px; font-size: 1.4em; font-weight: bold;\">", false, "</sup> </p></h1>", "", true) +
                "</sup> </p></h1>";

        String body = getField1("", false,
                "<td align=\"center\" valign=\"top\" width=\"10%\"><div id=\"cover\">", false,
                "<tr><td><h2>Закачек:</td><td><h2>", "", true);

        String htmlResult = "<html><head></head><body><table class=\"film\" width=\"100%\">\n" +
                "<tbody><tr>" +

                title +

                body +

                "</tbody></table>\n" +
                "\n" +
                "\n" +
                "<br><br></td></tr>\n" +
                "  <style type=\"text/css\">\n" +
                "      .la1{overflow: visible;width: 70px;}\n" +
                "      .la2{overflow: visible;width: 210px;} \n" +
                "\t  .la3{overflow: visible;width: 70px;}  \n" +
                "\t  .la4{overflow: visible;width: 89px;}  \n" +
                "\t  .la5{overflow: visible;width: 70px;}\n" +
                "  </style>\n" +
                "\t</tbody></table>" +

                "<h2>" +
                video.getFullDescription() +
                "</h2>" +

                style +
                "</body></html>";

        mProductDetailsResponse = htmlResult;
        String rating = getField1("", false,
                "<div style=\"float: right;  font-size: 8pt; margin-left: 5px;\">", false,
                "<p style=\"margin: 1px; color: gray;line-height:14px;\">", "", true);

        mProductDetailsResponse = htmlResult;
        String sameFilms = getField1("", false,
                "<tr><td valign=\"top\"><h2>", false,
                "</td></tr>", "", true) + "</td></tr>";

        return htmlResult.replace(rating, "").replace(sameFilms, "");
    }

    private static String getVideoUrl() throws ParserParamsNoFoundException {
        return getField1("", false, "rtmp:", false, "&", "");
    }

    private static String getVideoHttpUrl() throws ParserParamsNoFoundException {
        return getField1("", false, "video_url=", true, "&", "");
    }

    private static String getFullDescription() throws ParserParamsNoFoundException {
        return getField1("", false, "Описание:", false, "\"/>", "").replaceAll("\n", "");
    }

    public static void setCategoriesCategoriesList(ArrayList<Object> mCategories) {
        if(mCategories.size() > 0) return;
        mResponse = mFullResponse;
        int index = mResponse.indexOf("class=\"current\"");
        if(index == -1) return;
        mResponse = mResponse.substring(index);
        index = mResponse.indexOf("</ul>");
        if(index == -1) return;
        mResponse = mResponse.substring(0, index);
        LoggerUtil.log("Response: " + mResponse);

        String[] splitted = mResponse.split("\\n");
        String categoryName;
        String categoryUrl;
        for (String categoryStr : splitted){
            categoryStr = categoryStr.trim();
            if(!categoryStr.startsWith("<li><a  href=")) {
                if(categoryStr.startsWith("class=\"current\"")) {
                    index = categoryStr.indexOf(">");
                    if(index == -1) continue;
                    categoryStr = categoryStr.substring(index +1);
                    index = categoryStr.indexOf(" (");
                    if(index == -1) continue;
                    categoryName = categoryStr.substring(0, index);
                    Category category = new Category();
                    category.setUrl("pol=");
                    category.setCategoryName(categoryName);
                    mCategories.add(category);
                }
                continue;
            }
            categoryStr = categoryStr.replace("<li><a  href=http://5tv5.ru/index.php?", "");
            index = categoryStr.indexOf(">");
            if(index == -1) continue;
            categoryUrl = categoryStr.substring(0, index);
            categoryStr = categoryStr.substring(index +1);
            index = categoryStr.indexOf(" (");
            if(index == -1) continue;

            categoryName = categoryStr.substring(0, index);
//            LoggerUtil.log(" ####################### " + categoryName + " | " + categoryName.toLowerCase().contains("сериал"));

            if(categoryName.toLowerCase().contains("сериал")) continue;

            Category category = new Category();
            category.setUrl(categoryUrl);
            category.setCategoryName(categoryName);
            mCategories.add(category);
        }
    }
}
