package weibo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageDownload {

    public static void main(String[] args) throws Exception {
        download("http://ww1.sinaimg.cn/thumb180/006c4U6xly1g99zgz1qb5j34g02yoe81.jpg", "93694182a2ad4778933fa15aa2fe44c0.jpg", "Data/imgs/");
    }

    /**
     * 根据url路径下载图片
     *
     * @param urlString
     * @param filename
     * @param savePath
     * @throws Exception
     */
    public static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        //设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }

        OutputStream os = null;
        try {
            os = new FileOutputStream(sf.getPath() + "\\" + filename);
        }catch (Exception ex){
            System.out.println("跳过当前图片：" + urlString);
        }

        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }

    /**
     * 根据url路径下载图片
     *
     * @param urlString
     * @param savePath
     * @throws Exception
     */
    public static void download(String urlString, String savePath) throws Exception {
        //获取后缀名
        String sname = urlString.substring(urlString.lastIndexOf("."));
        //时间格式化格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        //获取当前时间并作为时间戳
        String timeStamp = simpleDateFormat.format(new Date());
        //拼接新的文件名
        String newName = timeStamp + sname;

        download(urlString, newName, savePath);
    }
}
