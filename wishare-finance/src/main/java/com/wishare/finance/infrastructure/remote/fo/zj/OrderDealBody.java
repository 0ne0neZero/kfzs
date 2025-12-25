package com.wishare.finance.infrastructure.remote.fo.zj;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("财务单据数据推送结果查询入参")
public class OrderDealBody {
    @ApiModelProperty("业务系统单据内码")
    private String djnm;
    @ApiModelProperty("业务系统单据编号")
    private String djbh;
    @ApiModelProperty("来源系统")
    private String lyxt;
    @ApiModelProperty("单据类型")
    private String businessCode;
}
