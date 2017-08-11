package com.wonder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by dell on 2017/8/3.
 */
public class Client {

    static Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
               MainFrame mainFrame = new  MainFrame();
               //mainFrame.setUndecorated(true);
                mainFrame.setTitle("Gif 2 CodeGif");
               mainFrame.setVisible(true);
               mainFrame.setBounds(dimension.width/2 -150, dimension.height/2 -50, 300, 100);
               mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        });
    }
}

class MainFrame extends JFrame{

    static String filePath = "";
    public static Integer WIDTH = 0;
    public static Integer HEIGHT = 0;
    JFrame frame = null;

    public MainFrame() {
        setTitle("Gif 2 Code");
        setResizable(false);
        JPanel panel = new JPanel();
        Container container = getContentPane();
        JTextField text = new JTextField(10);

        JButton button = new JButton("选择图片");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                fc.showOpenDialog(null);
                File file = fc.getSelectedFile();
                text.setText(file.getAbsolutePath());
                initBounds(text.getText());
                JPanel pan = new GifPanel();
            }
        });

        text.setHorizontalAlignment(JTextField.LEFT);
        panel.add(text);
        panel.add(button);
        container.add(panel);

    }

    private void initBounds(String path) {
        frame = new JFrame();
        filePath = path;
        frame.setUndecorated(true);
        frame.setVisible(true);

        //读取获取到的gif，并获取合成gif的每张图片
        ImgToChar.readGif(path);
        //将图片转化成code
        ImgToChar.gif2Char(frame);
        BufferedImage[] codeImgs = ImgToChar.charImgs;
        if(codeImgs == null) return;

        //获取图片最大高和宽作为frame的宽和高
        for(BufferedImage img : codeImgs) {
            if(WIDTH < img.getWidth()) {
                WIDTH = img.getWidth();
            }
            if(HEIGHT < img.getHeight()) {
                HEIGHT = img.getHeight();
            }
        }
        String gifPath = path.substring(0, path.indexOf("."));
        gifPath += "Code.gif";
        ImgToChar.jpgToGif(gifPath);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container container = frame.getContentPane();
        JPanel pan = new GifPanel();
        container.add(pan);
    }

}
