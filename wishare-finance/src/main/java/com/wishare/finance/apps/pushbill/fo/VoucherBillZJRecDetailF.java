package com.wishare.finance.apps.pushbill.fo;

import com.wishare.finance.apps.pushbill.vo.VoucherBillZJRecDetailV2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "资金收款单下-应收款明细 提交 参数")
@SuppressWarnings("all")
public class VoucherBillZJRecDetailF {

    @ApiModelProperty(value = "报账单编号")
    private String voucherBillNo;

    @ApiModelProperty(value = "应收款明细-V2")
    private List<VoucherBillZJRecDetailV2> recDetailList;

}
