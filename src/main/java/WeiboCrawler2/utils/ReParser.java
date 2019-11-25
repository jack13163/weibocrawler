package WeiboCrawler2.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ReParser {
	public String utf8Parser(String utf8){
		String utf8CodeStr = utf8.substring(2);
    	char cUTF8 = (char) Integer.parseInt(utf8CodeStr,16);
    	String cString = String.valueOf(cUTF8);
    	
    	return cString;
	}
	
	public String getRealHTML(String htmlPath) throws IOException{
		String html = FileOperation.html2String(htmlPath);
		Document doc = Jsoup.parse(html);
		Elements eles = doc.select("script");
		String realHTML = "null";
		for(Element ele : eles){
			String script = ele.toString();
			
			if(script.contains("\"pl_weibo_direct\"")){
				
				script = script.substring(script.indexOf("{"), script.indexOf("}")+1);//<\/div>\n  "})</script>
				try{
					JSONObject json = JSON.parseObject(script);
					realHTML = json.getString("html");
				}
				catch(JSONException e){
					System.out.println("****JSON error****");
				}
				
				//System.out.println(realHTML);
			}
			else{
				//System.out.println("��Ŀ��script�ڵ�");
			}
		}
		
		return realHTML;
	}
}
