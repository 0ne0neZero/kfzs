package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("创建临时收费账单请求信息")
@Valid
public class AddTemporaryChargeBillRf extends AddTemporaryBillRf {

    @ApiModelProperty(value = "费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型", required = true)
//    @NotNull(message = "费项类型不能为空")
    private Integer chargeItemType;

    @ApiModelProperty(value = "计费面积")
    private Integer chargingArea;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "是否引用：0未被引用，1已被引用")
    private String referenceState;

    @ApiModelProperty(value = "收付类型：0收款（临时收款），1付款（临时付款）")
    private String payType;

    @ApiModelProperty(value = "计费方式")
    private Integer billMethod;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "合同名称")
    private String contractName;

}
