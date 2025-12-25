package com.wishare.finance.apps.model.configure.cashflow.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 现金流量信息
 * @author dxclay
 * @since  2023/3/14
 * @version 1.0
 */
@Getter
@Setter
@ApiModel("现金流量信息")
public class CreateCashFlowF {

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "父编码")
    private String fCode;

    @ApiModelProperty(value = "父名称")
    private String fName;

    @ApiModelProperty(value = "全路径名称")
    private String allName;

    @ApiModelProperty(value = "现金流量项目类型 0流入流出， 1现金流入，2现金流出")
    private String itemType;

    /**
     * 数据来源id
     */
    private String idExt;
}
