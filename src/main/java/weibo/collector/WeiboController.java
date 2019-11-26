package weibo.collector;

import weibo.utils.FileOperation;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weibo.utils.HttpRequestHelper;
import weibo.utils.ImageDownload;
import weibo.utils.PropertiesFileReadHelper;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 利用WebCollector获取的cookie爬取新浪微博并抽取数据
 */
public class WeiboController extends BreadthCrawler {
    private String cookie = "";
    private Properties properties = null;
    private static final String cookiePath = "Data/cookie.txt";
    private static final String plainIPsPath = "Data/plainIPs.txt";

    public WeiboController(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);
        // 读取配置文件
        properties = PropertiesFileReadHelper.readProperties("userconfig.properties");

        // 获取登陆凭证
        String tmp = FileOperation.html2String(cookiePath);
        if (tmp.isEmpty() || tmp == "") {
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
     *
     * @param datum
     * @param next
     * @throws Exception
     */
    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        String topicResult = HttpRequestHelper.getResultOkHttpClient(datum.url(), cookie);

        // 存储详情链接
        Map<String, String> detailMap = new HashMap<String, String>();
        Matcher matcher = Pattern.compile("<a\\s+?href=\"([^\"]*)\".+?nick-name=\"([^\"]*)\".+?>").matcher(topicResult);
        while (matcher.find()) {
            String url = matcher.group(1);
            String name = matcher.group(2);
            detailMap.put(name, url);
            System.out.println(name + " : " + url);
        }

        for (String name : detailMap.keySet()) {
            // 前往博客首页抓取最新的动态
            String url = "http:" + detailMap.get(name);
            String detailResult = HttpRequestHelper.getResultOkHttpClient(url, cookie);

            Matcher matcher2 = Pattern.compile("\\{.+?\"html\":\"(.+?)\"\\}").matcher(detailResult);
            List<String> bowens = new ArrayList<String>();

            while (matcher2.find()) {
                String bowen = matcher2.group(1).replaceAll("\\\\", "");

                // 获取图片地址
                List<String> pics = new ArrayList<String>();
                Matcher matcher3 = Pattern.compile("<img.+?src=\"(.+?)\"[^>]+?>").matcher(bowen);
                while (matcher3.find()) {
                    String imgUrl = matcher3.group(1);
                    // 过滤用户头像
                    if (imgUrl.startsWith("//")) {
                        // 判断图片是否为缩略图，用于找到对应的原图
                        Matcher matcher4 = Pattern.compile("//wx[1-4].sinaimg.cn/.+?/(.*)").matcher(imgUrl);
                        if(matcher4.find()){
                            // 实际上，微博原图所存放的服务器和缩略图存在的服务器的域名可能不同，但是文件名称相同
                            imgUrl = "https://wx2.sinaimg.cn/mw690/" + matcher4.group(1);
                            System.out.println(imgUrl);
                            pics.add(imgUrl);
                        }
                    }
                }

                // 批量下载图片
                for (String p : pics) {
                    ImageDownload.download(p, properties.get("imageSavePath").toString());
                }

                bowens.add(bowen);
            }
        }
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
