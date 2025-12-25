package com.wishare.finance.apis.invoicereceipt;

import com.wishare.finance.domains.shortcode.ShortCodeService;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Api(tags = {"发票下载接口"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class InvoiceDownloadApi {

    private final ShortCodeService shortCodeService;

    private final HttpServletResponse response;

    @GetMapping("/invoice-download/{shortCode}")
    public void resolveShortUrl(@PathVariable("shortCode") String shortCode) throws IOException {

        if (StringUtils.isBlank(shortCode) || !shortCode.matches("[a-zA-Z0-9]+")
                || !shortCode.startsWith("22") || shortCode.length() != 8) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(getMessageHtml("参数错误，请确认链接是否正确。"));
            return;
        }

        String longUrl = shortCodeService.getLongUrl(shortCode);
        if (Objects.isNull(longUrl)) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(getMessageHtml("发票链接已失效，链接仅保留七日，如需要获取发票请联系管理员！"));
            return;
        }

        try {
            String[] fileUrls = longUrl.split(",");
            if (fileUrls.length == 1) {
                writeSingleFile(fileUrls[0]);
            } else {
                writeMultiFile(fileUrls);
            }
        } catch (Exception e) {
            log.error("发票文件下载错误", e);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().write(getMessageHtml("发票文件下载失败，请稍后重试，多次错误请联系管理员获取发票，很抱歉给您带来不便！"));
        }

    }

    @GetMapping("generateCode")
    @ApiOperation("生成短链码")
    public String generateCode() {
        return shortCodeService.createShortCode("https://bsp.fineland.cn/files/125322827237001/%E5%8F%91%E7%A5%A8/QueryInvoiceResultRV/160567264785137/20240321/1710986521995189.pdf,https://bsp.fineland.cn/files/125322827237001/%E5%8F%91%E7%A5%A8/QueryInvoiceResultRV/160567264785137/20240321/1710986521995189.pdf");
    }

    @GetMapping("/getLongUrl/{shortCode}")
    @ApiOperation("根据短链获取原始URL")
    public void getLongUrl(@PathVariable("shortCode") String shortCode) {
      try {
        response.sendRedirect(shortCodeService.getLongUrl(shortCode));
      } catch (IOException e) {
          log.error("InvoiceDownloadApi.getLongUrl() called with exception => 【shortCode = {}】", shortCode, e);
      }
    }

    /**
     * 写入单个文件输出流
     *
     * @param fileUrl 文件地址
     */
    private void writeSingleFile(String fileUrl) throws IOException {
        try {
            URL url = new URL(fileUrl);
            try (InputStream stream = url.openStream()) {
                response.setContentType("application/octet-stream");
                response.setHeader("content-type", "application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" +
                        URLEncoder.encode("电子发票_" + DateTimeUtil.nowDateTimeNoc() + ".pdf", StandardCharsets.UTF_8));

                ServletOutputStream outputStream = response.getOutputStream();
                byte[] bytes = new byte[4096];
                int line;
                while ((line = stream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, line);
                }
                outputStream.flush();
            }
        } catch (MalformedURLException e) {
            throw new IOException("下载文件失败，文件地址：" + fileUrl, e);
        }
    }

    /**
     * 写入多个文件输出流
     *
     * @param fileUrls 文件地址
     */
    private void writeMultiFile(String[] fileUrls) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        for (int i = 0; i < fileUrls.length; i++) {
            String fileUrl = fileUrls[i];
            URL url = new URL(fileUrl);
            try (InputStream stream = url.openStream()) {
                ZipEntry zipEntry = new ZipEntry("发票_" + (i + 1) + fileUrl.substring(fileUrl.lastIndexOf(".")));
                zos.putNextEntry(zipEntry);
                byte[] bytes = new byte[4096];
                int line;
                while ((line = stream.read(bytes)) != -1) {
                    zos.write(bytes, 0, line);
                }
                zos.closeEntry(); //关闭当前条目，准备下一个
            }
        }
        zos.finish();
        zos.close();

        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" +
                URLEncoder.encode("电子发票_" + DateTimeUtil.nowDateTimeNoc() + ".zip", StandardCharsets.UTF_8));

        // 将压缩包内容写入响应输出流
        response.getOutputStream().write(baos.toByteArray());
    }

    private String getMessageHtml(String message){
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>发票下载</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<span>" + message + "</span>\n" +
                "</body>\n" +
                "</html>";
    }

}
