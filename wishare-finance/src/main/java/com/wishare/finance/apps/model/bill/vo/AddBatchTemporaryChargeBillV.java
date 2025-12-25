package com.wishare.finance.apps.model.bill.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@ApiModel("批量新增临时收费账单详情")
public class AddBatchTemporaryChargeBillV {

    @ApiModelProperty(value = "成功条数", required = true)
    private long successCount;

    @ApiModelProperty(value = "失败条数", required = true)
    private long failCount;

    @ApiModelProperty("失败结果")
    private List<AddBatchBillV> failResult;

}
