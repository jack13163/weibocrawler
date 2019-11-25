package WeiboCrawler2.weibo.collector;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class YanzhengFrame {
    JFrame frame;
    JPanel panel;
    JTextField input;
    int inputWidth = 100;
    BufferedImage img;
    String userInput = null;

    public YanzhengFrame(BufferedImage img) {
        this.img = img;
    }

    public String getUserInput() {
        frame = new JFrame("输入验证码");
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
        JButton btn = new JButton("登录");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userInput = input.getText().trim();
                synchronized (this) {
                    this.notify();
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
