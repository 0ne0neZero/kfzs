package com.wishare.contract.apps.remote.fo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 初审
 * @author wyt
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class FirstExamineMessageF {
    @ApiModelProperty("接收者用户id")
    private String userId;
    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("项目Id")
    private String communityId;
}
