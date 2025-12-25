package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 导入科目数据
 *
 * @author yancao
 */
@Getter
@Setter
public class SubjectData {

    @ExcelProperty("科目名称")
    private String name;

    @ExcelProperty("科目编码")
    private String code;

    @ExcelProperty("科目类型")
    private String type;

    @ExcelProperty("辅助核算")
    private String auxiliaryCount;

    @ExcelProperty("末级")
    private String leaf;

    @ExcelProperty("上级科目")
    private String parentCode;

}
