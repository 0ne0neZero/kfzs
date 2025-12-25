package com.wishare.finance.apps.model.reconciliation.fo;

import com.wishare.finance.domains.bill.dto.FlowBillPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Setter
@Getter
@ApiModel("新增暂存数据入参")
public class AddFlowTempF {

    @ApiModelProperty("流水集合")
    private List<Long> flowIds;

    @ApiModelProperty("收款单集合")
    @NotEmpty(message = "收款单集合不能为空")
    private List<FlowBillPageDto> gatherBillList;


    @ApiModelProperty("暂存名称")
    @NotEmpty(message = "暂存名称不能为空")
    private String name;


    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("项目Id")
    private String supCpUnitId;

}
