package com.wishare.finance.apps.model.reconciliation.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.finance.infrastructure.remote.vo.cfg.CfgExternalDeportData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流水领用流水明细详细信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("流水领用流水明细详细信息")
public class FlowDetailPageV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("流水号")
    private String serialNumber;

    @ApiModelProperty("交易金额")
    private Long settleAmount;

    @ApiModelProperty("计算金额(元)")
    private String settleAmountUnitYuan;

    @ApiModelProperty("交易日期")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDateTime payTime;

    @ApiModelProperty("对方账户")
    private String oppositeAccount;

    @ApiModelProperty("对方名称")
    private String oppositeName;

    @ApiModelProperty("对方开户行")
    private String oppositeBank;

    @ApiModelProperty("本方账户")
    private String ourAccount;

    @ApiModelProperty("本方名称")
    private String ourName;

    @ApiModelProperty("本方开户行")
    private String ourBank;

    @ApiModelProperty("摘要")
    private String summary;

    @ApiModelProperty("资金用途")
    private String fundPurpose;

    @ApiModelProperty("交易平台")
    private String tradingPlatform;

    @ApiModelProperty("交易方式")
    private String transactionMode;

    @ApiModelProperty("是否为同步数据（0否，1是）")
    private Integer syncData;

    @ApiModelProperty("认领状态：0未认领，1已认领 2 挂起 3 报账审核中 4 认领审核中")
    private Integer claimStatus;

    @ApiModelProperty("流水类型：1收入 2退款")
    private Integer type;

    @ApiModelProperty("流水认领记录id")
    private Long flowClaimRecordId;

    @ApiModelProperty("认领批次号")
    private String flowClaimRecordSerialNumber;

    @ApiModelProperty("报账单id")
    private Long voucherBillId;

    @ApiModelProperty("报账单号")
    private String voucherBillNo;

    @ApiModelProperty("付款方式(计算方式)")
    private Integer payChannelType;

    @ApiModelProperty("付款方式(计算方式)")
    private Long statutoryBodyId;

    private Long costOrgId;

    @ApiModelProperty("成本中心所属项目id")
    private String communityId;

    @ApiModelProperty(value = "部门List")
    private List<CfgExternalDeportData> departmentList;
    @ApiModelProperty(value = "推送部门code")
    private String externalDepartmentCode;
}
