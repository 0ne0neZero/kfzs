package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 付款凭证信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/2/21
 */
@Getter
@Setter
@ApiModel("付款凭证信息")
public class BillPayVoucherInfoV {

    @ApiModelProperty("报账凭证编号")
    private String voucherNo;

    @ApiModelProperty("凭证类别: ACCOUNT: 记账凭证 默认：ACCOUNT")
    private String voucherType;

    //@ApiModelProperty(value = "凭证分录详情列表")
    //private List<BillVoucherDetailV> voucherDetails;

}
