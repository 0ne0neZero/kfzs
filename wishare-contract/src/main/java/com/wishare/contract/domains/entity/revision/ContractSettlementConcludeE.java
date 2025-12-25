package com.wishare.contract.domains.entity.revision;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 结算单表信息(831新增)
 *
 * @author 龙江锋
 * @date 2023/8/25 10:12
 */
@Data
@Accessors(chain = true)
public class ContractSettlementConcludeE {
    /**
     * 合同名称
     */
    private String contractName;

    /**
     * 附件(前端传数组)
     */
    private String attachments;
}
