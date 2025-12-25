package com.wishare.finance.infrastructure.remote.vo.bill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("账单详情")
public class BillDetailRV {

    @ApiModelProperty("账单id")
    private Long id;

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

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("收费对象类型（0:业主，1开发商，2租客）")
    private Integer payerType;

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

    @ApiModelProperty("外部业务id")
    private String outBusId;

    @ApiModelProperty("外部业务单号")
    private String outBusNo;

    @ApiModelProperty("币种(货币代码)（CNY:人民币）")
    private String currency;

    @ApiModelProperty("账单金额")
    private Long totalAmount;

    @ApiModelProperty("应收金额  (单位： 分)")
    private Long receivableAmount;

    @ApiModelProperty("应收减免金额  (单位： 分)")
    private Long deductibleAmount;

    @ApiModelProperty("违约金金额 (单位： 分)")
    private Long overdueAmount;

    @ApiModelProperty("实收减免金额 (单位： 分)")
    private Long discountAmount;

    @ApiModelProperty("结算金额 (单位： 分)")
    private Long settleAmount;

    @ApiModelProperty("结转金额")
    private Long carriedAmount;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("开票金额")
    private Long invoiceAmount;

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

    @ApiModelProperty("收款账号id")
    private String sbAccountId;

    @ApiModelProperty("账单状态（0正常，1作废，2冻结，3挂账）")
    private Integer state;

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

    @ApiModelProperty("是否挂账：0未挂账，1已挂账")
    private Integer onAccount;

    @ApiModelProperty("税率id")
    private Long taxRateId;

    @ApiModelProperty("税率")
    private BigDecimal taxRate;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("应用名称")
    private String appName;

    @ApiModelProperty(value = "应用编码")
    private String appNumber;

    @ApiModelProperty("实际缴费金额")
    private Long actualPayAmount;

    @ApiModelProperty("实际应缴金额")
    private Long actualUnpayAmount;

    @ApiModelProperty("账单说明")
    private String description;

    /**
     * 计算实收金额
     *
     * @return
     */
    public Long getActualPayAmount() {
        return settleAmount - refundAmount - carriedAmount;
    }
}
