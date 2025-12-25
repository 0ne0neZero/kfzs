package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

/**
 * (款项明细)说明
 */
@Getter
@Setter
public class DSJJKJMXData {
    /**
     * 扣减明细内码
     */
    private String KJMXNM;
    private String DL;
    /**
     *	计量内码
     */
    private String JLNM;
    /**
     *	款项ID
     */
    private String KJXM;
    /**
     *	变动
     */
    private String BD;
    /**
     *	应收应付ID
     */
    private String YSYFBH;
    /**
     *	原币ID
     */
    private String YBID;
    /**
     *	汇率
     */
    private Double HL;
    /**
     *	金额（原币）
     */
    private Double KJJEYB;
    /**
     *	金额（本位币）
     */
    private Double KJJEBB;
    /**
     *	到期日期
     */
    private String DQRQ;
    /**
     *	预计收付款日期
     */
    private String SFKRQ;
    /**
     *	本次核销税额（原币）
     */
    private Double BCHXSEYB;
    /**
     *	本次核销税额（本位币）
     */
    private Double BCHXSEBB;
    /**
     *	预留字段1
     */
    private String YLZD1;
    /**
     *预留字段2
     */
    private String YLZD2;
    /**
     *预留字段3
     */
    private String YLZD3;
    /**
     *预留字段4
     */
    private String YLZD4;
    /**
     *预留字段5
     */
    private String YLZD5;
}
