package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 账单审核合计信息
 *
 * @Author dxclay
 * @Date 2022/8/26
 * @Version 1.0
 */
@Setter
@Getter
@ApiModel("账单审核合计信息")
public class BillApproveTotalV {

    @ApiModelProperty(value = "审核总数", required = true)
    private Long total = 0L;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer operateType;

    @ApiModelProperty(value = "审核状态（0未审核，1审核中，2已审核，3未通过）", required = true)
    private Integer approveState;

    @ApiModelProperty(value = "应收账单审核总数", required = true)
    private Long receivableTotal = 0L;

    @ApiModelProperty(value = "临时收费账单审核总数", required = true)
    private Long temporaryChargeTotal = 0L;

    @ApiModelProperty(value = "预收账单审核总数", required = true)
    private Long advanceTotal = 0L;

    @ApiModelProperty(value = "付款账单审核总数", required = true)
    private Long paymentTotal = 0L;

    @ApiModelProperty(value = "退款账单审核总数", required = true)
    private Long refundTotal = 0L;

}
