package com.wishare.finance.apps.model.invoice.invoice.vo;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("收款单发票刷新信息")
public class GatherInvoiceRefreshV {

    private String result;

    List<RefreshStateV> refreshStates;
}
