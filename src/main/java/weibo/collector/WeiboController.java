package weibo.collector;

import weibo.ui.JWindowsFrame;
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

    public WeiboController(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);
        // 读取配置文件
        properties = PropertiesFileReadHelper.readProperties("userconfig.properties");

        // 获取登陆凭证
        String tmp = FileOperation.html2String(properties.get("cookiePath").toString());
        if (tmp.isEmpty() || tmp == "") {
            cookie = XinLangCookie.loginAndGetCookies(properties.get("userName").toString(), properties.get("password").toString());
            FileOperation.writeString(cookie, properties.get("cookiePath").toString());
            if (!cookie.contains("SUB")) {
                throw new Exception("weibo login failed");
            }
        } else {
            cookie = tmp;
        }
    }

    public void visit(Page page, CrawlDatums next) {

        String url = page.url();
        try {
            String topicResult = HttpRequestHelper.getResultOkHttpClient(url, cookie);

            // 解析本页所有的博客信息
            WeiboPartical.getWeiboInfo(topicResult, JWindowsFrame.JTARunInfo);

            // 解析本页所有博主的昵称和个人主页链接
            Map<String, String> detailMap = new HashMap<String, String>();
            Matcher matcher = Pattern.compile("<a\\s+?href=\"([^\"]*)\".+?nick-name=\"([^\"]*)\".+?>").matcher(topicResult);
            while (matcher.find()) {
                String homepage_url = "http:" + matcher.group(1);
                String name = matcher.group(2);

                // 前往博客首页抓取最新的动态
                if (!detailMap.containsKey(name)) {
                    detailMap.put(name, homepage_url);
                    WeiboPartical.downHomePageImages(homepage_url, cookie, properties.get("imageSavePath").toString());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
