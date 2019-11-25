package WeiboCrawler2.utils;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class HTMLParser {
    public Vector<String> splitHTML(String html) {
        Vector<String> pieces = new Vector<String>();
        Document piecesDoc = Jsoup.parse(html);
        Elements elePieces = piecesDoc.select("dl[class][action-type][mid]");
        for (Element elePiece : elePieces) {
            pieces.add(elePiece.toString());
        }
//		Pattern p = Pattern
//				.compile("<dl class=\"feed_list\".+?<dd class=\"clear\">");
//		Matcher m = p.matcher(html);
//		while (m.find()) {
//			pieces.add(m.group());
//			// System.out.println(m.group());
//		}

        return pieces;
    }

    public String parse(String html) {
        String s = "<weibo>";
        Document doc = Jsoup.parse(html);
        Element userName = doc.select("dt[class].face > a[title][href][target]").first();
        //System.out.println(userName.toString());
        if (userName != null) {//��if elseѡ������Է���һ����ֹ�����������ʵ���ϼ���������else�����
            //System.out.println(userName.toString());
            String attrUserName = userName.attr("title");
            s += "<userName_s " + attrUserName + " userName_e>";
        }

        Element userid = doc.select("dt[class].face > a[title][href][target]> img").first();
        if (userid != null) {
            String attrUserid = userid.attr("src");
            attrUserid = attrUserid.substring(attrUserid.indexOf("cn") + 3);
            attrUserid = attrUserid.substring(0, 10);
            //System.out.println(attrUserid);
            //attrUserid = attrUserid.substring(0, 9);
            //System.out.println(attrUserid);
//			Pattern p = Pattern.compile("[0-9]{10}");
//			Matcher m = p.matcher(attrUserid);
//			if (m.find()) {
//				attrUserid = m.group();
//			}
//			else{
//				attrUserid = "null";
//			}
            s += "<userid_s " + attrUserid + " userid_e>";
        }

        Element date = doc.select("dd[class].content > p[class] > a[date]").first();
        //System.out.println(date);
        if (date != null) {
            String attrDate = date.attr("date");
            //System.out.println(attrDate);
            //String timeInmillis = "1396595190000";//1396595190000
            long dateLong = Long.parseLong(attrDate);
            Date dateTime = new Date(dateLong);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = dateFormat.format(dateTime);
            //System.out.println(dateString);
            s += "<date_s " + dateString + " date_e>";
        }

        Element weiboid = doc.select("dl[mid]").first();
        if (weiboid != null) {
            String attrWeiboid = weiboid.attr("mid");
            s += "<weiboid_s " + attrWeiboid + " weiboid_e>";
        }

        Element weibo = doc.select("dd[class].content > p[node-type] > em").first();
        if (weibo != null) {
            String weiboString = weibo.toString();
            //System.out.println(weibo);//΢��em��
            //System.out.println(weiboText);//���������΢���ı�
            Document emotionDoc = Jsoup.parse(weiboString);
            Elements emotionFrags = emotionDoc.select("img[src][alt][title]");
            Vector<String> emotion = new Vector<String>();
            for (Element emotionFrag : emotionFrags) {
                String attrEmotion = emotionFrag.attr("title");
                emotion.add(attrEmotion);
            }
            for (int i = 0; i < emotion.size(); i++) {
                weiboString = weiboString.replaceFirst("<img src=.+?title.+?alt.+?type.+?>", emotion.get(i));
            }
            Document replacedWeiboDoc = Jsoup.parse(weiboString);
            Element replacedWeibo = replacedWeiboDoc.select("em").first();//ÿ��ֻ��һ��em�壬������first
            String EmotionWeibo = replacedWeibo.text();
            //System.out.println(EmotionWeibo);//�������΢���ı�
            s += "<weiboSentence_s " + EmotionWeibo + " weiboSentence_e>";
        }

        Elements forwardNums = doc.select("dd[class].content > p[class] > span > a");
        //doc.select("a:contains(ת��)").first();
        for (Element forwardNum : forwardNums) {
            if (forwardNum != null) {
                String attrForwardNum = forwardNum.text();
                if (attrForwardNum.contains("ת��")) {
                    if (attrForwardNum.equals("ת��")) {
                        attrForwardNum = "0";
                    } else {
                        if (!attrForwardNum.contains("ת��(")) {
                            attrForwardNum = "0";
                        } else {
                            attrForwardNum = attrForwardNum.substring(attrForwardNum.indexOf("ת��(") + 3,
                                    attrForwardNum.indexOf(")"));
                        }
                    }
                    //System.out.println("zhuanfa " + attrForwardNum);
                    s += "<forwardNum_s " + attrForwardNum + " forwardNum_e>";
                }
            }
        }


        Elements commentNums = doc.select("dd[class].content > p[class] > span > a");
        //doc.select("a:contains(ת��)").first();
        for (Element commentNum : commentNums) {
            if (commentNum != null) {
                String attrCommentNum = commentNum.text();
                if (attrCommentNum.contains("����")) {
                    if (attrCommentNum.equals("����")) {
                        attrCommentNum = "0";
                    } else {
                        if (!attrCommentNum.contains("����(")) {
                            attrCommentNum = "0";
                        } else {
                            attrCommentNum = attrCommentNum.substring(attrCommentNum.indexOf("����(") + 3,
                                    attrCommentNum.indexOf(")"));
                        }
                    }
                    //System.out.println("pinglun " + attrCommentNum);
                    s += "<commentNum_s " + attrCommentNum + " commentNum_e>";
                }
            }
        }

        return s;
    }

    public Vector<String> write2txt(String searchword, String dirPath,
                                    String saveTXTPath) throws IOException {
        Vector<String> weibos = new Vector<String>();
        String onePiece;
        File f = new File(saveTXTPath);
        FileWriter fw = new FileWriter(f);
        BufferedWriter bw = new BufferedWriter(fw);
        // dirPath = "d:/data/weibo/getweibo/20xxxxxx/xxxx"
        for (int page = 0; page < 50; page++) {
            String path = dirPath + "/" + searchword + page + ".html";// d:/data/getweibo/20xxxxxx/�Ϻ�ʶ����/�Ϻ�ʶ����10.html
            File ff = new File(path);
            if (ff.exists()) {
                String html = FileOperation.html2String(path);
                if (!html.equals("null")) {
                    Vector<String> pieces = new HTMLParser().splitHTML(html);
                    //System.out.println(pieces.size());
                    for (int i = 0; i < pieces.size(); i++) {
                        onePiece = pieces.get(i);
//						if (onePiece.contains("feed_list_forwardContent")) {
//							Pattern p = Pattern
//									.compile("feed_list_forwardContent.+?<p class=\"info W_linkb W_textb");
//							Matcher m = p.matcher(onePiece);
//							if (m.find()) {
//								onePiece = onePiece.replace(m.group(), "");
//								//System.out.println(onePiece);
//							}
//						}
                        String s = new HTMLParser().parse(onePiece);
                        //System.out.println(s);
                        weibos.add(s);
                        bw.write(s + "\r\n");
                    }
                }

            }
        }
        bw.close();

        return weibos;
    }

    public void writeVector2xml(Vector<String> vector, String saveXMLPath) throws IOException {
        int vectorSize = vector.size();
        String oneIniWeibo;
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");// ��xml��ʽ����Ϊgb2321����������ʶ��Ĭ����utf8������ʶ��
        File f = new File(saveXMLPath);
        f.createNewFile();// �Ƚ�һ����xml�ļ�
        FileWriter fw = new FileWriter(f);
        org.dom4j.Document document = DocumentHelper.createDocument();// ��document����ʵ��
        org.dom4j.Element rootElement = document.addElement("weibos");// �ڵ����ӷ���
        rootElement.addAttribute("totalNumber", String.valueOf(vectorSize));// ��������
        for (int j = 0; j < vectorSize; j++) {
            oneIniWeibo = vector.get(j);
            System.out.println(oneIniWeibo);
            if (oneIniWeibo.contains("userName_s")
                    && oneIniWeibo.contains("userid_s")
                    && oneIniWeibo.contains("date_s")
                    && oneIniWeibo.contains("weiboid_s")
                    && oneIniWeibo.contains("forwardNum_s")
                    && oneIniWeibo.contains("commentNum_s")
                    && oneIniWeibo.contains("weiboSentence_s")) {
                String userName = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<userName_s ") + 12,
                        oneIniWeibo.indexOf(" userName_e>"));
                String userid = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<userid_s ") + 10,
                        oneIniWeibo.indexOf(" userid_e>"));
                String date = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<date_s ") + 8,
                        oneIniWeibo.indexOf(" date_e>"));
                String weiboid = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<weiboid_s ") + 11,
                        oneIniWeibo.indexOf(" weiboid_e>"));
                // String praisedNum =
                // oneIniWeibo.substring(oneIniWeibo.indexOf("<praisedNum_s ")+14,
                // oneIniWeibo.indexOf(" praisedNum_e>"));
                String forwardNum = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<forwardNum_s ") + 14,
                        oneIniWeibo.indexOf(" forwardNum_e>"));
                String commentNum = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<commentNum_s ") + 14,
                        oneIniWeibo.indexOf(" commentNum_e>"));
                String weiboSentence = oneIniWeibo.substring(
                        oneIniWeibo.indexOf("<weiboSentence_s ") + 17,
                        oneIniWeibo.indexOf(" weiboSentence_e>"));
                org.dom4j.Element weiboElement = rootElement.addElement("weibo");
                weiboElement.addAttribute("polarity", "unknown");
                weiboElement.addAttribute("opinionated", "unknown");
                weiboElement.addAttribute("userName", userName);
                weiboElement.addAttribute("userid", userid);
                weiboElement.addAttribute("date", date);
                weiboElement.addAttribute("weiboid", weiboid);
                // weiboElement.addAttribute("praisedNum", praisedNum);
                weiboElement.addAttribute("forwardNum", forwardNum);
                weiboElement.addAttribute("commentNum", commentNum);
                weiboElement.setText(weiboSentence);// ���ýڵ��ı�����
            }
        }
        XMLWriter xw = new XMLWriter(fw, format);
        xw.write(document);// дdocument��xml�ļ�
        xw.close();
    }
}
