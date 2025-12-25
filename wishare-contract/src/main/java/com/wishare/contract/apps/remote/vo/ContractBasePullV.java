package com.wishare.contract.apps.remote.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/12/14/16:56
 */
@Data
@ApiModel(value = "合同入库返回实体类")
public class ContractBasePullV {
    @ApiModelProperty(value = "主数据合同编码")
    private String conmaincode;

    @ApiModelProperty(value = "主数据合同ID")
    private String fromid;

    @ApiModelProperty(value = "失败或者成功信息")
    private String message;

    @ApiModelProperty(value = "状态信息")
    private Integer status;

    @ApiModelProperty(value = "校验结果")
    private String isright;

    @ApiModelProperty(value = "校验结果")
    private String checkinfo;
}
