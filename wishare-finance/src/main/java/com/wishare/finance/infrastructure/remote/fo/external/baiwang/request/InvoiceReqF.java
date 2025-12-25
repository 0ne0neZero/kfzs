package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/19 15:03
 * @descrption: 发票开具接口
 */
@Data
@ApiModel(value = "发票开局请求体")
public class InvoiceReqF {

    // 必填，长度20
    @ApiModelProperty(value = "税号")
    private String taxNo;

    // 非必填，长度30
    @ApiModelProperty(value = "服务器/税务ukey 用户专普票必填，电子票非必填；税务ukey终端 \n" +
            "使用机器编号；盘用户，盘号终端选填，如果只有一个终端可不填 \n" +
            "，全电票种选填")
    private String invoiceTerminalCode;

    // 非必填，长度10
    @ApiModelProperty(value = "是否需要拆分开具，默认不拆分（拆分只支持发票类型代码为 \n" +
            "专票 004 普票 007 电子票 026的发票）")
    private Boolean isSplit;

    // 非必填，长度64
    @ApiModelProperty(value = "组织机构编码，如果为空则上传至税号对应的机构下，如果维护了 \n" +
            "机构则按照机构归属待开信息，根据判断自行信息管理选择是否设 \n" +
            "置开票相关信息；根据组织机构编码获取销方信息")
    private String orgCode;

    // 非必填，长度12
    @ApiModelProperty(value = "盘号终端选填，如果只有一个终端可不填")
    private String taxDiskNo;

    // 非必填，长度10
    @ApiModelProperty(value = "是否需要生成版式返回版式链接(true / false);此参数仅对税控类开具的电子发票生效")
    private Boolean formatGenerate;

    // 非必填，长度1
    @ApiModelProperty(value = "版式生成是否推送(true / false);此参数仅对税控类开具的电子发票生效")
    private Boolean formatPushType;

    // 非必填，长度1
    @ApiModelProperty(value = "值为（1/0 1 需要补全 0不需要补全，默认为0）是否根据客户编码")
    private String completionCustom;

    // 必填
    private InvoiceDataReqF data;
}
