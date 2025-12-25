package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Setter
@ApiModel("批量修改应收账单与预收账单")
public class UpdateBillApprovedStateF {

    @ApiModelProperty("账单减免信息")
    @Size(min = 1,  message = "账单减免信息不能为空")
    private List<BatchDeductionF> billDetailVs;

    @ApiModelProperty("调整信息")
    @Size(min = 1,  message = "调整信息不能为空")
    private List<BillAdjustDetailF> adjustBillFList;

    @ApiModelProperty(value = "上级收费单元id")
    @NotBlank(message = "上级收费单元id不能为空")
    private String supCpUnitId;

}
