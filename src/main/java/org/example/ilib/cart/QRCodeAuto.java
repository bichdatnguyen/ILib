package org.example.ilib.cart;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeAuto {

    public static String taoQrCode(String path) throws IOException, WriterException {
        String data = path;
        int size = 300;

        String resourcePath = "src/main/resources/org/assets";
        File directory = new File(resourcePath);

        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
        }

        // Lưu file mã QR vào thư mục
        File outputFile = new File(directory, "qrcode.png");
        BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }

        ImageIO.write(image, "png", outputFile);

        return "file:" + outputFile.getAbsolutePath();
    }

}
