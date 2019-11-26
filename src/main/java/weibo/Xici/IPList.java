package weibo.Xici;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于多线程控制
 */
public class IPList {
    private static List<IPBean> ipBeanList = new ArrayList<IPBean>();

    // 计数器,线程结束即+1, 用于判断所有副线程是否完成
    private static int count = 0;

    /**
     * 支持并发操作
     *
     * @param bean
     */
    public static synchronized void add(IPBean bean) {
        ipBeanList.add(bean);
    }

    public static int getSize() {
        return ipBeanList.size();
    }


    public static synchronized void increase() {
        count++;
    }

    public static synchronized int getCount() {
        return count;
    }
}
