package com.wishare.contract.domains.vo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("开收据返回结果")
public class ContractReceiptResultV {

    @ApiModelProperty("收据明细id")
    private Long receiptDetailId;

    @ApiModelProperty("中台收据详情id")
    private Long financeReceiptDetailId;
}
