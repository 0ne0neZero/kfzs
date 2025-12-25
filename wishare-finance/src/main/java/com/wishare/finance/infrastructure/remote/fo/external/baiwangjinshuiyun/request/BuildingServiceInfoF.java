package com.wishare.finance.infrastructure.remote.fo.external.baiwangjinshuiyun.request;

import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceZoningE;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author szh
 * @date 2024/5/9 11:34
 */

@Data
@ApiModel("建筑服务类信息相关信息")
public class BuildingServiceInfoF {
    @ApiModelProperty("建筑服务类信息")
    private InvoiceBuildingServiceInfoF serviceInfoF;
    // 地区选择树
    @ApiModelProperty("地区树形结构")
    private List<InvoiceZoningE> tree;
}
