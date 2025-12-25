package com.wishare.contract.apis.file.utils;

import com.github.rholder.retry.Retryer;
import com.github.rholder.retry.RetryerBuilder;
import com.github.rholder.retry.StopStrategies;
import com.github.rholder.retry.WaitStrategies;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FileUtil {

    private static final Retryer<File> retryer = RetryerBuilder.<File>newBuilder()
            .retryIfResult(result -> result == null || !result.exists() || result.length() <= 0)
            .withStopStrategy(StopStrategies.stopAfterAttempt(5))
            .withWaitStrategy(WaitStrategies.fixedWait(5, TimeUnit.SECONDS))
            .build();

    public static MultipartFile getMultipartFile(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        FileInputStream inputStream = null;
        OutputStream outputStream = null;
        try {
//            File file = getFile(url);
            File file = retryer.call(() -> getFile(url));
            FileItem fileItem = new DiskFileItem("formFieldName",//form表单文件控件的名字随便起
                    Files.probeContentType(file.toPath()),//文件类型
                    false, //是否是表单字段
                    file.getName(),//原始文件名
                    (int) file.length(),//Interger的最大值可以存储两部1G的电影
                    file.getParentFile());//文件会在哪个目录创建
            //为DiskFileItem的OutputStream赋值
            inputStream = new FileInputStream(file);
            outputStream = fileItem.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            return new CommonsMultipartFile(fileItem);
        } catch (Exception e) {
            log.error("文件类型转换失败:url:{}", url, e);
            return null;
        } finally {
            try {
                if (null != inputStream) {
                    inputStream.close();
                }

                if (null != outputStream) {
                    outputStream.close();
                }
            } catch (IOException e) {
                log.error(">>文件流关闭失败" + e.getMessage());
            }
        }

    }

    public static File getFile(String url) throws Exception {
        //对本地文件命名
        String fileName = url.substring(url.lastIndexOf("."));
        if (StringUtils.isNotBlank(fileName) && (fileName.contains("/") || fileName.contains("?"))) {
//            fileName = fileName.substring(fileName.lastIndexOf("=")) + ".pdf";
            fileName = ".pdf";
        }
        File file = null;

        URL urlfile;
        InputStream inStream = null;
        OutputStream os = null;
        HttpURLConnection connection = null;
        try {
            file = File.createTempFile("fwj_url", fileName);
            //下载
            urlfile = new URL(url);
            connection = (HttpURLConnection)urlfile.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            inStream = connection.getInputStream();
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = inStream.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            log.error("临时文件保存错误:url:{} {}", url, e.getMessage(), e);
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != inStream) {
                    inStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

}
