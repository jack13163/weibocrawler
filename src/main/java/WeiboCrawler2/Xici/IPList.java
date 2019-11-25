package WeiboCrawler2.Xici;

import java.util.ArrayList;
import java.util.List;

/**
 * ���ڶ��߳̿���
 */
public class IPList {
    private static List<IPBean> ipBeanList = new ArrayList<IPBean>();

    // ������,�߳̽�����+1, �����ж����и��߳��Ƿ����
    private static int count = 0;

    /**
     * ֧�ֲ�������
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
