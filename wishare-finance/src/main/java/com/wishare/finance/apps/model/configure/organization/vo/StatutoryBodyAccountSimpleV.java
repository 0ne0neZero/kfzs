package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 成本中心查询银行账户返回参数
 *
 * @author luzhonghe
 * @version 1.0
 * @since 2023/3/28
 */
@Getter
@Setter
@ApiModel("银行账户返回参数")
public class StatutoryBodyAccountSimpleV {

    @ApiModelProperty("银行账户id")
    private Long id;

    @ApiModelProperty("开户行")
    private String bankName;

    @ApiModelProperty("账户名称")
    private String name;

    @ApiModelProperty("法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("银行卡号")
    private String bankAccount;

}
