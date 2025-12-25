package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hhb
 * @describe
 * @date 2025/10/17 10:55
 */
@Data
public class HyContractListV {

    @ApiModelProperty(value = "合同ID")
    private String id;
    @ApiModelProperty(value = "合同名称")
    private String name;
}
