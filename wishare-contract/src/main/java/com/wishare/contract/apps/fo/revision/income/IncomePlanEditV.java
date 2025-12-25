package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.domains.vo.revision.income.IncomeConcludePlanV2;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
@ApiModel(value = "收款计划V2-新增或编辑的请求信息")
public class IncomePlanEditV {


    @ApiModelProperty("合同id")
    @NotBlank(message = "合同ID不能为空")
    private String contractId;


    @ApiModelProperty("收款计划")
    @NotNull(message = "收款计划不能为空")
    private List<List<IncomeConcludePlanV2>> planV2List;

}
