package com.wishare.finance.apps.model.configure.organization.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author dongpeng
 * @version 1.0
 * @since 2023/7/25
 */
@Getter
@Setter
@ApiModel("编辑成本中心关联分成费项")
public class ShareChargeCostOrgF {

    @ApiModelProperty(value = "成本中心Id", required = true)
    @NotNull(message = "成本中心Id不能为空")
    private Long costOrgId;

    @ApiModelProperty(value = "新增分成费项Id")
    private List<Long> addShareChargeList;

    @ApiModelProperty(value = "删除分成费项Id")
    private List<Long> deleteShareChargeList;

    @ApiModelProperty(value = "分成比例")
    private BigDecimal shareProportion;


}
