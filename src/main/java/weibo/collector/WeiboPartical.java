package weibo.collector;

import org.apache.http.client.ClientProtocolException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import weibo.model.WeiboInfo;
import weibo.utils.FileOperation;
import weibo.utils.HttpRequestHelper;
import weibo.utils.ImageDownload;
import weibo.utils.PropertiesFileReadHelper;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboPartical {
    /**
     * 前往博客首页抓取最新的动态
     *
     * @param homeURL       主页URL
     * @param cookie        用户身份认证cookie
     * @param imageSavePath 文件保存的路径
     */
    public static void downHomePageImages(String homeURL, String cookie, String imageSavePath) {
        String detailResult = HttpRequestHelper.getResultOkHttpClient(homeURL, cookie);

        Matcher matcher2 = Pattern.compile("\\{.+?\"html\":\"(.+?)\"\\}").matcher(detailResult);

        while (matcher2.find()) {
            String bowen = matcher2.group(1).replaceAll("\\\\", "");
            Matcher matcher3 = Pattern.compile("<img.+?src=\"(.+?)\"[^>]+?>").matcher(bowen);
            while (matcher3.find()) {
                String imgUrl = matcher3.group(1);
                // 过滤用户头像
                if (imgUrl.startsWith("//")) {
                    // 判断图片是否为缩略图，用于找到对应的原图
                    Matcher matcher4 = Pattern.compile("//wx[1-4].sinaimg.cn/.+?/(.*)").matcher(imgUrl);
                    if (matcher4.find()) {
                        // 实际上，微博原图所存放的服务器和缩略图存在的服务器的域名可能不同，但是文件名称相同
                        imgUrl = "https://wx2.sinaimg.cn/mw690/" + matcher4.group(1);
                        // 下载图片
                        try {
                            ImageDownload.download(imgUrl, imageSavePath);
                            System.out.println("downloaded: " + imgUrl);
                        } catch (Exception e) {
                            System.out.println("downloaded: " + imgUrl + "failed.");
                        }
                    }
                }
            }
        }
    }

    /**
     * 用默认浏览器打开指定网址
     *
     * @param url
     * @throws URISyntaxException
     * @throws IOException
     */
    private static void runBroswer(String url) throws URISyntaxException, IOException {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }

    /**
     * 获取博文ID
     *
     * @param bowen
     * @return
     */
    private static String getWeiboid(String bowen) {
        String id = "";

        Matcher matcher = Pattern.compile("mid=\"(\\d*)\"").matcher(bowen);
        if (matcher.find()) {
            id = matcher.group(1);
        }

        return id;
    }

    /**
     * 获取博文的用户名
     *
     * @param bowen
     * @return
     */
    private static String getUserName(String bowen) {
        String nickName = "";

        Matcher matcher = Pattern.compile("<a\\s+?href=\"[^\"]*\".+?nick-name=\"([^\"]*)\".+?>").matcher(bowen);
        if (matcher.find()) {
            nickName = matcher.group(1);
        }

        return nickName;
    }

    /**
     * 获取博文的用户ID
     *
     * @param bowen
     * @return
     */
    private static String getUserid(String bowen) {
        String userid = "";
        Pattern puserid = Pattern.compile("uid=(\\d*)");
        Matcher muserid = puserid.matcher(bowen);
        if (muserid.find()) {
            userid = muserid.group(1);
        }

        return userid;
    }

    /**
     * 获取博文内容
     *
     * @param bowen
     * @return
     */
    private static String getWeiboSentence(String bowen) {
        // 利用jsoup解析html
        Document document = Jsoup.parseBodyFragment(bowen);
        String content = document.selectFirst("p").text();

        return content;
    }

    /**
     * 获取点赞个数
     *
     * @param bowen
     * @return
     */
    private static int getPraisedNum(String bowen) throws Exception {
        int prasedNum = 0;
        try {
            // 利用jsoup解析html
            Document document = Jsoup.parseBodyFragment(bowen);
            Elements contents = document.select("ul:last-child").select("li");

            Matcher matcher = Pattern.compile("<em>(\\d*)</em>").matcher(contents.get(3).toString());
            if (matcher.find()) {
                prasedNum = Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            throw new Exception("解析完成");
        }

        return prasedNum;
    }

    /**
     * 获取转发个数
     *
     * @param bowen
     * @return
     */
    private static int getForwardNum(String bowen) throws Exception {
        int forwardNum = 0;
        try {
            // 利用jsoup解析html
            Document document = Jsoup.parseBodyFragment(bowen);
            Elements contents = document.select("ul:last-child").select("li");

            Matcher matcher = Pattern.compile("<em>(\\d*)</em>").matcher(contents.get(1).toString());
            if (matcher.find()) {
                forwardNum = Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            throw new Exception("解析完成");
        }

        return forwardNum;
    }

    /**
     * 获取评论个数
     *
     * @param bowen
     * @return
     */
    private static int getCommentNum(String bowen) throws Exception {
        int commenttemp = 0;
        try {
            // 利用jsoup解析html
            Document document = Jsoup.parseBodyFragment(bowen);
            Elements contents = document.select("ul:last-child").select("li");

            Matcher matcher = Pattern.compile("<em>(\\d*)</em>").matcher(contents.get(2).toString());
            if (matcher.find()) {
                commenttemp = Integer.parseInt(matcher.group(1));
            }
        } catch (Exception e) {
            throw new Exception("解析完成");
        }

        return commenttemp;
    }

    /**
     * 获取发表日期
     *
     * @param bowen
     * @return
     */
    private static String getDate(String bowen) {

        // 利用jsoup解析html
        Document document = Jsoup.parseBodyFragment(bowen);
        Elements contents = document.select("p");

        return contents.get(1).text();
    }

    /**
     * 获取某一网页上所有微博的详细信息
     *
     * @param htmlString
     * @return
     */
    private static List<WeiboInfo> getWeiboInfo(String htmlString) {
        List<WeiboInfo> weiboInfos = new ArrayList<WeiboInfo>();

        // 利用jsoup解析html
        Document document = Jsoup.parseBodyFragment(htmlString);
        Elements contents = document.selectFirst(".m-con-l").select(".card-wrap");

        for (int i = 0; i < contents.size(); i++) {
            String oneIniWeibo = contents.get(i).toString();

            try {
                String userName = getUserName(oneIniWeibo);
                String date = getDate(oneIniWeibo);
                String userid = getUserid(oneIniWeibo);
                String weiboid = getWeiboid(oneIniWeibo);
                String weiboSentence = getWeiboSentence(oneIniWeibo);
                // 若下述三条信息不存在，则说明不是一个有效的博文，会忽略解析
                int praisedNum = getPraisedNum(oneIniWeibo);
                int forwardNum = getForwardNum(oneIniWeibo);
                int commentNum = getCommentNum(oneIniWeibo);

                WeiboInfo weiboInfo = new WeiboInfo(userName, date, userid, weiboid, weiboSentence, praisedNum, forwardNum, commentNum);
                weiboInfos.add(weiboInfo);
                System.out.println(weiboInfo);
            } catch (Exception ex) {
            }
        }

        return weiboInfos;
    }

    /**
     * 判断是否需要输入验证码
     *
     * @param html
     * @return
     */
    private static boolean isVerification(String html) {
        boolean isVerify = false;
        Pattern pVerify = Pattern.compile("\\\\u4f60\\\\u7684\\\\u884c\\\\u4e3a\\\\u6709"
                + "\\\\u4e9b\\\\u5f02\\\\u5e38\\\\uff0c\\\\u8bf7\\\\u8f93\\\\u5165\\\\u9a8c"
                + "\\\\u8bc1\\\\u7801\\\\uff1a");//你的行为有些异常，请输入验证码（被系统检测出你是机器，会蜂鸣提示，刷新微博输入验证码就可以继续了）
        Matcher mVerify = pVerify.matcher(html);
        if (mVerify.find()) {
            isVerify = true;
        }

        return isVerify;
    }

    /**
     * 是否存在指定页数的搜索结果页面
     *
     * @param html
     * @return
     */
    private static boolean isExistResult(String html) {
        boolean isExist = true;
        Pattern pExist = Pattern.compile("\\\\u60a8\\\\u53ef\\\\u4ee5\\\\u5c1d\\\\u8bd5"
                + "\\\\u66f4\\\\u6362\\\\u5173\\\\u952e\\\\u8bcd\\\\uff0c\\\\u518d\\\\u6b21"
                + "\\\\u641c\\\\u7d22\\\\u3002");//您可以尝试更换关键词，再次搜索。（表示指定页没有结果）
        Matcher mExist = pExist.matcher(html);
        if (mExist.find()) {
            isExist = false;
        }

        return isExist;
    }


    /**
     * 根据关键词爬取
     *
     * @param cookie
     * @param searchWord
     */
    private static List<String> crawlByKeyWord(String cookie, String searchWord) throws Exception {
        return crawlPagesByKeyWord(cookie, searchWord, 1);
    }

    /**
     * 根据关键词爬取【多页】
     *
     * @param cookie
     * @param searchWord
     * @param totalPage
     */
    public static List<String> crawlPagesByKeyWord(String cookie, String searchWord, int totalPage) throws Exception {
        List<String> result = new ArrayList<String>();
        for (int i = 1; i <= totalPage; i++) {
            //开始爬取，先把一个话题下的html都爬下来，再用这些html文件
            String html = HttpRequestHelper.getResultOkHttpClient("http://s.weibo.com/weibo/" + searchWord + "&page=" + i, cookie);
            // 休息1s
            Thread.sleep(1000);

            if (isVerification(html)) {
                System.out.println("****十秒内请在弹出页面输入验证码，程序会自动重连****");
                Toolkit.getDefaultToolkit().beep();//蜂鸣提示需要输入验证码
                runBroswer("http://s.weibo.com/weibo/%25E6%259D%258E%25E9%259B%25AA%25E5%25B1%25B1hakka&Refer=index");
                Toolkit.getDefaultToolkit().beep();
                Thread.sleep(10000);//你有十秒时间可以填入验证码
                result.add(html);
            } else if (isExistResult(html)) {
                result.add(html);
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 把微博信息全写到文件中去
     *
     * @param weiboInfos
     * @param savePath
     */
    private static void writeWeiboInfoToFile(List<WeiboInfo> weiboInfos, String savePath) {
        try {
            File f = new File(savePath);
            FileWriter fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < weiboInfos.size(); i++) {
                bw.write(weiboInfos.get(i).toString() + "\n");
            }
            bw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 搜索某一关键字相关的微博内容
     * @param searchWord
     * @param totalPage
     * @throws Exception
     */
    public static void searchWeibo(String searchWord, int totalPage) throws Exception {
        // 读取配置文件
        Properties properties = PropertiesFileReadHelper.readProperties("userconfig.properties");

        // 获取登陆凭证
        String cookie = "";
        String tmp = FileOperation.html2String(properties.get("cookiePath").toString());
        if (tmp.isEmpty() || tmp == "") {
            cookie = XinLangCookie.loginAndGetCookies(properties.get("userName").toString(), properties.get("password").toString());
        } else {
            cookie = tmp;
        }

        List<WeiboInfo> allWeibos = new ArrayList<WeiboInfo>();//一个关键词搜索出来的所有微博
        System.out.println("****开始爬取 \"" + searchWord + "\" 关键字的微博****");
        List<String> htmlStrs = crawlPagesByKeyWord(cookie, searchWord, totalPage);

        // 从搜索页面html文件中得到微博，微博id，存入向量容器中
        for (int i = 0; i < htmlStrs.size(); i++) {
            List<WeiboInfo> oneHTMLWeibos = getWeiboInfo(htmlStrs.get(i));
            for (int j = 0; j < oneHTMLWeibos.size(); j++) {
                // 存到总微博里
                if (!allWeibos.contains(oneHTMLWeibos.get(j))) {
                    allWeibos.add(oneHTMLWeibos.get(j));
                }
            }
        }

        // 将该关键词相关的微博信息写入到txt文件
        writeWeiboInfoToFile(allWeibos, properties.get("bowenSavePath").toString() + searchWord + ".txt");
    }

    public static void main(String[] args) throws Exception {
        searchWeibo("程序员",5);
    }
}