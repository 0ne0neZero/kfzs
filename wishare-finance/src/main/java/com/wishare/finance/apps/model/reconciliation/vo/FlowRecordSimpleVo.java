package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "流水认领批次简略信息")
public class FlowRecordSimpleVo {

    @ApiModelProperty(value = "认领批次id")
    private Long flowRecordId;

    @ApiModelProperty(value = "认领批次流水号")
    private String serialNumber;

}
