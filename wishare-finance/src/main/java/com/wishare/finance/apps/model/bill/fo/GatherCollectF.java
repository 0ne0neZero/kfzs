package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: Linitly
 * @date: 2023/7/17 11:28
 * @descrption:
 */
@Data
@ApiModel(value = "收款单代收请求信息")
public class GatherCollectF {

    @ApiModelProperty(value = "收款单ID")
    @NotNull(message = "收款单ID不能为空")
    private Long gatherBillId;

    @ApiModelProperty(value = "代收银行卡号")
    @NotBlank(message = "代收银行卡号不能为空")
    private String collectBankAccount;

    @ApiModelProperty(value = "代收流水单号")
    @NotBlank(message = "代收流水单号不能为空")
    private String collectSerialNumber;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
