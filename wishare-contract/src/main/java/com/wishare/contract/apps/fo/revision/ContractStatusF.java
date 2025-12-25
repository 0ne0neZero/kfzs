package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "合同状态同步枫行梦实体类")
public class ContractStatusF {
    /**
     * 合同 ID，必填
     */
    @ApiModelProperty(value = "合同 ID")
    private String agreementId;

    /**
     * 合同状态：0、草稿，1、审批通过，2、已终止，3、已ᨀ交，4、已撤回，5、已到期，6、已变更
     */
    @ApiModelProperty(value = "合同状态：0、草稿，1、审批通过，2、已终止，3、已ᨀ交，4、已撤回，5、已到期，6、已变更")
    private Integer status;
}
