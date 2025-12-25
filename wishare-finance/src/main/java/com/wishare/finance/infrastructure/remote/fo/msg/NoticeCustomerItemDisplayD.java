package com.wishare.finance.infrastructure.remote.fo.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NoticeCustomerItemDisplayD {

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
    @ApiModelProperty(value = "样式，1 换行 2 对称显示", required = true)
    private int noticeConsumerMobileCardItemDisplayType;

    public NoticeCustomerItemDisplayD() {
    }

    public NoticeCustomerItemDisplayD(String head, String content, int noticeConsumerMobileCardItemDisplayType) {
        this.head = head;
        this.content = content;
        this.noticeConsumerMobileCardItemDisplayType = noticeConsumerMobileCardItemDisplayType;
    }

}
