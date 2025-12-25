package com.wishare.finance.infrastructure.remote.fo.zj;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("中交汇总单据信息-对下结算单【删除】")
public class OrderStatusDel {
    @ApiModelProperty("单据id")
    private Long id;
    @ApiModelProperty("业务系统单据编号")
    private String djbh;
}
