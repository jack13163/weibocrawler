package WeiboCrawler2.weibo.collector;


import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * ����Selenium��ȡ��½����΢��weibo.cn��cookie
 *
 * @author hu
 */
public class WeiboCN {

    /**
     * ��ȡ����΢����cookie������������weibo.cn��Ч����weibo.com��Ч weibo.cn��������ʽ�������ݣ���ʹ��С��
     * �����ֻ��˵�½API
     *
     * @param username ����΢���û���
     * @param password ����΢������
     * @return
     * @throws Exception
     */
    public static String getSinaCookie(String username, String password) throws Exception {
        StringBuilder sb = new StringBuilder();
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.setJavascriptEnabled(true);

        driver.get("https://passport.weibo.cn/signin/login");

        WebElement mobile = driver.findElementByCssSelector("input[id=loginName]");
        mobile.sendKeys(username);
        WebElement pass = driver.findElementByCssSelector("input[id=loginPassword]");
        pass.sendKeys(password);
        WebElement submit = driver.findElementByCssSelector("a#loginAction");
        submit.click();
        String result = concatCookie(driver);
        System.out.println("Get Cookie: " + result);
        driver.close();

        if (result.contains("login")) {
            return result;
        } else {
            throw new Exception("weibo login failed");
        }
    }

    public static String concatCookie(HtmlUnitDriver driver) {
        Set<Cookie> cookieSet = driver.manage().getCookies();
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookieSet) {
            sb.append(cookie.getName() + "=" + cookie.getValue() + ";");
        }
        String result = sb.toString();
        return result;
    }

    public static class CaptchaFrame {
        JFrame frame;
        JPanel panel;
        JTextField input;
        int inputWidth = 100;
        BufferedImage img;
        String userInput = null;

        public CaptchaFrame(BufferedImage img) {
            this.img = img;
        }

        public String getUserInput() {
            frame = new JFrame("������֤��");
            final int imgWidth = img.getWidth();
            final int imgHeight = img.getHeight();
            int width = imgWidth * 2 + inputWidth * 2;
            int height = imgHeight * 2 + 50;
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            int startx = (dim.width - width) / 2;
            int starty = (dim.height - height) / 2;
            frame.setBounds(startx, starty, width, height);
            Container container = frame.getContentPane();
            container.setLayout(new BorderLayout());
            panel = new JPanel() {
                @Override
                public void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(img, 0, 0, imgWidth * 2, imgHeight * 2, null);
                }
            };
            panel.setLayout(null);
            container.add(panel);
            input = new JTextField(6);
            input.setBounds(imgWidth * 2, 0, inputWidth, imgHeight * 2);
            panel.add(input);
            JButton btn = new JButton("��¼");
            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    userInput = input.getText().trim();
                    synchronized (CaptchaFrame.this) {
                        CaptchaFrame.this.notify();
                    }
                }
            });
            btn.setBounds(imgWidth * 2 + inputWidth, 0, inputWidth, imgHeight * 2);
            panel.add(btn);
            frame.setVisible(true);
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            frame.dispose();
            return userInput;
        }
    }
}