package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/6/19 17:18
 * 发票开票 返回实体
 */
@Getter
@Setter
public class ReturnElectroniceV {

    /**
     * 操作码
     * 0000：下发成功
     * 9999：下发失败
     */
    private String CZDM;
    /**
     * 订单号
     */
    private String DDH;
    /**
     * 发票流水号
     */
    private String FPQQLSH;
    /**
     * 开票类型
     * 1正票、2红票
     */
    private String KPLX;
    /**
     * 开票日期
     */
    private String KPRQ;
    /**
     * 发票号码
     */
    private String FP_HM;
    /**
     * 发票代码
     */
    private String FP_DM;
    /**
     * 检验码
     */
    private String JYM;
    /**
     * 版式文件名称
     * 格式：流水号_发票代码_发票号码.pdf
     */
    private String PDF_FILE;
    /**
     * 下载链接
     * http://61.144.36.122:5000/IS/pd?id=20160506181822000886_4400082620_00784025.pdf
     */
    private String PDF_URL;
    /**
     * 税额
     */
    private String SE;
    /**
     * 含税金额
     */
    private String KPHJJE;
    /**
     * 合计不含税金额
     */
    private String HJBHSJE;
    /**
     * 购货方税号
     */
    private String FPZL_DM;
    /**
     * 未知数据
     */
    private String EWM;
    /**
     * 未知数据
     */
    private String KPLSH;
    /**
     * 未知数据
     */
    private String FWM;
    /**
     * 结果代码
     * 0：下发成功
     * 9999：下发失败
     * 1003：该单据已作废，不可进行开票
     */
    private String returnCode;
    /**
     * 结果描述
     * 错误原因
     */
    private String returnMsg;
}
