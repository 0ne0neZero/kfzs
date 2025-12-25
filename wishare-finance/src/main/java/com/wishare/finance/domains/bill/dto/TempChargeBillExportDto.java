package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


/**
 * 临时账单导出数据
 *
 * @author yancao
 */
@Getter
@Setter
public class TempChargeBillExportDto {

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "房号名称")
    private String roomName;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "账单创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "收费对象")
    private String payerName;

    @ApiModelProperty(value = "联系人名称")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty(value = "结算状态")
    private Integer settleState;

    @ApiModelProperty(value = "账单来源")
    private String source;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;
}
