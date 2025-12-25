package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yyx
 * @date 2023/5/10 14:08
 */
@Getter
@Setter
@ApiModel("编辑应收账单")
public class BatchEditBillF {

    @ApiModelProperty(value = "应收账单id",required = true)
    @NotNull(message = "账单id不能为空")
    private List<Long> billIds;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty("账单状态（0正常，1作废，2冻结，3挂账）")
    private Integer state;


}
