package com.wishare.finance.apps.model.configure.arrearsCategory.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("欠费类别返回信息")
public class ArrearsCategoryLastLevelV extends TreeE {

    @ApiModelProperty("欠费类别名称")
    private String arrearsCategoryName;

    @ApiModelProperty("上级名称")
    private String parentName;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer status;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人名称")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新人id")
    private String operator;

    @ApiModelProperty("更新人名称")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

//    @ApiModelProperty("下级欠费类别")
//    private List<ArrearsCategoryLastLevelV> arrearsCategoryLastLevelVs;

}
