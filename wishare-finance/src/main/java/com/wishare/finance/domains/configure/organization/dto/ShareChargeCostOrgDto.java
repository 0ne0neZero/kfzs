package com.wishare.finance.domains.configure.organization.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author dongpeng
 * @date 2023/7/22
 * @Description:
 */
@Getter
@Setter
public class ShareChargeCostOrgDto {

    @ApiModelProperty("分成费项id")
    private Long shareChargeId;

    @ApiModelProperty("成本中心id")
    private Long costOrgId;

    @ApiModelProperty("分成费项名称")
    private String shareChargeName;

    @ApiModelProperty("分成比例")
    private BigDecimal shareProportion;
}
