package com.wishare.finance.domains.reconciliation.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 应收账单详情
 *
 * @Author dxclay
 * @Date 2022/10/13
 * @Version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
public class ReconciliationRecBillDetailOBV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "账单id")
    private Long billId;

    @ApiModelProperty(value = "账单类型")
    private Integer billType;

    @ApiModelProperty(value = "账单id")
    private String billNo;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "应收金额")
    private Long receivableAmount = 0L;

    @ApiModelProperty(value = "实收金额")
    private Long actualAmount = 0L;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty("报账单id")
    private String voucherBillId;


    @ApiModelProperty("报账单编号")
    private String voucherBillNo;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("房间名称")
    private String roomName;

    @ApiModelProperty("项目id")
    private String communityId;
}
