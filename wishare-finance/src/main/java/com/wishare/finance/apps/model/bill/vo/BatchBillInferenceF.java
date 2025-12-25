package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
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
@ApiModel("账单推凭")
public class BatchBillInferenceF {

    @ApiModelProperty("账单id")
    private List<Long> billIds;

    @ApiModelProperty("账单推凭动作")
    private int actionEventCode;

    @ApiModelProperty("上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
