package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 对账记录表
 *
 * @author dxclay
 * @since 2022-10-12
 */
@Getter
@Setter
@ApiModel("对账单分页信息")
public class ReconciliationPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty(value = "对账单id")
    private String id;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "项目ID")
    private String communityId;

    @ApiModelProperty(value = "项目名称")
    private String communityName;

    @ApiModelProperty(value = "对账规则id")
    private Long reconcileRuleId;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "收款账号名称")
    private String sbAccountName;

    @ApiModelProperty(value = "对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer result;

    @ApiModelProperty(value = "对账状态：0待运行，1运行中，2已完成")
    private Integer state;

    @ApiModelProperty(value = "实收总金额")
    private BigDecimal actualTotal;

    @ApiModelProperty(value = "开票总金额")
    private BigDecimal invoiceTotal;

    @ApiModelProperty(value = "流水认领总金额")
    private BigDecimal flowClaimTotal;

    @ApiModelProperty(value = "账单总数")
    private Long billCount;

    @ApiModelProperty(value = "平账总账单数")
    private Long balanceCount;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime executeStartTime;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime executeEndTime;

    @ApiModelProperty(value = "对账日期")
    private LocalDateTime reconcileTime;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty("报账单id")
    private String voucherBillId;

    @ApiModelProperty("报账单编号")
    private String voucherBillNo;

    @ApiModelProperty("报账单id列表")
    private List<String> voucherBillIdList;

    @ApiModelProperty("报账单编号列表")
    private List<String> voucherBillNoList;

    @ApiModelProperty(value = "手续费")
    private BigDecimal commission;
}
