package com.wishare.contract.apps.remote.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 账单分页信息
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("账单分页信息")
public class BillPageInfoRv extends BillPageRv{

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private Long unitPrice;

    @ApiModelProperty("是否逾期：0未逾期，1已逾期")
    private Integer overdueState;

    @ApiModelProperty("账单开始时间")
    private LocalDate startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDate endTime;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）")
    private String settleChannel;

    @ApiModelProperty("费项类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "联系人姓名")
    private String contactName;

    @ApiModelProperty(value = "联系人手机号")
    private String contactPhone;

    @ApiModelProperty("合同id")
    private Long contractId;

    @ApiModelProperty("合同名称")
    private String contractName;

}
