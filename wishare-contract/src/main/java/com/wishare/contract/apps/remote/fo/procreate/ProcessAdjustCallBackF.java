package com.wishare.contract.apps.remote.fo.procreate;

import com.wishare.contract.domains.vo.revision.procreate.BusinessInfoF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 功能解释
 *
 * @author long
 * @date 2023/7/18 16:40
 */
@Getter
@Setter
@ApiModel("流程创建回调具体参数")
public class ProcessAdjustCallBackF {
    @ApiModelProperty("流程id")
    private String processId;

    @ApiModelProperty("合同id")
    private String contractId;

    /**
     * 审核状态 0:未提交 1:审批中  2:通过
     */
    @ApiModelProperty("审核状态 审核状态 0:未提交 1:审批中  2:通过")
    private Integer reviewStatus;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectReason;

    /**
     * 修改人id
     */
    @ApiModelProperty("修改人id")
    private String operator;

    /**
     * 修改人名称
     */
    @ApiModelProperty("修改人名称")
    private String operatorName;

    @ApiModelProperty("流程表单数据")
    private BusinessInfoF formData;

}
