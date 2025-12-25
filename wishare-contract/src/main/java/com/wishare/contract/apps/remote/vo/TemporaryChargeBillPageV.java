package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel(value = "临时账单分页列表")
public class TemporaryChargeBillPageV extends BillPageV{

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "是否引用：0未被引用，1已被引用")
    private Integer referenceState;

    @ApiModelProperty(value = "收付类型：0收款（临时收款），1付款（临时付款）")
    private String payType;
}
