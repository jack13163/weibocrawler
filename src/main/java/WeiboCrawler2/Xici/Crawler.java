package WeiboCrawler2.Xici;

import WeiboCrawler2.utils.FileOperation;
import WeiboCrawler2.utils.HTML;
import WeiboCrawler2.utils.HTMLParser;
import org.apache.http.client.ClientProtocolException;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    /**
     * �Ƿ����ָ��ҳ�����������ҳ��
     *
     * @param html
     * @return
     */
    public boolean isExistResult(String html) {
        boolean isExist = true;
        Pattern pExist = Pattern.compile("��Ǹ��û���ҵ�.+?span>��صĽ��");// �����Գ��Ը����ؼ��ʣ��ٴ�����������ʾָ��ҳû�н����
        Matcher mExist = pExist.matcher(html);
        if (mExist.find()) {
            isExist = false;
        }

        return isExist;
    }

    public static String getMyTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormat.format(date);

        return today;
    }

    public void excute(String[] args, JTextArea jta) throws ClientProtocolException, URISyntaxException, IOException, InterruptedException {
        long t1 = System.currentTimeMillis();
        String words = args[0];
        words = words.replaceAll("\n", " ");
        String[] searchwords = words.split(" ");
        for (int i = 0; i < searchwords.length; i++) {
            System.out.println(searchwords[i]);
        }
        String saveHTMLPath = args[1];
        String saveTXTPath = args[2];
        String saveXMLPath = args[3];
        String plainIPsPath = args[4];
        String pageNum = args[5];

        String today = getMyTime();
        jta.append("��������  " + today + "\r\n");
        File dirGetweiboSub = new File(saveHTMLPath + "/" + today);
        dirGetweiboSub.mkdirs();
        File dirWeibostxtSub = new File(saveTXTPath + "/" + today);
        dirWeibostxtSub.mkdirs();
        File dirWeibosxmlSub = new File(saveXMLPath + "/" + today);
        dirWeibosxmlSub.mkdirs();
        Vector<String> ip = new Vector<String>();
        ip = FileOperation.getLines(plainIPsPath);
        if (ip == null) {
            System.out.println("�ڸ���·�����Ҳ���plainIP.txt�ļ�");
            jta.append("�ڸ���·�����Ҳ���plainIP.txt�ļ�" + "\r\n");
        }
        int ipNum = ip.size();
        int iIP = 0;

        for (int n = 0; n < searchwords.length; n++) {

            String searchword = searchwords[n];
            String dirPath = saveHTMLPath + "/" + today + "/" + searchword;
            File f = new File(dirPath);
            f.mkdirs();

            // ������Ҫ������ҳ������������ΧΪ���������µĵ�1����totalPageҳ
            int totalPage = Integer.parseInt(pageNum);

            System.out.println("****Start getting weibos of the keyword \"" + searchword + "\"****");
            jta.append("****��ʼ��ȡ�ؼ��� \"" + searchword + "\" �µ�΢��****" + "\r\n");
            // ��ָ��ҳ��������ҳ��html�ļ���ȡ����������
            String html;

            for (int i = totalPage; i > 0; i--) {
                // ��ʼ��ȡ���Ȱ�һ�������µ�html����������������Щhtml�ļ�
                String hostName = ip.get(iIP).split(":")[0];
                int port = Integer.parseInt(ip.get(iIP).split(":")[1]);

                // ��ȡ��ҳ����
                String url = "http://s.weibo.com/weibo/" + searchword + "&nodup=1&page=" + String.valueOf(i);
                html = new HTML().getHTML(url, hostName, port, jta);

                int iReconn = 0;
                while (html.equals("null")) {
                    html = new HTML().getHTML(url, hostName, port, jta);
                    iReconn++;
                    System.out.println("****" + ip.get(iIP) + " reconnected " + iReconn + " time(s)****");
                    jta.append("****" + ip.get(iIP) + " ������ " + iReconn + " ��****" + "\r\n");
                    if (iReconn == 4) {// 4
                        break;
                    }
                }
                if (html.equals("null")) {
                    System.out.println("****5 consecutive connections were failed, now using next IP****");
                    jta.append("****���� 5 ������ʧ�ܣ���ʼʹ����һ������IP****" + "\r\n");
                    if (iIP == ipNum - 1) {
                        System.out.println("****All valid proxy IPs have been tried, still can not get all the data. Now trying the valid proxy IP list again.****");
                        jta.append("****���п��ô���IP�����Թ�һ�飬��δ�ܻ�ȡ����΢�����ݡ���ʼ��һ�ֳ��Կ��ô���IP�б�****" + "\r\n");
                        iIP = 0;
                        System.out.println("****Turn to" + ip.get(iIP) + ", start connecting****");
                        jta.append("****ʹ�ô�IP" + ip.get(iIP) + "����ʼ����****" + "\r\n");
                    } else {
                        iIP++;
                        System.out.println("****Turn to" + ip.get(iIP) + ", start connecting****");
                        jta.append("****ʹ�ô�IP" + ip.get(iIP) + "����ʼ����****" + "\r\n");
                    }
                    i++;
                }
                if (html.contains("version=2012")) {//�˴��Ǻ����еĺ���
                    if (!html.contains("���ÿո񽫶���ؼ��ʷֿ�")) {
                        FileOperation.writeString(html, saveHTMLPath + "/"
                                + today + "/" + searchword + "/" + searchword
                                + String.valueOf(i) + ".html");
                        System.out.println("\"" + searchword + "\"" + " No."
                                + i
                                + " page's html have been saved successfully!");
                        jta.append("\"" + searchword + "\"" + "�ɹ���ȡ�˹ؼ������������ "
                                + i + " ҳhtml�ļ�!" + "\r\n");
                    } else {
                        System.out.println("****\"" + searchword + "\"" + "No."
                                + i + " page does not exist****");
                        jta.append("****\"" + searchword + "\"" + "�˹ؼ��ʵ� " + i
                                + " ҳ�������������****" + "\r\n");
                    }
                }
            }
            System.out.println("****\"" + searchword
                    + "\" crawling has been done!!****");
            jta.append("****\"" + searchword + "\" ��ȡ�˹ؼ���΢������!!****" + "\r\n");
            System.out
                    .println("****Now writing the weibos to local files (txt & xml)****");
            jta.append("****���ڽ����д�뱾���ļ� (txt & xml)****" + "\r\n");
            String saveEachTXTPath = saveTXTPath + "/" + today + "/"
                    + searchword + ".txt";
            HTMLParser htmlParser = new HTMLParser();
            Vector<String> weibos = htmlParser.write2txt(searchword, dirPath,
                    saveEachTXTPath);
            String saveEachXMLPath = saveXMLPath + "/" + today + "/"
                    + searchword + ".xml";
            htmlParser.writeVector2xml(weibos, saveEachXMLPath);
            System.out.println("****Writing has been done!****");
            jta.append("****�ļ�д�����!****" + "\r\n");

            long t2 = System.currentTimeMillis();
            System.out.println((double) (t2 - t1) / 60000 + " mins");
            jta.append("�Ѻ�ʱ " + (double) (t2 - t1) / 60000 + " ����" + "\r\n");
        }
    }
}
