package com.wishare.finance.apps.model.configure.accountbook.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Getter
@Setter
@ApiModel("统计条件入参")
public class SatisticsF {

    @ApiModelProperty("账簿编码")
    private String code;
}
