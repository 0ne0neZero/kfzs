package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 收款计划/付款计划分页查询参数
 * @author ljx
 */

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractCollectionPlanPageF {

    @ApiModelProperty("合同分类id")
    private Long category;

    @ApiModelProperty("合同名称")
    private String name;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同性质 1 收入 2 支出 3 其他")
    private Integer contractNature;
}
