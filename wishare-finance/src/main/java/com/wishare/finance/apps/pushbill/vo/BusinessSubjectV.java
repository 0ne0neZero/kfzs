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
@ApiModel(value="支付申请单-业务科目")
public class BusinessSubjectV implements Serializable {

    @ApiModelProperty(value = "ID")
    private String ID;
    @ApiModelProperty(value = "业务科目名称")
    private String name;
    @ApiModelProperty(value = "业务科目全称")
    private String fullName;
    @ApiModelProperty(value = "余额方向 1:应付 0:应收")
    private Integer BALANCEDIR;

}
