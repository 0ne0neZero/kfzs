package com.wishare.finance.apps.model.bill.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillContractV
 * @date 2024.07.04  14:23
 * @description
 */
@Getter
@Setter
@Accessors(chain = true)
public class BillContractV {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("账单ID")
    private Long billId;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("合同编号")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("备注")
    private String reason;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("修改人姓名")
    private String operatorName;

    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;
}
