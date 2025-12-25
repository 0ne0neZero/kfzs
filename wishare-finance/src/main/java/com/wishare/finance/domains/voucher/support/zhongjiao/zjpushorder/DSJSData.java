package com.wishare.finance.domains.voucher.support.zhongjiao.zjpushorder;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DSJSData {
    /**
     * 单据内码
     */
    private String DJNM;
    /**
     * 单据编号
     */
    private String DJBH;
    /**
     * 核算组织编号
     */
    private String HSDW;
    /**
     * 核算部门编号
     */
    private String HSBM;
    /**
     * 行政组织OID
     */
    private String XZZZ;
    /**
     * 行政部门OID
     */
    private String XZBM;
    /**
     * 项目ID
     */
    private String XMID;
    /**
     * 经办人ID
     */
    private String ZDR;
    private String BEBMC;
    /**
     * 制单日期
     */
    private String DJRQ;
    /**
     * 来源系统
     */
    private String LYXT;
    private String BZID;
    private String USER;
    private String DSJJHL;
    /**
     * 业务类型ID
     */
    private String YWLX;
    private String HTMC;
    /**
     * 合同编号
     */
    private String HTBH;
    /**
     * 业主单位
     */
    private String YZDW;
    private String DJZT;
    /**
     * 结算单号
     */
    private String JSDH;
    /**
     * 是否签认
     */
    private String SFQR;
    /**
     * 计量开始日期 格式：yyyy-MM-dd
     */
    private String JLKSRQ;
    private String BZMC;
    private String USERCODE;
    /**
     * 计量截止日期
     */
    private String JLJZRQ;
    /**
     * 计量确认日期
     */
    private String JLQRRQ;
    /**
     * 本期计量含税金额（本位币）
     */
    private Double HSJEBB;
    /**
     * 	本期计量不含税金额（本位币）
     */
    private Double BHSJEBB;
    /**
     * 本期计量税额（本位币）
     */
    private Double SEBB;
    /**
     *本期累计计量含税金额（本位币）
     */
    private Double  LJHSJEBB;
    /**
     * 	计税方式
     */
    private String JSFS;
    /**
     * 	本位币ID
     */
    private String BWBID;
    /**
     * 	附件张数
     */
    private Integer DJFJZS;
    /**
     * 报账事由
     */
    private String BZSY;
    /**
     * 摘要
     */
    private String DJZY;
    /**
     * 	本期计量税额（原币）
     */
    private String SEYB;
    /**
     * 	税率 小数点后2位，生成预收款时必填
     */
    private Double SL;
    /**
     * 是否生成准则差异账
     */
    private String NWZBZ;
    /**
     * 凭证日期
     */
    private String PZRQ;
    /**
     * 预留字段1
     */
    private String YLZD1;
    /**
     * 预留字段2
     */
    private String YLZD2;
    /**
     * 预留字段3
     */
    private String YLZD3;
    /**
     * 预留字段4
     */
    private String YLZD4;
    /**
     * 预留字段5
     */
    private String YLZD5;
    /**
     * 是否主营  默认1
     */
    private Integer SFZY;

}
