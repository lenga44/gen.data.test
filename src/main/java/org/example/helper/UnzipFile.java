package org.example.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {
    @Deprecated
    public static void unzip(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        // Tạo thư mục đích nếu nó không tồn tại
        if (!dir.exists()) {
            dir.mkdirs();
        }
        FileInputStream fis = new FileInputStream(zipFilePath);
        ZipInputStream zipInputStream = new ZipInputStream(fis);
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // Truy cập từng phần tử trong tập tin ZIP
        while (zipEntry != null) {
            String filePath = destDir + File.separator + zipEntry.getName();
            if (!zipEntry.isDirectory()) {
                // Nếu đây là một tập tin, giải nén nó
                extractFile(zipInputStream, filePath);
            } else {
                // Nếu đây là một thư mục, tạo thư mục mới
                File dir1 = new File(filePath);
                dir1.mkdir();
            }
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

    private static void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        System.out.println(filePath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[1024];
        int read;
        while ((read = zipInputStream.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
