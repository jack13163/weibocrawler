package WeiboCrawler2.Xici;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class XiciEntrance {
    public static void main(String[] args) {
        // 爬取的页数，默认一页100个
        int pages = 3;

        // 获取所有可用IP
        List<IPBean> ips = getIPBeanList(pages);
    }

    /**
     * 获取可用IP列表
     * @param pages
     */
    public static List<IPBean> getIPBeanList(int pages) {
        final List<IPBean> result = new ArrayList<IPBean>();

        // 从网站上获取IP列表
        IPSpider spider = new IPSpider();
        List<IPBean> list = spider.crawlHttp(pages);
        System.out.println("爬取数量：" + list.size());

        Gson gson = new Gson();
        for (IPBean ipBean : list) {
            final IPBean ip_bean = ipBean;
            System.out.println(gson.toJson(ipBean));

            // 开启多个线程验证
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // 检验爬去到的IP是否可用
                    boolean valid = XiciProxyIP.isValid(ip_bean);

                    // 若可用，则加入到IP列表中
                    if (valid) {
                        IPList.add(ip_bean);
                        result.add(ip_bean);
                    }
                    IPList.increase();
                }
            }).start();
        }

        while (true) {
            // 判断所有副线程是否完成
            if (IPList.getCount() == list.size()) {
                System.out.println("有效数量：" + IPList.getSize());
                break;
            }
        }

        return result;
    }
}