package com.wishare.finance.apps.model.reconciliation.vo;

import com.alibaba.fastjson.JSONArray;

import com.wishare.finance.apps.model.bill.vo.ContractFlowBillV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 根据id获取认领明细数据
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("根据id获取认领明细数据")
public class FlowClaimRecordDetailV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("流水批次号")
    private String serialNumber;

    @ApiModelProperty("认领金额")
    private Long claimAmount;

    @ApiModelProperty("实收金额")
    private Long settleAmount;

    @ApiModelProperty("认领差额")
    private BigDecimal compareAmount;

    @ApiModelProperty("认领日期")
    private LocalDateTime claimDate;

    @ApiModelProperty("本方名称")
    private String ourName;

    @ApiModelProperty("认领人")
    private String creatorName;

    @ApiModelProperty("系统来源:1收费系统，2合同系统")
    private Integer sysSource;

    @ApiModelProperty("认领类型：1:发票认领;2:账单认领;")
    private Integer claimType;
    /**
     * 对账标识，是否对账  0 未对账  1 已对账
     */
    @ApiModelProperty("对账标识，是否对账  0 未对账  1 已对账")
    private Integer reconcileFlag;

    @ApiModelProperty("当前流水的状态")
    private Integer currentFlowDetailState;

    @ApiModelProperty("审核状态: 0 已审核 1 审核中")
    private Integer approveState;

    @ApiModelProperty("资金收款单推送 0未生成资金收款单 1待推送 2已推送 3 推送失败")
    private Integer pushState;

    @ApiModelProperty("流水明细")
    private List<FlowDetailPageV> flowDetailList;

    @ApiModelProperty("发票明细")
    private List<FlowInvoiceDetailV> flowInvoiceDetailList;

    @ApiModelProperty("账单明细")
    private List<ContractFlowBillV> flowBillDetailList;

    @ApiModelProperty("认领方式 1全额认领 0差额认领")
    private String flowType;

    @ApiModelProperty("差额认领备注")
    private String differenceRemark;

    @ApiModelProperty("差额认领原因 差额认领必传  0 银行流水已扣除手续费")
    private String differenceReason;

    private String flowFiles;

    private String reportFiles;
    @ApiModelProperty("银行回单文件")
    private JSONArray flowFilesJson;

    @ApiModelProperty("日报表文件")
    private JSONArray reportFilesJson;

    /**
     * 创建时间
     */

    private LocalDateTime gmtCreate;

}
