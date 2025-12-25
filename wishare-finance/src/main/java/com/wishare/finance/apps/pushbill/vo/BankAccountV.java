package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 资金计划编号
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@ApiModel(value="支付申请单-往来单位银行账号信息")
public class BankAccountV implements Serializable {

    @ApiModelProperty(value = "往来单位编号")
    private String wldwbh;
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "人行联行名称")
    private String BankNameOfChina;
    @ApiModelProperty(value = "账号")
    private String ACCOUNTNO;
    @ApiModelProperty(value = "中文名称")
    private String ACCOUNTNAME_CHS;
    @ApiModelProperty(value = "账户状态 1,未启用;2,启用;3,停用;4,已销户;5,冻结")
    private String AccountStatus;


}
