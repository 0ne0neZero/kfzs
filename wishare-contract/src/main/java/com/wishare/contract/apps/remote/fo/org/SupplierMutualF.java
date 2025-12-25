package com.wishare.contract.apps.remote.fo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/4  20:16
 */
@Getter
@Setter
@ApiModel("供应商数据合同交互更新参数")
public class SupplierMutualF {


    /**
     * 供应商ID
     */
    @ApiModelProperty("供应商ID")
    private String id;
    /**
     * 合同数量 提交成功之后的所有状态
     */
    @ApiModelProperty("合同数量")
    private Integer contractNum;
    /**
     * 待提交&已拒绝合同数量
     */
    @ApiModelProperty("待提交&已拒绝合同数量")
    private Integer unPassNum;
    /**
     * 关联合同数量 审批中 & 未开始 & 执行中
     */
    @ApiModelProperty("关联合同数量")
    private Integer relationNum;
    /**
     * 关联合同数量（所有状态）
     */
    @ApiModelProperty("关联合同数量（所有状态）")
    private Integer checkNum;
    /**
     * 合作状态(1合作中 2未合作 3已结束) 根据执行中状态的合同数量判断
     */
    @ApiModelProperty("合作状态(1合作中 2未合作 3已结束)")
    private Integer coopStatus;

}
