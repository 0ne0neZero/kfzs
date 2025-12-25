package com.wishare.finance.domains.invoicereceipt.consts.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: Linitly
 * @date: 2023/8/8 9:13
 * @descrption: 票据模板电子签章类型
 */
@Getter
@AllArgsConstructor
public enum ElectSignTypeEnum {


    系统默认(1, "系统默认"),
    手动上传(2, "手动上传"),
    ;

    private Integer code;

    private String desc;

    public static ElectSignTypeEnum valueOfCode(Integer code) {
        for (ElectSignTypeEnum signTypeEnum : ElectSignTypeEnum.values()) {
            if (signTypeEnum.getCode().equals(code)) {
                return signTypeEnum;
            }
        }
        return null;
    }
}
