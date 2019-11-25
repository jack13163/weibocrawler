package WeiboCrawler2.weibo.collector;

import WeiboCrawler2.Xici.IPBean;
import WeiboCrawler2.Xici.XiciEntrance;
import WeiboCrawler2.Xici.XiciProxyIP;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * ����WebCollector��ȡ��cookie��ȡ����΢������ȡ����
 */
public class WeiboLogin extends BreadthCrawler {
    private String cookie;
    private static final String plainIPsPath = "Data/plainIPs.txt";
    private List<IPBean> ips = null;
    private static final String userName = "18163132129";
    private static final String pass = "cgm111513100349Q";

    public WeiboLogin(String crawlPath, boolean autoParse) throws Exception {
        super(crawlPath, autoParse);
        cookie = WeiboCN.getSinaCookie(userName, pass);
        // ��ȡ���п���IP
        this.ips = XiciEntrance.getIPBeanList(1);
    }

//    /**
//     * ����ִ���������
//     *
//     * @param crawlDatum
//     * @return
//     * @throws Exception
//     */
//    @Override
//    public Page getResponse(CrawlDatum crawlDatum) throws Exception {
//        // ��ȡ����IP
//        int ip = -1;
//        for (int i = 0; i < ips.size(); i++) {
//            if (XiciProxyIP.isValid(ips.get(i))) {
//                ip = i;
//                break;
//            }
//        }
//        if (ip == -1) {
//            throw new Exception("û�п��õĴ���IP");
//        }
//
//        // ʹ��HtmlUnit����js
//        WebClient webClient = new WebClient();
//
//        //���ô���
//        ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
//        proxyConfig.setProxyHost(ips.get(ip).getIp());
//        proxyConfig.setProxyPort(Integer.valueOf(ips.get(ip).getPort()));
//
//        // ����cookies
//        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
//        credentialsProvider.addCredentials(userName, pass);
//
//        //���ò���
//        webClient.getOptions().setCssEnabled(false);
//        webClient.getOptions().setJavaScriptEnabled(false);
//        webClient.getOptions().setThrowExceptionOnScriptError(false);
//        HtmlPage page = webClient.getPage(crawlDatum.url());
//        webClient.closeAllWindows();
//
//        System.out.println(page);
//
//        return super.getResponse(crawlDatum);
//    }

    @Override
    public void execute(CrawlDatum datum, CrawlDatums next) throws Exception {
        // ��ȡ����IP
        int ip = -1;
        for (int i = 0; i < ips.size(); i++) {
            if (XiciProxyIP.isValid(ips.get(i))) {
                ip = i;
                break;
            }
        }
        if (ip == -1) {
            throw new Exception("û�п��õĴ���IP");
        }

        // ʹ��HtmlUnit����js
        WebClient webClient = new WebClient();

        //���ô���
        ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
        proxyConfig.setProxyHost(ips.get(ip).getIp());
        proxyConfig.setProxyPort(Integer.valueOf(ips.get(ip).getPort()));

        // ����cookies
        DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
        credentialsProvider.addCredentials(userName, pass);

        //���ò���
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage page = webClient.getPage(datum.url());
        webClient.close();

        System.out.println(page.asText());
    }

    public void visit(Page page, CrawlDatums next) {
        int pageNum = Integer.valueOf(page.meta("pageNum"));
        /*��ȡ΢��*/
        Elements weibos = page.select("#app");
        for (Element weibo : weibos) {
            System.out.println("��" + pageNum + "ҳ\t" + weibo.text());
        }
    }

    public static void main(String[] args) throws Exception {
        // ����webcollector����
        WeiboLogin crawler = new WeiboLogin("Data/env", false);// ��ҳ��ͼƬ���ļ����洢��download�ļ�����
        crawler.setThreads(3);
        crawler.addRegex("http://m.weibo.cn/.*");

        /*��ĳ��΢��ǰ5ҳ������ȡ*/
        for (int i = 1; i <= 5; i++) {
            // �����ȡ����,Ҳ������Ҫ��ȡ����վ��ַ,�Լ���ȡ���
            crawler.addSeed(new CrawlDatum("https://m.weibo.cn/search?containerid=100103type%3D1%26q%3D%E6%91%84%E5%BD%B1&page=" + i).meta("pageNum", i + ""));
        }
        crawler.start(1);
    }
}
