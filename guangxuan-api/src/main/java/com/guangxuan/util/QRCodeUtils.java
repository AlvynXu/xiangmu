package com.guangxuan.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.io.OutputStream;

public class QRCodeUtils {


    public static void generateQRCodeImage(String text, int width, int height, OutputStream outputStream) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToStream(bitMatrix, "JPG", outputStream);
    }

//    public static void main(String[] args) throws IOException, WriterException {
//
//        // 填加文字
//        // 创建图片
//        BufferedImage image = new BufferedImage(200, 50,
//                BufferedImage.TYPE_INT_BGR);
//        Graphics g = image.getGraphics();
//        g.setClip(0, 0, 50, 20);
//        g.setColor(Color.black);
//        // 先用黑色填充整张图片,也就是背景
//        g.fillRect(0, 0, 50, 20);
//        // 在换成红色
//        g.setColor(Color.red);
//        // 设置画笔字体
//        Font font = new Font("黑体", Font.BOLD, 20);
//        g.setFont(font);
//        /** 用于获得垂直居中y */
//        Rectangle clip = g.getClipBounds();
//        FontMetrics fm = g.getFontMetrics(font);
//        int ascent = fm.getAscent();
//        int descent = fm.getDescent();
//        int y = (clip.height - (ascent + descent)) / 2 + ascent;
//        // 256 340 0 680
//        for (int i = 0; i < 6; i++) {
//            // 画出字符串
//            g.drawString("123345", i * 680, y);
//        }
//        g.dispose();
//        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//        BitMatrix bitMatrix = qrCodeWriter.encode("http://baidu.comads'fas;dlkfaskdfl;aksgfkasfkdskdfsdafk;", BarcodeFormat.QR_CODE, 110, 110);
//        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
//        Thumbnails.of(new File("D:\\1.png")).size(414, 890)
//                .watermark(new Coordinate(150, 390), bufferedImage, 1f)
//                .watermark(new Coordinate(100, 50), image, 1f)
//                .toFile("D:\\2_cat.jpg");
//    }

}

