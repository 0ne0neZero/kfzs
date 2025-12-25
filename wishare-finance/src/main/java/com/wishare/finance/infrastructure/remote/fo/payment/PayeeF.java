package com.wishare.finance.infrastructure.remote.fo.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * 收款人信息
 *
 * @Author dxclay
 * @Date 2022/12/1
 * @Version 1.0
 */
@Getter
@Setter
@ApiModel("收款人信息")
public class PayeeF {

    @ApiModelProperty(value = "收款人id")
    @Length(max = 64, message = "收款人id格式不正确")
    private String payeeId;

    @ApiModelProperty(value = "渠道收款人id")
    @Length(max = 64, message = "渠道收款人id格式不正确")
    private String channelpayeeId;

    @ApiModelProperty(value = "收款人名称")
    @Length(max = 100, message = "收款人名称格式不正确")
    private String payeeName;

    @ApiModelProperty(value = "收款人手机号")
    @Length(max = 32, message = "收款人手机号格式不正确")
    private String phone;

    @ApiModelProperty(value = "银行类型, 银企直连和银行转账必填,银行联行号填了可不填")
    private String backType;

    @ApiModelProperty(value = "银行联行号, 银企直连和银行转账必填")
    private String bankNo;

    @ApiModelProperty(value = "收款人账号, 银企直连和银行转账必填")
    private String payeeAccount;

    @ApiModelProperty(value = "收款人开户行, 银企直连和银行转账必填")
    private String payeeBank;

}
