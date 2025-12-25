package com.wishare.finance.infrastructure.remote.fo.external.baiwang.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: Linitly
 * @date: 2023/6/20 10:24
 * @descrption: 业务接口调用成功后返回的具体响应信息，不同的接口返回的model不同
 */
@Data
@ApiModel(value = "发票开具响应model")
public class InvoiceResModelV {

    @ApiModelProperty(value = "拆分开具失败返回拆分的发票金额明细信息")
    private List<InvoiceFailResV> fail;

    @ApiModelProperty(value = "开具成功返回数据")
    private List<InvoiceSuccessResV> success;

}
