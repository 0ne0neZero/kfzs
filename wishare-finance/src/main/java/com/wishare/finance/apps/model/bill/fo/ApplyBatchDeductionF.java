package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("账单减免批量申请信息")
public class ApplyBatchDeductionF {

    @ApiModelProperty(value = "账单id列表", required = true)
    @Size(min = 1, max = 1000, message = "账单id列表大小不正确，区间[1,1000]")
    private Map<Integer,List<Long>> billIds;

    @ApiModelProperty(value = "申请原因")
    private String reason;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款")
    private Integer approveOperateType;

    @ApiModelProperty(value = "临时账单审核详情（该字段为JSON LIST字符串，根据不同操作传不同参数）")
    private String temporaryDetail;

    @ApiModelProperty(value = "应收账单审核详情（该字段为JSON LIST字符串，根据不同操作传不同参数）")
    private String receivableDetail;

    @ApiModelProperty("上级收费id")
    private String supCpUnitId;

    @ApiModelProperty("操作id")
    private Long operationId;
}
