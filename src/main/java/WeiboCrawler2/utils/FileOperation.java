package WeiboCrawler2.utils;

import WeiboCrawler2.Xici.IPBean;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.*;

public class FileOperation {

    public static Vector<String> getLines(String path) throws IOException {
        Vector<String> lines = new Vector<String>();
        File f = new File(path);// "d:/data/weibo/validIPs.txt"
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            lines.add(s);
        }
        br.close();

        return lines;
    }

    public static void write2txt(Vector<String> vector, String savePath) throws IOException {
        File f = new File(savePath);//
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i = 0; i < vector.size(); i++) {
            bw.write(vector.get(i) + "\r\n");
            // System.out.println(vector.get(i));
        }
        bw.close();
    }

    /**
     * 将IP写入到文件
     *
     * @param ips
     * @param savePath
     * @throws IOException
     */
    public static void write2txt(List<IPBean> ips, String savePath) throws IOException {
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i = 0; i < ips.size(); i++) {
            bw.write(ips.get(i).getIp() + ":" + ips.get(i).getPort() + "\r\n");
        }
        bw.close();
    }

    /**
     * 将字符串写入到指定的文件中
     *
     * @param s
     * @param savePath
     * @throws IOException
     */
    public static void writeString(String s, String savePath) throws IOException {
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(s);
        bw.close();
    }

    /**
     * 读取文本
     *
     * @param htmlPath
     * @return
     * @throws IOException
     */
    public static String html2String(String htmlPath) throws IOException {
        String html = "";
        File f = new File(htmlPath);
        if (f.exists()) {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String s;
            while ((s = br.readLine()) != null) {
                html += s;
            }
            br.close();
        }

        return html;
    }

    /**
     * 写入文件
     *
     * @param vector
     * @param savePath
     * @throws IOException
     */
    public void writeVector(Vector<String> vector, String savePath) throws IOException {
        File f = new File(savePath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        for (int i = 0; i < vector.size(); i++) {
            bw.write(vector.get(i) + "\r\n");
        }
        bw.close();
    }
}
