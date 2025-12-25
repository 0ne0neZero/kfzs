package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import lombok.Data;

/**
 * 方圆全电发票商品明细信息
 * @author dongpeng
 * @date 2023/7/4 19:14
 */
@Data
public class XmxxsF {

    /**
     * 发票行编号--非必填--长度 30
     * 发票行编号
     */
    private String fphbh;
    /**
     * 发票行性质--必填--长度 1
     * 0 正常行、1 折扣行、2 被折扣行
     */
    private String fphxz;
    /**
     * 项目名称--必填--长度 90
     * 商品名称(长度是按 byte 算，英文数
     * 字字符按 1byte 算，汉字按 2byte 算)
     */
    private String xmmc;
    /**
     * 单位--非必填--长度 20
     * 计量单位(长度是按 byte 算，英文数
     * 字字符按 1byte 算，汉字按 2byte 算)
     */
    private String dw;
    /**
     * 规格型号--非必填--长度 40
     * 规格型号(长度是按 byte 算，英文数
     * 字字符按 1byte 算，汉字按 2byte 算)
     */
    private String ggxh;
    /**
     * 项目数量--必填--长度 18
     * 项目数量，最多 8 位小数，红票或为
     * 折扣行时为负数
     */
    private String xmsl;
    /**
     * 项目单价--必填--长度 18
     * 项目单价，最多 8 位小数，单价必须
     * 是正数
     */
    private String xmdj;
    /**
     * 项目金额--必填--长度 18
     * 单位：元（2 位小数）红票或为折扣
     * 行时为负数
     */
    private String xmje;
    /**
     * 税率--必填--长度 2
     * 整数，如 6 代表 6%税率
     */
    private String sl;
    /**
     * 税额--必填--长度 18
     * 单位：元（2 位小数）红票或为折扣
     * 行时为负数
     */
    private String se;
    /**
     * 含税标志--必填--长度 1
     * 1，含税
     */
    private String hsbz;
    /**
     * 商品税收分类编码--必填--长度 19
     * 商品 19 位税收分类编码
     */
    private String spbm;
    /**
     * 自行编码--非必填--长度 16
     */
    private String zxbm;
    /**
     * 优惠政策标识--必填--长度 1
     * 0：不使用，1 使用
     */
    private String yhzcbs;
    /**
     * 零税率标识--非必填--长度 1
     * 空：非零税率，1：免税，2：不征
     * 税，3 普通零税率(一般用于出口发票)
     */
    private String lslbs;
    /**
     * 增值税特殊管理--非必填--长度 500
     */
    private String zzstsgl;
    /**
     * 扣除额--必填--长度 18
     */
    private String kce;
}
