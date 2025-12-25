package com.wishare.finance.apps.mq.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketContent{

    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("内容")
    private String bodyMsg;
    @ApiModelProperty("是否存在超链接 true:存在 false:不存在")
    private boolean urlFlag;
    @ApiModelProperty("超链接地址")
    private String url;
    @ApiModelProperty("超链接名称")
    private String urlName;
    @ApiModelProperty("项目ID")
    private String communityId;

}