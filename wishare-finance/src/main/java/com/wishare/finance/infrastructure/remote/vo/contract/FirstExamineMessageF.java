package com.wishare.finance.infrastructure.remote.vo.contract;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 初审
 * @author wyt
 */
@ApiModel
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class FirstExamineMessageF {
    @ApiModelProperty("接收者用户id")
    private List<String> userIds;
    @ApiModelProperty("原因")
    private String reason;
    @ApiModelProperty("项目Id")
    private String communityId;
}
