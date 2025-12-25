package com.wishare.finance.infrastructure.utils;

import com.wishare.finance.infrastructure.conts.TextContentEnum;

import java.text.MessageFormat;

/**
 * 文本生成工具
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/31
 */
public class TextContentUtil {

    /**
     * 收据内容模板
     * 内容分别是房号、租户简称、下载链接、项目名称
     */
    private static final String RECEIPT_CONTENT_TEMP = "尊敬的{0}客户：\n" +
            "{1}已经为您开具一张电子收据，具体信息如下:\n" +
            "您可以点击“{2}“获取该收据文件，同时您也可以到物业服务中心或联系管家协助下载电子收据。\n" +
            "感谢您对物业工作的支持与配合，祝生活愉快！\n" +
            "{3}";

    /**
     * 发票补发内容模板
     * 内容分别是客户名、租户简称、发票数量、发票具体信息、项目名称
     */
    private static final String INVOICE_RESEND_CONTENT_TEMP = "尊敬的{0}：\n" +
            "\n" +
            "{1}已经为您开具{2}张电子普通发票，具体信息如下:\n" +
            "\n" +
            "{3}同时您也可以到物业服务中心或联系管家协助下载电子发票。\n" +
            "\n" +
            "电子普通发票是税务机关认可的有效收付款凭证，与纸质发票具有同等法律效力，可用于报销入账、售后维权等。\n" +
            "\n" +
            "感谢您对物业工作的支持与配合，祝生活愉快！\n" +
            "\n" +
            //项目名称
            "{4}";

    /**
     * 收据补发内容模板
     * 内容分别是房号、租户简称、收据数量、下载链接、项目名称
     */
    private static final String RECEIPT_RESEND_CONTENT_TEMP = "尊敬的{0}客户：\n" +
            "{1}已经为您开具{2}张电子收据，具体信息如下:\n" +
            "您可以点击“{3}“获取该收据文件，同时您也可以到物业服务中心或联系管家协助下载电子收据。\n" +
            "感谢您对物业工作的支持与配合，祝生活愉快！\n" +
            "{4}";;

    /**
     * 收据补发主题模板
     * 内容分别是收据张数、法定单位
     */
    private static final String RECEIPT_RESEND_SUBJECT_TEMP = "您收到{0}张【{1}】开具的电子收据";

    /**
     * 发票补发主题模板
     * 内容分别是发票张数、开票单位名称、发票号码
     */
    private static final String INVOICE_RESEND_SUBJECT_TEMP = "您收到{0}张【{1}】开具的发票【发票号码：{2}】";

    /**
     * 收据主题模板
     * 内容为收据的法定单位
     */
    private static final String RECEIPT_SUBJECT_TEM = "您收到一张【{0}】开具的电子收据。";

    public static String replacePlaceholders(String template, Object[] data) {
        MessageFormat formatter = new MessageFormat(template);
        return formatter.format(data);
    }

    public static String getEmailSubject(TextContentEnum textContentEnum, Object[] data) {
        switch (textContentEnum) {
            case 电子收据:
                return replacePlaceholders(RECEIPT_SUBJECT_TEM, data);
            case 发票补发:
                return replacePlaceholders(INVOICE_RESEND_SUBJECT_TEMP, data);
            case 收据补发:
                return replacePlaceholders(RECEIPT_RESEND_SUBJECT_TEMP, data);
        }
        return "";
    }

    public static String getEmailContent(TextContentEnum textContentEnum, Object[] data) {
        switch (textContentEnum) {
            case 电子收据:
                return replacePlaceholders(RECEIPT_CONTENT_TEMP, data);
            case 发票补发:
                return replacePlaceholders(INVOICE_RESEND_CONTENT_TEMP, data);
            case 收据补发:
                return replacePlaceholders(RECEIPT_RESEND_CONTENT_TEMP, data);
        }
        return "";
    }

//    public static void main(String[] args) {
//        String emailContent = getEmailContent(TextContentEnum.电子收据, new Object[]{"1", "2", "3", "4"});
//        System.out.println(emailContent);
//    }

}
