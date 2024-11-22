package org.example.ilib.Processor;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCode {
    public static void main(String[] args) {
        String data = "https://payment.yoursite.com/transaction?id=12345"; // URL hoặc thông tin giao dịch
        int size = 300;

        try {
            // Đường dẫn tới thư mục src/main/resources/org/assets
            String resourcePath = "src/main/resources/org/assets";
            File directory = new File(resourcePath);

            // Kiểm tra và tạo thư mục nếu chưa tồn tại
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Lưu file mã QR
            File outputFile = new File(directory, "qrcode.png");
            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }

            ImageIO.write(image, "png", outputFile);
            System.out.println("QR Code created: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
