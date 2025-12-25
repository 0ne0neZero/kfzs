package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-26
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractPlanRf {

    @ApiModelProperty("appKey")
    private String appKey;
    @ApiModelProperty("合同编号")
    private String agreementNo;
    @ApiModelProperty("合同名称")
    private String agreementName;
    @ApiModelProperty("客商编码")
    private String customerCode;
    @ApiModelProperty("合同状态")
    private Integer agreementStatus;
    @ApiModelProperty("删除状态")
    private Integer deleteFlag;
    @ApiModelProperty("收款计划")
    private List<ContractBillListRf> billList;
    @ApiModelProperty("保证金计划")
    private List<ContractBondListRf> bondList;
}
