package com.wishare.finance.apps.template;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.wishare.component.imports.ExcelImport;
import com.wishare.component.imports.extension.easyexcel.EasyExcelImport;
import com.wishare.finance.infrastructure.aspect.ExcelErrMsg;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Setter
@Getter
public class BaseExcelImport extends EasyExcelImport implements ExcelImport, Serializable {

    @ExcelProperty("导入错误原因")
    @ExcelErrMsg
    @ExcelIgnore  //错误信息
    private String errMsg = StringUtils.EMPTY;

    @ExcelIgnore  //行号
    protected Long rowNumber;
}
