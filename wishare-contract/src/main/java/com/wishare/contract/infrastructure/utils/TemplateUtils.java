package com.wishare.contract.infrastructure.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.*;
import com.itextpdf.tool.xml.css.CssFilesImpl;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.HTML;
import com.itextpdf.tool.xml.html.TagProcessorFactory;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.template.ContractNewtemplateE;
import com.wishare.contract.domains.mapper.revision.template.ContractNewtemplateMapper;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.tools.starter.api.FileStorage;
import com.wishare.tools.starter.service.FileStorageService;
import com.wishare.tools.starter.vo.FileVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/29/14:02
 */
@Component
@Slf4j
public class TemplateUtils implements IOwlApiBase {


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractNewtemplateMapper contractNewtemplateMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FileStorage fileStorage;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private FileStorageService fileStorageService;

    public FileVo genTemplateInfo(String tempId, Object o, String name){

        ContractNewtemplateE contractNewtemplateE = contractNewtemplateMapper.selectById(tempId);

        String content = contractNewtemplateE.getFileContent();

        BeanChangeUtil<Object> t = new BeanChangeUtil<>();

        Map<String, Object> keyMap =  t.EntityToMapUtil(o, null);

        List<String> spanList = ReUtil.findAllGroup0("<span((?!/span>).)*data-wk-tag=\".*?\".*?</span>", content);
        for (String s : spanList){
            String key = ReUtil.getGroup1("<span.*?data-wk-tag=\"(.*?)\".*?</span>", s);
            System.out.println(key);
            String fieldName = ReUtil.delFirst("^[A-Za-z]*\\.", key);
            String value = "";
            if(ObjectUtil.isNotEmpty(keyMap.get(fieldName))){
                value = keyMap.get(fieldName).toString();
            }
            content = content.replaceAll("(<span((?!/span>).)*data-wk-tag=\"" + key + "\".*?)(\\{.*?\\})(</span>)", "$1" + value + "$4");
        }

        String html = "<html>\n" +
                "<head>\n" +
                "<style>\n" +
                "/**\n" +
                "* Copyright (c) Tiny Technologies, Inc. All rights reserved.\n" +
                "* Licensed under the LGPL or a commercial license.\n" +
                "* For LGPL see License.txt in the project root for license information.\n" +
                "* For commercial licenses see https://www.tiny.cloud/\n" +
                "*/\n" +
                "body {\n" +
                "  font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "  line-height: 1.4;\n" +
                "  padding: 60px;\n" +
                "  width: 595px;\n" +
                "  margin: 0 auto;\n" +
                "  border-radius: 4px;\n" +
                "  background: white;\n" +
                "  min-height: 100%;\n" +
                "}\n" +
                "p { margin: 5px 0;\n" +
                "  line-height: 1.5;\n" +
                "}\n" +
                "table {\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "table th,\n" +
                "table td {\n" +
                "  border: 1px solid #ccc;\n" +
                "  padding: 0.4rem;\n" +
                "}\n" +
                "*{\n" +
                "     font-family:  simsun, serif,-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, 'Open Sans', 'Helvetica Neue', sans-serif;\n" +
                "}\n" +
                "figure {\n" +
                "  display: table;\n" +
                "  margin: 1rem auto;\n" +
                "}\n" +
                "figure figcaption {\n" +
                "  color: #999;\n" +
                "  display: block;\n" +
                "  margin-top: 0.25rem;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "hr {\n" +
                "  border-color: #ccc;\n" +
                "  border-style: solid;\n" +
                "  border-width: 1px 0 0 0;\n" +
                "}\n" +
                "code {\n" +
                "  background-color: #e8e8e8;\n" +
                "  border-radius: 3px;\n" +
                "  padding: 0.1rem 0.2rem;\n" +
                "}\n" +
                ".mce-content-body:not([dir=rtl]) blockquote {\n" +
                "  border-left: 2px solid #ccc;\n" +
                "  margin-left: 1.5rem;\n" +
                "  padding-left: 1rem;\n" +
                "}\n" +
                ".mce-content-body[dir=rtl] blockquote {\n" +
                "  border-right: 2px solid #ccc;\n" +
                "  margin-right: 1.5rem;\n" +
                "  padding-right: 1rem;\n" +
                "}\n" +
                "\n" +
                "</style>\n" +
                "</head>\n" +
                "<body>\n" +
                content +
                "</body>\n" +
                "</html>";
        String fileName = name + ".docx";
        ByteArrayInputStream byteArrayInputStream = IoUtil.toUtf8Stream(html);
        String tenantId = contractNewtemplateE.getTenantId();
        FileVo fileVo = fileStorage.tmpSave(byteArrayInputStream.readAllBytes(), fileName , tenantId);
        return fileVo;
    }


    public void writePDFContent(String content, String path) {
        Document document = new Document(PageSize.A4);
        File file = FileUtil.file(path);
        PdfWriter pdfWriter = null;
        try (
                ByteArrayInputStream inputStream = IoUtil.toUtf8Stream(content);
        ) {
            pdfWriter = PdfWriter.getInstance(document, FileUtil.getOutputStream(file));
            document.open();
            TagProcessorFactory tagProcessorFactory = Tags.getHtmlTagProcessorFactory();
            tagProcessorFactory.removeProcessor(HTML.Tag.IMG);
            tagProcessorFactory.addProcessor(new ImageTagProcessor(), HTML.Tag.IMG);
            CssFilesImpl cssFiles = new CssFilesImpl();
            cssFiles.add(XMLWorkerHelper.getInstance().getDefaultCSS());
            StyleAttrCSSResolver cssResolver = new StyleAttrCSSResolver(cssFiles);
            HtmlPipelineContext hpc = new HtmlPipelineContext(new CssAppliersImpl(new FontProvider()));
            hpc.setAcceptUnknown(true).autoBookmark(true).setTagFactory(tagProcessorFactory);
            HtmlPipeline htmlPipeline = new HtmlPipeline(hpc, new PdfWriterPipeline(document, pdfWriter));
            CssResolverPipeline pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
            XMLWorker worker = new XMLWorker(pipeline, true);
            XMLParser parser = new XMLParser(true, worker, Charset.defaultCharset());
            parser.parse(inputStream, Charset.defaultCharset());
            pdfWriter.flush();
        } catch (Exception ex) {
            log.error("生成pdf异常", ex);
        } finally {
            document.close();
            if (pdfWriter != null) {
                pdfWriter.close();
            }
        }


    }

    public FileVo saveFile(String path){
        File file = new File(path);
        byte[] data;
        try (FileInputStream fis = new FileInputStream(path);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = fis.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            data = byteArrayOutputStream.toByteArray();
            FileVo fileVo = fileStorageService.tmpSave(data, file.getName(), tenantId());
            fileVo.setName(file.getName());
            return fileVo;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void test(String path){
        File inputWord = new File(path);
        File outputFile = new File(path.replace(".docx",".pdf"));
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();
            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class ImageTagProcessor extends com.itextpdf.tool.xml.html.Image {
        @Override
        public List<Element> end(final WorkerContext ctx, final Tag tag, final List<Element> currentContent) {
            final Map<String, String> attributes = tag.getAttributes();
            String src = attributes.get(HTML.Attribute.SRC);
            List<Element> elements = new ArrayList<>(1);
            if (null != src && src.length() > 0) {
                com.itextpdf.text.Image img = null;
                if (src.startsWith("data:image/")) {
                    final String base64Data = src.substring(src.indexOf(",") + 1);
                    try {
                        img = com.itextpdf.text.Image.getInstance(Base64.decode(base64Data));
                    } catch (Exception ignored) {
                    }
                    if (img != null) {
                        try {
                            final HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
                            elements.add(getCssAppliers().apply(new Chunk((com.itextpdf.text.Image) getCssAppliers().apply(img, tag, htmlPipelineContext), 0, 0, true), tag,
                                    htmlPipelineContext));
                        } catch (NoCustomContextException e) {
                            throw new RuntimeWorkerException(e);
                        }
                    }
                }
                if (img == null) {
                    elements = super.end(ctx, tag, currentContent);
                }
            }
            return elements;
        }
    }


    private class FontProvider extends XMLWorkerFontProvider {
        @Override
        public Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont, size, style, color);
            } catch (Exception e) {
                log.error("本地字体引用出错", e);
            }
            return super.getFont(fontname, encoding, embedded, size, style, color);
        }

        @Override
        public Font getFont(String fontname, String encoding, float size, int style) {
            try {
                BaseFont baseFont = BaseFont.createFont("/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
                return new Font(baseFont, size, style);
            } catch (Exception e) {
                log.error("本地字体引用出错", e);
            }
            return super.getFont(fontname, encoding, size, style);
        }
    }

}
