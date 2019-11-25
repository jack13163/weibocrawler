package WeiboCrawler2.utils;

import com.sleepycat.je.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BerkelyDBTest {


    private Environment env;
    private Database db;

    public BerkelyDBTest() {
    }

    public void setUp(String path, long cacheSize) {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        envConfig.setCacheSize(cacheSize);
        try {
            env = new Environment(new File(path), envConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void open(String dbName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);//是否允许创建

        try {
            db = env.openDatabase(null, dbName, dbConfig);
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (db != null) {
                db.close();
            }

            if (env != null) {
                env.close();
            }
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public String get(String key) throws Exception {
        DatabaseEntry queryKey = new DatabaseEntry();
        DatabaseEntry value = new DatabaseEntry();
        queryKey.setData(key.getBytes("UTF-8"));

        OperationStatus status = db.get(null, queryKey, value,
                LockMode.DEFAULT);
        if (status == OperationStatus.SUCCESS) {
            return new String(value.getData());
        }
        return null;
    }

    /**
     * put data for one to one
     *
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    public boolean put(String key, String value) throws Exception {
        byte[] theKey = key.getBytes("UTF-8");
        byte[] theValue = value.getBytes("UTF-8");

        OperationStatus status = db.put(null, new DatabaseEntry(theKey), new DatabaseEntry(theValue));
        if (status == OperationStatus.SUCCESS) {
            return true;
        }
        return false;
    }

    /**
     * get all value
     */
    public static List<String> readFile(String filePathAndName) throws IOException {

        FileInputStream fis = new FileInputStream(filePathAndName);
        InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        LineNumberReader lnr = new LineNumberReader(br);

        List<String> returnValue = new ArrayList<String>();
        int cnt = 0;
        while (true) {
            cnt++;
            String tempStr = lnr.readLine();
            if (tempStr == null)
                break;
            if (tempStr.length() < 2)
                continue;
            returnValue.add(tempStr);
        }
        lnr.close();
        br.close();
        isr.close();
        fis.close();
        return returnValue;
    }

    public List<String> getAllValue() {
        List<String> returnValue = new ArrayList<String>();
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        Cursor cursor = null;
        try {
            cursor = db.openCursor(null, null);
            while (cursor.getNext(keyEntry, dataEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
                returnValue.add(new String(dataEntry.getData()));
            }
            cursor.close();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
        return returnValue;
    }

    public int getCount(String key) {
        DatabaseEntry keyEntry = new DatabaseEntry();
        DatabaseEntry dataEntry = new DatabaseEntry();
        keyEntry.setData(key.getBytes());
        int returnValue = 0;
        Cursor cursor = null;
        try {
            cursor = db.openCursor(null, null);
            cursor.getSearchKey(keyEntry, dataEntry, LockMode.DEFAULT);
            returnValue = cursor.count();
            cursor.close();
        } catch (Exception e) {
            returnValue = 0;
        }
        return returnValue;
    }


    public static void main(String[] args) throws Exception {
        BerkelyDBTest mbdb = new BerkelyDBTest();
        mbdb.setUp("Data/env/test.bdb", 1000000);//设置文件夹进行存储，并设置大小
        mbdb.open("myDB");

        List<String> list1 = mbdb.getAllValue();
        for (String str : list1) {
            System.out.println(str);
        }

        System.out.println("开始向Berkeley DB中存入数据...");
        try {

            for (int i = 0; i < 10; i++) {
                String key = "myKey" + i;
                String value = "myValuesq" + i;
                mbdb.put(key, value);//存入数据
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mbdb.close();
    }
}
