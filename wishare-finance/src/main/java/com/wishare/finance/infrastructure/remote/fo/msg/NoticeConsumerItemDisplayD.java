package com.wishare.finance.infrastructure.remote.fo.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 为C端移动用户推送通知
 * </p>
 *
 * @author light
 * @since 2023/2/9
 */
@Data
public class NoticeConsumerItemDisplayD {

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
