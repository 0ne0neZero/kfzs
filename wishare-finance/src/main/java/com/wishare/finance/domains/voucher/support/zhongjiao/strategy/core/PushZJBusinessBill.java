package com.wishare.finance.domains.voucher.support.zhongjiao.strategy.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.bill.support.PayInfosJSONListTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PushZJBusinessBill extends Bill {
    @ApiModelProperty(value = "流水id")
    private Long gatherBillDetailId;
    @ApiModelProperty(value = "收款单编号")
    private String gatherBillNo;
    @ApiModelProperty(value = "收款单编号")
    private String gatherBillId;
    @ApiModelProperty(value = "单据编号")
    private String voucherBillDetailNo;
    @ApiModelProperty(value = "单据编号")
    private Long billId;
    @ApiModelProperty(value = "汇总单号")
    private String voucherBillNo;
    @ApiModelProperty(value = "账单类型 1:应收账单，2:预收账单，3:临时收费账单")
    private Integer billType;
    @ApiModelProperty(value = "项目id")
    private String communityId;
    @ApiModelProperty(value = "所属项目")
    private String communityName;
    @ApiModelProperty(value = "资产名称")
    private String roomName;
    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;
    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;
    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;
    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;
    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;
    @ApiModelProperty(value = "客户")
    private String customName;
    @ApiModelProperty(value = "客户id")
    private String customId;
    @ApiModelProperty(value = "收款方ID")
    private String payeeId;
    @ApiModelProperty(value = "收款方名称")
    private String payeeName;
    @ApiModelProperty(value = "付款方ID")
    private String payerId;
    @ApiModelProperty(value = "付款方类型")
    private Integer payerType;
    @ApiModelProperty(value = "付款方名称")
    private String payerName;
    @ApiModelProperty(value = "开票状态：0未开票，1开票中，2部分开票，3已开票")
    private Integer invoiceState;
    @ApiModelProperty(value = "税率id")
    private Long taxRateId;
    @ApiModelProperty(value = "税率")
    private BigDecimal taxRate;
    @ApiModelProperty(value = "含税金额")
    private Long taxIncludAmount;
    @ApiModelProperty(value = "不含税金额 对应 应收金额")
    private Long taxExcludAmount;
    @ApiModelProperty(value = "税额")
    private Long taxAmount;
    @ApiModelProperty(value = "账单费用分类 1历史欠费,2当期应收,3预收款项")
    private Integer billCostType;
    @ApiModelProperty(value = "现金流项目")
    private String cashFlowItem;

    @ApiModelProperty(value = "业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "业务单元id")
    private Long sbAccountId;
    @ApiModelProperty(value = "银行账号")
    private String bankAccount;
    @ApiModelProperty(value = "银行流水号")
    private String bankSerialNumber;

    @ApiModelProperty(value = "支付信息")
    @TableField(typeHandler = PayInfosJSONListTypeHandler.class, javaType = true)
    private List<PayInfo> payInfos;
    @ApiModelProperty(value = "结转详情")
    private String carryoverDetail;
    @ApiModelProperty(value = "结算渠道")
    private String payChannel;
    @ApiModelProperty(value = "结转账单id")
    private Long billCarryoverId;
    @ApiModelProperty(value = "收款明细id")
    private Long gatherDetailId;
    @ApiModelProperty(value = "触发事件类型：1对账核销 2未收款开票 3欠费计提 4坏账确认 5收款结转 6预收结转 ")
    private Integer billEventType;

    @ApiModelProperty(value = "业务场景记录ID")
    private Long sceneId;
    @ApiModelProperty("系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统")
    private Integer sysSource;
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    @ApiModelProperty(value = "业务科目id")
    private Long subjectId;
    @ApiModelProperty(value = "业务科目名称")
    private String subjectName;

    @ApiModelProperty(value = "业务科目来源id")
    private String subjectExtId;
    @ApiModelProperty(value = "现金流项目来源id")
    private String cashFlowItemExtId;
    @ApiModelProperty(value = "业务类型编码")
    private String businessType;
    @ApiModelProperty(value = "业务类型id")
    private String businessTypeId;
    @ApiModelProperty(value = "业务类型名称")
    private String businessTypeName;
    /**
     * 逆向流程标识 ， 默认0， 1 冲销  2作废  3调整 4 减免
     *
     */
    private Integer reverseFlag = 0;

    @ApiModelProperty("变动类型编码")
    private String changeCode;

    @ApiModelProperty("变动类型名称")
    private String changeName;

    @ApiModelProperty(value = "内部合同id")
    private String innerContractId;

    @ApiModelProperty(value = "内部合同编号")
    private String innerContractNo;

    @ApiModelProperty(value = "税率字符")
    private String taxRateStr;
}
