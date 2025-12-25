package com.wishare.contract.apps.remote.vo.imagefile;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 识别结果返回
 *
 * @author 龙江锋
 * @date 2023/8/8 17:33
 */
@Data
@ApiModel("识别结果返回")
public class OcrInfo {
    /**
     * 发票类型：
     * 区块链发票	25
     * 增值税普票	2
     * 增值税卷票	3
     * 增值税电子发票	11
     * 火车票主表	30
     * 出租车票主表	6
     * 定额发票主表	56
     * 增值税专票	1
     */
    @ApiModelProperty("发票类型")
    private String invoicetype;

    /**
     * 发票代码
     */
    @ApiModelProperty("发票代码")
    private String invoicecode;

    /**
     * 开票日期
     */
    @ApiModelProperty("发票号码")
    private String invoicenum;

    /**
     * 开票日期
     */
    @ApiModelProperty("开票日期")
    private String invoicedate;

    /**
     * 不含税金额
     */
    @ApiModelProperty("不含税金额")
    private String jamount;

    /**
     * 价税合计
     */
    @ApiModelProperty("价税合计")
    private String totalamount;

    /**
     * 校验码，20位
     */
    @ApiModelProperty("校验码，20位")
    private String checkcode;

    /**
     * 购方识别号
     */
    @ApiModelProperty("购方识别号")
    private String gfsbh;

    /**
     * 购方识别号
     */
    @ApiModelProperty("销方识别号")
    private String xfsbh;

    /**
     * 购方名称
     */
    @ApiModelProperty("购方名称")
    private String inname;

    /**
     * 销方名称
     */
    @ApiModelProperty("销方名称")
    private String outname;

    /**
     * 核验是否成功,0/成功,1/失败
     */
    @ApiModelProperty("核验是否成功,0/成功,1/失败")
    private String code;

    /**
     * 核验文本消息
     */
    @ApiModelProperty("核验文本消息")
    private String message;

    /**
     * 电子凭证验证结果。
     * 0:验证通过
     * 1:文件被篡改
     * 2:没有签名或印章
     * 3:OFD印章不合法
     * 4.xbrl解析失败
     */
    @ApiModelProperty("电子凭证验证结果")
    private String verifyresult;

}
