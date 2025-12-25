package com.wishare.contract.domains.enums;

/**
 * 开票类型：
 * 1: 增值税普通发票
 * 2: 增值税专用发票
 * 3: 增值税电子发票
 * 4: 增值税电子专票
 * 5: 收据
 * 6：电子收据
 * 7：纸质收据
 * 8：全电普票
 *
 * @author 永遇乐 yeoman <76164451@.qq.com>
 * @line --------------------------------
 * @date 2023/02/25
 */
public enum InvoiceTypeEnum {
    增值税普通发票("1"),
    增值税专用发票("2"),
    增值税电子发票("3"),
    增值税电子专票("4"),
    收据("5"),
    电子收据("6"),
    纸质收据("7"),
    全电普票("8");

    private String value;

    InvoiceTypeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
