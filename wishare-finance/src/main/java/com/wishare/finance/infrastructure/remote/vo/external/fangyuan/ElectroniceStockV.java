package com.wishare.finance.infrastructure.remote.vo.external.fangyuan;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/6/19 17:19
 * 发票库存查询接口 返回实体信息
 */
@Getter
@Setter
public class ElectroniceStockV {

    /**
     * 开票纳税人识别号
     */
    private String KP_NSRSBH;
    /**
     * 发票库存剩余分数
     */
    private String SYFPFS;
    /**
     * 返回代码
     */
    private String returnCode;
    /**
     * 返回信息
     */
    private String returnMsg;
}
