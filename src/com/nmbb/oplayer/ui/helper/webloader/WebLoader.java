package com.nmbb.oplayer.ui.helper.webloader;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.nmbb.oplayer.ui.helper.constants.Constants;
import com.nmbb.oplayer.ui.helper.Utils.LoggerUtil;
import com.nmbb.oplayer.ui.helper.Utils.NonServerConnector;
import com.nmbb.oplayer.ui.search.SearchKeywordsConverter;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class WebLoader {

    private static String mUrl;
    private static String mResult = null;
    private static boolean mIsResponse = false;
    private static Context mContext;

    public WebLoader(Context context){
        mContext = context;
    }

    public static String load(String url) {
        return load(url, "windows-1251");
    }

    public static String load(String url, String encoding) {
        url = encoding.equalsIgnoreCase("utf-8") ? SearchKeywordsConverter.getConvertedKeyword(url) : SearchKeywordsConverter.getConvertedKeywordForLinks(url);
        LoggerUtil.log("AUTOCOMPLETE RESULT: " + url);
        mResult = null;
        mUrl = url;
        mIsResponse = false;
        runInitTestInBackGround(encoding);
        waitForResponse();

        LoggerUtil.log("mResult: " + mResult);
        if(mResult != null && mResult.contains("Caught exception : You have an error in your SQL syntax;")){
            mResult = null;
        }
        if(mResult == null){
            Intent intent = new Intent(Constants.INTENT_DISMISS_DIALOG);
            mContext.sendBroadcast(intent);
        }
        return mResult;
    }

    public static String loadSearchRequest(String url, String searchQuery){
        return loadSearchRequest(url, searchQuery, "windows-1251");
    }

    public static String loadSearchRequest(String url, String searchQuery, String encoding){
        LoggerUtil.log("SEARCH RESULT: URL: " + url);
        mResult = null;
        mUrl = url;
        mIsResponse = false;
        runInitTestPostRequestInBackGround(encoding, searchQuery);
        waitForResponse();

        LoggerUtil.log("SEARCH RESULT: " + mResult);
        if(mResult != null && mResult.contains("Caught exception : You have an error in your SQL syntax;")){
            mResult = null;
        }
        if(mResult == null){
            Intent intent = new Intent(Constants.INTENT_DISMISS_DIALOG);
            mContext.sendBroadcast(intent);
        }
        return mResult;
    }

    private static void waitForResponse() {
        while(!mIsResponse)  {
            LoggerUtil.log("############# wait for response ############");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for(String str : mResult.split("\\n")){
            LoggerUtil.log("SEARCH RESULT:" + str);
        }

        LoggerUtil.log("SEARCH RESULT:" + mResult.indexOf("Авиакатастрофа внезапно разрушает счстливую"));
    }

    private static void runInitTestInBackGround(final String encoding){
        Thread thread = new Thread() {
            @Override
            public void run(){
                NonServerConnector connector = new NonServerConnector();

                try {
                    mResult = connector.readDataGet(mUrl, encoding);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mIsResponse = true;
            }

        };
        thread.run();
    }

    private static void runInitTestPostRequestInBackGround(final String encoding, final String searchQuery){
        Thread thread = new Thread() {
            @Override
            public void run(){
                NonServerConnector connector = new NonServerConnector();
                String convertedSearchQuery = SearchKeywordsConverter.getConvertedKeywordForLinks(searchQuery);
                LoggerUtil.log("SEARCH RESULT: QUERY:" + convertedSearchQuery);
                try {
                    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("whatresult", "1"));
                    params.add(new BasicNameValuePair("ter", "undefined"));
                    params.add(new BasicNameValuePair("namerus", convertedSearchQuery));
                    BasicHeader[] headers = new BasicHeader[]{
                            new BasicHeader("Host", "5tv5.ru"),
                            new BasicHeader("Connection", "keep-alive"),
                            new BasicHeader("Content-Length", "31"),
                            new BasicHeader("Cache-Control", "max-age=0"),
                            new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"),
                            new BasicHeader("Origin", "http://5tv5.ru"),
                            new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36"),
                            new BasicHeader("Content-Type", "application/x-www-form-urlencoded"),
                            new BasicHeader("Referer", "http://5tv5.ru/index.php"),
                            new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"),
                            new BasicHeader("Accept-Language", "en-US,en;q=0.8,ru;q=0.6"),
                            new BasicHeader("Cookie", "id_ses=1377627930; otkuda=5tv5.ru; _lxretpopupignore=1; zif=7; __utma=94434712.1385363062.1377627925.1378717671.1378723027.27; __utmb=94434712.6.10.1378723027; __utmc=94434712; __utmz=94434712.1377627925.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); _ym_visorc=w; MarketGidStorage=%7B%220%22%3A%7B%22svspr%22%3A%22%22%2C%22svsds%22%3A63%2C%22TejndEEDj%22%3A%22MTM3ODcxNzY3MDg0MTM2MjM0ODI1%22%7D%2C%22C36234%22%3A%7B%22page%22%3A6%2C%22time%22%3A1378723576977%7D%7D"),
                    };
                    mResult = connector.readDataPost(mUrl, params, encoding);

                    LoggerUtil.log("SEARCH RESULT: +++ " + mResult);
                    for(String str : mResult.split("\\n")){
                        LoggerUtil.log("SEARCH RESULT: " + str);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                mIsResponse = true;
            }

        };
        thread.run();
    }

    public static final String ISO_8859_1_CODEPAGE = "ISO-8859-1";
    public static final String UTF_8_CODEPAGE = "UTF-8";
    public static final String WINDOWS_1251_CODEPAGE = "Windows-1251";

    public static String convertText(String text, String inCodepage, String outCodepage) {

        String outText = "";
        try {
            byte[] bytes = TextUtils.isEmpty(inCodepage) ? text.getBytes() : text.getBytes(inCodepage);
            outText = new String(bytes, outCodepage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return outText;
    }

    public static String toISO_8859_1(String value) {
        return convertText(value, ISO_8859_1_CODEPAGE, UTF_8_CODEPAGE);
    }

    public static String castom(String value) {
        return convertText(value, UTF_8_CODEPAGE, WINDOWS_1251_CODEPAGE);
    }

    public static String toWindows1251(String text) {
        String converted = convertText(text,
                ISO_8859_1_CODEPAGE, WINDOWS_1251_CODEPAGE);
        return converted;
    }
}
