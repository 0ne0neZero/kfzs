package com.wishare.contract.domains.vo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同编号返回信息")
public class ContractNoInfoV {

    @ApiModelProperty(value = "合同编号")
    private String contractNo;

    @ApiModelProperty(value = "是否成功")
    private Integer code;

    @ApiModelProperty(value = "失败原因")
    private String failReason;

    /**
     * 内部使用 - 合同序号的redisKey
     **/
    private String redisKey;

    /**
     * 内部使用 - 合同序号[补]的redisKey
     **/
    private String supplyRedisKey;
}
