package weibo.collector;

import weibo.Xici.IPBean;
import weibo.Xici.XiciEntrance;
import weibo.Xici.XiciProxyIP;
import weibo.utils.FileOperation;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weibo.utils.PropertiesFileReadHelper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Properties;

/**
 * 利用WebCollector获取的cookie爬取新浪微博并抽取数据
 */
public class WeiboController extends BreadthCrawler {
    private String cookie = "";
    private static final String cookiePath = "Data/cookie.txt";
    private static final String plainIPsPath = "Data/plainIPs.txt";

    public WeiboController(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);

        // 获取登陆凭证
        String tmp = FileOperation.html2String(cookiePath);
        if (tmp.isEmpty() || tmp == "") {
            Properties properties = PropertiesFileReadHelper.readProperties("userconfig.properties");
            cookie = XinLangCookie.loginAndGetCookies(properties.get("userName").toString(), properties.get("password").toString());
            FileOperation.writeString(cookie, cookiePath);
            if (!cookie.contains("SUB")) {
                throw new Exception("weibo login failed");
            }
        } else {
            cookie = tmp;
        }
    }

    /**
     * 暂时不支持代理
     * @param datum
     * @param next
     * @throws Exception
     */
    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        // 使用OkHttpClient发送请求
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(datum.url())
                .get()
                .addHeader("Cookie", cookie)
                .addHeader("cache-control", "no-cache")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36")
                .build();
        Response response = client.newCall(request).execute();

        System.out.println(datum.url() + "\n\n" + response.body().string());
    }

    public void visit(Page page, CrawlDatums next) {
        int pageNum = Integer.valueOf(page.meta("pageNum"));
        /*抽取微博*/
        Elements weibos = page.select("#app");
        for (Element weibo : weibos) {
            System.out.println("第" + pageNum + "页\t" + weibo.text());
        }
    }
}
