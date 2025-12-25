package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/8/22 15:16
 * @descrption:
 */
@Data
@ApiModel(value = "版式文件生成请求体")
public class FormatCreateReqF {

    // 必填，长度20
    @ApiModelProperty(value = "销方机构税号")
    private String taxNo;

    // 请求体，必填
    private FormatCreateDataReqF data;
}
