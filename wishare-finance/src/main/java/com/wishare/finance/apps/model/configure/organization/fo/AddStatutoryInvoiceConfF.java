package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Getter
@Setter
@ApiModel("新增电子开票设置")
public class AddStatutoryInvoiceConfF {

    @ApiModelProperty(value = "法定单位Id", required = true)
    @NotNull(message = "法定单位Id不能为空")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称", required = true)
    @NotNull(message = "法定单位名称不能为空")
    private String statutoryBodyName;

    @ApiModelProperty("机器编号（12位盘号）")
    @Size(max = 12,message = "机器编号小于等于12位")
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
