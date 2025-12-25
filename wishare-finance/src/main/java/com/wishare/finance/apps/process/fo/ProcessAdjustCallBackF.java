package com.wishare.finance.apps.process.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("合同流程创建回调具体参数")
public class ProcessAdjustCallBackF {

    @ApiModelProperty("流程id")
    private String processId;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("审核状态 0:未提交 1:审批中  2:通过")
    private Integer reviewStatus;

    @ApiModelProperty("驳回原因")
    private String rejectReason;

    @ApiModelProperty("修改人id")
    private String operator;

    @ApiModelProperty("修改人名称")
    private String operatorName;

    @ApiModelProperty("签约日期")
    private String signDate;

    @ApiModelProperty("流程表单数据")
    private BusinessInfoF formData;

}
