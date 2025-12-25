package com.wishare.finance.infrastructure.beans;

import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Getter
public class ExcelImporReturn {

    private Integer failTotal = 0;

    private Integer successTotal = 0;

    private String tip;

    private Map<String, List<?>> successData;

    private Map<String, List<?>> failData;

    private Map<String, List<Map<String, Object>>> convertFailData;

    private String excelLinkUrl;

    public void setTip(String tip) {
        this.tip = tip;
    }

    public void setSuccessData(Map<String, List<?>> successData) {
        this.successData = successData;
        if (!CollectionUtils.isEmpty(failData)) {
            successData.forEach((x, y) -> {
                successTotal = y.size() + successTotal;
            });
        }
    }

    public void setFailData(Map<String, List<?>> failData) {
        this.failData = failData;
        if (!CollectionUtils.isEmpty(failData)) {
            failData.forEach((x, y) -> {
                failTotal = y.size() + failTotal;
            });
        }
    }

    public void setSuccessTotal(Map<String, List<?>> successData) {
        successData.forEach((x, y) -> {
            successTotal = y.size() + successTotal;
        });
    }

    public void setFailTotal(Map<String, List<?>> failData) {
        if (!CollectionUtils.isEmpty(failData)) {
            failData.forEach((x, y) -> {
                failTotal = y.size() + failTotal;
            });
        }
    }

    public void setConvertFailData(Map<String, List<Map<String, Object>>> convertFailData) {
        this.convertFailData = convertFailData;
        if (!CollectionUtils.isEmpty(convertFailData)) {
            convertFailData.forEach((k, v) -> {
                int size = v.size();
                failTotal = failTotal + size;
            });
        }
    }

    public void setExcelLinkUrl(String excelLinkUrl) {
        this.excelLinkUrl =  excelLinkUrl;
    }


    public void setConvertFailTotal(Map<String, List<Map<String, Object>>> convertMap) {
        if (!CollectionUtils.isEmpty(convertMap)) {
            convertMap.forEach((k, v) -> {
                int size = v.size();
                failTotal = failTotal + size;
            });
        }
    }
}
