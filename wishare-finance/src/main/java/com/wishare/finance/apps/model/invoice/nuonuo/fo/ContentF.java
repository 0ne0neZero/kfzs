package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import com.wishare.finance.apps.model.invoice.nuonuo.vo.InvoiceItemsV;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author dongpeng
 * @date 2023/8/22 16:41
 */
@Data
@ApiModel("开票信息")
public class ContentF {

    @ApiModelProperty(value = "发票状态（1：开票完成、2：开票失败、3：开票成功签章失败（电票时））")
    private String c_status;

    @ApiModelProperty(value = "开票日期")
    private String c_kprq;

    @ApiModelProperty(value = "发票代码（全电电票时为空，全电纸票时有值）")
    private String c_fpdm;

    @ApiModelProperty(value = "发票号码（全电电票时为空，全电纸票时有值）")
    private String c_fphm;

    @ApiModelProperty(value = "价税税额")
    private String c_hjse;

    @ApiModelProperty(value = "不含税金额")
    private String c_bhsje;

    @ApiModelProperty(value = "订单号")
    private String c_orderno;

    @ApiModelProperty(value = "发票流水号")
    private String c_fpqqlsh;

    @ApiModelProperty(value = "发票pdf地址")
    private String c_url;

    @ApiModelProperty(value = "发票详情地址")
    private String c_jpg_url;

    @ApiModelProperty(value = "购方邮箱")
    private String email;

    @ApiModelProperty(value = "购方手机")
    private String phone;

    @ApiModelProperty(value = "购方税号")
    private String taxnum;

    @ApiModelProperty(value = "购方名称")
    private String buyername;

    @ApiModelProperty(value = "购方电话")
    private String telephone;

    @ApiModelProperty(value = "购方地址")
    private String address;

    @ApiModelProperty(value = "购方银行开户行及账号")
    private String bankAccount;

    @ApiModelProperty(value = "开票员")
    private String c_clerk;

    @ApiModelProperty(value = "收款人")
    private String c_payee;

    @ApiModelProperty(value = "复核人")
    private String c_checker;

    @ApiModelProperty(value = "销方银行账号")
    private String c_salerAccount;

    @ApiModelProperty(value = "销方税号")
    private String c_saletaxnum;

    @ApiModelProperty(value = "销方名称")
    private String c_salerName;

    @ApiModelProperty(value = "销方电话")
    private String c_salerTel;

    @ApiModelProperty(value = "销方地址")
    private String c_salerAddress;

    @ApiModelProperty(value = "备注")
    private String c_remark;

    @ApiModelProperty(value = "失败原因")
    private String c_errorMessage;

    @ApiModelProperty(value = "发票种类")
    private String c_invoice_line;

    @ApiModelProperty(value = "校验码")
    private String checkCode;

    @ApiModelProperty(value = "二维码")
    private String qrCode;

    @ApiModelProperty(value = "发票ofd地址")
    private String c_ofd_url;

    @ApiModelProperty(value = "成品油标志")
    private String productOilFlag;

    @ApiModelProperty(value = "分机号")
    private String extensionNumber;

    @ApiModelProperty(value = "终端号")
    private String terminalNumber;

    @ApiModelProperty(value = "发票jpg图片地址")
    private String c_imgUrls;

    @ApiModelProperty(value = "红票对应的蓝票发票代码")
    private String c_yfpdm;

    @ApiModelProperty(value = "红票对应的蓝票发票号码")
    private String c_yfphm;

    @ApiModelProperty(value = "清单标志")
    private String c_qdbz;

    @ApiModelProperty(value = "清单项目名称")
    private String c_qdxmmc;

    @ApiModelProperty(value = "发票密文")
    private String cipherText;

    @ApiModelProperty(value = "机动车专票标志")
    private String vehicleFlag;

    @ApiModelProperty(value = "冲红原因")
    private String redReason;

    @ApiModelProperty(value = "发票特定要素")
    private String specificFactor;

    @ApiModelProperty(value = "购买方经办人姓名")
    private String buyerManagerName;

    @ApiModelProperty(value = "经办人证件类型")
    private String managerCardType;

    @ApiModelProperty(value = "经办人身份证件号码")
    private String managerCardNo;

    @ApiModelProperty("发票明细集合")
    private List<InvoiceItemsV> invoiceItems;

}
