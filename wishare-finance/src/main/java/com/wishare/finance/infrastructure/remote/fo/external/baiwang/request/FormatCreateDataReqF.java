package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/22 15:19
 * @descrption:
 */
@Data
@ApiModel(value = "版式文件生成请求数据体")
public class FormatCreateDataReqF {

    @ApiModelProperty(value = "发票代码")
    private String invoiceCode = "";

    @ApiModelProperty(value = "版式通道标识，为空时默认为10000，传入非法值时会导致版式无法推送")
    private String pushType = "";

    @ApiModelProperty(value = "1 全电版式生成 其他代表税控发票生成")
    private String invoiceIssueMode = "";

    @ApiModelProperty(value = "手机号码")
    private String phone = "";

    @ApiModelProperty(value = "最多可配置5个邮件地址，地址之间用英文逗号分隔")
    private String emailCarbonCopy = "";

    @ApiModelProperty(value = "全电发票号码")
    private String einvoiceNo = "";

    @ApiModelProperty(value = "发票号码")
    private String invoiceNo = "";

    @ApiModelProperty(value = "发票请求流水号")
    private String serialNo = "";

    @ApiModelProperty(value = "邮箱")
    private String email = "";
}
