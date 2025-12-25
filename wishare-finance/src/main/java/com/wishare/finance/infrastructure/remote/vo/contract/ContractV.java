package com.wishare.finance.infrastructure.remote.vo.contract;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("合同信息")
public class ContractV {

    private String id;
    /**
     * 中交合同id
     */
    private String fromid;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同编号
     */
    private String contractNo;


}
