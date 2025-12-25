package com.wishare.finance.domains.voucher.strategy.core;

import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 凭证业务单据信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/7
 */
@Getter
@Setter
public class VoucherBusinessBill {

    /**
     * 收费对象调整时用到
     * */
    private Long originalAmount;

    /**
     * 结转金额
     * */
    private Long carryoverAmount;

    @ApiModelProperty(value = "业务单据id")
    private Long businessBillId;

    private Long accountBookId;

    @ApiModelProperty(value = "业务单据编号")
    private String businessBillNo;

    @ApiModelProperty(value = "业务场景记录ID")
    private String sceneId;

    @ApiModelProperty(value = "场景类型(事件类型 1应收计提 2收款结算 3预收应收核销 4账单调整 5账单开票 6冲销作废 7未认领暂收款 8应付计提 9付款结算 )")
    private Integer sceneType;

    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称中文")
    private String statutoryBodyName;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "业务单据类型: 1应收单,2预收单, 3临时单, 4应付单, 5退款单, 6付款单, 7收款单，8发票，9收据，10银行流水， 11银行对账单")
    private Integer businessBillType;

    @ApiModelProperty("业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "手续费")
    private Long fee;

    @ApiModelProperty(value = "账单金额")
    private Long totalAmount;

    @ApiModelProperty(value = "应收/付金额")
    private Long receivableAmount;

    @ApiModelProperty(value = "应收减免金额")
    private Long deductibleAmount;

    @ApiModelProperty(value = "实收减免金额")
    private Long discountAmount;

    @ApiModelProperty(value = "实收/付金额")
    private Long actualPayAmount;

    private Long adjustAmount;

    @ApiModelProperty(value = "退款金额")
    private Long refundAmount;

    @ApiModelProperty(value = "税额")
    private Long taxAmount;

    @ApiModelProperty(value = "税率id")
    private Long taxRateId;

    @ApiModelProperty(value = "增值税税率")
    private BigDecimal taxRate;

    @ApiModelProperty(value = "归属年")
    private Integer accountYear;

    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "收费对象类型（0:业主，1开发商，2租客，3客商，4法定单位）")
    private Integer customerType;

    @ApiModelProperty(value = "收费对象ID")
    private String customerId;

    @ApiModelProperty(value = "收费对象名称")
    private String customerName;

    @ApiModelProperty(value = "收款账号id")
    private Long sbAccountId;

    @ApiModelProperty(value = "系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;

    @ApiModelProperty(value = "房号ID")
    private Long roomId;

    @ApiModelProperty(value = "业务类型编码")
    private String businessCode;

    @ApiModelProperty(value = "业务类型名称")
    private String businessName;

    @ApiModelProperty(value = "支付方式")
    private String payChannel;

    @ApiModelProperty(value = "支付渠道")
    private Integer payWay;

    @ApiModelProperty(value = "收款时间")
    private LocalDateTime payTime;

    /**
     * {@linkplain BillSettleStateEnum}
     */
    @ApiModelProperty(value = "结算状态")
    private Integer settleState;

    private Long gatherBillId;


    private Integer adjustWay;

    @ApiModelProperty("源收费对象ID")
    private String originalPayerId;


    @ApiModelProperty("源收费对象类型")
    private Integer originalPayerType;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    @ApiModelProperty(value = "借贷类型： credit贷方， debit借方")
    private String type;

    /**
     * 付款方ID
     */
    private String payerId;
    private Long receivableBillId;
    private Long targetBillId;
    private Long advanceId;
    private Integer advanceCarried;
}
