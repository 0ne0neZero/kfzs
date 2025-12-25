package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/8/7 20:20
 * @descrption:
 */
@Getter
@AllArgsConstructor
public enum ReceiptTemplateStyleEnum {

    // 目前是慧享云、远洋
    样式一(1, "样式一", "templates/receipt/receipt.ftl"),

    //方圆通用(区别于上方调整了缴费时间、开具时间顺序)
    样式二(2, "样式一", "templates/receipt/receipt_fy.ftl"),

    //拈花湾
    样式三(3, "样式一", "templates/receipt/receipt_nhw.ftl"),

    // 慧享云 增加模板
    样式四(4, "样式二", "templates/receipt/receipt_o.ftl"),

    // 远洋


    // 方圆
    方圆电子收据模板样式01(101, "默认套打收据", "templates/receipt/fangyuan_template_01.ftl"),
    方圆电子收据模板样式02(102, "方圆电子专用收据样式二", "templates/receipt/fangyuan_template_02.ftl"),
    方圆电子收据模板样式03(103, "方圆电子专用收据样式三", "templates/receipt/fangyuan_template_03.ftl"),

    // 中交
    中交电子收据模板样式(201, "电子专用收据", "templates/receipt/zhongjiao_template.ftl"),

    ;
    private Integer code;

    private String desc;

    private String templatePath;

    public static ReceiptTemplateStyleEnum valueOfCode(Integer code) {
        for (ReceiptTemplateStyleEnum styleEnum : ReceiptTemplateStyleEnum.values()) {
            if (styleEnum.getCode().equals(code)) {
                return styleEnum;
            }
        }
        return null;
    }
}
