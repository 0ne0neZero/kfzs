package com.wishare.finance.apps.pushbill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@ApiModel("资金收款单下的影像资料")
public class VoucherBillZJFileSV {

    private String fileKey;

    @ApiModelProperty("附件名称")
    private String fileName;
    @ApiModelProperty("附件名称")
    private String name;

    @ApiModelProperty("附件类型")
    private String fileType;

    @ApiModelProperty("附件格式")
    private String fileFormat;

    @ApiModelProperty("附件大小")
    private String fileSize;

    @ApiModelProperty("上传人")
    private String creatName;

    @ApiModelProperty("上传时间")
    private LocalDateTime creatTime;

    private String id;

    @ApiModelProperty("附件在影像系统唯一ID")
    private String fileYxxxID;
}

