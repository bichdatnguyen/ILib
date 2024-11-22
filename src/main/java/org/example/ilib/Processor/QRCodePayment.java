package org.example.ilib.Processor;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodePayment {

    public static void main(String[] args) {
        double money = 100.50; // Số tiền thanh toán
        String transactionId = "12345"; // Mã giao dịch (có thể được tạo ngẫu nhiên hoặc lấy từ hệ thống)

        // Tạo URL thanh toán, có thể thay bằng cổng thanh toán thực tế
        String paymentUrl = "https://payment.yoursite.com/transaction?id=" + transactionId + "&amount=" + money;

        int size = 300; // Kích thước mã QR

        try {
            // Tạo mã QR từ URL thanh toán
            BitMatrix bitMatrix = new QRCodeWriter().encode(paymentUrl, BarcodeFormat.QR_CODE, size, size);
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            // Lưu mã QR vào thư mục src/main/resources/org/assets
            String resourcePath = "src/main/resources/org/assets";
            File directory = new File(resourcePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File outputFile = new File(directory, "qrcode_payment.png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("QR Code created for payment: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
