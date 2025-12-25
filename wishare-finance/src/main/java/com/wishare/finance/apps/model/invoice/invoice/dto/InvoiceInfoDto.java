package com.wishare.finance.apps.model.invoice.invoice.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xujian
 * @date 2022/11/10
 * @Description:
 */
@Getter
@Setter
@ApiModel("发票详细信息")
public class InvoiceInfoDto {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 开票类型：1:蓝票;2:红票
     */
    private Integer invoiceType;
    /**
     * 发票收据主表id
     */
    private Long invoiceReceiptId;

    /**
     * 原蓝票发票主表id,可为多个,红票时必填
     */
    private Long blueInvoiceReceiptId;

    /**
     * 票据编号
     */
    private String invoiceReceiptNo;
    /**
     * 票据类型
     1: 增值税普通发票
     2: 增值税专用发票
     3: 增值税电子发票
     4: 增值税电子专票
     5: 收据
     6：电子收据
     7：纸质收据
     */
    private Integer type;
    /**
     * 账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 价税合计金额
     */
    private Long priceTaxAmount;
    /**
     * 开票状态：1 开票中 2 开票成功 3 开票失败 4 红冲中 5 已红冲 6 已作废
     */
    private Integer state;

    /**
     * 发票明细
     */
    private List<InvoiceReceiptDetailE> invoiceReceiptDetailES;

}
