package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 账单推凭
 * @author: pgq
 * @since: 2022/10/24 20:55
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("新增账单推凭")
public class BatchAddBillInferenceF {

    @ApiModelProperty("账单id")
    private List<Long> billIds;

    @ApiModelProperty("关联的ids")
    private List<Long> concatIds;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("触发事件")
    private Integer eventType;
}
