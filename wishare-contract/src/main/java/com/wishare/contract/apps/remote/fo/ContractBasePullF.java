package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 合同信息推送请求实接口
 *
 * @author long
 * @Date 2023年7月13日15:30:47
 */
@Data
@ApiModel(value = "合同信息推送请求")
public class ContractBasePullF {
    @ApiModelProperty(value = "jsonstr")
    private String requestBody;

    @ApiModelProperty(value = "0-新增 1-编辑")
    private int type;
}
