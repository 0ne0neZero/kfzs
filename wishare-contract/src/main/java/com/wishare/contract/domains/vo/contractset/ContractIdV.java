package com.wishare.contract.domains.vo.contractset;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/10/16 17:23
 */
@Data
public class ContractIdV {
    @NotBlank(message = "合同ID不可为空")
    private String contractId;

    private List<String> settlementIdList;
}
