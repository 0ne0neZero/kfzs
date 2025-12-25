package com.wishare.finance.apps.model.configure.chargeitem.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("审核单条收款单信息")
public class ApproveBillF {

    @ApiModelProperty(value = "账单id", required = true)
    @NotNull(message = "账单id不能为空")
    private Long billId;

    @ApiModelProperty("上级收费单元id")
    @NotNull(message = "上级收费单元id不能为空")
    private String supCpUnitId;

    @ApiModelProperty("1,初始审核2,变更审核")
    private Integer checkType;

    @ApiModelProperty(value = "审核状态：0未审核，1审核中，2已审核，3未通过", required = true)
    @NotNull(message = "审核状态不能为空")
    private Integer approveState;

    @ApiModelProperty(value = "驳回理由")
    private String rejectReason;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款 7.收款单退款")
    private Integer operateType;

    @ApiModelProperty(value = "审核操作时间")
    private LocalDateTime operateTime;

    @ApiModelProperty(value ="账单类型1-应收账单，2-预收账单，3-临时收费账单")
    private Integer billType;

    @ApiModelProperty(value = "已审核已结算账单调低和减免审核后操作：1退款，2转预收")
    private Integer approvedAction;

    @ApiModelProperty(value = "转预收的费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "转预收的费项名称")
    private String chargeItemName;


    public ApproveBillF() {
    }

    public ApproveBillF(Long billId, String supCpUnitId, Integer approveState, Integer operateType, Integer billType) {
        this.billId = billId;
        this.supCpUnitId = supCpUnitId;
        this.approveState = approveState;
        this.operateType = operateType;
        this.billType = billType;
    }
}
