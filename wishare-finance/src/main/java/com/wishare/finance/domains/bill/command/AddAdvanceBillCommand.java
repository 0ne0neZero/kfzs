package com.wishare.finance.domains.bill.command;

import com.wishare.finance.apps.model.bill.fo.BillSettleChannelInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 创建预收账单
 *
 * @author yancao
 */
@Getter
@Setter
public class AddAdvanceBillCommand {

    /**
     * 是否已审核 true已审核，false待审核
     */
    private Boolean approvedFlag;

    /**
     * 结算渠道（ALIPAY：支付宝，WECHATPAY:微信支付，CASH:现金，UNIONPAY:银联，BANK:银行汇款，OTHER: 其他）
     */
    private String settleChannel;

    /**
     * 结算方式(0线上，1线下)
     */
    private Integer settleWay;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称
     */
    private String statutoryBodyName;

    @ApiModelProperty("优惠金额")
    private Long preferentialAmount = 0L;

    @ApiModelProperty("赠送金额")
    private Long presentAmount = 0L;

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
    private Long chargeItemId;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 计费方式
     */
    private Integer billMethod;

    /**
     * 计费面积：1-计费面积/2-建筑面积/3-套内面积/4-花园面积/5-物业面积/6-租赁面积
     */
    private Integer billArea;

    /**
     * 房号ID
     */
    private String roomId;

    /**
     * 房号名称
     */
    private String roomName;

    /**
     * 积分面积
     */
    private BigDecimal chargingArea;

    /**
     * 计费数量
     */
    private Integer chargingCount;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 增值税普通发票 1: 增值税普通发票 2: 增值税专用发票 3: 增值税电子发票 4: 增值税电子专票 5: 收据 6：电子收据
     */
    private String invoiceType;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 外部账单编号
     */
    private String outBillNo;

    /**
     * 外部业务单号 (必传：用于账单的重复性校验)
     */
    private String outBusNo;

    /**
     * 外部业务id
     */
    private String outBusId;

    /**
     * 账单说明
     */
    private String description;

    /**
     * 币种(货币代码)（默认：CNY:人民币）
     */
    private String currency;

    /**
     * 账单金额
     */
    private Long totalAmount;

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
     * 付款方手机号码
     */
    private String payerPhone;

    /**
     * 扩展参数
     */
    private String attachParams;

    /**
     * 账单来源
     */
    private String source;

    /**
     * 账单开始时间
     */
    private LocalDateTime startTime;

    /**
     * 账单结束时间
     */
    private LocalDateTime endTime;

    /**
     * 预收时间
     */
    private LocalDateTime payTime;

    /**
     * 行号
     */
    private Long rowNumber;

    /**
     * 应用id
     */
    private String appId;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用编码
     */
    private String appNumber;

    /**
     * 税率id
     */
    private Long taxRateId;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 收款账户id
     */
    private Long sbAccountId;

    @ApiModelProperty("业务单元id")
    private Long businessUnitId;

    @ApiModelProperty(value = "渠道交易单号")
    private String tradeNo;

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

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "归属月（账期）")
    private LocalDate accountDate;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "组合支付信息 方圆定制")
    private List<BillSettleChannelInfo> settleChannelInfos;

    public Long getPreferentialAmount() {
        return Optional.ofNullable(preferentialAmount).orElse(0L);
    }

    public Long getPresentAmount() {
        return Optional.ofNullable(presentAmount).orElse(0L);
    }
}
