package com.wishare.finance.apps.model.reconciliation.fo;

import com.wishare.finance.domains.reconciliation.enums.StaCostEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrgOidQueryF implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("组织类型")
    private StaCostEnum type;

    @ApiModelProperty("id列表")
    private List<Long> ids;

}
