package com.wishare.finance.apps.model.configure.chargeitem.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/12/2
 * @Description:
 */
@Getter
@Setter
@ApiModel("辅助核算列表查询入参")
public class AssisteAccountF {

    @ApiModelProperty("辅助核算编码")
    private String asAcCode;

    @ApiModelProperty("辅助核算项目")
    private String asAcItem;

    @ApiModelProperty("辅助核算对象")
    private String asAcTarget;

    @ApiModelProperty("参照名称")
    private String referenceName;
}
