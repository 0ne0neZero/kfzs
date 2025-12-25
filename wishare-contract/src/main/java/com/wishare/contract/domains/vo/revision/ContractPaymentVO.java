package com.wishare.contract.domains.vo.revision;

import lombok.Getter;
import lombok.Setter;

/**
 * @author hhb
 * @describe
 * @date 2025/7/18 14:44
 */
@Getter
@Setter
public class ContractPaymentVO {

    //合同ID
    private String contractId;
    //收款人ID
    private String payeeId;
    //收款人名称
    private String payeeName;
    //收入方ID
    private String incomeId;
    //收入方名称
    private String incomeName;
    //合同类型（1.收入；2.支出）
    private Integer contractType;
}
