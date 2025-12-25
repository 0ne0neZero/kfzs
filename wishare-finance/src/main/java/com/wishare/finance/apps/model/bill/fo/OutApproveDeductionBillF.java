package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Setter
@Getter
public class OutApproveDeductionBillF {

    @ApiModelProperty(value = "审核状态：2已审核，3未通过")
    @NotNull(message = "审核状态不能为空")
    private Integer approveState;

    @ApiModelProperty(value = "驳回理由")
    private String rejectReason;

    @ApiModelProperty(value = "已审核已结算账单调低和减免审核后操作：1退款，2转预收")
    private Integer approvedAction;

//    @ApiModelProperty("外部审批标识")
//    private String outApproveId;

    @ApiModelProperty(value = "审批ids")
    @NotEmpty(message = "审批id不能为空")
    List<Long> approveIds;

    @ApiModelProperty("审核类型： 0内部审核，1外部审核")
    private Integer approveType;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;
}
