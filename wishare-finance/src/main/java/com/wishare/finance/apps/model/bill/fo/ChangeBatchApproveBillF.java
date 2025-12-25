package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fengxiaolin
 * @date 2023/6/29
 */
@Getter
@Setter
@ApiModel("批量审核form")
public class ChangeBatchApproveBillF {

    @ApiModelProperty(value = "账单ids", required = true)
    @NotEmpty(message = "账单ids不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单 7-收款账单")
    private Integer billType;

    @ApiModelProperty(value = "审核状态：2已审核，3未通过")
    @NotNull(message = "审核状态不能为空")
    private Integer approveState;

    @ApiModelProperty(value = "驳回理由")
    private String rejectReason;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款")
    private Integer operateType;

    @ApiModelProperty(value = "审核操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty(value = "已审核已结算账单调低和减免审核后操作：1退款，2转预收")
    private Integer approvedAction;

    @ApiModelProperty(value = "转预收的费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "转预收的费项名称")
    private String chargeItemName;

    @ApiModelProperty("外部审批标识")
    private String outApproveId;

    @ApiModelProperty("审核类型： 0内部审核，1外部审核")
    private Integer approveType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;
}
