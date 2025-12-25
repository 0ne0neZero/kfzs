package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ReconcileMerchantClearingF extends CommunityList {


    @ApiModelProperty("对账开始结束时间")
    private List<String> reconciliationDate;
}
