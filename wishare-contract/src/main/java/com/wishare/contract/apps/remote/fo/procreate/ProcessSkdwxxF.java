package com.wishare.contract.apps.remote.fo.procreate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/6/12/11:12
 */
@Data
@ApiModel(value = "业务数据实体类")
public class ProcessSkdwxxF {


    @ApiModelProperty(value = "收入方名称")
    private String PAYEE;

    @ApiModelProperty(value = "是否适用账户支付")
    private String sfsy;

    @ApiModelProperty(value = "收入方账户类型")
    private String PAYEEACCOUNTYPE;

    @ApiModelProperty(value = "收入方开户行国别")
    private String BANKCOUNTRY;

    @ApiModelProperty(value = "收入方账户-开户行")
    private String PAYEEACCOUNBANK;

    @ApiModelProperty(value = "收入方账户-账号")
    private String PAYEEACCOUNNUMBER;

    @ApiModelProperty(value = "收入方 swift code")
    private String SWIFTCODE;

    @ApiModelProperty(value = "实际收款人名称")
    private String TRUEPAYEE;

    @ApiModelProperty(value = "实际收款人账户类型")
    private String TRUEPAYEEACCOUNTYPE;

    @ApiModelProperty(value = "实际收款人开户行国别")
    private String TRUEBANKCOUNTRY;

    @ApiModelProperty(value = "实际收款人账户-开户行")
    private String TRUEPAYEEACCOUNBANK;

    @ApiModelProperty(value = "实际收款人账户-账户名称")
    private String TRUEPAYEEACCOUNNAME;

    @ApiModelProperty(value = "实际收款人账户-账号")
    private String TRUEPAYEEACCOUNNUMBER;

    @ApiModelProperty(value = "实际收款人swift code")
    private String TRUESWIFTCODE;

    @ApiModelProperty(value = "应收合同金额")
    private String PAYEEAMT;

}
