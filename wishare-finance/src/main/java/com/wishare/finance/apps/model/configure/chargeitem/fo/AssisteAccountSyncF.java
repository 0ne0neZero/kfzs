package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2023/1/30
 * @Description:
 */
@Getter
@Setter
@ApiModel("同步")
public class AssisteAccountSyncF {

    @ApiModelProperty("系统来源：1 收费系统 2合同系统 22 用友ncc")
    private Integer sysSource;
}
