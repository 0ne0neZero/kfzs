package com.wishare.finance.apps.model.bill.vo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 无效账单分页查询(用于业主端查询账单)
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("无效账单分页查询")
public class AllBillPageVo{

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("账单标识（空.无标识 1.冲销标识）")
    private Integer billLabel;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("费项id")
    private Long chargeItemId;

    @ApiModelProperty("费项名称")
    private String chargeItemName;

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

    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty("收款账号id")
    private Long sbAccountId;

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("账单编号")
    private String billNo;

    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("外部业务id(用于跳转)")
    private String outBusId;

    @ApiModelProperty("账单说明")
    private String description;

    @ApiModelProperty("CNY\t币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty("账单类型（1:应收账单，2:预收账单，3:临时收费账单,4:应付账单）")
    private Integer type;

    @ApiModelProperty("账单金额")
    private BigDecimal totalAmount;

    @ApiModelProperty("应收金额")
    private BigDecimal receivableAmount;

    @ApiModelProperty("应收减免金额")
    private BigDecimal deductibleAmount;

    @ApiModelProperty("违约金金额")
    private BigDecimal overdueAmount;

    @ApiModelProperty("实收减免金额")
    private BigDecimal discountAmount;

    @ApiModelProperty("实收金额")
    private BigDecimal settleAmount;

    @ApiModelProperty("退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty("结转金额")
    private BigDecimal carriedAmount;

    @ApiModelProperty("开票金额")
    private BigDecimal invoiceAmount;

    @ApiModelProperty("冲销金额")
    private BigDecimal reverseAmount;

    @ApiModelProperty("付款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payerType;

    @ApiModelProperty("收款方类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer payeeType;

    @ApiModelProperty("账单费用分类 1历史欠费,2当期应收,3预收款项")
    private Integer billCostType;

    @ApiModelProperty("收款方ID")
    private String payeeId;

    @ApiModelProperty("收款方名称")
    private String payeeName;

    @ApiModelProperty("付款方ID")
    private String payerId;

    @ApiModelProperty("付款方名称")
    private String payerName;

    @ApiModelProperty("增值税普通发票\n" +
            "     *   1: 增值税普通发票\n" +
            "     *   2: 增值税专用发票\n" +
            "     *   3: 增值税电子发票\n" +
            "     *   4: 增值税电子专票\n" +
            "     *   5: 收据\n" +
            "     *   6：电子收据")
    private String invoiceType;

    @ApiModelProperty("收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    @ApiModelProperty("扩展参数")
    private String attachParams;

    @ApiModelProperty("账单来源")
    private String source;

    @ApiModelProperty("账单状态（0正常，1冻结，2作废，3挂账）")
    private Integer state = 0;

    @ApiModelProperty("是否挂账：0未挂账，1已挂账")
    private Integer onAccount = 0;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState = 0;

    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState = 0;

    @ApiModelProperty("核销状态（0未核销，1已核销）")
    private Integer verifyState = 0;

    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState = 0;

    @ApiModelProperty("结转状态：0未结转，1待结转，2部分结转，3已结转")
    private Integer carriedState = 0;

    @ApiModelProperty("开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState = 0;

    @ApiModelProperty("账票流水对账状态：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState = 0;

    @ApiModelProperty("商户清分对账状态：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

    @ApiModelProperty("是否交账：0未交账，1已交账")
    private Integer accountHanded = 0;

    @ApiModelProperty("是否已推凭过： 0未推凭，1已推凭")
    private Integer inferenceState = 0;

    @ApiModelProperty("税率id")
    private Long taxRateId;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("是否拆单：0未拆单，1已拆单")
    private Integer separated;

    @ApiModelProperty("是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty("是否调整：0未调整，1已调整")
    private Integer adjusted;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty("应用名称")
    private String appNumber;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("缴费时间")
    private LocalDateTime payTime;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

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

    @ApiModelProperty("收费对象ID")
    private String customerId;

    @ApiModelProperty("收费对象名称")
    private String customerName;

    @ApiModelProperty("收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty("收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    @ApiModelProperty("实际缴费金额")
    private BigDecimal actualPayAmount;

    private BigDecimal actualPayAmountNew;

    @ApiModelProperty("是否负数手续费 0：否 1：是")
    private Integer negativeCommission = 0;

    @ApiModelProperty("实际应缴金额")
    private BigDecimal actualUnpayAmount;

    @ApiModelProperty("归属年")
    private Integer accountYear;

    @ApiModelProperty("归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("金额单位")
    private String amountUnit = "元";

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("无效状态")
    private String invalidState;

    @ApiModelProperty("计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)")
    private Integer billMethod;

    @ApiModelProperty("计费面积")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;

    /**
     * 扩展字段6(废弃原因---作废或冲销的原因)
     */
    private String extField6;


    /**
     * 获取实际收款金额
     * @return  可结转的金额 = 结算金额 - 退款金额 - 结转金额 - 冲销金额
     */
    public BigDecimal getActualSettleAmount(){
        return settleAmount.subtract(refundAmount).subtract(carriedAmount).subtract(reverseAmount).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 获取实际应缴总金额
     *
     * @return long
     */
    public BigDecimal getActualUnpayAmount() {
        return getRemainingSettleAmount();
    }

    /**
     * 计算实收金额
     */
    public BigDecimal getActualPayAmount() {
        return getActualSettleAmount();
    }

    /**
     * 获取剩余可支付金额
     * @return  可支付金额 = 应收金额 + 违约金金额 - 实收减免金额 + 退款金额 + 结算金额（上次结算金额）-收款金额
     */
    public BigDecimal getRemainingSettleAmount(){
        return receivableAmount.add(overdueAmount).subtract(discountAmount).add(refundAmount).add(carriedAmount).subtract(settleAmount);
    }

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;
}
