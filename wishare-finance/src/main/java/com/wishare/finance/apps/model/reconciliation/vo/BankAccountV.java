package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BankAccountV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("银行账户列表")
    private List<String> bankAccounts;

}
