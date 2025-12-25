package com.wishare.finance.apps.model.bill.fo;

import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ApiModel("账单成本中心调整")
public class BillingCostCenterAdjustF {

    @NotBlank
    @ApiModelProperty("项目id")
    String communityId;


    @NotNull
    @ApiModelProperty("期区ids")
    List<String> blockIds;

    @NotNull
    @ApiModelProperty("k期区idv期区名称")
    Map<String,String> blockIdsName;

    @NotNull
    @ApiModelProperty("账单类型（1:应收账单，2:预收账单，3:临时收费账单）")
    Integer billType;

}

