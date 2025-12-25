package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 *
 * </p>
 *
 * @author wangrui
 * @since 2022-09-29
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractProfitLossPlanF {

    @ApiModelProperty("id")
    private Long id;
    @ApiModelProperty("合同Id")
    private Long contractId;
    @ApiModelProperty("收付款Id")
    private Long collectionId;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer paymentStatus;
    @ApiModelProperty("收款/付款状态 0未收/付  1部分收/付  2已收/付")
    private Integer setPaymentStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer invoiceStatus;
    @ApiModelProperty("开票/收票状态 0未开/收  1部分开/收  2已开/收")
    private Integer setInvoiceStatus;
    @ApiModelProperty("删除状态")
    private Integer deleted;
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
}
