package weibo.ui;

import weibo.Xici.IPBean;
import weibo.Xici.XiciEntrance;
import weibo.utils.FileOperation;
import weibo.collector.WeiboController;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintStream;
import java.util.List;

public class JWindowsFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    static Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    static int SCREEN_WIDTH = scrSize.width;
    static int SCREEN_HEIGHT = scrSize.height;
    static int WINDOW_WIDTH = 700;
    static int WINDOW_HEIGHT = 565;
    static int AUTHOR_WIDTH = 290;
    static int AUTHOR_HEIGHT = 280;
    static int INSTRUCTION_WIDTH = 380;
    static int INSTRUCTION_HEIGHT = 230;

    private JMenuItem JMIAuthor, JMIInstruction;
    private JMenu JMAbout;
    private JMenuBar JMBOperation;

    private JScrollBar JScroB;
    private JScrollPane JSPRunInfo;
    private JScrollPane JSPSearchWords;

    public static JTextArea JTARunInfo;
    private JTextArea JTASearchWords;

    private JPanel JWordsPanel;
    private JPanel JPathPanel;
    private JPanel JButtonPanel;
    private JPanel JLeftPanel;
    private JPanel JRightPanel;
    private JPanel JPagesPanel;

    private JLabel JLSearchWords;
    private JLabel JLRunInfo;
    private JLabel JLpages;

    final private int PATH_FIELD_LENGTH = 32;
    private JTextField JTFPages;

    private JButton JBCrawl;

    public JWindowsFrame() {

        // 设置菜单选项
        JMBOperation = new JMenuBar();
        JMIAuthor = new JMenuItem("软件作者");
        JMIInstruction = new JMenuItem("使用说明");
        JMAbout = new JMenu("关于");
        JMAbout.add(JMIAuthor);
        JMAbout.add(JMIInstruction);
        JMBOperation.add(JMAbout);
        setJMenuBar(JMBOperation);

        JLeftPanel = new JPanel();
        JLeftPanel.setLayout(new BorderLayout());

        JRightPanel = new JPanel();
        JRightPanel.setLayout(new BorderLayout());

        JScroB = new JScrollBar();
        JScroB.setVisible(true);

        JLRunInfo = new JLabel("相关信息");

        JTARunInfo = new JTextArea();
        JTARunInfo.setEditable(false);// 设置文本框是否可以编辑

        JSPRunInfo = new JScrollPane(JTARunInfo);
        JSPRunInfo.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSPRunInfo.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSPRunInfo.add(JScroB);

        JRightPanel.add(JLRunInfo, BorderLayout.NORTH);
        JRightPanel.add(JSPRunInfo);

        JWordsPanel = new JPanel();
        JWordsPanel.setLayout(new BorderLayout());

        JLSearchWords = new JLabel("输入搜索话题，关键词用换行分开");
        // 话题之间用换行分开

        JTASearchWords = new JTextArea(8, 1);
        JTASearchWords.setEditable(true);
        JTASearchWords.setText("摄影");

        JSPSearchWords = new JScrollPane(JTASearchWords);
        JSPSearchWords.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        JSPSearchWords.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JSPSearchWords.add(JScroB);

        JWordsPanel.add(JLSearchWords, BorderLayout.CENTER);
        JWordsPanel.add(JSPSearchWords, BorderLayout.SOUTH);

        JPathPanel = new JPanel();
        JPathPanel.setLayout(new GridLayout(9, 1));

        JLpages = new JLabel("设置每个关键词获取页数(1~50，默认50): ");

        JTFPages = new JTextField(PATH_FIELD_LENGTH / 10);
        JTFPages.setEditable(true);
        JTFPages.setText("8");

        JPagesPanel = new JPanel();
        JPagesPanel.add(JLpages, BorderLayout.WEST);
        JPagesPanel.add(JTFPages, BorderLayout.EAST);
        JPathPanel.add(JPagesPanel);

        JBCrawl = new JButton("爬取微博");

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

                jDialog.setTitle("作者信息");

                jDialog.setBounds((SCREEN_WIDTH - AUTHOR_WIDTH) / 2, (SCREEN_HEIGHT - AUTHOR_HEIGHT) / 2, AUTHOR_WIDTH, AUTHOR_HEIGHT);
                JTextArea jta = new JTextArea();
                jta.setBackground(getBackground());
                jta.setText("软件作者：call me jack." + "\r\n");
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

                jDialog.setTitle("使用说明");
                jDialog.setResizable(false);

                jDialog.setBounds((SCREEN_WIDTH - INSTRUCTION_WIDTH) / 2, (SCREEN_HEIGHT - INSTRUCTION_HEIGHT) / 2, INSTRUCTION_WIDTH, INSTRUCTION_HEIGHT);
                JTextArea jta = new JTextArea();
                jta.setBackground(getBackground());
                jta.setText("基于代理IP的新浪微博数据获取器，有效躲避了新浪微博的反爬虫机制，能够全自动连续获取相关微博数据。");
                jta.setEditable(false);
                jDialog.add(jta);
            }
        };
        JMIInstruction.addActionListener(actionAboutInstruction);

        ActionListener actionCrawl = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent click) {
                final String searchwords = JTASearchWords.getText();
                final int pageNum = Integer.parseInt(JTFPages.getText());
                final String[] crawlerArgs = {searchwords, pageNum + ""};

                if (searchwords.equals("")) {
                    JTARunInfo.append("error\r\n请输入搜索关键词\r\n");
                    return;
                }
                if (pageNum < 0 || pageNum > 50) {
                    JTARunInfo.append("warning\r\n未指定获取页数，默认将获取最大页数50页\r\n");
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            for (String keyword : searchwords.trim().split("\n")) {
                                // 设置webcollector参数
                                WeiboController controller = new WeiboController("Data/env", false);// 网页、图片、文件被存储在download文件夹中
                                controller.setThreads(3);
                                controller.addRegex("http://m.weibo.cn/.*");

                                /*对某人微博前5页进行爬取*/
                                for (int i = 1; i <= pageNum; i++) {
                                    // 添加爬取种子,也就是需要爬取的网站地址,以及爬取深度
                                    controller.addSeed(new CrawlDatum("https://s.weibo.com/weibo?q=" + keyword + "&page=" + i).meta("pageNum", i + ""));
                                }
                                // 爬取深度
                                controller.start(1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        };
        JBCrawl.addActionListener(actionCrawl);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                final JWindowsFrame frame = new JWindowsFrame();

                // 设置输出流到gui中
                System.setOut(new PrintStream(new JTextAreaOutputStream(JTARunInfo)));

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
