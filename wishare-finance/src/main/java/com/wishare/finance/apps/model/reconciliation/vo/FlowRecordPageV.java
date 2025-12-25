package com.wishare.finance.apps.model.reconciliation.vo;

import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 流水认领记录
 */

@Setter
@Getter
@ApiModel("流水认领记录")
public class FlowRecordPageV {

    /**
     * 流水批次id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 认领批次号
     */
    @ApiModelProperty("认领批次号")
    private String serialNumber;

    /**
     * 认领方式
     */
    @ApiModelProperty("认领方式 1全额认领 0差额认领 ")
    private String flowType;


    /**
     * 认领类型
     */
    @ApiModelProperty("认领类型1:发票认领;2:账单认领; ")
    private String claimType;

    /**
     * 认领流水金额
     */
    @ApiModelProperty("认领流水金额")
    private BigDecimal claimAmount;

    /**
     * 认领单据金额
     */
    @ApiModelProperty("认领单据金额")
    private BigDecimal settleAmount;

    /**
     * 认领差额
     */
    @ApiModelProperty("认领差额")
    private BigDecimal compareAmount;

    /**
     * 报账单推送数据
     */
    @ApiModelProperty("报账单推送状态: 1待推送 2已推送 3 推送失败")
    private String  pushState;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态: 0 已审核 1 审核中 2 差额认领审批驳回")
    private String approveState;

    /**
     * 资金收款单推送状态
     */
    @ApiModelProperty("资金收款单推送状态")
    private String billState;

    /**
     * 认领人
     */
    @ApiModelProperty("认领人")
    private String operatorName;
    /**
     * 认领时间
     */
    @ApiModelProperty("认领时间")
    private LocalDateTime claimDateTime;

    /**
     * 银行到账时间
     */
    @ApiModelProperty("银行到账时间")
    private String payTime;

    @ApiModelProperty("上级收费单元")
    private String supCpUnitId;


    @ApiModelProperty("项目")
    private String supCpUnitName;

    @ApiModelProperty("银行账户")
    private String bankAccount;

    @ApiModelProperty("银行回单文件")
    private String flowFiles;

    @ApiModelProperty("日报表文件")
    private String reportFiles;

    @ApiModelProperty("认领记录对应的流水的id集合")
    private List<Long> flowDetailIdList;


    @ApiModelProperty("账单明细")
    private List<ContractFlowBillV> flowBillDetailList;

    @ApiModelProperty("新账单明细")
    private List<FlowBillPageDto> flowBillPageDtos;

    @ApiModelProperty("差额认领备注")
    private String differenceRemark;

    @ApiModelProperty("差额认领原因 差额认领必传  0 银行流水已扣除手续费")
    private String differenceReason;

    @ApiModelProperty("审核意见")
    private String reviewComments;
    @ApiModelProperty("驳回人")
    private String refuseName;

    @ApiModelProperty("驳回时间")
    private LocalDateTime refuseTime;

    @ApiModelProperty("支付方式 0 现金  1 非现金")
    private Integer payChannelType;

    @ApiModelProperty("凭证单号")
    private String voucherBillNo;

    @ApiModelProperty("凭证单号")
    private Long voucherBillId;

    private String flowDetailSimpleStr;

    @ApiModelProperty("流水明细简略信息")
    List<FlowDetailSimpleVo> flowDetailSimpleVoList;


}
