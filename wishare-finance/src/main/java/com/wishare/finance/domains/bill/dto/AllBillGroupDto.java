package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * 账单分组分页列表(用于业主端查询账单)
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("账单分组分页列表")
public class AllBillGroupDto extends BillGroupDetailDto<AllBillGroupDto>{

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("账单归属年")
    private Integer accountYear;

    @ApiModelProperty("账单归属月")
    private String accountDate;

    @ApiModelProperty("账单归属季度")
    private Integer accountQuarter;

    @ApiModelProperty("账单归属半年度")
    private Integer accountPartYear;

    @ApiModelProperty("缴费周期 1-月 2-季度 3-半年度 4-年")
    private Integer settleType;

    @ApiModelProperty("移动端展示样式1-费项2-时间")
    private Integer showType;

    @ApiModelProperty("缴费周期 1-月 2-季度 3-半年度 4-年")
    private Integer type;

    @ApiModelProperty("是否合并费项 1-是 2-否")
    private Integer isMerge;

    @ApiModelProperty("是否是违约金：0-否/1-是")
    private Integer overdue;

    /**
     * 账单id
     */
    private String billIds;

    public static String getGroupKey(AllBillGroupDto billGroupDetailDto) {
        return StringUtils.join(Arrays.asList(
                billGroupDetailDto.getRoomId(),
                String.valueOf(billGroupDetailDto.getChargeItemId())), "|");
    }

}
