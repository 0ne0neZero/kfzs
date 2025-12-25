package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import com.wishare.finance.domains.invoicereceipt.consts.enums.NuonuoInvoicesStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("诺诺发票反参")
public class InvoicesV {

    @ApiModelProperty("发票请求流水号")
    private String serialNo;

    @ApiModelProperty("企业税号")
    private String sellerTaxNo;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("发票状态： 2 :开票完成（ 最终状 态），其他状\n" +
            "态分别为: 20:开票中; 21:开票成功签章中;22:开\n" +
            "票失败;24: 开票成功签章失败;3:发票已作废 31:\n" +
            "发票作废中 备注：22、24状态时，无需再查询\n" +
            "，请确认开票失败原因以及签章失败原因；3、3\n" +
            "1只针对纸票 注：请以该状态码区分发票状态")
    private Integer status;

    @ApiModelProperty("发票状态描述")
    private String statusMsg;

    @ApiModelProperty("发票代码")
    private String invoiceCode;

    @ApiModelProperty("发票号码")
    private String invoiceNo;

    @ApiModelProperty("不含税金额")
    private String exTaxAmount;

    @ApiModelProperty("合计税额")
    private String taxAmount;

    @ApiModelProperty("购方名称")
    private String payerName;

    @ApiModelProperty("发票种类")
    private String invoiceKind;

    @ApiModelProperty("创建时间")
    private String addTime;

    @ApiModelProperty("开票时间")
    private String invoiceTime;

    @ApiModelProperty("校验码")
    private String checkCode;

    public String getStatusMsg() {
        NuonuoInvoicesStatusEnum invoicesStatusEnum = NuonuoInvoicesStatusEnum.getEnum(this.status);
        if (null != invoicesStatusEnum) {
            return invoicesStatusEnum.getDes();
        }
        return status.toString();
    }

}
