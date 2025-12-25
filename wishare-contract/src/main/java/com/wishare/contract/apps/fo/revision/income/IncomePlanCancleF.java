package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 支出合同付款计划信息表新增参数
 *
 * @author zhangfuyu
 * @mender 龙江锋
 * @Date 2023/7/6/11:22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class IncomePlanCancleF {


    @ApiModelProperty("合同id")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;


}
