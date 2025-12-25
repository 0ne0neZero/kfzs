package com.wishare.finance.domains.invoicereceipt.entity.invoicing;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/10/12
 * @Description:
 */
@Getter
@Setter
public class BillOjv {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单编号
     */
    private String billNo;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号
     */
    private String outBusNo;

    /**
     * 账单说明
     */
    private String description;

    @ApiModelProperty("成本中心id")
    private Long costCenterId;

    @ApiModelProperty("成本中心名称")
    private String costCenterName;

    /**
     * CNY	币种(货币代码)（CNY:人民币）
     */
    private String currency;

    /**
     * 收款账号id
     */
    private String sbAccountId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer type;

    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 类型账单id，对应账单类型的账单id
     */
    private Long typeBillId;

    /**
     * 账单金额
     */
    private Long totalAmount;

    /**
     * 应收金额
     */
    private Long receivableAmount;

    /**
     * 应收减免金额
     */
    private Long deductibleAmount;

    /**
     * 违约金金额
     */
    private Long overdueAmount;

    /**
     * 实收减免金额
     */
    private Long discountAmount;

    /**
     * 实收金额（实收金额 = 应收金额金额 + 违约金金额 - 优惠总额）
     */
    private Long settleAmount;

    /**
     * 开票金额
     */
    private Long invoiceAmount;

    /**
     * 退款金额
     */
    private Long refundAmount;

    /**
     * 实收金额
     */
    private Long actualPayAmount;

    /**
     * 实际应缴金额
     */
    private Long actualUnpayAmount;

    /**
     * 收款方ID
     */
    private String payeeId;

    /**
     * 收款方名称
     */
    private String payeeName;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 增值税普通发票
     *   1: 增值税普通发票
     *   2: 增值税专用发票
     *   3: 增值税电子发票
     *   4: 增值税电子专票
     *   5: 收据
     *   6：电子收据
     */
    private String invoiceType;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 账单状态（0正常，1作废，2冻结，3挂账）
     */
    private Integer state;

    /**
     * 结算状态（0未结算，1部分结算，2已结算）
     */
    private Integer settleState;

    /**
     * 结转状态：0未结转，1待结转，2部分结转，3已结转
     */
    private Integer carriedState;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    private Integer refundState;

    /**
     * 核销状态（0未核销，1已核销）
     */
    private Integer verifyState;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    private Integer approvedState;

    /**
     * 开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    private Integer invoiceState;

    /**
     * 是否交账：0未交账，1已交账
     */
    private Integer accountHanded;

    /**
     * 是否拆单：0未拆单，1已拆单
     */
    private Integer separated;

    /**
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

    /**
     * 是否调整：0未调整，1已调整
     */
    private Integer adjusted;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    private String statutoryBodyName;

    /**
     * 项目ID
     */
    private String communityId;

    /**
     * 项目名称
     */
    private String communityName;

    /**
     * 费项id
     */
    private String chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 计费方式 (1:单价*面积/月，2:单价/月，3:单价*面积/天，4:单价/天)
     */
    private Integer billMethod;

    /**
     * 计费面积
     */
    private BigDecimal chargingArea;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 是否逾期：0未逾期，1已逾期
     */
    private Integer overdueState;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 应用名称
     */
    private String appName;

}
