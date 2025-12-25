package com.wishare.finance.apps.model.voucher.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 基础凭证业务单据信息
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/15
 */
@Getter
@Setter
@ApiModel("基础凭证业务单据信息")
public class SimpleVoucherBusinessV {

    @ApiModelProperty(value = "业务单据id")
    private Long businessBillId;

    @ApiModelProperty(value = "业务单据编码")
    private String businessBillNo;

    @ApiModelProperty(value = "业务单据类型: 1应收单, 2预收单, 3临时单, 4应付单, 5退款单, 6付款单, 7收款单, 8发票, 9收据, 10银行流水, 11清分流水")
    private Integer businessBillType;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

}
