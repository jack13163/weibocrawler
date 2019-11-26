package weibo.utils;

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

public class HTML {

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
