package com.wishare.finance.infrastructure.pdf;

import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luzhonghe
 * @date 2023/3/27
 * @Description:
 */
@Slf4j
public class PDFUtil {


    /**
     * freemarker 配置
     */
    private static volatile Configuration cfg;

    /**
     * 单例获取freemarker配置
     * @return
     */
    public static Configuration getConfigure() {
        if (cfg == null) {
            synchronized (PDFUtil.class) {
                if (cfg == null) {
                    // 配置 Freemarker
                    cfg = new Configuration(Configuration.VERSION_2_3_31);
                    cfg.setClassForTemplateLoading(PDFUtil.class, "/");
                    cfg.setDefaultEncoding("UTF-8");
                }
            }
        }
        return cfg;
    }

    /**
     * 获取生成的html代码
     * @param templatePath ftl模板路径
     * @param data 填充数据
     * @return
     * @throws Exception
     */
    public static String getHtml(String templatePath, Map<String, Object> data) throws Exception {
        Configuration configure = getConfigure();
        // 加载模板
        Template template = configure.getTemplate(templatePath);
        // 设置模板参数
        StringWriter writer = new StringWriter();
        template.process(data, writer);
        // 将 HTML 渲染为 PDF
        return writer.toString();
    }

    public static ITextRenderer getRenderer(String templatePath, Map<String, Object> data) throws Exception {
        // 将 HTML 渲染为 PDF
        String html = getHtml(templatePath, data);
        ITextRenderer renderer = new ITextRenderer();
        // 添加中文字体支持
        String fontPath = PDFUtil.class.getResource("/fonts/simsun.ttf").toURI().toURL().toString();
        renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        renderer.setDocumentFromString(html);
        renderer.layout();
        return renderer;
    }


    /**
     * 生成本地PDF
     * （测试用）
     * @param templatePath ftl模板路径
     * @param outputPath 本地保存路径 如 xxx.pdf
     * @param data 模板填充数据
     * @throws Exception
     */
    public static void generateLocalPdf(String templatePath, String outputPath, Map<String, Object> data) throws Exception {
        ITextRenderer renderer = getRenderer(templatePath, data);
        OutputStream os = new FileOutputStream(outputPath);
        renderer.createPDF(os);
        // 关闭流
        os.close();
    }

    /**
     * 生成pdf byte数组数据
     * @param templatePath ftl模板路径
     * @param data 本地保存路径 如 xxx.pdf
     * @return
     * @throws Exception
     */
    public static byte[] getByteData(String templatePath, Map<String, Object> data) throws Exception {
        ITextRenderer renderer = getRenderer(templatePath, data);
        log.info("after getRenderer");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        log.info("after renderer.createPDF");
        byte[] fileBytes = outputStream.toByteArray();
        outputStream.close();
        renderer.finishPDF();
        log.info("after renderer.finishPDF");
        return fileBytes;
    }


//    public static void main(String[] args) throws Exception {
//        // 生成 PDF 文件
//        String templatePath = PDFTemplate.RECEIPT;
//        String outputPath = "output7.pdf";
//        Map<String, Object> data = new HashMap<>();
//        data.put("remark", "World");
//        generatePdf(templatePath, outputPath, data);
//    }
}