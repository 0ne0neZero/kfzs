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
@ApiModel("根据条件查询账簿列表入参")
public class AccountBookListF {

    @ApiModelProperty("账簿编码")
    private String code;
}
