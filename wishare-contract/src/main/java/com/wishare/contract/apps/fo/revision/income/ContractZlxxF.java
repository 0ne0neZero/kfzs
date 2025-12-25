package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表租赁信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractZlxxF {
    @ApiModelProperty("租赁期限")
    private String rentperiod;

    @ApiModelProperty("免租期")
    private String rentfreeperiod;

    @ApiModelProperty("租赁开始日期")
    private String rentstartdate;

    @ApiModelProperty("租赁结束日期")
    private String rentenddate;

}
