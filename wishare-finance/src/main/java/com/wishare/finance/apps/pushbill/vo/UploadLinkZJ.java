package com.wishare.finance.apps.pushbill.vo;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadLinkZJ {

    @ApiModelProperty(value = "附件上传链接")
    private String uploadLink;
    @ApiModelProperty(value = "附件上传ID")
    private String uploadLinkId;
    @ApiModelProperty(value = "影像id")
    private String imageIdZJ;
    @ApiModelProperty(value = "文件名称")
    private String name;
}
