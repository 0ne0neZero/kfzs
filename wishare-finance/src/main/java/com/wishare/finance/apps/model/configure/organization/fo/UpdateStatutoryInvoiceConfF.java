package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("修改电子开票设置")
public class UpdateStatutoryInvoiceConfF {

    @ApiModelProperty(value = "电子开票设置id")
    private Long id;

    @ApiModelProperty(value = "法定单位Id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;

    @ApiModelProperty("机器编号（12位盘号）")
    @Length(max = 13,message = "机器编号小于等于12位")
    private String machineCode;

    @ApiModelProperty("分机号")
    @Max(value = 99999,message = "分机号最大为5位")//校检的是值
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
    private String token;
}
