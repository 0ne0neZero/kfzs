package com.wishare.contract.apps.remote.fo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2023-1-5
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractBondListRf {

    @ApiModelProperty("收款计划id")
    private String billId;
    @ApiModelProperty("摘要")
    private String remark;
    @ApiModelProperty("费项")
    private String feeName;
    @ApiModelProperty("成本中心")
    private String costCenterName;
    @ApiModelProperty("责任部门")
    private String deptName;
    @ApiModelProperty("计划收款日期")
    private String firstStartDate;
    @ApiModelProperty("计划收款金额（原币/含税）")
    private String firstAmount;
    @ApiModelProperty("本币金额（含税）")
    private String amount;
    @ApiModelProperty("已收金额")
    private String realAmount;
    @ApiModelProperty("已退金额")
    private String refundAmount;
    @ApiModelProperty("收款状态")
    private Integer billStatus;
    @ApiModelProperty("删除状态 0否 1是 默认0")
    private Integer deleteFlag;

}
