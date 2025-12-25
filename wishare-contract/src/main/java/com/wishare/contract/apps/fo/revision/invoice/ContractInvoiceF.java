package com.wishare.contract.apps.fo.revision.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/9/11/16:45
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "票据查询参数", description = "票据查询参数")
public class ContractInvoiceF {

    @ApiModelProperty("合同供应商客商id")
    private String oppositeOneId;

    @ApiModelProperty("业务主键ID")
    private String id;
}
