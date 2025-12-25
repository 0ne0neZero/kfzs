package com.wishare.finance.infrastructure.remote.vo.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.starter.beans.LabelValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
public class CfgExternalDataV {

    private Integer id;

    private String tenantId;

    private LabelValue itemLabelValue;

    private LabelValue externalDataTypeLabelValue;

    private String dictionaryTypeCode;

    /**
     * 数据类型名称
     */
    @ApiModelProperty("数据名称")
    private String dataName;

    /**
     * 数据简称 ，唯一的
     */
    @ApiModelProperty("数据编号")
    private String dataCode;

    @ApiModelProperty("数据字典类型名称")
    private String dictionaryItemName;

    @ApiModelProperty("数据字典类型编号")
    private String dictionaryItemCode;

    private Collection<CfgExternalDataV> externalDataMap;

    @ApiModelProperty("外部数据类型")
    private String externalDataType;

    @ApiModelProperty("外部数据类型名称")
    private String externalDataTypeName;

    /**
     * 是否启用 0 禁用 1 启用
     */
    @ApiModelProperty("是否启用 0 禁用 1 启用")
    private Byte disabled;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;

    /***
     * 是否删除 true 删除 false 正常
     */

    private Byte deleted;

    public LabelValue getItemLabelValue() {
        LabelValue labelValue = new LabelValue();
        labelValue.setLabel(dictionaryItemName);
        labelValue.setValue(dictionaryItemCode);
        return labelValue;
    }

    public LabelValue getExternalDataTypeLabelValue() {
        LabelValue labelValue = new LabelValue();
        labelValue.setLabel(externalDataTypeName);
        labelValue.setValue(externalDataType);
        return labelValue;
    }
}
