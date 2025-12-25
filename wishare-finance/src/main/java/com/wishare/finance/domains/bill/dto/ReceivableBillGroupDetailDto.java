package com.wishare.finance.domains.bill.dto;

import com.wishare.finance.domains.bill.entity.PayInfo;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
@ApiModel("应收账单分组分页列表")
public class ReceivableBillGroupDetailDto extends BillGroupDetailDto<ReceivableBillGroupDetailDto> {

    @ApiModelProperty("计费方式")
    private Integer billMethod;

    @ApiModelProperty("计费面积(单位：m²)")
    private BigDecimal chargingArea;

    @ApiModelProperty("单价（单位：分）")
    private BigDecimal unitPrice;

    @ApiModelProperty("是否逾期：0未逾期，1已逾期")
    private Integer overdueState;

    @ApiModelProperty("账单开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("账单结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("账单已缴时间")
    private LocalDateTime chargeTime;

    @ApiModelProperty("支付信息")
    private List<PayInfo> payInfos;

    @ApiModelProperty("收款方式拼接")
    private String payInfosString;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;

    public static String getGroupKey(ReceivableBillGroupDetailDto billGroupDetailDto, DateTimeFormatter dateTimeFormatter){
        String dateKey = billGroupDetailDto.getStartTime() == null ? "null" : billGroupDetailDto.getStartTime().format(dateTimeFormatter);
        return StringUtils.join(Arrays.asList(
                billGroupDetailDto.getCommunityId(),
                billGroupDetailDto.getRoomId(),
                String.valueOf(billGroupDetailDto.getChargeItemId()),
                dateKey), "|");
    }

    public static String getGroupKey(ReceivableBill bill, DateTimeFormatter dateTimeFormatter){
        String dateKey = bill.getStartTime() == null ? "null" : bill.getStartTime().format(dateTimeFormatter);
        return StringUtils.join(Arrays.asList(
                bill.getCommunityId(),
                bill.getRoomId(),
                String.valueOf(bill.getChargeItemId()),
                dateKey), "|");
    }




}
