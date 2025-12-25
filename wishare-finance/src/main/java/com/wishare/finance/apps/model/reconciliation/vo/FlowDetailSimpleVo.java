package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "流水认领简略信息")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlowDetailSimpleVo {

    @ApiModelProperty(value = "流水认领id")
    private Long flowDetailId;

    @ApiModelProperty(value = "流水号")
    private String serialNumber;

}
