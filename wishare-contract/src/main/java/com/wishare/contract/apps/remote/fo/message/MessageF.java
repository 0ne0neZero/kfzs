package com.wishare.contract.apps.remote.fo.message;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 用于创建消息
 * @author suyangyang
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
public class MessageF {
    @ApiModelProperty("接收者用户id")
    private String userIds;
    @ApiModelProperty("接收者租户id")
    private String tenantId;
    @ApiModelProperty("接收者组织id")
    private String orgId;
    @ApiModelProperty("接收者角色id")
    private String roleId;
    @ApiModelProperty("消息标题")
    @NotBlank(message = "消息标题不能为空")
    private String title;
    @ApiModelProperty("副标题")
    private String subTitle;
    @ApiModelProperty(value = "模板ID", required = true)
    @NotBlank(message = "模板ID不能为空")
    private Long templateId;
    @ApiModelProperty(value = "模板变量。格式：[\"param1\",\"param2\", ...]")
    private String[] params;
    @ApiModelProperty("消息分类ID")
    @NotBlank(message = "消息类型不能为空")
    private String typeId;
    @ApiModelProperty("消息状态")
    private String state;
}
