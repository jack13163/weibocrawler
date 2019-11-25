package WeiboCrawler2.Xici;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.*;

public class XiciProxyIP {


    private static final String MY_IP_API = "https://www.ipip.net/ip.html";

    // 获取当前ip地址，判断是否代理成功
    public static String getMyIp() {
        try {
            String html = HttpUtils.getResponseContent(MY_IP_API);

            Document doc = Jsoup.parse(html);
            Element element = doc.select("div.tableNormal").first();

            Element ele = element.select("table").select("td").get(1);

            String ip = element.select("a").text();

            // System.out.println(ip);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检测代理ip是否有效
     *
     * @param ipBean
     * @return
     */
    public static boolean isValid(IPBean ipBean) {
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ipBean.getIp(), ipBean.getPort()));
        try {
            URLConnection httpCon = new URL("https://www.baidu.com/").openConnection(proxy);
            httpCon.setConnectTimeout(5000);
            httpCon.setReadTimeout(5000);
            int code = ((HttpURLConnection) httpCon).getResponseCode();
            System.out.println(code);
            return code == 200;
        } catch (IOException e) {
            // 无法通过代理服务器访问百度，则说明代理IP无效
            System.out.println("IP无效");
        }
        return false;
    }
}
