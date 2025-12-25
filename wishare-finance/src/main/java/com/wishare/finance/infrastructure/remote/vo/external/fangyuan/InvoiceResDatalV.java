package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;


import lombok.Data;

/**
 * 方圆开票查询反参数据
 * @author dongpeng
 * @date 2023/7/4 19:42
 */
@Data
public class InvoiceResDatalV {

    /**
     * 开票方纳税人识别号
     */
    private String nsrsbh;
    /**
     * 发票请求流水号
     */
    private String fpqqlsh;
    /**
     * 开票日期
     * 开票日期，格式 yyyy-MM-dd
     * HH:mm:ss
     */
    private String kprq;
    /**
     * 发票种类
     * 0 专票，2 普票，51 电子普票，52 电
     * 子专票
     */
    private String fpzl;
    /**
     * 发票代码
     */
    private String fpdm;
    /**
     * 发票号码
     */
    private String fphm;
    /**
     * 价税合计金额
     */
    private String jshj;
    /**
     * 合计不含税金额
     */
    private String hjje;
    /**
     * 合计税额
     */
    private String hjse;
    /**
     * 作废标志
     */
    private String zfbz;
    /**
     * 电子发票文件地址
     */
    private String ofdUrl;
    /**
     * 电子发票文件地
     */
    private String pdfUrl;
    /**
     * 纸票打印 ID
     */
    private String printId;
    /**
     * 普票校验码
     */
    private String jym;
}
