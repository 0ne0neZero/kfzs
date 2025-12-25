package com.wishare.finance.infrastructure.remote.fo.external.baiwang.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: Linitly
 * @date: 2023/6/25 13:47
 * @descrption:
 */
@Data
@ApiModel(value = "发票查询请求体")
public class InvoiceSearchReqF {

    @ApiModelProperty(value = "销方机构税号")
    private String taxNo;

    @ApiModelProperty(value = "组织机构编码，可以查询对应税号下该组织机构编码的发票数据")
    private String orgCode;

    @ApiModelProperty(value = "集合")
    private InvoiceSearchDataReqF data;
}
