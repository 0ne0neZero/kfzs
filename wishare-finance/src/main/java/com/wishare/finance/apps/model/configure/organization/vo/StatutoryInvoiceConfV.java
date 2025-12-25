package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("电子开票设置")
public class StatutoryInvoiceConfV {

    @ApiModelProperty("电子开票设置id")
    private Long id;

    @ApiModelProperty(value = "法定单位Id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("机器编号（12位盘号）")
    private String machineCode;

    @ApiModelProperty("分机号")
    private Long extensionNumber;

    @ApiModelProperty("终端号")
    private Long terminalNumber;

    @ApiModelProperty("开票人")
    private String clerk;

    @ApiModelProperty("终端代码")
    private String terminalCode;

    @ApiModelProperty("用户代码")
    private String userCode;
    /**
     * 诺诺token
     */
    @ApiModelProperty("诺诺token")
    private String token;
}
