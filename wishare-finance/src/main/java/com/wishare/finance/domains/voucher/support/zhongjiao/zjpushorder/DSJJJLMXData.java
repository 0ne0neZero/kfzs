package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

/**
 * （本期计量小计）说明
 */
@Getter
@Setter
public class DSJJJLMXData {
    /**
     *	计量明细内码
     */
    private String JLMXNM;
    /**
     *	计量内码
     */
    private String JLNM;
    /**
     *	清单项目
     */
    private String QDXM;
    /**
     *	数量
     */
    private String SLNUMBER;
    /**
     *	单价
     */
    private String DJ;
    /**
     *	原币
     */
    private String BZID;
    /**
     *	汇率
     */
    private String JLHL;
    /**
     *	含税金额（原币）
     */
    private Double HSJEYB;
    /**
     *	含税金额（本币）
     */
    private Double HSJEBB;
    /**
     *	税率
     */
    private String SL;
    /**
     *	税额（原币）
     */
    private String SEYB;
    /**
     *	税额（本位币）
     */
    private Double SEBB;
    /**
     *	不含税金额（原币）
     */
    private Double BHSYB;
    /**
     *	不含税金额（本位币）
     */
    private String BHSBB;
    private String SFXT;
    private String JLLX;
    private String SFYSKZ;
    private String SFYFH;
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
