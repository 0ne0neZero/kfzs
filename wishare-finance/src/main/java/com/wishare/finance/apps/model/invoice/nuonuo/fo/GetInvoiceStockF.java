package com.wishare.finance.apps.model.invoice.nuonuo.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("企业发票余量查询入参")
public class GetInvoiceStockF {

    @ApiModelProperty(value = "企业税号",required = true)
    @NotBlank(message = "企业税号不能为空")
    private String taxnum;

    @ApiModelProperty(value = "租户id",required = true)
    @NotBlank(message = "租户id不能为空")
    private String tenantId;

    @ApiModelProperty("部门id（不传分机号、机器编号时使用）；部门和分机、机器编号等都不传时，返回税号下全部报税信息")
    private String departmentId;

    @ApiModelProperty("分机号列表（只传分机号时使用）")
    private List<String> extensionNums;

    @ApiModelProperty("机器编号单个查询（只传机器编号时使用）")
    private String machineCode;

    @ApiModelProperty("分机号+机器编号联合查询（只能传入一对分机号和机器编号），精确查询某设备时建议使用此种方式")
    private List<ExtMachineCodePairF> extMachineCodePairs;
}
