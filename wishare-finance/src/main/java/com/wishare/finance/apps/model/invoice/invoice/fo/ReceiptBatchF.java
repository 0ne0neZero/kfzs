package com.wishare.finance.apps.model.invoice.invoice.fo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddInvoiceReceiptDetailCommand;
import com.wishare.finance.domains.invoicereceipt.command.invocing.AddReceiptCommand;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvSourceEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.infrastructure.remote.enums.NuonuoPushModeEnum;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/13
 * @Description:
 */
@Getter
@Setter
@ApiModel("开具收据")
public class ReceiptBatchF {

    @ApiModelProperty(value = "开票类型：\n" +
            "     1: 增值税普通发票\n" +
            "     2: 增值税专用发票\n" +
            "     3: 增值税电子发票\n" +
            "     4: 增值税电子专票\n" +
            "     5: 收据\n" +
            "     6：电子收据\n" +
            "     7：纸质收据", required = true)
    @NotNull(message = "开票类型不能空")
    private Integer type;

    @ApiModelProperty("收据号")
    private Long receiptNo;

    @ApiModelProperty(value = "开票员名称", required = true)
    // @NotBlank(message = "开票员名称不能为空")
    private String clerk;

    @ApiModelProperty(value = "是否展示开票人(收据pdf文件)")
    private String clerkStatus;

    @ApiModelProperty(value = "系统来源：1 收费系统 2合同系统", required = true)
    @NotNull(message = "系统来源不能为空")
    private Integer sysSource;

    @ApiModelProperty(value = "收据来源：1.开具的收据 2.收入的收据", required = true)
    @NotNull(message = "收据来源不能为空")
    private Integer invSource;

    @ApiModelProperty("备注信息")
    private String remark;

    @ApiModelProperty("缴费时间")
    private LocalDateTime paymentTime;

    @ApiModelProperty("缴费方式")
    private String paymentType;

    @ApiModelProperty("图章url")
    private String stampUrl;

    @ApiModelProperty("优惠信息" +
            " {\n" +
            "     \"goodsName\":\"\",\n" +
            "     \"price\":\"\",\n" +
            "     \"num\":\"\",\n" +
            "     \"totalPrice\":\"\",\n" +
            "     \"remark\":\"\"\n" +
            "     }")
    private String discountInfo;

    @ApiModelProperty(value = "价税合计", required = true)
    @NotNull(message = "价税合计不能为空")
    @Min(value = 1,message = "价税合计必须大于0")
    private Long priceTaxAmount;

    @ApiModelProperty(value = "账单ids", required = true)
    private List<Long> billIds;

    @ApiModelProperty("指定账单开票金额")
    private List<InvoiceBillAmount> invoiceBillAmounts;

    @ApiModelProperty(value = "账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty(value = "推送方式：-1,不推送,0,邮箱;1,手机")
    @NotNull(message = "推送方式不能为空")
    private List<Integer> pushMode;

    @ApiModelProperty(value = "购方手机（pushMode为1时，此项为必填）")
    private String buyerPhone;

    @ApiModelProperty(value = "推送邮箱（pushMode为0时，此项为必填）")
    private String email;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "票据模板ID")
    private Long receiptTemplateId;

    @ApiModelProperty(value = "是否签章 0是 1否")
    private Integer signStatus;

    /** 目前中交e签宝专用 */
    @ApiModelProperty(value = "流程id")
    private Long invoiceFlowMonitorId;
    @ApiModelProperty("收款单id列表")
    private List<Long> gatherBillIds;

    @ApiModelProperty("收款单明细id列表")
    private List<Long> gatherDetailBillIds;

    @ApiModelProperty(value = "收款单类型 0-收款单， 1-收款明细")
    private Integer gatherBillType;

    @ApiModelProperty("收款明细信息")
    private List<GatherInvoiceBatchF> gatherInvoiceBatchFList;

    @ApiModelProperty(value = "是否批量开票：Y是，N否")
    private String batch;

    /** 中交 当前标识 开收据 不对账单等数据做任何改动 */
    @ApiModelProperty(value = "是否跳过")
    private Boolean skip;

    /**
     * 校检入参
     */
    public void checkParam() {
        //校检收据类型
        this.checkType();
        //校检账单类型，收据可能包含多个账单类型，不再校验
        // this.checkBillType();
        //校检推送方式
        this.checkPushMode();
        //校检系统来源
        this.checkSysSource();
        //校检收据来源
        this.checkInvSource();
    }

    /**
     * 校检收据来源
     */
    private void checkInvSource() {
        InvSourceEnum invSourceEnum = InvSourceEnum.valueOfByCode(this.getInvSource());
        if (null == invSourceEnum) {
            throw BizException.throw400("收据来源异常");
        }
    }

    /**
     * 校检系统来源
     */
    private void checkSysSource() {
        SysSourceEnum sysSourceEnum = SysSourceEnum.valueOfByCode(this.getSysSource());
        if (null == sysSourceEnum) {
            throw BizException.throw400("系统来源异常");
        }
    }

    /**
     * 校检账单类型
     */
    private void checkBillType() {
        BillTypeEnum billTypeEnum = BillTypeEnum.valueOfByCode(this.getBillType());
        if (billTypeEnum == null) {
            throw BizException.throw400("账单类型异常");
        }
    }

    /**
     * 校检收据类型
     */
    public void checkType() {
        InvoiceLineEnum typeEnum = InvoiceLineEnum.valueOfByCode(this.getType());
        if (null == typeEnum) {
            throw BizException.throw400("收据类型异常");
        }
    }

    /**
     * 校检推送方式
     */
    private void checkPushMode() {
        List<Integer> pushModeList = this.getPushMode();
        for (int i = 0; i < pushModeList.size(); i++) {
            NuonuoPushModeEnum nuonuoPushModeEnum = NuonuoPushModeEnum.valueOfByCode(pushModeList.get(i));
            switch (nuonuoPushModeEnum) {
                case 不推送:
                    break;
                case 手机:
                    if (StringUtils.isBlank(buyerPhone)) {
                        throw BizException.throw400("推送方式为手机,请检查购方手机号");
                    }
                    break;
                case 邮箱:
                    if (StringUtils.isBlank(email)) {
                        throw BizException.throw400("推送方式为邮箱,请检查邮箱号");
                    }
                    break;
            }
        }
    }


}
