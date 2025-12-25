package com.wishare.finance.infrastructure.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelErrorFixUtil {

    /**
     * 收集导入时的错误数据
     *
     * @param exception
     * @param context   出现异常的内容
     * @param data      将错误数据转换格式后赋值给空集合
     * @param size      title的长度
     */
    public static void setErrorData(Exception exception, AnalysisContext context, List<Map<String, Object>> data, String[] dataStrMap) {
        if (exception instanceof ExcelDataConvertException) {

            ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
            int column = convertException.getColumnIndex();

            Map<String, Object> errMap = new HashMap<>();

            //错误信息
            Map<Integer, Cell> cellMapResult = context.readRowHolder().getCellMap();
            //添加提示信息
            Object errValue = null;
            //String[] dataStrMap = {"name", "age", "money", "birthday", "remark"};
            for (int i = 0; i < dataStrMap.length - 1; i++) {
                ReadCellData readCellData = (ReadCellData) cellMapResult.get(i);
                //特殊处理日期类型变成数字的情况
                if (readCellData != null && "yyyy/m/d".equals(readCellData.getDataFormatData().getFormat())
                        && readCellData.getType().equals(CellDataTypeEnum.NUMBER)) {
                    LocalDateTime convertLocalDateTime = convertLocalDateTime(readCellData);
                    errMap.put(dataStrMap[i], convertLocalDateTime);
                } else {
                    Object value = null;
                    // 说明当前列没有数据
                    if (readCellData == null) {
                        errMap.put(dataStrMap[i], "");
                    } else {
                        CellDataTypeEnum type = readCellData.getType();
                        if (type.name().equals("NUMBER")) {
                            value = readCellData.getNumberValue();
                        } else if (type.name().equals("STRING")) {
                            value = readCellData.getStringValue();
                        } else if (type.name().equals("EMPTY")) {
                            value = "";
                        }
                        errMap.put(dataStrMap[i], value);
                        if (i == column) {
                            errValue = value;
                        }
                    }
                }
            }

            errValue = errValue + "数据格式转换错误";
            errMap.put("errMsg", errValue);
            data.add(errMap);
        }
    }

    /**
     * 收集导入时Excel的title
     *
     * @param headMap 传入需要处理的title map集合
     * @param head    赋值给空集合
     */
    public static void setExcelErrorHead(Map<Integer, String> headMap, List<List<String>> head) {
        List<String> errorTips = new ArrayList<>();
        errorTips.add("错误数据行");
        head.add(errorTips);
        for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
            List<String> errorHead = new ArrayList<>();
            errorHead.add(entry.getValue());
            head.add(errorHead);
        }
    }


    public static LocalDateTime convertLocalDateTime(ReadCellData cellData) {
        if (cellData.getType().equals(CellDataTypeEnum.NUMBER)) {
            LocalDate localDate = LocalDate.of(1900, 1, 1);
            //excel 有些奇怪的bug, 导致日期数差2
            localDate = localDate.plusDays(cellData.getNumberValue().longValue() - 2);
            return localDate.atStartOfDay();
        }
        if (cellData.getStringValue() == null) {
            return null;
        }
        return null;
    }

}
