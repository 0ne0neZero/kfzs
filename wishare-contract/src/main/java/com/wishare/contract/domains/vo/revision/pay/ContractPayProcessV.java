package com.wishare.contract.domains.vo.revision.pay;

import lombok.Data;

/**
 * @author hhb
 * @describe
 * @date 2025/10/20 18:31
 */
@Data
public class ContractPayProcessV {

    //原接口返回参数
    private String oldString;
    //NK结算单发起流程自动同步YJ结算单对应结算单ID，若有数据，则跳转YJ结算审批对应结算单详情，若无则不跳转
    private String settlementId;
    //合同名称
    private String curContractName;
    //合同ID
    private String contractId;
    //错误编码。500
    private String code = "200";
    //错误信息
    private String message;
    private Integer type;
}
