package com.wishare.finance.apps.model.bill.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author 强尼
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel("账单成本中心调整(项目层)")
public class BillingCostCenterAdjustCommunityF {

    @NotBlank
    @ApiModelProperty("项目id")
    String communityId;

    @NotNull
    @ApiModelProperty("账单类型（1:应收账单，2:预收账单，3:临时收费账单）")
    Integer billType;

}

