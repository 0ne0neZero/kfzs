package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 导入费项数据
 *
 * @author yancao
 */
@Getter
@Setter
public class ChargeItemData {

    @ExcelProperty("费项名称")
    private String name;

    @ExcelProperty("费项编码")
    private String code;

    @ExcelProperty("费项类型")
    private String type;

    @ExcelProperty("费项属性")
    private String attribute;

    @ExcelProperty("用途/备注")
    private String remark;

    @ExcelProperty("上级费项")
    private String parentCode;
}
