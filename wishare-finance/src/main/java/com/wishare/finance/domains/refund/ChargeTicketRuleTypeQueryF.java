package com.wishare.finance.domains.refund;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Accessors(chain = true)
public class ChargeTicketRuleTypeQueryF {

    @ApiModelProperty("账单id集合")
    private List<Long> billIds;

    @ApiModelProperty(value = "违约金账单ids",required = true)
    private List<Long> overdueBillIds;

    @ApiModelProperty("收款单ID")
    private Long gatherBillId;

    @ApiModelProperty(value = "收款单明细id集合")
    private List<Long> gatherDetailIds;

    @ApiModelProperty("项目id")
    private String communityId;

    @ApiModelProperty("捷达项目id")
    private String jdCommunityId;

    @ApiModelProperty("账单类型")
    private Integer billType;

    @ApiModelProperty("费项列表")
    private List<Long> itemsIds;

    public ChargeTicketRuleTypeQueryF(ChargeGatherInvoiceTicketF gatherInvoiceTicketF){
        gatherBillId = gatherInvoiceTicketF.getGatherBillId();
        communityId = gatherInvoiceTicketF.getCommunityId();
    }
}
