package com.wishare.finance.apps.model.reconciliationFile.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReconcileAccountF {

    @ApiModelProperty(value = "下载账单路径，银联特有字段")
    private String download = "/download/";

    @ApiModelProperty(value = "支付渠道")
    private Integer channelCode;

    @ApiModelProperty(value = "拉取账单日期")
    private String recDate;
}
