package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/6  11:25
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同信息表PayPageF", description = "收入合同信息表PayPageF")
public class ContractIncomePageF {

    /**
     * name
     */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;
    /**
     * chooseType
     */
    @ApiModelProperty("选择操作业务类型")
    private Integer chooseType;


}
