package com.wishare.finance.infrastructure.remote.fo.external.nuonuo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 诺税通saas红字确认单申请接口入参
 * @author luzhonghe
 * @version 1.0
 * @since 2023/4/13
 */
@Getter
@Setter
public class ElectronInvoiceRedApplyF {

//    /**
//     * 红字确认单申请号，需要保持唯一，不传
//     * 的话系统自动生成一个
//     */
    private String billId;

    /**
     * 对应蓝票发票种类: bs:电子发票(增值税专
     * 用发票)， pc:电子发票(普通发票)，es:全
     * 电纸质发票(增值税专用发票)， ec:全电纸
     * 质发票(普通发票)
     */
    private String blueInvoiceLine;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 申请方（录入方）身份： 0 销方 1 购方
     */
    private String applySource;

    /**
     * 对应蓝票全电号码（全电普票、全电专票
     * 都需要）
     */
    private String blueInvoiceNumber;

    /**
     * 销方税号
     */
    private String sellerTaxNo;

    /**
     * 销方名称，申请说明为销方申请时可为空
     */
    private String sellerName;

    /**
     * 购方名称
     */
    private String buyerName;

    /**
     * 购方税号
     */
    private String buyerTaxNo;

    /**
     * 冲红原因： 1销货退回 2开票有误 3服务中
     * 止 4销售折让
     */
    private String redReason;

    /**
     * 红字确认单明细信息列表（全电部分冲红
     * 时才需要传）
     */
    private List<RedConfirmDetailF> detail;
}
