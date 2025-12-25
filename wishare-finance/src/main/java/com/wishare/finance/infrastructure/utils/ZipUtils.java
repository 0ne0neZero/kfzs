package com.wishare.finance.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * pdf文件下载压缩
 *
 * @author: scx
 * @date: 2024/6/5
 */
@Slf4j
public class ZipUtils {

    private ZipUtils() {}

    public static InputStream downloadPdfFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();
        log.info("downloadPdfFromUrl httpStatus is :{}；url is ：{}", responseCode, urlString);
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("Failed to retrieve PDF: HTTP error code : " + responseCode);
        }
    }

    public static void doWrite(String url, String name, ZipOutputStream zipOut) {
        try (InputStream pdfInputStream = ZipUtils.downloadPdfFromUrl(url);
             ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream()) {

            IOUtils.copy(pdfInputStream, pdfOutputStream);
            byte[] pdfBytes = pdfOutputStream.toByteArray();

            ZipEntry zipEntry = new ZipEntry(name + ".pdf");
            zipOut.putNextEntry(zipEntry);
            zipOut.write(pdfBytes);
            zipOut.closeEntry();

        } catch (IOException e) {
            // 处理单个PDF下载失败的情况
            // 这里可以记录更详细的日志，或者考虑其他的异常处理方式
            log.error("大有秋批量下载异常: ",e);
        }
    }
}
