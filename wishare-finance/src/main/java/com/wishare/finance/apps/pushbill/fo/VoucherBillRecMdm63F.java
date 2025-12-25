package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "资金收款单-应收款明细-MDM63筛选请求新题")
public class VoucherBillRecMdm63F {

    @ApiModelProperty(value = "项目id")
    @NotBlank(message = "项目id不允许为空")
    private String communityId;

    @ApiModelProperty(value = "合同付款人-主数据编码 BP码")
    private String contractPayer;

    @ApiModelProperty(value = "合同付款人-名称 后端使用")
    private String contractPayerName;

    @ApiModelProperty(value = "应收应付编号-模糊搜索使用")
    private String billNum;

    @ApiModelProperty(value = "业务日期")
    private Date bizDate;

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "科目id")
    private String paymentId;

    @ApiModelProperty(value = "收入确认单编号")
    private String queryVoucherBillNo;
}
