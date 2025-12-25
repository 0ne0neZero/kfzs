package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 导入税目数据
 *
 * @author yancao
 */
@Getter
@Setter
public class TaxItemData {

    @ExcelProperty("税目名称")
    private String name;

    @ExcelProperty("税目编码")
    private String code;

    @ExcelProperty("关联的费项编码")
    private String chargeItemCode;
}
