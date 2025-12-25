package com.wishare.finance.apps.model.configure.subject.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 科目-现金流量
 *
 * @author dxclay
 * @since 2023-03-11
 */
@Getter
@Setter
@ApiModel("科目现金流量信息")
public class SubjectCashFlowF {

    @ApiModelProperty(value = "主表 id")
    private Long masterId;

    @ApiModelProperty(value = "主表编码")
    private String masterCode;

    @ApiModelProperty(value = "主表名称")
    private String masterName;

    @ApiModelProperty(value = "附表 id")
    private Long attachedId;

    @ApiModelProperty(value = "附表编码")
    private String attachedCode;

    @ApiModelProperty(value = "附表名称")
    private String attachedName;

    @ApiModelProperty(value = "流入流出方向 0 流入  1 流出")
    private Integer inOut;

}
