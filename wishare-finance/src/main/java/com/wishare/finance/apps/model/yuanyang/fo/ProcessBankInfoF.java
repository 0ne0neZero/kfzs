package com.wishare.finance.apps.model.yuanyang.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

/**
 *
 * @author luzhonghe
 * @version 1.0
 * @since 2023/8/24
 */
@Getter
@Setter
@ApiModel("银企直连信息")
public class ProcessBankInfoF {

    @Valid
    @ApiModelProperty(value = "支付方银行信息")
    private List<ProcessBankPayInfoF> payBankInfos;

    @Valid
    @ApiModelProperty(value = "对公收款方信息")
    private List<ProcessBankPublicF> publicPayees;

    @Valid
    @ApiModelProperty(value = "对私收款方信息")
    private List<ProcessBankPrivateF> privatePayees;
}
