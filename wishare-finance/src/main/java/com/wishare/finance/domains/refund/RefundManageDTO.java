package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel(value="RefundManagement对象", description="退款管理")
public class RefundManageDTO {
    @ApiModelProperty(value = "项目id")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "收款单号")
    private String gatherNo;

    @ApiModelProperty(value = "房间id")
    private String cpUnitId;

    @ApiModelProperty(value = "房号")
    private String cpUnitName;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "收款类型 0应收,临时，1预收")
    private Integer gatherType;

    @ApiModelProperty(value = "退款总金额")
    private Long totalRefundAmount;

    @ApiModelProperty(value = "账单数")
    private Long billCount;

    @ApiModelProperty(value = "支付申请单编号")
    private String payApplyCode;

    @ApiModelProperty(value = "支付申请单id")
    private Long payApplyId;

    @ApiModelProperty(value = "支付状态 1:未支付 2:已支付")
    private Integer payStatus;

    @ApiModelProperty(value = "支付状态 1:未支付 2:已支付")
    private String payStatusStr;

    @ApiModelProperty(value = "业务申请单审批状态 1:草稿 2:待审批 3:完成审批 ")
    private Integer approvalStatus;

    @ApiModelProperty(value = "业务申请单审批状态 1:草稿 2:待审批 3:完成审批 ")
    private String approvalStatusStr;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payDate;

    @ApiModelProperty(value = "业务事由")
    private String businessReasons;

    @ApiModelProperty(value = "区域id")
    private String regionId;

    @ApiModelProperty(value = "区域")
    private String region;

    public RefundManageDTO(){

    }

    public RefundManageDTO(String communityId, String payApplyCode, Long payApplyId, Integer approvalStatus, String approvalStatusStr) {
        this.communityId = communityId;
        this.payApplyCode = payApplyCode;
        this.payApplyId = payApplyId;
        this.approvalStatus = approvalStatus;
        this.approvalStatusStr = approvalStatusStr;
    }
}
