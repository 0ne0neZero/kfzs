package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.AdvanceBill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * 预收账单分组分页列表
 *
 * @author yancao
 */
@Setter
@Getter
@ApiModel("预收账单分组分页列表")
public class AdvanceBillGroupDetailDto extends BillGroupDetailDto<AdvanceBillGroupDetailDto>{

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("收款方式拼接")
    private String payInfosString;

    @ApiModelProperty("收费对象类型名称")
    private String payerTypeStr;

    public static String getGroupKey(AdvanceBillGroupDetailDto billGroupDetailDto, DateTimeFormatter dateTimeFormatter){
        String dateKey = billGroupDetailDto.getStartTime() == null ? "null" : billGroupDetailDto.getStartTime().format(dateTimeFormatter);
        return StringUtils.join(Arrays.asList(
                billGroupDetailDto.getCommunityId(),
                billGroupDetailDto.getRoomId(),
                String.valueOf(billGroupDetailDto.getChargeItemId()),
                dateKey), "|");
    }

    public static String getGroupKey(AdvanceBill bill, DateTimeFormatter dateTimeFormatter){
        String dateKey = bill.getStartTime() == null ? "null" : bill.getStartTime().format(dateTimeFormatter);
        return StringUtils.join(Arrays.asList(
                bill.getCommunityId(),
                bill.getRoomId(),
                String.valueOf(bill.getChargeItemId()),
                dateKey), "|");
    }

}
