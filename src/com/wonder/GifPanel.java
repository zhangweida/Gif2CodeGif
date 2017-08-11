package com.wonder;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by dell on 2017/8/4.
 */
public class GifPanel extends JPanel {

    BufferedImage[] charImgs = null;
    BufferedImage background = null;
    Integer count = 0;
    Integer fps = 6;

    public GifPanel() {
        //获取字符图片
        charImgs = ImgToChar.charImgs;
        if(charImgs == null) return;

        //初始化gif图片背景
        initCharGifBackground(charImgs);
        MyThread thread = new MyThread();
        thread.start();
    }

    private void initCharGifBackground(BufferedImage[] charImgs) {
        background = new BufferedImage(MainFrame.WIDTH, MainFrame.HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics g = background.getGraphics();
        //背景颜色为白色
        g.setColor(Color.white);
        g.fillRect(100, 100, MainFrame.WIDTH, MainFrame.HEIGHT);
        g.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if(charImgs == null) return;

        int size = charImgs.length;
        int index = count % size;
        BufferedImage img = charImgs[index];
        int w = img.getWidth();
        int h = img.getHeight();
        int x = (MainFrame.WIDTH - w)/2;
        int y = (MainFrame.HEIGHT - h)/2;
        //背景涂白
        g.setColor(Color.white);
        g.fillRect(0, 0, MainFrame.WIDTH, MainFrame.HEIGHT);
        g.setColor(Color.black);
        // 3/4为显示比例，考虑到有的gif图片太大，所以缩小至一定比例在窗口中显示
        g.drawImage(img, 0, 0, w/3, h/3, null);
        count++;
    }

    class MyThread extends Thread {

        long startTime;
        long endTime;
        long sleepTime;
        long spendTime;
        long period = 1000/fps;
        @Override
        public void run() {
            while(true) {
                startTime = System.currentTimeMillis();
                repaint();
                endTime = System.currentTimeMillis();
                spendTime = endTime - startTime;
                sleepTime = period - spendTime;
                if(sleepTime < 0)
                    sleepTime = 2;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }
}
