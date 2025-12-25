package com.wishare.finance.domains.invoicereceipt.command.invocing;

import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.model.invoice.invoice.dto.OtherAmountDto;
import com.wishare.finance.apps.model.invoice.invoice.dto.ReceiptDetailDto;
import com.wishare.finance.apps.model.invoice.invoice.fo.GatherInvoiceBatchF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBillAmount;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceClaimStatusEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.ReceiptE;
import com.wishare.finance.infrastructure.remote.enums.BillSettleStateEnum;
import com.wishare.finance.infrastructure.remote.enums.SettleWayChannelEnum;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description: 开具收据command
 */
@Getter
@Setter
public class AddReceiptCommand {

    /**
     * 收据号
     */
    private Long receiptNo;

    /**
     * 收据编号
     */
    private String invoiceReceiptNo;

    /**
     * 开票类型
     */
    private Integer type;

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
     * 客户id
     */
    private Long customerId;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 开票员名称
     */
    private String clerk;

    /**
     * 系统来源：1 收费系统 2合同系统
     */
    private Integer sysSource;

    /**
     * 发票来源：1.开具的发票 2.收入的发票
     */
    private Integer invSource;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 缴费时间
     */
    private LocalDateTime paymentTime;

    /**
     * 缴费方式
     */
    private String paymentType;

    /**
     * 图章url
     */
    private String stampUrl;

    /**
     * 优惠信息
     */
    private String discountInfo;

    /**
     * 价税合计
     */
    private Long priceTaxAmount;

    /**
     * 账单ids
     */
    private List<Long> billIds;

    /**
     * 指定账单开票金额
     */
    private List<InvoiceBillAmount> invoiceBillAmounts;

    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 推送方式不能为空
     * 推送方式：-1,不推送,0,邮箱;1,手机;2,站内信
     */
    private List<Integer> pushMode;

    /**
     * 购方手机（pushMode为1或2时，此项为必填）
     */
    private String buyerPhone;

    /**
     * 推送邮箱（pushMode为0或2时，此项为必填）
     */
    private String email;

    /**
     * 票据模板ID
     */
    private Long receiptTemplateId;
    /**
     * 上级收费单元id
     */
    private String supCpUnitId;

    /** 目前中交e签宝专用 */
    @ApiModelProperty(value = "流程id")
    private Long invoiceFlowMonitorId;

    @ApiModelProperty(value = "是否签章 0是 1否")
    private Integer signStatus;

    /**
     * 票据模板名称
     */
    private String receiptTemplateName;

    @ApiModelProperty(value = "是否批量开票：Y是，N否")
    private String batch;

    @ApiModelProperty("收款单id列表")
    private List<Long> gatherBillIds;

    @ApiModelProperty("收款单明细id列表")
    private List<Long> gatherDetailBillIds;

    @ApiModelProperty(value = "收款单类型 0-收款单， 1-收款明细")
    private Integer gatherBillType;

    @ApiModelProperty("收款明细信息")
    private List<GatherInvoiceBatchF> gatherInvoiceBatchFList;
}
