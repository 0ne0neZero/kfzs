package com.wishare.finance.infrastructure.remote.vo.external.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description: 现金流量
 * @author: pgq
 * @since: 2022/12/6 19:20
 * @version: 1.0.0
 */
@ApiModel("现金流量")
@Getter
@Setter
public class CashFlowRv {

    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 父编码
     */
    @ApiModelProperty("编码")
    private String fcode;

    /**
     * 父名称
     */
    @ApiModelProperty("父名称")
    private String fname;

    /**
     * 现金流量项目类型
     * 1=现金流入;
     * 2=现金流出;
     */
    @ApiModelProperty("现金流量项目类型"
        + "1=现金流入;\n"
        + "2=现金流出;")
    private String itemtype;

}
