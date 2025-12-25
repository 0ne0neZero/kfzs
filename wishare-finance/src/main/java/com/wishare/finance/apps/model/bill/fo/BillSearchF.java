package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author xujian
 * @date 2022/11/2
 * @Description:
 */

@ApiModel("账单id和类型查询")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BillSearchF {

    @ApiModelProperty("账单id")
    private Long billId;

    @ApiModelProperty("账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单")
    private Integer billType;

    @ApiModelProperty("项目ID")
    private String supCpUnitId;
}
