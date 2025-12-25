package com.wishare.finance.apps.model.reconciliation.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BankAccountQueryF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("成本中心id列表")
    private List<Long> costIds;

}
