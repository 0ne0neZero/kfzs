package com.wishare.finance.apps.model.third;

import com.wishare.finance.apps.model.bill.fo.BasePageF;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description: 账单查询req
 * @author: luguilin
 * @date: 2025-02-05 10:28
 **/
@Data
@EqualsAndHashCode(callSuper=false)
public class QueryBillReq extends BasePageF {

    @ApiModelProperty("项目id列表")
    @NotNull(message = "项目id不能为空")
    private String communityId;

    @ApiModelProperty("更新时间")
    @NotNull(message = "更新时间不能为空")
    private LocalDateTime updateTime;

    @ApiModelProperty("楼栋id列表")
    private List<String> buildingIds;

    @ApiModelProperty("单元id列表")
    private List<String> unitIds;

    @ApiModelProperty("房间id列表")
    private List<String> roomIds;

    @ApiModelProperty("账单id列表")
    private List<Long> billIds;

    @ApiModelProperty("社区id列表")
    private List<Integer> billTypes;

    @ApiModelProperty("账单状态")
    private Integer state;

    @ApiModelProperty("结算状态")
    private Integer settleState;

    @ApiModelProperty("审核状态")
    private Integer approvedState;

    @ApiModelProperty("账单来源 1-收费 2-合同")
    private Integer sysSouce;

    @ApiModelProperty("合同账单类型 1-收入 2-支出")
    private Integer attribute;

    @ApiModelProperty("付款方id列表")
    private List<String> payerIds;
}
