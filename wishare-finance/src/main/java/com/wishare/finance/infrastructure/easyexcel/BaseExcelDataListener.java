package com.wishare.finance.infrastructure.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.ConverterUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author yancao
 */
@Slf4j
public class BaseExcelDataListener<T> extends AnalysisEventListener<T> {


    private static final int BATCH_COUNT = 1000;
    private List<T> cachedDataList;
    private final Consumer<List<T>> consumer;

    public BaseExcelDataListener(Consumer<List<T>> consumer) {
        this.cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        this.consumer = consumer;
    }

    @Override
    public void invokeHead(Map<Integer, ReadCellData<?>> headMap, AnalysisContext context) {
        log.info("解析到一条头数据:{}", JSON.toJSONString(ConverterUtils.convertToStringMap(headMap, context)));
    }

    @Override
    public void invoke(T data, AnalysisContext context) {
        this.cachedDataList.add(data);
        if (this.cachedDataList.size() >= BATCH_COUNT) {
            this.consumer.accept(this.cachedDataList);
            this.cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        if (CollectionUtils.isNotEmpty(this.cachedDataList)) {
            this.consumer.accept(this.cachedDataList);
        }
    }
}
