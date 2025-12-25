package com.wishare.finance.domains.invoicereceipt.command.invocing;

import com.wishare.finance.apps.model.invoice.invoice.fo.GatherInvoiceBatchF;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceBillAmount;
import com.wishare.finance.apps.model.invoice.invoice.fo.InvoiceGatherDetailAmount;
import com.wishare.finance.apps.model.invoice.invoice.fo.QuotaInvoiceBindF;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.InvoiceBuildingServiceInfoF;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/9/23
 * @Description: 开具发票command
 */
@Getter
@Setter
public class AddInvoiceCommand {
    private List<ReceivableBill> oldDeveloperPayBill;
    /**
     * 开票类型：1:蓝票;2:红票 （全电发票暂不支持红票）
     */
    private Integer InvoiceType;


    /**
     * 建筑服务类-开票，入参
     * */
    private InvoiceBuildingServiceInfoF serviceInfoF;

    /**
     * 开票类型
     * 1: 增值税普通发票
     * 2: 增值税专用发票
     * 3: 增值税电子发票
     * 4: 增值税电子专票
     * 5: 收据
     * 6：电子收据
     * 7：纸质收据
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
    private String customerId;
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 开票金额
     */
    private Long invoiceAmount;
    /**
     * 申请开票时间
     */
    private LocalDateTime applyTime;
    /**
     * 开票员名称
     */
    private String clerk;

    /**
     * 发票抬头类型
     */
    private Integer invoiceTitleType;
    /**
     * 购方名称
     */
    private String buyerName;
    /**
     * 购方税号
     */
    private String buyerTaxNum;
    /**
     * 购方电话
     */
    private String buyerTel;
    /**
     * 购方地址
     */
    private String buyerAddress;
    /**
     * 购方银行开户行及账号
     */
    private String buyerAccount;
    /**
     * 销方税号
     */
    private String salerTaxNum;
    /**
     * 销方电话
     */
    private String salerTel;
    /**
     * 销方地址
     */
    private String salerAddress;
    /**
     * 销方银行开户行及账号
     */
    private String salerAccount;
    /**
     * 机器编号
     */
    private String machineCode;
    /**
     * 分机号
     */
    private Long extensionNumber;

    /**
     * 终端号
     */
    private Long terminalNumber;
    /**
     * 终端代码
     */
    private String terminalCode;
    /**
     * 用户代码
     */
    private String userCode;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 价税合计金额
     */
    private Long priceTaxAmount;

    /**
     * 收款人
     */
    private String payee;

    /**
     * 复核人
     */
    private String checker;

    /**
     * 备注信息
     */
    private String remark;
    /**
     * 推送方式：-1,不推送（默认）,0,邮箱;1,手机;2,站内信
     */
    private List<Integer> pushMode;
    /**
     * 购方手机（pushMode为1或2时，此项为必填）
     */
    private String buyerPhone;
    /**
     * 推送邮箱（pushMode为0或2时，此项为必填，）
     */
    private String email;

    /**
     * 系统来源：1 收费系统 2合同系统
     */
    private Integer sysSource;

    /**
     * 发票来源：1.开具的发票 2.收入的发票
     */
    private Integer invSource;

    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 账单ids
     */
    private List<Long> billIds;

    /**
     * 指定账单开票金额
     */
    private List<InvoiceBillAmount> invoiceBillAmounts;

    /**
     * 指定收款明细开票金额
     */
    private List<InvoiceGatherDetailAmount> invoiceGatherDetailAmounts;

    /**
     * 扩展字段：合同系统（合同名称）
     */
    private String extendFieldOne;

    private String supCpUnitId;

    /**
     * 回调地址
     */
    private String callBackUrl;

    /**
     * 收款单id
     */
    private Long gatherBillId;
    /**
     * 收款单id列表
     */
    private List<Long> gatherBillIds;
    /**
     * 收款单明细id
     */
    private List<Long> gatherDetailBillIds;
    /**
     * 收款明细信息
     */
    private List<GatherInvoiceBatchF> gatherInvoiceBatchFList;
    /**
     * 收款单明细id
     */
    private List<Long> gatherDetailBillId;

    /**
     * 收款单类型 0-收款单， 1-收款明细
     */
    private Integer gatherBillType;

    /**
     * 是否免税：0不免税，1免税， 默认不免税
     */
    private Integer freeTax;
    /**
     * 开票单元名称
     */
    private String invRecUnitName;

    /**
     * 是否批量开票：Y是，N否
     * 收款明细左上角批量开票时为Y
     */
    private String batch;

    /**
     * 发票收据主表ids和对应开票金额
     */
    private List<QuotaInvoiceBindF.InvoiceReceiptIdAndAmountF> invoiceReceiptIdAndAmount;

    /**
     * 发票总额 单位元
     */
    private Double totalInvoiceAmount;

    /**
     * 开票金额 单位元
     */
    private Double totalAmount;


    public void checkInvoice(Long canInvoiceAmount) {
        if (this.getPriceTaxAmount() > canInvoiceAmount) {
            throw BizException.throw400("价税合计异常：可开票金额为 " + canInvoiceAmount );
        }
    }
}
