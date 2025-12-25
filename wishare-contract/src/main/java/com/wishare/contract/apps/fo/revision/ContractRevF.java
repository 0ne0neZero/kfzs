package com.wishare.contract.apps.fo.revision;

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
 * @since： 2023/8/16  9:55
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "ContractRevF合同通用F", description = "ContractRevF合同通用F")
public class ContractRevF {


    /**
     * 合同名称
     */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;

}
