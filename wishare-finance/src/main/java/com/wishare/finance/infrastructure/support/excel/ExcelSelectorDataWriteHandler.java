package com.wishare.finance.infrastructure.support.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.StyleUtil;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.wishare.finance.infrastructure.aspect.HeadExcelColor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor
@Accessors(chain = true)
@Getter
@Setter
public class ExcelSelectorDataWriteHandler implements CellWriteHandler {

    /**
     * 头列字体颜色
     */
    private Map<Integer, Short> headColumnMap = Collections.emptyMap();

    public ExcelSelectorDataWriteHandler(Map<Integer, Short> headColumnMap) {
        this.headColumnMap = headColumnMap;
    }


    /**
     * excel 导出样式 默认
     *
     * @return
     */
    public static WriteHandler setExcelStyle() {
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为橙色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setColor(IndexedColors.WHITE.getIndex());
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteFont.setColor(IndexedColors.BLACK.getIndex());
        // 字体大小
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }


    /**
     * 获取必填列Map
     *
     * @param clazz 类class
     * @return java.util.Map<java.lang.Integer, java.lang.Short>
     * @author SunLingDa
     * @date 2022/11/3 13:23
     */
    public static Map<Integer, Short> getRequiredMap(Class<?> clazz, boolean isErrMsgIsHead) {
        Map<Integer, Short> requiredMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        if (isErrMsgIsHead) {
            requiredMap.put(0, IndexedColors.RED.index);
            for (Field field : fields) {
                if ((field.isAnnotationPresent(HeadExcelColor.class) && field.isAnnotationPresent(ExcelProperty.class)) || (field.isAnnotationPresent(ExcelProperty.class))) {
                    HeadExcelColor excelColor = field.getAnnotation(HeadExcelColor.class);
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    if (Objects.nonNull(excelColor)) {
                        boolean required = excelColor.required();
                        IndexedColors indexedColors = excelColor.backgroundColor();
                        if (!required && Objects.isNull(indexedColors)) {
                            requiredMap.put(excelProperty.index() + 1, IndexedColors.GREY_25_PERCENT.index);
                        } else {
                            requiredMap.put(excelProperty.index() + 1, excelColor.backgroundColor().getIndex());
                        }
                    } else {
                        requiredMap.put(excelProperty.index() + 1, IndexedColors.RED.index);
                    }
                }
            }
        } else {
            for (Field field : fields) {
                if ((field.isAnnotationPresent(HeadExcelColor.class) && field.isAnnotationPresent(ExcelProperty.class)) || (field.isAnnotationPresent(ExcelProperty.class))) {
                    HeadExcelColor excelColor = field.getAnnotation(HeadExcelColor.class);
                    ExcelProperty excelProperty = field.getAnnotation(ExcelProperty.class);
                    if (Objects.nonNull(excelColor)) {
                        boolean required = excelColor.required();
                        IndexedColors indexedColors = excelColor.backgroundColor();
                        if (!required && Objects.isNull(indexedColors)) {
                            requiredMap.put(excelProperty.index(), IndexedColors.GREY_25_PERCENT.index);
                        } else {
                            requiredMap.put(excelProperty.index(), excelColor.backgroundColor().getIndex());
                        }
                    } else {
                        requiredMap.put(excelProperty.index(), IndexedColors.RED.index);
                    }
                }
            }
        }
        return requiredMap;
    }


    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        WriteCellData<?> cellData = context.getFirstCellData();
        WriteCellStyle writeCellStyle = cellData.getOrCreateStyle();

        DataFormatData dataFormatData = new DataFormatData();
        // 单元格设置为文本格式
        dataFormatData.setIndex((short) 49);
        Cell cell = context.getCell();
        WriteSheetHolder writeSheetHolder = context.getWriteSheetHolder();
        Workbook workbook = writeSheetHolder.getSheet().getWorkbook();
        // 设置标题字体样式
        WriteFont headWriteFont = new WriteFont();
        // 加粗
        headWriteFont.setBold(false);
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        if (context.getHead()) {
            headWriteFont.setColor(IndexedColors.WHITE.getIndex());
            if (!CollectionUtils.isEmpty(headColumnMap) && headColumnMap.containsKey(cell.getColumnIndex())) {
                // 设置字体颜色
                headWriteFont.setColor(IndexedColors.WHITE.getIndex());
                //设置单元格背景颜色
                writeCellStyle.setFillForegroundColor(headColumnMap.get(cell.getColumnIndex()));
            }
        }
        writeCellStyle.setWriteFont(headWriteFont);
        CellStyle cellStyle = StyleUtil.buildCellStyle(workbook, null, writeCellStyle);
        cell.setCellStyle(cellStyle);
    }
}
