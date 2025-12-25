package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @description: 删除账单推凭
 * @author: pgq
 * @since: 2022/10/24 20:55
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("删除账单推凭")
public class BatchDelBillInferenceF {

    @ApiModelProperty("账单推凭id")
    private List<Long> inferIds;

    @ApiModelProperty("关联的ids")
    private List<Long> concatIds;

    @ApiModelProperty("账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty("账单")
    private Integer eventType;
}
