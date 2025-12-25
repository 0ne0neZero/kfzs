package com.wishare.contract.apps.fo.revision.pay;

import com.wishare.contract.apps.fo.revision.ContractPlanAddF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

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
@ApiModel(value = "支出合同付款计划信息表新增参数AddF", description = "支出合同付款计划信息表新增参数AddF")
public class ContractPayPlanAddF extends ContractPlanAddF {


    @ApiModelProperty("计划id")
    private String id;

    @ApiModelProperty("供应商")
    @NotBlank(message = "供应商不能为空")
    private String merchant;

    @ApiModelProperty("供应商")
    @NotBlank(message = "供应商不能为空")
    private String merchantName;

    @ApiModelProperty("所属部门")
    private String departName;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "结算计划分组")
    private String settlePlanGroup;

    @ApiModelProperty(value = "成本预估编码")
    private String costEstimationCode;

    @ApiModelProperty("合同清单id")
    private String contractPayFundId;


}
