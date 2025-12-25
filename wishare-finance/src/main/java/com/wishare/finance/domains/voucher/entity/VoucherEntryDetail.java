package com.wishare.finance.domains.voucher.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 分录详情
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/6
 */
@Getter
@Setter
public class VoucherEntryDetail {

    /**
     * 借贷类型： debit借方， icredit贷方
     */
    private String type;
    /**
     * 借贷类型名称
     */
    private String typeName;
    /**
     * 借贷金额
     */
    private String amount;
    /**
     *
     */
    private String localCredit;
    /**
     * 规则描述
     */
    private String ruleRemark;
    /**
     * 科目代码
     */
    private String subjectCode;
    /**
     * 科目名称
     */
    private String subjectName;
    /**
     * 币种名称 （人民币）
     */
    private String current;
    /**
     * 辅助核算详情信息
     */
    private String supItemName;
    /**
     * 辅助编码集合
     */
    private String auxiliaryCount;

}
