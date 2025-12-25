package com.wishare.finance.apps.model.bill.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Setter
@Getter
@ApiModel("批量交账表单")
public class GetPaySourceByBillNoF {

    /**
     * 账单编号集合
     */
    @ApiModelProperty(value = "账单编号集合", required = true)
    @NotEmpty
    private List<String> billNos;

    @ApiModelProperty("上级收费单元ID")
    @NotNull(message = "上级收费单元ID不能为空")
    private String supCpUnitId;

}
