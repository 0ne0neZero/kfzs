package com.wishare.contract.domains.bo;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalConverter implements Converter<BigDecimal> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return BigDecimal.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        // 指定Excel中存储为字符串类型，避免科学计数法
        return CellDataTypeEnum.STRING;
    }

    @Override
    public BigDecimal convertToJavaData(com.alibaba.excel.metadata.data.ReadCellData<?> cellData,
                                        ExcelContentProperty contentProperty,
                                        GlobalConfiguration globalConfiguration) {
        // 从Excel读取时转换
        return new BigDecimal(cellData.getStringValue());
    }

    @Override
    public WriteCellData<?> convertToExcelData(BigDecimal value,
                                               ExcelContentProperty contentProperty,
                                               GlobalConfiguration globalConfiguration) {
        // 写入Excel时转换
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return new WriteCellData<>(df.format(value));
    }
}