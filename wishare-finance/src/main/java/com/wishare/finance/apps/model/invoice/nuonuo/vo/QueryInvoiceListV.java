package com.wishare.finance.apps.model.invoice.nuonuo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/9
 * @Description:
 */
@Getter
@Setter
@ApiModel("开票列表查询接口反参")
public class QueryInvoiceListV {

    @ApiModelProperty("发票数")
    private Long totalCount;

    @ApiModelProperty("诺诺开票列表")
    private List<InvoicesV> invoices;
}
