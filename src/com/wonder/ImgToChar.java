package com.wonder;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

/**
 * Created by dell on 2017/8/3.
 */
public class ImgToChar {

    static String defaultPath = "D:\\temps\\realPerson.jpg";
    public static BufferedImage[] realImgs;
    public static BufferedImage[] charImgs;
    private static int size = 4;
    private static String[] chars= {" ",".",":",";","-","~","1","i","o","r","a",
            "2","c","3","b","n","q","k","x","S","X",
            "7","Z","O","8","#","$","%","&","M","B",
            "W","@","@"};

    public static void gif2Char(JFrame frame) {
        //获取像素与字符比例
        int interval = 16777215 / (chars.length-1);
        if(realImgs == null) return;

        //取出每张图片,转成对应的code图片
        BufferedImage oneRealImg = null;
        for(int i=0; i<realImgs.length; i++) {
            oneRealImg = realImgs[i];
            Integer width = oneRealImg.getWidth();
            Integer height = oneRealImg.getHeight();
            BufferedImage oneCharImg = new BufferedImage(width*3, height*3, BufferedImage.TYPE_INT_RGB);
            Graphics g = oneCharImg.getGraphics();
            g.setColor(Color.red);
            g.fillRect(100, 100, width, height);
            frame.setBounds(100, 100, width, height);
            g.setFont(new Font("楷体", Font.BOLD, 20));

            String codeText = MainFrame.filePath + ".text";
            BufferedWriter codeOutStream = null;
            try {
                codeOutStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(codeText)));
            } catch (FileNotFoundException e) {
                System.out.println("文本路径不存在");
            }

            //将每块像素的平均值用字符代替
            for(int h=0; h<height; h++) {
                for(int w=0; w<width; w++) {
                    int rgb = avgRGB(w, h, oneRealImg);
                    int avg = rgb / interval;
                    g.drawString(chars[avg], 12*w/size, 12*h/size);
                    try {
                        codeOutStream.write(chars[avg]);
                    } catch (IOException e) {
                        System.out.println("文本文件空");
                    }
                }

                try {
                    codeOutStream.write('\n');
                } catch (IOException e) {
                    System.out.println("写个回车都有问题");
                }
            }

            try {
                codeOutStream.flush();
                codeOutStream.close();
            } catch (IOException e) {
                System.out.println("芝麻关门");
            }

            g.dispose();
            charImgs[i] = oneCharImg;
        }


    }

    private static int avgRGB(int w, int h, BufferedImage oneRealImg) {
        Integer result = 0;

        for(int m=0; m<size; m++) {
            for(int n=0; n<size; n++) {
                if(m+w <oneRealImg.getWidth() && n+h < oneRealImg.getHeight()) {
                    result = oneRealImg.getRGB(m+w, n+h);
                }
            }
        }

        return Math.abs(result) / size * size;
    }

    public static void readGif(String path) {
        try {
            //读取文件
            File file = new File(path);
            if(!file.exists()) {
                file = new File(defaultPath);
            }

            //获取文件名和文件类型
            String fileName = file.getName();
            String suffix = null;
            if(fileName.contains(".")) {
                suffix = fileName.substring(fileName.lastIndexOf(".")+1);
            }
            if(suffix == null) return;

            //获取文件输入流并获取图片个数
            Iterator<ImageReader> it = ImageIO.getImageReadersBySuffix(suffix);
            ImageReader reader = it.next();
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            reader.setInput(iis);
            Integer count = reader.getNumImages(true);

            realImgs = new BufferedImage[count];
            charImgs = new BufferedImage[count];
            for(int i=0; i<count; i++) {
                realImgs[i] = reader.read(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把多张jpg图片合成一张
     * @param newPic String 生成的gif文件名 包含路径
     */
    public static synchronized void jpgToGif(String newPic) {
        try {
            AnimatedGifEncoder e = new AnimatedGifEncoder();
            e.setRepeat(0);
            e.start(newPic);
            BufferedImage src[] = charImgs;
            for (int i = 0; i < src.length; i++) {
                e.setDelay(200); //设置播放的延迟时间
                e.addFrame(src[i]);  //添加到帧中
            }
            e.finish();
        } catch (Exception e) {
            System.out.println( "jpgToGif Failed:");
            e.printStackTrace();
        }
    }
}
