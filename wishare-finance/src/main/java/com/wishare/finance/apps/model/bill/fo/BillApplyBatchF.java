package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ApiModel("账单批量申请信息")
public class BillApplyBatchF {

    @ApiModelProperty(value = "账单id列表", required = true)
    @Size(min = 1, max = 1000, message = "账单id列表大小不正确，区间[1,1000]")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "申请原因")
    private String reason;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款,8bpm跳收")
    private Integer approveOperateType;

    @ApiModelProperty(value = "审核详情（该字段为JSON LIST字符串，根据不同操作传不同参数）")
    private String detail;

    @ApiModelProperty(value = "账单类型（1:应收账单，2:预收账单，3:临时收费账单，4：应付账单）", required = true)
    private Integer billType;

    @ApiModelProperty("扩展字段1（冲销时如果需要生成新的应收传入：ReversedInitBill）(BPM审核的时候传BPM)")
    private String extField1;

    @ApiModelProperty("操作id")
    private Long operationId;

    @ApiModelProperty(value = "操作备注")
    private String operationRemark;
}
