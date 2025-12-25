package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/6
 * @Description:
 */
@Getter
@Setter
@ApiModel("根据条件获取业务类型")
public class BusinessTypeListF {

    @ApiModelProperty("code")
    private String code;

    @ApiModelProperty("name")
    private String name;

    @ApiModelProperty("推凭状态：0已启用，1已禁用")
    private Integer state;

}
