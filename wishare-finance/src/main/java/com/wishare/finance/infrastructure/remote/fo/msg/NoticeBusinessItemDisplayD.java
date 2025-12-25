package com.wishare.finance.infrastructure.remote.fo.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author fxl
 * @describe
 * @date 2024/1/11
 */
@Data
public class NoticeBusinessItemDisplayD {
    /**
     * 头部内容
     */
    @ApiModelProperty(value = "头部内容", required = true)
    @NotBlank(message = "头部内容")
    private String head;

    /**
     * 具体内容
     */
    @ApiModelProperty(value = "信息内容", required = true)
    @NotBlank(message = "信息内容不可为空")
    private String content;
    @ApiModelProperty(value = "样式", required = true)
    private NoticeConsumerMobileCardItemDisplayTypeEnum noticeConsumerMobileCardItemDisplayType;
}
