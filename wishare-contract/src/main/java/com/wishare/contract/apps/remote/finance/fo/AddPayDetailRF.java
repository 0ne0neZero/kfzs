package com.wishare.contract.apps.remote.finance.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2023/2/3
 * @Description:
 */
@Getter
@Setter
@ApiModel("付款单明细")
public class AddPayDetailRF {

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "结算渠道 ")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "外部支付编号（支付宝单号，银行流水号等）")
    private String outPayNo;

    @ApiModelProperty(value = "应付金额（单位：分）")
    private Long recPayAmount;

    @ApiModelProperty(value = "付款金额（单位：分）(合单付款时，付款金额 > 结算金额)")
    private Long payAmount;

    @ApiModelProperty(value = "收费对象类型")
    private Integer payerType;

    @ApiModelProperty(value = "付款人id")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "收款人id")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;
}
