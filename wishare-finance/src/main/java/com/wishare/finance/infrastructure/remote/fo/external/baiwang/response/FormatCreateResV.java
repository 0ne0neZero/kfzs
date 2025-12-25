package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/22 15:22
 * @descrption:
 */
@Data
@ApiModel(value = "版式文件生成响应体")
public class FormatCreateResV {

    @ApiModelProperty(value = "版式下载地址")
    private String eInvoiceUrl;

    @ApiModelProperty(value = "下载地址")
    private String queryData;

    @ApiModelProperty(value = "版式类型")
    private String fileType;
}
