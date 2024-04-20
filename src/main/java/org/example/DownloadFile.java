package org.example;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class DownloadFile {
    public static void downloadFileStatus( String fileURL) {
        try {
            downloadFile(fileURL, Constant.ZIP_FOLDER_PATH);
            System.out.println("File downloaded successfully!");
        } catch (IOException e) {
            System.out.println("Error downloading the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void downloadFile(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        String fileName = "";

        // Extracting the file name from the URL
        String disposition = connection.getHeaderField("Content-Disposition");
        if (disposition != null) {
            int index = disposition.indexOf("filename=");
            if (index > 0) {
                fileName = disposition.substring(index + 10, disposition.length() - 1);
            }
        } else {
            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1);
        }

        // Opens a file output stream to save into the specified directory
        OutputStream outputStream = new FileOutputStream(saveDir + File.separator + fileName);

        int bytesRead;
        byte[] buffer = new byte[4096];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        inputStream.close();
    }
}
