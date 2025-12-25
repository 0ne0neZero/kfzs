package com.wishare.contract.domains.vo.contractset;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同预警信息统计
 * </p>
 *
 * @author wangrui
 * @since 2022-10-20
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractStatisticsV {

    @ApiModelProperty("合同临期数量")
    private Integer contractAdvent;
    @ApiModelProperty("合同到期数量")
    private Integer contractExpire;
    @ApiModelProperty("应收账单临期数量")
    private Integer collectionAdvent;
    @ApiModelProperty("应收账单逾期数量")
    private Integer collectionExpire;
    @ApiModelProperty("应付账单临期数量")
    private Integer payAdvent;
    @ApiModelProperty("应付账单逾期数量")
    private Integer payExpire;

}
