package com.wishare.finance.apps.model.configure.accountbook.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author dongpeng
 * @date 2023/9/5 15:16
 */
@Getter
@Setter
@ApiModel("法定单位")
public class StatutoryBody {


    @ApiModelProperty(value = "法定单位id")
    private Long statutoryBodyId;

    @ApiModelProperty(value = "法定单位名称")
    private String statutoryBodyName;
}
