package WeiboCrawler2.weibo.collector;

import WeiboCrawler2.Xici.IPBean;
import WeiboCrawler2.Xici.XiciEntrance;
import WeiboCrawler2.Xici.XiciProxyIP;
import WeiboCrawler2.utils.FileOperation;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * 利用WebCollector获取的cookie爬取新浪微博并抽取数据
 */
public class WeiboController extends BreadthCrawler {
    private String cookie;
    private static final String cookiePath = "Data/cookie.txt";
    private static final String plainIPsPath = "Data/plainIPs.txt";
    private List<IPBean> ips = null;
    private static final String userName = "18163132129";
    private static final String password = "cgm111513100349q";

    public WeiboController(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);

        // 获取所有可用IP
        this.ips = XiciEntrance.getIPBeanList(3);

        // 获取登陆凭证
        String tmp = FileOperation.html2String(cookiePath);
        if (tmp.isEmpty() || tmp == "") {
            cookie = XinLangCookie.loginAndGetCookies(userName, password);
            FileOperation.writeString(cookie, cookiePath);
            if (!cookie.contains("SUB")) {
                throw new Exception("weibo login failed");
            }
        } else {
            cookie = tmp;
        }
    }

    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        // 获取可用IP
        int ip = -1;
        for (int i = 0; i < ips.size(); i++) {
            if (XiciProxyIP.isValid(ips.get(i))) {
                ip = i;
                break;
            }
        }
        if (ip == -1) {
            // 重新获取所有可用IP
            this.ips = XiciEntrance.getIPBeanList(5);
            ip = 0;
        }

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

    public static void main(String[] args) throws Exception {

        // 设置webcollector参数
        WeiboController crawler = new WeiboController("Data/env", false);// 网页、图片、文件被存储在download文件夹中
        crawler.setThreads(3);
        crawler.addRegex("http://m.weibo.cn/.*");

        /*对某人微博前5页进行爬取*/
        for (int i = 1; i <= 5; i++) {
            // 添加爬取种子,也就是需要爬取的网站地址,以及爬取深度
            crawler.addSeed(new CrawlDatum("https://s.weibo.com/weibo?q=" + "摄影" + "&page=" + i).meta("pageNum", i + ""));
        }
        crawler.start(1);
    }
}
