package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/11
 * @Description:
 */
@Getter
@Setter
@ApiModel("银行账户列表查询入参")
public class StatutoryBodyAccountListF {

    @ApiModelProperty("银行账户id")
    private Long id;

    @ApiModelProperty("银行账户id集合")
    private List<Long> ids;

    @ApiModelProperty("法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty("银行账户名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;
}
