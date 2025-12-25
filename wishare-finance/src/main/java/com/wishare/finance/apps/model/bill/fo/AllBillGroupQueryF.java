package com.wishare.finance.apps.model.bill.fo;

import com.wishare.finance.domains.bill.dto.ChargeSettleRuleDto;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 所有账单分组分页查询入参
 *
 * @author yancao
 */
@Getter
@Setter
@ApiModel("所有账单分组分页查询入参")
public class AllBillGroupQueryF {

    @ApiModelProperty("房号信息")
    private List<RoomInfo> roomInfoList;

    @ApiModelProperty("查询条件")
    private PageF<SearchF<?>> query;

    @ApiModelProperty("是否查询子账单")
    private Boolean loadChildren;

    @ApiModelProperty("合并缴费规则")
    Map<String, ChargeSettleRuleDto> sortMap;

    @ApiModelProperty("合并缴费规则费项id")
    private List<String> itemIdList;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("移动端展示样式1-费项2-时间")
    private Integer showType;

    //只用户管家催缴账单分享使用,不影响既存逻辑  2023/06/26 李彪 start
    @ApiModelProperty("账单ids")
    private List<Long> bills;
    //2023/06/26 李彪 end

    //装修办理缴费账单分享使用,不影响既存逻辑
    @ApiModelProperty("装修办理缴费账单ids")
    private List<Long> decorationBills;

    @Setter
    @Getter
    public static class RoomInfo{
        @ApiModelProperty("房号id")
        private Long roomId;

        @ApiModelProperty("付款人id")
        private String payerId;
    }

}
