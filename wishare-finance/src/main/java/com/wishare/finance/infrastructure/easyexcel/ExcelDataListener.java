package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author yancao
 */
@Slf4j
public class ExcelDataListener<T> extends AnalysisEventListener<T> {


    private static final int BATCH_COUNT = 10000;
    private List<T> cachedDataList;
    private List<List<Object>> cachedFailDataList;
    private final Consumer<List<T>> successDataList;
    private final Consumer<List<List<Object>>> failDataList;
    private List<String> headList;

    public ExcelDataListener(Consumer<List<T>> successDataList,
                             Consumer<List<List<Object>>> failDataList) {
        this.cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        this.cachedFailDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        this.successDataList = successDataList;
        this.failDataList = failDataList;
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JSON.toJSONString(ConverterUtils.convertToStringMap(headMap, context)));
        headList = new ArrayList<>(headMap.size());
        headMap.forEach((integer, readCellData) ->{
            if(readCellData.getStringValue() != null) {
                headList.add(readCellData.getStringValue());
            }
        });
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        this.cachedDataList.add(data);
        if (this.cachedDataList.size() >= BATCH_COUNT) {
            this.successDataList.accept(this.cachedDataList);
            this.cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception exception
     * @param context context
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        Map<Integer, Cell> cellMap = context.readRowHolder().getCellMap();
        ArrayList<Object> currentFailData = new ArrayList<>();

        for (int i = 0; i < headList.size(); i++) {
            ReadCellData<Object> cell = (ReadCellData<Object>) cellMap.get(i);
            if(Objects.isNull(cell)){
                currentFailData.add(null);
            }else{
                CellDataTypeEnum type = cell.getType();
                switch (type) {
                    case STRING:
                        currentFailData.add(cell.getStringValue());
                        break;
                    case NUMBER:
                        if(cell.getDataFormatData().getFormat().contains("yyyy")){
                            LocalDateTime localDate = LocalDateTime.of(1900, 1, 1,0,0,0);
                            //excel 有些奇怪的bug, 导致日期数差2
                            currentFailData.add(localDate.plusDays(cell.getNumberValue().longValue() - 2));
                        }else{
                            currentFailData.add(cell.getNumberValue());
                        }
                        break;
                    case BOOLEAN:
                        currentFailData.add(cell.getBooleanValue());
                        break;
                    default:
                        break;
                }
            }
        }

        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            currentFailData.add(headList.get(excelDataConvertException.getColumnIndex())+"格式错误");
            cachedFailDataList.add(currentFailData);
        }
        if (this.cachedFailDataList.size() >= BATCH_COUNT) {
            this.failDataList.accept(this.cachedFailDataList);
            this.cachedFailDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(this.cachedDataList)) {
            this.successDataList.accept(this.cachedDataList);
        }
        if( CollectionUtils.isNotEmpty(this.cachedFailDataList)){
            this.failDataList.accept(this.cachedFailDataList);
        }
    }
}
