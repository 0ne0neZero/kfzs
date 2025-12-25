package com.wishare.contract.apps.fo.revision.pay;

import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hhb
 * @describe
 * @date 2025/11/3 11:25
 */
@Data
public class ContractPayConcludeQuery extends ContractPayConcludeE {
    @ApiModelProperty("合同业务线搜索条件")
    private Integer contractBusinessLineSelect;
}
