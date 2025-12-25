package com.wishare.finance.apps.model.voucher.vo;

import com.wishare.finance.domains.voucher.entity.VoucherCostCenterOBV;
import com.wishare.finance.domains.voucher.entity.VoucherDetailOBV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 凭证
 * @author: pgq
 * @since: 2022/10/24 19:41
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("凭证明细")
public class VoucherV {

    @ApiModelProperty(value = "凭证id")
    private Long id;

    @ApiModelProperty(value = "报账凭证编号")
    private String voucherNo;

    @ApiModelProperty(value = "同步系统凭证编号")
    private String syncSystemVoucherNo;

    @ApiModelProperty(value = "录制方式：0自动录制，1手动录制")
    private Integer madeType;

    @ApiModelProperty(value = "凭证类别： 1记账凭证")
    private Integer voucherType;

    @ApiModelProperty(value = "账簿id")
    private Long accountBookId;

    @ApiModelProperty(value = "账簿编码")
    private String accountBookCode;

    @ApiModelProperty(value = "账簿名称")
    private String accountBookName;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位编码")
    private String statutoryBodyCode;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心 [ {\"costCenterId\": 成本中心id, \"costCenterName\": \"成本中心名称\"}]")
    private List<VoucherCostCenterOBV> costCenters;

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "推凭金额 单位：分")
    private Long amount;

    @ApiModelProperty(value = "推凭状态：0待同步，1成功，2失败，3同步中")
    private Integer state;

    @ApiModelProperty(value = "业务单据信息")
    private List<SimpleVoucherBusinessV> businessBills;

    @ApiModelProperty(value = "分录详情")
    private List<VoucherDetailOBV> details;

    @ApiModelProperty(value = "会计期间")
    private LocalDate fiscalPeriod;

    @ApiModelProperty(value = "会计年度")
    private Integer fiscalYear;

    @ApiModelProperty(value = "记账日期")
    private LocalDate bookkeepingDate;

    @ApiModelProperty(value = "推凭记录id")
    private Long voucherRuleRecord;

    @ApiModelProperty(value = "触发事件")
    private Integer evenType;

    @ApiModelProperty(value = "同步系统: 1用友NCC")
    private Integer syncSystem;

    @ApiModelProperty(value = "同步时间")
    private LocalDateTime syncTime;

    @ApiModelProperty(value = "制单人id")
    private String makerId;

    @ApiModelProperty(value = "制单人名称")
    private String makerName;

    @ApiModelProperty(value = "凭证来源：0 系统生成，1 BPM推送")
    private Integer voucherSource;

    /**
     * {@linkplain SysSourceEnum}
     */
    private Integer sysSource;

    @ApiModelProperty(value = "推凭失败原因")
    private String errorReason;

    @ApiModelProperty(value = "创建人名称")
    private String creatorName;

    @ApiModelProperty(value = "创建人id")
    private String creator;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人名称")
    private String operatorName;

    @ApiModelProperty(value = "操作人id")
    private String operator;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty(value = "触发事件")
    private String evenValue;


    /**
     * 成本中心名称
     */
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式")
    private String payChannelValue;

}
