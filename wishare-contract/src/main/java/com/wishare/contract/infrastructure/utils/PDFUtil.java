package com.wishare.contract.infrastructure.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author luzhonghe
 * @date 2023/3/27
 * @Description:
 */
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
        if(data != null){
            template.process(data, writer);
        }
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.createPDF(outputStream);
        byte[] fileBytes = outputStream.toByteArray();
        outputStream.close();
        renderer.finishPDF();
        return fileBytes;
    }

    /**
     * HTML 转 PDF
     * @param content html内容
     * @return PDF字节数组
     */
    public static byte[] htmlToPdf(String content) {
        ByteArrayOutputStream outputStream = null;
        try {
            Document document = new Document();
            outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,new ByteArrayInputStream(content.getBytes()), null, Charset.forName("UTF-8"));
            document.close();
        } catch (Exception e) {
            System.out.println("------生成pdf失败-------");
        }
        return outputStream.toByteArray();
    }

    public void test(){

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