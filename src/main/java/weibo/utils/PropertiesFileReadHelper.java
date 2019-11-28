package weibo.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReadHelper {
    /**
     * 读取属性[位于maven的resource文件夹下]
     * @param fileName
     * @return
     */
    public static Properties readProperties(String fileName) {
        Properties properties = new Properties();
        try {
            // 用流读入properties配置文件
            InputStream inputStream = PropertiesFileReadHelper.class.getClassLoader().getResourceAsStream(fileName);
            // 从输入字节流读取属性列表（键和元素对）
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }
}
