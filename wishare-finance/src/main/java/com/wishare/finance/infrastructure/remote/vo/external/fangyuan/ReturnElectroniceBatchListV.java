package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/6/19 17:19
 * 查询电子发票信息接口（多张） 返回实体信息
 */
@Getter
@Setter
public class ReturnElectroniceBatchListV {

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
    private int KPLX;
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
     * 购货方名称
     */
    private String CZDM;
    /**
     * 购货方税号
     */
    private String FPZL_DM;
    /**
     * 含税金额
     */
    private double KPHJJE;
    /**
     * 税额
     */
    private double SE;
    /**
     * 版式文件名称
     * 格式：流水号_发票代码_发票号码.pdf
     */
    private String PDF_FILE;
    /**
     * 版式文件下载地址
     * 通过地址获取文件,例如：
     * http://61.144.36.122:5000/IS/pd?id=20160506181822000886_4400082620_00784025.pdf
     */
    private String PDF_URL;
    /**
     * 结果代码
     * 0000：查询成功
     * 9996：销货方纳税人识别号为空
     * 9998：查询返回数据为空
     * 9997：查询报错失败
     * 9999：开票日期区间或者发票代码发票号码组合不能为空
     */
    private String RETURNCODE;
    /**
     * 结果描述
     * 返回信息
     */
    private String RETURNMESSAGE;


}
