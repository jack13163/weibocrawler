package WeiboCrawler2.weibo.client;

import WeiboCrawler2.Xici.Crawler;
import WeiboCrawler2.Xici.IPBean;
import WeiboCrawler2.Xici.XiciEntrance;
import WeiboCrawler2.utils.FileOperation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

class OpenFile {
    private JFileChooser choose;
    private JFrame jf;
    private File f;
    private String filePath;
    public static String lastPath = "";

    public OpenFile() {
        // this.jf = jf;
        choose = new JFileChooser();
        choose.setCurrentDirectory(new File(lastPath));
        choose.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        choose.setMultiSelectionEnabled(false);
    }

    public void open() {
        f = null;
        int click = choose.showOpenDialog(jf);
        if (click == JFileChooser.APPROVE_OPTION) {
            f = choose.getSelectedFile();
        }
        // choose.showOpenDialog(jf);
    }

    public String getFilePath() {
        filePath = null;
        if (f != null) {
            filePath = f.getPath();
        }
        return filePath;
    }
}

public class JWindowsFrame extends JFrame {
    static Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    //set the width and height of all windows
    static int SCREEN_WIDTH = scrSize.width;
    static int SCREEN_HEIGHT = scrSize.height;
    static int WINDOW_WIDTH = 700;
    static int WINDOW_HEIGHT = 565;
    static int AUTHOR_WIDTH = 290;
    static int AUTHOR_HEIGHT = 280;
    static int INSTRUCTION_WIDTH = 380;
    static int INSTRUCTION_HEIGHT = 230;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private JMenuItem JMIAuthor, JMIInstruction;
    private JMenu JMAbout;
    private JMenuBar JMBOperation;

    private JScrollBar JScroB;
    private JScrollPane JSPRunInfo;
    private JScrollPane JSPSearchWords;

    public static JTextArea JTARunInfo;
    private JTextArea JTASearchWords;

    private JPanel JGetIPPanel;
    private JPanel JButtonGetIPPanel;
    private JPanel JWordsPanel;
    private JPanel JPathPanel;
    private JPanel JButtonPanel;
    private JPanel JLeftPanel;
    private JPanel JRightPanel;
    private JPanel JSaveHTMLPathPanel;
    private JPanel JSaveIPPathPanel;
    private JPanel JSaveTXTPathPanel;
    private JPanel JSaveXMLPathPanel;
    private JPanel JPagesPanel;

    private JLabel JLSavePlainIPs;
    private JLabel JLSearchWords;
    private JLabel JLSaveHTMLPath;
    private JLabel JLSaveTXTPath;
    private JLabel JLSaveXMLPath;
    private JLabel JLPlainIPs;
    private JLabel JLRunInfo;
    private JLabel JLpages;

    final private int PATH_FIELD_LENGTH = 32;
    private JTextField JTFSavePlainIPs;
    private JTextField JTFSaveHTMLPath;
    private JTextField JTFSaveTXTPath;
    private JTextField JTFSaveXMLPath;
    private JTextField JTFPlainIPs;
    private JTextField JTFPages;

    final private String OPEN_FILE_BUTTON_NAME = "���";
    private JButton JBCrawl;
    private JButton JBGetIP;
    private JButton JBSaveIPOpenFile;
    private JButton JBSaveHTMLOpenFile;
    private JButton JBSaveTXTOpenFile;
    private JButton JBSaveXMLOpenFile;
    private JButton JBIPOpenFile;

    public JWindowsFrame() {

        JMIAuthor = new JMenuItem("�������");
        JMIInstruction = new JMenuItem("ʹ��˵��");

        JMAbout = new JMenu("����");

        JMAbout.add(JMIAuthor);
        JMAbout.add(JMIInstruction);

        JMBOperation = new JMenuBar();
        JMBOperation.add(JMAbout);

        setJMenuBar(JMBOperation);

        JLeftPanel = new JPanel();
        JLeftPanel.setLayout(new BorderLayout());

        JRightPanel = new JPanel();
        JRightPanel.setLayout(new BorderLayout());

        JGetIPPanel = new JPanel();
        JGetIPPanel.setLayout(new BorderLayout());

        JSaveIPPathPanel = new JPanel();
        JSaveIPPathPanel.setLayout(new BorderLayout());

        JLSavePlainIPs = new JLabel("������õĴ���IP��·����*��Ҫ��б�ܽ�β*��");

        JTFSavePlainIPs = new JTextField(PATH_FIELD_LENGTH);
        JTFSavePlainIPs.setEditable(true);
        JTFSavePlainIPs.setText("Data/");

        JBSaveIPOpenFile = new JButton(OPEN_FILE_BUTTON_NAME);

        JBGetIP = new JButton("��ȡ����IP");

        JSaveIPPathPanel.add(JTFSavePlainIPs, BorderLayout.WEST);
        JSaveIPPathPanel.add(JBSaveIPOpenFile, BorderLayout.EAST);

        JButtonGetIPPanel = new JPanel();
        JButtonGetIPPanel.setLayout(new FlowLayout());

        JButtonGetIPPanel.add(JBGetIP);

        JGetIPPanel.add(JLSavePlainIPs, BorderLayout.NORTH);
        JGetIPPanel.add(JSaveIPPathPanel, BorderLayout.CENTER);
        JGetIPPanel.add(JButtonGetIPPanel, BorderLayout.SOUTH);

        JScroB = new JScrollBar();
        JScroB.setVisible(true);

        JLRunInfo = new JLabel("�����Ϣ");

        JTARunInfo = new JTextArea();
        // JTARunInfo.setBounds(300, 250, 600, 200);
        JTARunInfo.setEditable(false);// �����ı����Ƿ���Ա༭

        JSPRunInfo = new JScrollPane(JTARunInfo);
        JSPRunInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSPRunInfo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSPRunInfo.add(JScroB);

        JRightPanel.add(JLRunInfo, BorderLayout.NORTH);
        JRightPanel.add(JSPRunInfo);

        JWordsPanel = new JPanel();
        JWordsPanel.setLayout(new BorderLayout());

        JLSearchWords = new JLabel("�����������⣬�ؼ����ÿո���зֿ�");
        // ����֮���ÿո�ֿ�����һ�������ж���ؼ��ʣ���֮��ӡ�%20��

        JTASearchWords = new JTextArea(8, 1);
        JTASearchWords.setEditable(true);
        JTASearchWords.setText("��Ӱ");

        JSPSearchWords = new JScrollPane(JTASearchWords);
        JSPSearchWords.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSPSearchWords.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSPSearchWords.add(JScroB);

        JWordsPanel.add(JGetIPPanel, BorderLayout.NORTH);

        JWordsPanel.add(JLSearchWords, BorderLayout.CENTER);
        JWordsPanel.add(JSPSearchWords, BorderLayout.SOUTH);

        JPathPanel = new JPanel();
        JPathPanel.setLayout(new GridLayout(9, 1));

        JSaveHTMLPathPanel = new JPanel();
        JSaveHTMLPathPanel.setLayout(new BorderLayout());

        JLSaveHTMLPath = new JLabel("��������HTML�ļ���·����*��Ҫ��б�ܽ�β*��");

        JTFSaveHTMLPath = new JTextField(PATH_FIELD_LENGTH);
        JTFSaveHTMLPath.setEditable(true);
        JTFSaveHTMLPath.setText("Data/HTML/");

        JBSaveHTMLOpenFile = new JButton(OPEN_FILE_BUTTON_NAME);

        JSaveHTMLPathPanel.add(JTFSaveHTMLPath, BorderLayout.WEST);
        JSaveHTMLPathPanel.add(JBSaveHTMLOpenFile, BorderLayout.EAST);

        JLSaveTXTPath = new JLabel("��������TXT�ļ���·����*��Ҫ��б�ܽ�β*��");

        JSaveTXTPathPanel = new JPanel();
        JSaveTXTPathPanel.setLayout(new BorderLayout());

        JTFSaveTXTPath = new JTextField(PATH_FIELD_LENGTH);
        JTFSaveTXTPath.setEditable(true);
        JTFSaveTXTPath.setText("Data/TXT/");

        JBSaveTXTOpenFile = new JButton(OPEN_FILE_BUTTON_NAME);

        JSaveTXTPathPanel.add(JTFSaveTXTPath, BorderLayout.WEST);
        JSaveTXTPathPanel.add(JBSaveTXTOpenFile, BorderLayout.EAST);

        JLSaveXMLPath = new JLabel("��������XML�ļ���·����*��Ҫ��б�ܽ�β*��");

        JSaveXMLPathPanel = new JPanel();
        JSaveXMLPathPanel.setLayout(new BorderLayout());

        JTFSaveXMLPath = new JTextField(PATH_FIELD_LENGTH);
        JTFSaveXMLPath.setEditable(true);
        JTFSaveXMLPath.setText("Data/XML/");

        JBSaveXMLOpenFile = new JButton(OPEN_FILE_BUTTON_NAME);

        JSaveXMLPathPanel.add(JTFSaveXMLPath, BorderLayout.WEST);
        JSaveXMLPathPanel.add(JBSaveXMLOpenFile, BorderLayout.EAST);

        JLPlainIPs = new JLabel("���ô���IP��·��(plainIPs.txt�ļ�)");

        JSaveIPPathPanel = new JPanel();
        JSaveIPPathPanel.setLayout(new BorderLayout());

        JTFPlainIPs = new JTextField(PATH_FIELD_LENGTH);
        JTFPlainIPs.setEditable(true);
        JTFPlainIPs.setText("Data/plainIPs.txt");

        JBIPOpenFile = new JButton(OPEN_FILE_BUTTON_NAME);

        JSaveIPPathPanel.add(JTFPlainIPs, BorderLayout.WEST);
        JSaveIPPathPanel.add(JBIPOpenFile, BorderLayout.EAST);

        JPagesPanel = new JPanel();
        JPagesPanel.setLayout(new BorderLayout());

        JLpages = new JLabel("����ÿ���ؼ��ʻ�ȡҳ��(1~50��Ĭ��50): ");

        JTFPages = new JTextField(PATH_FIELD_LENGTH / 10);
        JTFPages.setEditable(true);
        JTFPages.setText("8");

        JPagesPanel.add(JLpages, BorderLayout.WEST);
        JPagesPanel.add(JTFPages, BorderLayout.EAST);

        JPathPanel.add(JLSaveHTMLPath);
        JPathPanel.add(JSaveHTMLPathPanel);
        JPathPanel.add(JLSaveTXTPath);
        JPathPanel.add(JSaveTXTPathPanel);
        JPathPanel.add(JLSaveXMLPath);
        JPathPanel.add(JSaveXMLPathPanel);
        JPathPanel.add(JLPlainIPs);
        JPathPanel.add(JSaveIPPathPanel);
        //JPathPanel.add(JLpages);
        JPathPanel.add(JPagesPanel);

        JBCrawl = new JButton("��ȡ΢��");

        JButtonPanel = new JPanel();
        JButtonPanel.setLayout(new FlowLayout());

        // JButtonPanel.add(JBGetIP);
        JButtonPanel.add(JBCrawl);

        JLeftPanel.add(JWordsPanel, BorderLayout.NORTH);
        JLeftPanel.add(JPathPanel, BorderLayout.CENTER);
        JLeftPanel.add(JButtonPanel, BorderLayout.SOUTH);

        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        c.add(JLeftPanel, BorderLayout.WEST);
        c.add(JRightPanel);
    }

    public static void showData(String s) {
        JWindowsFrame.JTARunInfo.append(s);
        JTARunInfo.paintImmediately(JTARunInfo.getBounds());
    }

    public void action() {
        ActionListener actionAboutAuthor = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setVisible(true);
                jDialog.setResizable(false);

                jDialog.setTitle("������Ϣ");

                jDialog.setBounds((SCREEN_WIDTH - AUTHOR_WIDTH) / 2, (SCREEN_HEIGHT - AUTHOR_HEIGHT) / 2, AUTHOR_WIDTH, AUTHOR_HEIGHT);
                JTextArea jta = new JTextArea();
                jta.setBackground(getBackground());
                jta.setText("           ������ߣ���ѩɽ" + "\r\n"
                        + "\r\n  ����������߱��˶���������ּ���ṩһ��"
                        + "\r\n  ץȡ΢���ı������ߡ�������������ҵ��Ϊ��" + "\r\n"
                        + "\r\n  �������Դ�����д��������CSDN����:"
                        + "\r\n  http://blog.csdn.net/codingmirai/" + "\r\n"
                        + "\r\n  �������ʣ���ӭ�������ҵ������ַ��" + ""
                        + "\r\n        hainanlxs@gmail.com" + "\r\n"
                        + "\r\n    LI XUESHAN, All Rights Reserved");
                jta.setEditable(false);
                jDialog.add(jta);
            }
        };
        JMIAuthor.addActionListener(actionAboutAuthor);
        ActionListener actionAboutInstruction = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog jDialog = new JDialog();
                jDialog.setVisible(true);

                jDialog.setTitle("ʹ��˵��");
                jDialog.setResizable(false);

                jDialog.setBounds((SCREEN_WIDTH - INSTRUCTION_WIDTH) / 2, (SCREEN_HEIGHT - INSTRUCTION_HEIGHT) / 2, INSTRUCTION_WIDTH, INSTRUCTION_HEIGHT);
                JTextArea jta = new JTextArea();
                jta.setBackground(getBackground());
                jta.setText("               WeiboCrawler Version: 2.2"
                        + "\r\n  ������ǻ��ڴ���IP������΢�����ݻ�ȡ������Ч���������"
                        + "\r\n΢���ķ�������ƣ��ܹ�ȫ�Զ�������ȡ���΢�����ݡ�" + "\r\n  �����Ϊ������Ҫ���ܣ�"
                        + "\r\n1. ��ȡ��Ч����IP��" + "\r\n2. ���ݹؼ�����ȡ���΢�����ݡ�"
                        + "\r\n  " + "\r\n  д�����ˣ�������ܼܺ򵥣������Ͷ����ˣ�����������ʣ�"
                        + "\r\n��һ�²˵���ġ����ڡ�����>��������Ϣ�����������ҵĲ���"
                        + "\r\n��ַ����������������ϸ˵����");
                jta.setEditable(false);
                jDialog.add(jta);
            }
        };
        JMIInstruction.addActionListener(actionAboutInstruction);

        ActionListener actionSaveHTMLOpenFile = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile openFile = new OpenFile();
                openFile.open();
                String path = openFile.getFilePath();
                OpenFile.lastPath = path;
                JTFSaveHTMLPath.setText(path);
            }
        };
        JBSaveHTMLOpenFile.addActionListener(actionSaveHTMLOpenFile);

        ActionListener actionPlainIPOpenFile = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile openFile = new OpenFile();
                openFile.open();
                String path = openFile.getFilePath();
                OpenFile.lastPath = path;
                JTFPlainIPs.setText(path);
            }
        };
        JBIPOpenFile.addActionListener(actionPlainIPOpenFile);

        ActionListener actionSaveIPOpenFile = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile openFile = new OpenFile();
                openFile.open();
                String path = openFile.getFilePath();
                OpenFile.lastPath = path;
                JTFSavePlainIPs.setText(path);
            }
        };
        JBSaveIPOpenFile.addActionListener(actionSaveIPOpenFile);

        ActionListener actionSaveXMLOpenFile = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile openFile = new OpenFile();
                openFile.open();
                String path = openFile.getFilePath();
                OpenFile.lastPath = path;
                JTFSaveXMLPath.setText(path);
            }
        };
        JBSaveXMLOpenFile.addActionListener(actionSaveXMLOpenFile);

        ActionListener actionSaveTXTOpenFile = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                OpenFile openFile = new OpenFile();
                openFile.open();
                String path = openFile.getFilePath();
                OpenFile.lastPath = path;
                JTFSaveTXTPath.setText(path);
            }
        };
        JBSaveTXTOpenFile.addActionListener(actionSaveTXTOpenFile);

        ActionListener actionGetIP = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent click) {
                String savePlainIPs = JTFSavePlainIPs.getText();

                if (savePlainIPs.equals("")) {
                    JTARunInfo.append("path error\r\n�����뱣��IP��·��\r\n");
                } else {
                    final String plainIPsPath = savePlainIPs + "\\plainIPs.txt";

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // ��ʱ��ʼ
                                long t1 = System.currentTimeMillis();

                                // ��ȡ���п���IP
                                List<IPBean> ips = XiciEntrance.getIPBeanList(1);

                                // ������п���IP������
                                JTARunInfo.append("���յõ� " + ips.size() + "�����ô���IP���£�" + "\r\n");
                                for (int i = 0; i < ips.size(); i++) {
                                    JTARunInfo.append(ips.get(i).getIp() + ":" + ips.get(i).getPort() + "\r\n");
                                }

                                // �����ʱ
                                long t2 = System.currentTimeMillis();
                                System.out.println("��ȡ����IP��ʱ" + (double) (t2 - t1) / 60000 + "����");
                                JTARunInfo.append("��ȡ����IP��ʱ" + (double) (t2 - t1) / 60000 + "����" + "\r\n");

                                // ��ſ���IP���ļ���
                                FileOperation.write2txt(ips, plainIPsPath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        };
        JBGetIP.addActionListener(actionGetIP);

        ActionListener actionCrawl = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent click) {
                String searchwords, saveHTMLPath, saveTXTPath, SaveXMLPath, plainIPsPath, pageNum;
                searchwords = JTASearchWords.getText();
                saveHTMLPath = JTFSaveHTMLPath.getText();
                saveTXTPath = JTFSaveTXTPath.getText();
                SaveXMLPath = JTFSaveXMLPath.getText();
                plainIPsPath = JTFPlainIPs.getText();
                pageNum = JTFPages.getText();
                final String[] crawlerArgs = {searchwords, saveHTMLPath, saveTXTPath, SaveXMLPath, plainIPsPath, pageNum};

                int ifCrawlFlag = 0;
                for (int i = 0; i < 6; i++) {
                    if (crawlerArgs[i].equals("")) {
                        switch (i) {
                            case 0:
                                ifCrawlFlag++;
                                System.out.println("error\r\n�����������ؼ���");
                                JTARunInfo.append("error\r\n�����������ؼ���\r\n");
                                break;
                            case 1:
                                ifCrawlFlag++;
                                System.out.println("error\r\n�����뱣��html�ļ�·��");
                                JTARunInfo
                                        .append("error\r\n�����뱣��html�ļ�·��\r\n");
                                break;
                            case 2:
                                ifCrawlFlag++;
                                System.out.println("error\r\n�����뱣��txt�ļ�·��");
                                JTARunInfo.append("error\r\n�����뱣��txt�ļ�·��\r\n");
                                break;
                            case 3:
                                ifCrawlFlag++;
                                System.out.println("error\r\n�����뱣��xml�ļ�·��");
                                JTARunInfo.append("error\r\n�����뱣��xml�ļ�·��\r\n");
                                break;
                            case 4:
                                ifCrawlFlag++;
                                System.out.println("error\r\n��������ô���IP�ļ�·��(plainIPs.txt)");
                                JTARunInfo.append("error\r\n��������ô���IP�ļ�·��(plainIPs.txt)\r\n");
                                break;
                            case 5:
                                //ifCrawlFlag++;
                                System.out.println("warning\r\nδָ����ȡҳ����Ĭ�Ͻ���ȡ���ҳ��50ҳ");
                                JTARunInfo.append("warning\r\nδָ����ȡҳ����Ĭ�Ͻ���ȡ���ҳ��50ҳ\r\n");
                                crawlerArgs[5] = "50";
                                break;
                        }
                    }
                }
                if (ifCrawlFlag == 0) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Crawler c = new Crawler();
                                c.excute(crawlerArgs, JTARunInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        };
        JBCrawl.addActionListener(actionCrawl);

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                final JWindowsFrame frame = new JWindowsFrame();

                frame.setTitle("WeiboCrawler");

                frame.setBounds((SCREEN_WIDTH - WINDOW_WIDTH) / 2, (SCREEN_HEIGHT - WINDOW_HEIGHT) / 2, WINDOW_WIDTH, WINDOW_HEIGHT);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(false);
                frame.action();
            }
        });
    }
}
