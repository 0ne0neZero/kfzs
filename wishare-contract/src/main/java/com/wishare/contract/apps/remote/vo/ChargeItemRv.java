package com.wishare.contract.apps.remote.vo;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("费项返回信息")
public class ChargeItemRv extends Tree<ChargeItemRv, Long> {

    @ApiModelProperty(value = "费项id")
    private Long id;

    @ApiModelProperty(value = "父费项id")
    private Long parentId;

    @ApiModelProperty("费项编码")
    private String code;

    @ApiModelProperty("费项名称")
    private String name;

    @ApiModelProperty(value = "费项类型")
    private Integer type;

    @ApiModelProperty(value = "费项属性")
    private Integer attribute;

    @ApiModelProperty(value = "费项路径")
    private List<Long> chargePath;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("是否启用暂估收人:0未启用，1启用")
    private Integer estimated;

    @ApiModelProperty("是否显示:0隐藏 1显示")
    private Integer showed;

    @ApiModelProperty(value = "是否末级：0否,1是")
    private Integer lastLevel;

    @ApiModelProperty("创建人id")
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

    private String path;
}
