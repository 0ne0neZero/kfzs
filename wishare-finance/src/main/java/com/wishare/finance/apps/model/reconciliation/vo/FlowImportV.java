package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 流水导入返回数据
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("流水导入返回数据")
@AllArgsConstructor
public class FlowImportV {

    @ApiModelProperty("成功条数")
    private Integer successTotal;

    @ApiModelProperty("失败条数")
    private Integer failTotal;

    @ApiModelProperty("失败数据文件地址")
    private String excelLinkUrl;
}
