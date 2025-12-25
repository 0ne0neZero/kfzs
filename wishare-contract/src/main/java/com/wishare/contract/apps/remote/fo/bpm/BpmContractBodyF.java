package com.wishare.contract.apps.remote.fo.bpm;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * 合同主体信息
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class BpmContractBodyF {
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("合同角色")
    private String role;
    @ApiModelProperty("主体类别")
    private String bodyType;
    @ApiModelProperty("主体名称")
    private String bodyName;
    @ApiModelProperty("开户银行")
    private String bank;
    @ApiModelProperty("账号")
    private String account;

}
