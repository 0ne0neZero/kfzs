package com.wishare.finance.apps.model.bill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 收款单明细信息
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收款单明细信息")
public class GatherDetailV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "账单类型")
    private Integer gatherType;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "收款单id")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单号")
    private String gatherBillNo;

    @ApiModelProperty(value = "应收单id")
    private Long recBillId;

    @ApiModelProperty(value = "应收单编号")
    private String recBillNo;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "费项类型：1常规收费类型 2临时收费类型 3押金收费类型")
    private Integer chargeItemType;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "结算渠道 ALIPAY：支付宝， WECHATPAY:微信支付， CASH:现金， POS: POS机， UNIONPAY:银联， SWIPE: 刷卡， BANK:银行汇款， CARRYOVER:结转， CHEQUE: 支票 OTHER: 其他")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "外部支付编号（支付宝单号，银行流水号等）")
    private String outPayNo;

    @ApiModelProperty(value = "结转账单id")
    private Long carriedBillId;

    @ApiModelProperty(value = "结转账单编号")
    private String carriedBillNo;

    @ApiModelProperty(value = "结转账单类型 1:应收单，2:预收单，3:应付单，4:付款单，5:收款单，6预付单")
    private Integer carriedBillType;

    @ApiModelProperty(value = "结转记录id")
    private Long billCarryoverId;

    @ApiModelProperty(value = "应收收款金额（单位：分）")
    private Long recPayAmount;

    @ApiModelProperty(value = "收款金额（单位：分）(合单支付时，收款金额 > 结算金额)")
    private Long payAmount;

    @ApiModelProperty(value = "收费对象类型")
    private Integer payerType;

    @ApiModelProperty(value = "付款人id")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "付款人电话")
    private String payerPhone;

    @ApiModelProperty(value = "收款人id")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty(value = "租户id")
    private String tenantId;

    @ApiModelProperty(value = "是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty(value = "创建人ID")
    private String creator;

    @ApiModelProperty(value = "创建人姓名")
    private String creatorName;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty(value = "操作人ID")
    private String operator;

    @ApiModelProperty(value = "修改人姓名")
    private String operatorName;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty(value = "单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "计费数量")
    private Integer chargingCount;

    @ApiModelProperty(value = "归属月")
    private LocalDate accountDate;

    @ApiModelProperty(value = "应收金额")
    private Long receivableAmount;

    @ApiModelProperty(value = "账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty(value = "开票金额（分）")
    private Long invoiceAmount;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty(value = "审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty(value = "是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty(value = "新收款明细查询使用：所属收款单明细总数")
    private Integer total;

    @ApiModelProperty(value = "空间id")
    private Long spaceId;
    /**
     * 是否是违约金
     */
    private Integer isDefault;

    /**
     * 是否是违约金名称
     */
    private String isDefaultName;


    /**
     * 账单类型名称
     */
    private String billTypeName;

    /**
     * 收款方式名称
     */
    private String payWayName;

    /**
     * 收款方式拼接名称
     */
    private String payInfosString;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("现金流科目")
    private String subjectName;


    /**
     * 计费方式
     */
    @ApiModelProperty("计费方式")
    private Integer billMethod;

    /**
     * 是否有效 0有效 1失效
     */
    private Integer available;

    /**
     * 退款金额（分）
     */
    private Long refundAmount;

    /**
     * 结转金额（分）
     */
    private Long carriedAmount;

    /**
     * 扣款金额（分）
     */
    private Long deductionAmount;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;
}
