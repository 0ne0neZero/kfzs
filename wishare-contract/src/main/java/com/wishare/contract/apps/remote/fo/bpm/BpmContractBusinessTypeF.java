package com.wishare.contract.apps.remote.fo.bpm;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@ApiModel("bpm总部支出类申请参数")
@AllArgsConstructor
@NoArgsConstructor
public class BpmContractBusinessTypeF {
    @ApiModelProperty(value = "合同类型一级")
    private String levelOne;

    @ApiModelProperty(value = "合同类型二级")
    private String levelTwo;

    @ApiModelProperty(value = "合同类型三级")
    private String levelThree;

    @ApiModelProperty(value = "合同类型四级")
    private String levelFour;

    @ApiModelProperty(value = "合同类型一级名称")
    private String levelOneName;

    @ApiModelProperty(value = "合同类型二级名称")
    private String levelTwoName;

    @ApiModelProperty(value = "合同类型三级名称")
    private String levelThreeName;

    @ApiModelProperty(value = "合同类型四级名称")
    private String levelFourName;


}
