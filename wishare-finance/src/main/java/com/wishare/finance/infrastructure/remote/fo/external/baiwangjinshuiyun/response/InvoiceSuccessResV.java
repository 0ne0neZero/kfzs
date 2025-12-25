package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request.BlueInvoiceDetailF;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 响应体信息
 * @author dongpeng
 * @date 2023/10/25 20:28
 */
@Data
public class InvoiceSuccessResV {

    /**
     * 单据编号
     * 蓝票申请单据编号
     */
    private String djbh;

    /**
     * 全电发票号码
     */
    private String qdfphm;

    /**
     * 发票代码
     * 纸质发票时有值
     */
    private String fpdm;

    /**
     * 发票号码
     * 纸质发票时有值
     */
    private String fphm;

    /**
     * 发票类型代码
     * 01:增值税专用发票,02:普通发票
     */
    private String fplxdm;

    /**
     * 开票日期
     * yyyy-MM-dd HH:mm:ss
     */
    private String kprq;

    /**
     * 价税合计
     * 小数点2位
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal jshj;

    /**
     * 合计金额
     * 小数点2位
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal hjje;

    /**
     * 合计税额
     * 小数点2位
     */
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    private BigDecimal hjse;

    /**
     * 电子发票PDF地址
     * 电子发票有值
     */
    private String pdf;

    /**
     * 电子发票OFD地址
     * 电子发票有值
     */
    private String ofd;

    /**
     * 电子发票XML地址
     * 电子发票有值
     */
    private String xml;

    /**
     * 部门代码
     */
    private String bmdm;

    /**
     * 数据类型
     * 业务系统代号,如1:代表批发系统 2:代表零售系统
     */
    private String sjlx;

    /**
     * 数据来源
     */
    private String sjly;

    /**
     * 备注
     */
    private String bz;

    /**
     * 商品明细 企业开启全票面返回时有值
     */
    private List<BlueInvoiceDetailF> mxxx;
}
