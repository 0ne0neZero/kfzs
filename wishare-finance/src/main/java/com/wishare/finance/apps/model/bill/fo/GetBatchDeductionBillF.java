package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("获取批量减免账单")
public class GetBatchDeductionBillF {

    @ApiModelProperty(value = "房产id")
    private List<Long> roomIds;

    @ApiModelProperty("费项id")
    private List<Long> chargeItemId;

    @ApiModelProperty("结算状态（0未结算，1部分结算，2已结算）,可多选")
    private List<Integer> settleState;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

}
