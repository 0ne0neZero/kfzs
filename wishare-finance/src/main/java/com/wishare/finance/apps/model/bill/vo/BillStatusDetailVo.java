package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

/**
 * @author yyx
 * @date 2023/5/10 10:06
 */
@Getter
@Setter
@ApiModel("账单基础状态信息")
public class BillStatusDetailVo {

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "计费面积 (单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("房号id")
    private String roomId;

    @ApiModelProperty("付款方id")
    private String payerId;

    @ApiModelProperty("账单状态（0正常，1作废，2冻结，3挂账）")
    private Integer state;

    @ApiModelProperty("挂单状态（0未挂账，1已挂账，2已销账）")
    private Integer onAccount;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty("核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty("开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty("结转状态：0未结转，1部分结转，2已结转")
    private Integer carriedState;


}
