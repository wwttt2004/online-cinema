package com.nmbb.oplayer.ui.helper.Utils;

import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class NonServerConnector {

    public String readDataGet(String url, String encoding) throws Exception {
        HttpGet get = new HttpGet(url);
        get.setHeader("Host", "5tv5.ru");
        get.setHeader("Connection", "keep-alive");
        get.setHeader("Accept", "*/*");
        get.setHeader("X-Requested-With", "XMLHttpRequest");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.62 Safari/537.36");
        get.setHeader("Referer", "http://5tv5.ru/");
        get.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        get.setHeader("Accept-Language", "en-US,en;q=0.8,ru;q=0.6");
        get.setHeader("Cookie", "id_ses=1377627930; otkuda=5tv5.ru; _lxretpopupignore=1; PHPSESSID=la5ndu338p1j78gt7cgv8lum13; zif=5; _ym_visorc=w; MarketGidStorage=%7B%220%22%3A%7B%22svspr%22%3A%22%22%2C%22svsds%22%3A112%2C%22TejndEEDj%22%3A%22MTM3Nzg3MTU3OTkzNDM2MjM0NDM%3D%22%7D%2C%22C36234%22%3A%7B%22page%22%3A1%2C%22time%22%3A1378388401443%7D%7D; __utma=94434712.1385363062.1377627925.1378373275.1378388402.22; __utmb=94434712.1.10.1378388402; __utmc=94434712; __utmz=94434712.1377627925.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none)");
        return readData(get, encoding);
    }
    public String readDataGet(String url, BasicHeader header) throws Exception {
        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/json");
        get.setHeader(header);
        return readData(get);
    }
    public String readDataPost(String url, List<NameValuePair> params, String encoding) throws Exception {
        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(params, encoding));
        return readData(httppost, encoding);
    }

    public String readDataPost(String url, List<NameValuePair> params, BasicHeader[] headers) throws Exception {
        return readDataPost(url, params, headers, "UTF-8");
    }

    public String readDataPost(String url, List<NameValuePair> params, BasicHeader[] headers, String encoding) throws Exception {
        HttpPost httppost = new HttpPost(url);
        httppost.setHeaders(headers);
        httppost.setEntity(new UrlEncodedFormEntity(params, encoding));
        return readData(httppost, encoding);
    }

    public String readData(HttpUriRequest uri, String encoding) throws Exception {
        return new String(readBytes(uri), encoding);
    }

    public String readData(HttpUriRequest uri) throws Exception {
        return readData(uri, "UTF-8");
    }

    public byte [] readDataImage(String url) throws Exception {
        Log.e("url", ":" + url);
        HttpGet get = new HttpGet(url);
        get.setHeader("Cache-Control", "max-age=86400");
        get.setHeader("Accept-Ranges", "bytes");
        get.setHeader("Content-Type", "image/png");
        return readBytes(get);
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

    public static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    public HttpClient getNewHttpClient() throws Exception {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
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
