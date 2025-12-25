package com.wishare.contract.apps.fo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-21
 */
@Data
public class SettlementParamQuery {

    @ApiModelProperty("应结算开始日期")
    private String plannedCollectionStartTime;

    @ApiModelProperty("应结算结束日期")
    private String plannedCollectionEndTime;

    private String tenantId;

    @ApiModelProperty("项目id集合")
    private List<String> communityIdList;

    @ApiModelProperty("履约状态")
    private List<String> statusList;

    private Integer isNK = 0;
}
