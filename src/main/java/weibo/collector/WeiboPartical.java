package weibo.collector;

import weibo.utils.HttpRequestHelper;
import weibo.utils.ImageDownload;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeiboPartical {
    /**
     * 前往博客首页抓取最新的动态
     * @param homeURL 主页URL
     * @param cookie 用户身份认证cookie
     * @param imageSavePath 文件保存的路径
     */
    public static void downHomePageImages(String homeURL, String cookie, String imageSavePath){
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


}
