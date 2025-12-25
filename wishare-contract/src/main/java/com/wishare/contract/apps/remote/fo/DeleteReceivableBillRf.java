package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangrui
 */
@Getter
@Setter
@ApiModel("删除应收账单请求信息")
public class DeleteReceivableBillRf {

    @ApiModelProperty("账单ids")
    private List<Long> billIds;

    @ApiModelProperty("supCpUnitId")
    private String supCpUnitId;
}
