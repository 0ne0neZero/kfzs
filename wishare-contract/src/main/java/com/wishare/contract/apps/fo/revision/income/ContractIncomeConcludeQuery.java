package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hhb
 * @describe
 * @date 2025/11/1 14:48
 */
@Data
public class ContractIncomeConcludeQuery extends ContractIncomeConcludeE {
    @ApiModelProperty("合同业务线搜索条件")
    private Integer contractBusinessLineSelect;
}
