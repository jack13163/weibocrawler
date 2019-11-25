package WeiboCrawler2.Xici;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.*;

public class XiciProxyIP {


    private static final String MY_IP_API = "https://www.ipip.net/ip.html";

    // ��ȡ��ǰip��ַ���ж��Ƿ����ɹ�
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
     * ������ip�Ƿ���Ч
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
            // �޷�ͨ��������������ʰٶȣ���˵������IP��Ч
            System.out.println("IP��Ч");
        }
        return false;
    }
}
