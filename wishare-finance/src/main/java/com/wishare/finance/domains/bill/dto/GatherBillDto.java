package com.wishare.finance.domains.bill.dto;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.domains.bill.entity.DiscountOBV;
import com.wishare.finance.domains.bill.entity.GatherDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 收款单信息
 * @author yancao
 */
@Setter
@Getter
@ApiModel("收款单信息")
public class GatherBillDto {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty("交易单号")
    private String tradeNo;

    @ApiModelProperty(value = "账单编号")
    private String billNo;

    @ApiModelProperty(value = "外部账单编号")
    private String outBillNo;

    @ApiModelProperty(value = "外部业务单号")
    private String outBusNo;

    @ApiModelProperty(value = "外部业务id")
    private String outBusId;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private String costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private String chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

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

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "结算渠道 ALIPAY：支付宝， WECHATPAY:微信支付， CASH:现金， POS: POS机， UNIONPAY:银联， SWIPE: 刷卡， BANK:银行汇款， CARRYOVER:结转， CHEQUE: 支票 OTHER: 其他")
    private String payChannel;

    @ApiModelProperty("收款方式拼接")
    private String payInfosString;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "收款方式名称")
    private String payWayName;

    public List<DiscountOBV> getDiscounts() {
        if (StringUtils.isNotBlank(discounts)) {
            return JSON.parseArray(discounts, DiscountOBV.class);
        }
        return new ArrayList<>();
    }

    @ApiModelProperty(value = "减免说明列表")
    private String discounts;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "账单说明")
    private String description;

    @ApiModelProperty(value = "币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty(value = "账单金额（单位：分）")
    private Long totalAmount;

    @ApiModelProperty(value = "实收减免金额（单位：分）")
    private Long discountAmount;

    @ApiModelProperty(value = "退款金额（单位：分）")
    private Long refundAmount;

    @ApiModelProperty(value = "结转金额（单位：分）")
    private Long carriedAmount;

    @ApiModelProperty(value = "开票金额（单位：分）")
    private Long invoiceAmount;

    @ApiModelProperty(value = "扣款金额（单位：分）")
    private Long deductionAmount;

    @ApiModelProperty(value = "收款人ID")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款人ID")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "扩展参数")
    private String attachParams;

    @ApiModelProperty(value = "系统来源编码 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "账单状态（0正常，1冻结，2作废）")
    private Integer state;

    @ApiModelProperty(value = "是否挂账：0未挂账，1已挂账，2已销账")
    private Integer onAccount;

    @ApiModelProperty(value = "退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    @ApiModelProperty(value = "核销状态（0未核销，1已核销）")
    private Integer verifyState;

    @ApiModelProperty(value = "审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState;

    @ApiModelProperty(value = "结转状态：0未结转，1待结转，2部分结转，3已结转")
    private Integer carriedState;

    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;

    @ApiModelProperty("账票对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer reconcileState;

    @ApiModelProperty("商户清分对账结果：0未核对，1部分核对，2已核对，3核对失败")
    private Integer mcReconcileState;

    @ApiModelProperty(value = "是否交账：0未交账，1部分交账，2已交账")
    private Integer accountHanded;

    @ApiModelProperty(value = "是否推凭：0未推凭，1已推凭")
    private Integer inferenceState;

    @ApiModelProperty(value = "是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    @ApiModelProperty(value = "是否参与优惠赠送：0否，1是")
    private Integer preferential;

    @ApiModelProperty(value = "自定义项1")
    private String extField1;

    @ApiModelProperty(value = "自定义项2")
    private String extField2;

    @ApiModelProperty(value = "自定义项3")
    private String extField3;

    @ApiModelProperty(value = "自定义项4")
    private String extField4;

    @ApiModelProperty(value = "自定义项5")
    private String extField5;

    @ApiModelProperty(value = "自定义项6")
    private String extField6;

    @ApiModelProperty(value = "自定义项7")
    private String extField7;

    @ApiModelProperty(value = "自定义项8")
    private String extField8;

    @ApiModelProperty(value = "自定义项9")
    private String extField9;

    @ApiModelProperty(value = "自定义项10")
    private String extField10;

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

    @ApiModelProperty("收款明细")
    private List<GatherDetail> gatherDetails;

    @ApiModelProperty("归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("支付来源:0-PC管理后台 1-业主端app 2-业主端小程序 3-物管端app 4-物管端小程序 " +
            "10-亿家生活app，11-亿家生活公众号，12-亿家生活小程序，13-亿管家APP，14-亿管家智能POS机，15-亿家生活公众号物管端")
    private Integer paySource;

}
