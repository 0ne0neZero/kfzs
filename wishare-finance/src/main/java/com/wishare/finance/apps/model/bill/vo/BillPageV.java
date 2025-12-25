package com.wishare.finance.apps.model.bill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("账单信息")
public class BillPageV {

    @ApiModelProperty(value = "推凭状态 0-未推凭，1-已推凭")
    private Integer inferenceState;


    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("账单id")
    private Long id;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("业务单元id")
    private Long businessUnitId;

    @ApiModelProperty("业务单元名称")
    private String businessUnitName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("付款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payerType;

    @ApiModelProperty("收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payeeType;

    @ApiModelProperty("付款方手机号码")
    private String payerPhone;

    @ApiModelProperty("增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据")
    private String invoiceType;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("修改人姓名")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("外部业务id")
    private String outBusId;

    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount;

    @ApiModelProperty("冲销金额  (单位： 分)")
    private Long reverseAmount;

    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmount;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmount;

    @ApiModelProperty("实收金额（实收金额 = 应收金额金额 + 违约金金额 - 优惠总额） (单位： 分)")
    private Long settleAmount;

    @ApiModelProperty("结转金额")
    private Long carriedAmount;

    @ApiModelProperty("剩余可结转金额")
    private Long remainingCarriedAmount;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("收款方ID")
    private String payeeId;

    @ApiModelProperty("收款方名称")
    private String payeeName;

    @ApiModelProperty("付款方ID")
    private String payerId;

    @ApiModelProperty("付款方名称")
    private String payerName;

    @ApiModelProperty("账单来源")
    private String source;

    @ApiModelProperty("账单状态（0正常，1作废，2冻结，3挂账）")
    private Integer state;

    @ApiModelProperty("挂单状态（0未挂账，1已挂账，2已销账）")
    private Integer onAccount;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty("核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty("开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty("是否交账：0未交账，1已交账")
    private Integer accountHanded;

    @ApiModelProperty("是否拆单：0未拆单，1已拆单")
    private Integer separated;

    @ApiModelProperty("是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty("是否调整：0未调整，2已调整")
    private Integer adjusted;

    @ApiModelProperty("结转状态：0未结转，1部分结转，2已结转")
    private Integer carriedState;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty("商户清分对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty(value = "开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("结算渠道")
    private String payChannel;

    @ApiModelProperty("结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty("收费组织id")
    private String cpOrgId;

    @ApiModelProperty("收费组织名称")
    private String cpOrgName;

    @ApiModelProperty("上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty("收费单元id")
    private String cpUnitId;

    @ApiModelProperty("收费单元名称")
    private String cpUnitName;

    @ApiModelProperty("税额（单位：分）")
    private Long taxAmount;

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("扩展字段1")
    private String extField1;

    @ApiModelProperty("扩展字段2")
    private String extField2;

    @ApiModelProperty("扩展字段3")
    private String extField3;

    @ApiModelProperty("扩展字段4")
    private String extField4;

    @ApiModelProperty("扩展字段5")
    private String extField5;

    @ApiModelProperty("扩展字段6")
    private String extField6;

    @ApiModelProperty("扩展字段7")
    private String extField7;

    @ApiModelProperty("扩展字段8")
    private String extField8;

    @ApiModelProperty("扩展字段9")
    private String extField9;

    @ApiModelProperty("扩展字段10")
    private String extField10;

    @ApiModelProperty("账单备注")
    private String remark;

    @ApiModelProperty("税率id")
    private Long taxRateId;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    // 审核信息
    /**
     * 审核原因
     */
    @ApiModelProperty( "审核原因")
    private String oprReason;
    /**
     * 审核操作类型
     */
    @ApiModelProperty("审核操作类型")
    private Integer oprType;
    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String oprRemark;

}
