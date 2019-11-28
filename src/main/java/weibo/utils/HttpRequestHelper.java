package weibo.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.cookie.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import org.apache.http.impl.cookie.BestMatchSpecFactory;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.BrowserCompatSpecFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class HttpRequestHelper {

    /**
     * 根据url和cookie请求结果
     * @param url
     * @param cookie
     * @return
     */
    public static String getResultOkHttpClient(String url, String cookie){

        String result ="";
        Response response = null;
        try {
            // 使用OkHttpClient发送请求
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cookie", cookie)
                    .addHeader("cache-control", "no-cache")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 根据url和cookie请求结果
     * @param url
     * @param cookie
     * @return
     */
    public static String getResultWebClient(String url, String cookie){
        String result ="";
        // 使用OkHttpClient发送请求
        WebClient webclient = new WebClient(BrowserVersion.CHROME);//此处可以设置代理
        webclient.getCookieManager().setCookiesEnabled(true);
        webclient.getCookieManager().addCookie(new com.gargoylesoftware.htmlunit.util.Cookie(".weibo.com","Cookie",cookie));
        webclient.getOptions().setCssEnabled(false);
        webclient.getOptions().setUseInsecureSSL(true);
        webclient.getOptions().setJavaScriptEnabled(true);
        webclient.getOptions().setThrowExceptionOnScriptError(false);
        webclient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webclient.setAjaxController(new NicelyResynchronizingAjaxController());
        webclient.getOptions().setActiveXNative(false);
        webclient.getOptions().setDoNotTrackEnabled(false);
        webclient.getOptions().setTimeout(60000);
        webclient.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");

        HtmlPage page = null;
        try {
            page = webclient.getPage(url);
            Thread.sleep(3000);//这个线程的等待 因为js加载需要时间的
            result = page.asXml();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
