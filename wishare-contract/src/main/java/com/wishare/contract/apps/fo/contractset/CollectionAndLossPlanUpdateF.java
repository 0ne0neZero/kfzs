package com.wishare.contract.apps.fo.contractset;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同收款计划信息 更新请求参数
 * </p>
 *
 * @author wangrui
 * @since 2022-11-09
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class CollectionAndLossPlanUpdateF {

    @ApiModelProperty("收付款")
    private List<ContractCollectionPlanUpdateF> collectionPlanSaveFList;
    @ApiModelProperty("损益")
    private List<ContractProfitLossPlanUpdateF> profitLossPlanSaveFList;
    @ApiModelProperty("合同id")
    private Long contractId;
    @ApiModelProperty("合同属性")
    private Integer contractNature;

}
