package com.wishare.finance.apps.model.reconciliation.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrgOidV implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("4A组织id")
    private List<String> oids;

}
