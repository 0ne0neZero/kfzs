package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@ApiModel("保证金开收据")
public class BondPlanReceiptF {

    @ApiModelProperty(value = "合同id", required = true)
    @NotNull
    private Long contractId;

    @ApiModelProperty(value = "保证金计划id", required = true)
    @NotNull
    private Long bondPlanId;


    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty(value = "开票金额", required = true)
    @NotNull
    private BigDecimal invoiceAmount;

//    @ApiModelProperty("推送方式 1邮箱 2手机号")
//    private List<Integer> PushMode;
//
//    @ApiModelProperty("邮箱地址")
//    private String email;
//
//    @ApiModelProperty("手机号")
//    private String phone;

}
