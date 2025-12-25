package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import lombok.Data;

/**
 * @author dongpeng
 * @date 2023/7/5 16:06
 */
@Data
public class InterfaceInfoF {
    /**
     * 接口版本
     */
    private String version;
    /**
     * 接口编码
     */
    private String interfaceCode;
    /**
     * 纳税人识别号
     */
    private String taxpayerId;
    /**
     * 报文密码
     */
    private String passWord;
    /**
     * 开票分机号-开票终端号
     */
    private String fjh;
    /**
     * 机器编号
     */
    private String jqbh;
    /**
     * 数据交换流水号
     */
    private String dataExchangeId;
    /**
     *  (0,1根据数据包大小判断是否进行压缩)
     */
    private String zipCode;
    /**
     * (加密方式代码，0表示不用任何加密,1:3DES加密，2:CA
     * 加密)
     */
    private String encryptCode;
    /**
     * base64请求数据内容
     */
    private String content;
}
