package com.wishare.finance.apps.model.configure.accountbook.dto;

import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import com.wishare.finance.apps.model.configure.accountbook.fo.StatutoryBody;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author xujian
 * @date 2022/10/14
 * @Description:
 */
@Getter
@Setter
@ApiModel("账簿组合")
public class AccountBookGroupDto {

    @Valid
    @ApiModelProperty(value = "成本中心")
    private List<CostCenter> costCenterList;

    @ApiModelProperty(value = "成本中心id路径")
    private List<List<Long>> costCenterIdPath;

    @Valid
    @ApiModelProperty(value = "费项")
    private List<ChargeItem> chargeItemList;

    @ApiModelProperty(value = "费项id路径")
    private List<List<Long>> chargeItemIdPath;

    @Valid
    @ApiModelProperty(value = "法定单位")
    private List<StatutoryBody> statutoryBodyList;

    @ApiModelProperty(value = "法定单位id路径")
    private List<List<Long>> statutoryBodyIdPath;
}
