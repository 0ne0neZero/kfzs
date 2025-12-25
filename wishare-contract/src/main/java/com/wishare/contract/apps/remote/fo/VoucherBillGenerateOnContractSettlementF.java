package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author longhuadmin
 */
@Data
@ApiModel("新-报账单-实签-生成-请求信息")
public class VoucherBillGenerateOnContractSettlementF {

    @ApiModelProperty("所属项目ID")
    private String communityId;

    @ApiModelProperty("账单id")
    private List<String> billIdList;

    @ApiModelProperty(value = "触发事件类型：6:收入确认-实签")
    private Integer eventType;

    @ApiModelProperty(value = "结算单id-对应结算审批/确收审批")
    private String settlementId;
    @ApiModelProperty(value = "流程id")
    private String processId;
}
