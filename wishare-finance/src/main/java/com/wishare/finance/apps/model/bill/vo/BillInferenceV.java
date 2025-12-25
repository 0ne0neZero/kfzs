package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 账单推凭数据
 * @author: pgq
 * @since: 2022/10/24 20:46
 * @version: 1.0.0
 */
@Setter
@Getter
@ApiModel("账单推凭信息")
public class BillInferenceV {

    @ApiModelProperty("金额单位")
    private String amountUnit = "分";

    /**
     * 账单id
     */
    @ApiModelProperty("账单id")
    private Long id;

    /**
     * 关联（需修改推凭状态的id）
     * 调整凭证 - bill_adjust.id
     * 结算 - xxx_detail的.id
     */
    @ApiModelProperty("关联（需修改推凭状态的id）")
    private Long concatId;

    /**
     * 账单编号
     */
    @ApiModelProperty("账单编号")
    private String billNo;

    /**
     * 法定单位id
     */
    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    /**
     * 法定单位名称中文
     */
    @ApiModelProperty("法定单位名称中文")
    private String statutoryBodyName;

    /**
     * 成本中心id
     */
    @ApiModelProperty("法定单位id")
    private Long costCenterId;

    /**
     * 成本中心名称中文
     */
    @ApiModelProperty("法定单位名称中文")
    private String costCenterName;

    /**
     * 项目id
     */
    @ApiModelProperty("项目id")
    private String communityId;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String communityName;

    /**
     * 推断状态
     */
    @ApiModelProperty("推断状态")
    private int inferenceStatus;

    /**
     * 账单来源
     */
    @ApiModelProperty("账单来源")
    private String source;

    /**
     * 费项id
     */
    @ApiModelProperty("费项id")
    private Long chargeItemId;

    /**
     * 费项名称
     */
    @ApiModelProperty("费项名称")
    private String chargeItemName;

    /**
     * 房号ID
     */
    @ApiModelProperty("房号ID")
    private Long roomId;

    /**
     * 房号名称
     */
    @ApiModelProperty("房号名称")
    private String roomName;

    /**
     * 账单开始时间
     */
    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    @ApiModelProperty("收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

    /**
     * 外部账单编号
     */
    @ApiModelProperty("外部账单编号")
    private String outBillNo;

    /**
     * 外部业务单号
     */
    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    /**
     * 币种(货币代码)（CNY:人民币）
     */
    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency;

    /**
     * 结算金额
     */
    @ApiModelProperty("账单金额")
    private Long totalAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty("应收金额")
    private Long receivableAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty("违约金金额")
    private Long overdueAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty("实收减免金额")
    private Long discountAmount;

    /**
     * 结算金额
     */
    @ApiModelProperty("结算金额")
    private Long settleAmount;

    /**
     * 转结金额
     */
    @ApiModelProperty("应收减免金额")
    private Long deductibleAmount;

    /**
     * 转结金额
     */
    @ApiModelProperty("转结金额")
    private Long carriedAmount;

    /**
     * 退款金额
     */
    @ApiModelProperty("退款金额")
    private Long refundAmount;

    /**
     * 退款金额
     */
    @ApiModelProperty("开票金额")
    private Long invoiceAmount;

    /**
     * 费项类型 1收入 2支出
     */
    @ApiModelProperty("费项类型 1收入 2支出")
    private Integer chargeItemType;

    /**
     * 费项类型 1收入 2支出
     */
    @ApiModelProperty("收款方ID")
    private String payeeId;

    /**
     * 费项类型 1收入 2支出
     */
    @ApiModelProperty("收款方名称")
    private String payeeName;

    /**
     * 付款方ID
     */
    @ApiModelProperty("付款方ID")
    private String payerId;

    /**
     * 付款方名称
     */
    @ApiModelProperty("付款方名称")
    private String payerName;

    /**
     * 增值税普通发票\r\n1: 增值税普通发票\r\n2: 增值税专用发票\r\n3: 增值税电子发票\r\n4: 增值税电子专票\r\n5: 收据\r\n6：电子收据\r\n7:纸质收据
     */
    @ApiModelProperty("增值税普通发票\\r\\n1: 增值税普通发票\\r\\n2: 增值税专用发票\\r\\n3: 增值税电子发票\\r\\n4: 增值税电子专票\\r\\n5: 收据\\r\\n6：电子收据\\r\\n7:纸质收据")
    private String invoiceType;

    /**
     * 收费对象属性（1个人，2企业）
     */
    @ApiModelProperty("收费对象属性（1个人，2企业）")
    private Integer payerLabel;

    /**
     * 账单状态（0正常，1冻结，2作废）
     */
    @ApiModelProperty("账单状态（0正常，1冻结，2作废）")
    private Integer state;

    /**
     * 是否冲销：0未冲销，1已冲销
     */
    @ApiModelProperty("是否冲销：0未冲销，1已冲销")
    private Integer reversed;

    /**
     * 退款状态（0未退款，1退款中，2部分退款，已退款）
     */
    @ApiModelProperty("退款状态（0未退款，1退款中，2部分退款，已退款）")
    private Integer refundState;

    /**
     * 结算状态（0未结算，1部分结算，2已结算）
     */
    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）")
    private Integer settleState;

    /**
     * 核销状态（0未核销，1已核销）
     */
    @ApiModelProperty("核销状态（0未核销，1已核销）")
    private Integer verifyState = 0;

    /**
     * 审核状态：0未审核，1审核中，2已审核，3未通过
     */
    @ApiModelProperty("审核状态：0未审核，1审核中，2已审核，3未通过")
    private Integer approvedState = 0;

    /**
     * 开票状态：0未开票，1开票中，2部分开票，3已开票
     */
    @ApiModelProperty("开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState = 0;

    /**
     * 账单创建时间
     */
    @ApiModelProperty("账单创建时间")
    private LocalDateTime gmtCreate;

    /**
     * 税率
     */
    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    /**
     * 收款账号
     */
    @ApiModelProperty("收款账号")
    private Long sbAccountId;

    /**
     * 收费对象属性（1个人，2企业）
     */
    @ApiModelProperty("收费对象属性（1个人，2企业）")
    private Integer customerLabel;

    /**
     * 查询税率对应的账单id
     */
    private Long taxBillId;

    /**
     * 查询税率对应的账单id
     */
    private Integer taxBillType;

}
