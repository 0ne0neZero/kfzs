package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response;

import lombok.Data;


/**
 * 响应体信息
 * @author dongpeng
 * @date 2023/10/25 20:28
 */
@Data
public class RedInvoiceSuccessResV {

    /**
     * 单据编号
     * 蓝票申请单据编号
     */
    private String djbh;

    /**
     * 红字发票信息确认单编号
     */
    private String hzfpxxqrdbh;

    /**
     *
     * 红字申请成功UUID
     */
    private String uuid;

    /**
     * 录入方身份
     * 0销方 1购方
     */
    private String lrfsf;

    /**
     * 增值税用途状态
     */
    private String zzsytDm;

    /**
     * 消费税用途状态
     */
    private String xfsytDm;

    /**
     * 发票入账状态
     *
     */
    private String fprzztDm;

    /**
     * 红字确认信息状态
     * 01无需确认；
     * 02 销方录入待购方确认
     * 03 购方录入待销方确认
     * 04 购销方都已确认
     * 05 作废(销方录入购方否认)
     * 06 作废(购方录入销方否认)
     * 07 作废(超72小时未作废)
     * 08 作废(发起方已撤销)
     * 09 作废(确认后撤销)
     * 10 作废(异常凭证)
     */
    private String hzqrxxztDm;

    /**
     * 已开具红字发票标记
     */
    private String ykjhzfpbz;

    /**
     * 购销身份
     */
    private String gxsf;
}
