package com.wishare.contract.domains.vo.contractset;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author hhb
 * @describe
 * @date 2025/9/4 14:17
 */
@Data
public class ContractDetailV {
    @NotBlank(message = "合同ID不可为空")
    private String id;

    private Boolean isBc = Boolean.FALSE;
}
