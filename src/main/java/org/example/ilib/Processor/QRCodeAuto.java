package org.example.ilib.Processor;

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
        String data = path; // URL hoặc thông tin giao dịch
        int size = 300;

        // Đường dẫn tới thư mục org/assets trong thư mục gốc của dự án
        String resourcePath = "org" + File.separator + "assets";
        File directory = new File(resourcePath);

        // Kiểm tra và tạo thư mục nếu chưa tồn tại
        if (!directory.exists()) {
            directory.mkdirs(); // Tạo thư mục nếu chưa tồn tại
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

        // Ghi file mã QR vào thư mục đã chỉ định
        ImageIO.write(image, "png", outputFile);

        // Trả về đường dẫn với định dạng "org/assets/qrcode.png"
        return "/org/assets/qrcode.png";
    }
}
