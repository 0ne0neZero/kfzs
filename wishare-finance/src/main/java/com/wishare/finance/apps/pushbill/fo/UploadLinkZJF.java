package com.wishare.finance.apps.pushbill.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadLinkZJF {

    @ApiModelProperty(value = "报账单编号")
    private String billNo;
    @ApiModelProperty(value = "附件上传链接")
    private String uploadLink;
    @ApiModelProperty(value = "文件名称")
    private String name;
    @ApiModelProperty(value = "附件上传ID")
    private String uploadLinkId;
    @ApiModelProperty(value = "影像id")
    private String imageIdZJ;
}
