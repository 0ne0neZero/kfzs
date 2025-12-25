package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 账单审核申请数据
 * @Author dxclay
 * @Date 2022/8/28
 * @Version 1.0
 */
@Getter
@Setter
public class BillApproveApplyV {

    @ApiModelProperty(value = "主键id", required = true)
    private Long id;

    @ApiModelProperty(value = "账单ID", required = true)
    private Long billId;

    @ApiModelProperty(value = "外部审批标识")
    private String outApproveId;

    @ApiModelProperty(value = "审核申请原因", required = true)
    private String reason;

    @ApiModelProperty(value = "审核操作类型：0生成审核，1调整，2作废，3结转，4退款,5冲销,6减免,7收款单退款",required = true)
    private Integer operateType;

    @ApiModelProperty(value = "审核状态（1审核中，2已审核，3未通过）", required = true)
    private Integer approvedState;

    @ApiModelProperty(value = "审核结果说明")
    private String approvedRemark;

    @ApiModelProperty(value = "创建人ID", required = true)
    private String creator;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private String creatorName;

    @ApiModelProperty(value = "创建人姓名", required = true)
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID", required = true)
    private String operator;

    @ApiModelProperty(value = "修改人姓名", required = true)
    private String operatorName;

    @ApiModelProperty(value = "更新时间", required = true)
    private LocalDateTime gmtModify;

}
