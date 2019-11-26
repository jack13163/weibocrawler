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

    public static String[] getHTML(String url, JTextArea jta) throws ClientProtocolException, IOException {
        String[] html = new String[2];
        html[0] = "null";
        html[1] = "null";
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)// ����socket��ʱʱ��
                .setConnectTimeout(2000)// ����connect��ʱʱ��
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            System.out.println(response.getStatusLine().getStatusCode());

            html[0] = String.valueOf(response.getStatusLine().getStatusCode());
            html[1] = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            jta.append("****���ӳ�ʱ****" + "\r\n");
        }

        return html;
    }

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
        WebClient webclient = new WebClient(BrowserVersion.CHROME);
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

    /**
     * ͨ��html��������htmlҳ��
     *
     * @param targetURL
     * @param hostName
     * @param port
     * @param jta
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static String getHTMLbyProxy(String targetURL, String hostName, int port, JTextArea jta) throws ClientProtocolException, IOException {
        HttpHost proxy = new HttpHost(hostName, port);
        String html = "null";
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(2000)// ����socket��ʱʱ��
                .setConnectTimeout(2000)// ����connect��ʱʱ��
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setRoutePlanner(routePlanner)
                .setDefaultRequestConfig(requestConfig).build();
        HttpGet httpGet = new HttpGet(targetURL);
        httpGet.setHeader("User-Agent", "spider");// �������ͷ��
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);//�Ƿ����ӳ�ʱ������һ�о��ܾ���
            int statusCode = response.getStatusLine().getStatusCode();
            // System.out.println(response.getStatusLine().getStatusCode());
            if (statusCode == HttpStatus.SC_OK) {
                html = EntityUtils.toString(response.getEntity(), "utf-8");
            }
            response.close();
            // System.out.println(html);//��ӡ���ص�html
        } catch (IOException e) {
            System.out.println("****Connection time out****");
            jta.append("****���ӳ�ʱ****" + "\r\n");
        }

        return html;
    }

    /**
     * ���ô�����ȥ��ҳ����
     *
     * @param url
     * @param hostName
     * @param port
     * @param jta
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String getHTML(String url, String hostName, int port, JTextArea jta) throws URISyntaxException, ClientProtocolException, IOException {
        // �����û��Զ���cookie���ԣ�ֻ��ʹcookie rejected�ı������֣��˴�����Ȼ����
        HttpHost proxy = new HttpHost(hostName, port);
        DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
        CookieSpecProvider easySpecProvider = new CookieSpecProvider() {
            public CookieSpec create(HttpContext context) {
                return new BrowserCompatSpec() {
                    @Override
                    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
                        // Oh, I am easy
                    }
                };
            }
        };
        Registry<CookieSpecProvider> r = RegistryBuilder
                .<CookieSpecProvider>create()
                .register(CookieSpecs.BEST_MATCH, new BestMatchSpecFactory())
                .register(CookieSpecs.BROWSER_COMPATIBILITY, new BrowserCompatSpecFactory())
                .register("easy", easySpecProvider).build();
        RequestConfig requestConfig = RequestConfig.custom()
                .setCookieSpec("easy").setSocketTimeout(4000)// ����socket��ʱʱ��
                .setConnectTimeout(4000)// ����connect��ʱʱ��
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultCookieSpecRegistry(r).setRoutePlanner(routePlanner)
                .setDefaultRequestConfig(requestConfig).build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        // ����header����������
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.81 Safari/537.36");
        String html = "null";
        try {
            // ��ȡ���ص�html
            CloseableHttpResponse response = httpClient.execute(httpGet);
            html = EntityUtils.toString(response.getEntity(), "gb2312");

            //��ӡ���ص�html
            System.out.println(html);
        } catch (IOException e) {
            System.out.println("****Connection time out****");
            jta.append("****���ӳ�ʱ****" + "\r\n");
        }
        return html;
    }
}
