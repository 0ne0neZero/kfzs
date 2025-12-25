package com.wishare.contract.apps.fo.revision.income;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @Author dengjie03
 * @Description
 * @Date 2024-11-20
 */
@Data
public class IncomePlanListQuery {

    @ApiModelProperty("项目id 来源 成本中心")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("应结算开始日期")
    private String plannedCollectionStartTime;


    @ApiModelProperty("应结算结束日期")
    private String plannedCollectionEndTime;

    @ApiModelProperty("合同名称")
    private LocalDate name;

    @ApiModelProperty("合同-合同履约状态")
    private Integer status;

    @ApiModelProperty("合同CT码")
    private String conmaincode;

    @ApiModelProperty("合同编码")
    private String contractNo;

    @ApiModelProperty(value = "合同起始日期")
    private String gmtExpireStart;

    @ApiModelProperty(value = "合同结束日期")
    private String gmtExpireEnd;

    @ApiModelProperty("合同-是否四保1服 1-是 else-否")
    private Integer contractServeType;

    @ApiModelProperty("供应商id")
    private String oppositeOneId;

    @ApiModelProperty("供应商名称")
    private String oppositeOne;

    @ApiModelProperty(value = "费用开始日期")
    private LocalDate costStartTime;

    @ApiModelProperty(value = "费用结束日期")
    private LocalDate costEndTime;

    @ApiModelProperty(value = "结算周期")
    private Integer splitMode;

    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;
}
