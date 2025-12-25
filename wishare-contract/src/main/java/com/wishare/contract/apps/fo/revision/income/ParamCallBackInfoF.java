package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/10/18/13:59
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "获取市拓合同回调请求实体")
public class ParamCallBackInfoF {

    @ApiModelProperty(value = "参数id")
    private String id;

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "审批结果 0待提交 1审批中 2已通过 3已拒绝 4已驳回 9草稿")
    private Integer reviewStatus;

    @ApiModelProperty(value = "实际总合同额")
    private BigDecimal actualTotalContractAmount;
}
