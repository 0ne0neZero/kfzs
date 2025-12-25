package com.wishare.finance.apps.model.bill.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.wishare.finance.domains.bill.entity.CarryoverDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillCarryV
 * @date 2024.05.21  17:13
 * @description:账单结转 VO
 */
@Getter
@Setter
@ApiModel("账单结转信息")
public class BillCarryV {

    @ApiModelProperty("房号ID")
    private String roomId;

    @ApiModelProperty("房号名称")
    private String roomName;

    @ApiModelProperty("项目ID")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("结转金额（单位：分）")
    private Long carryoverAmount;

    @ApiModelProperty("结转账单id")
    private Long carriedBillId;

    @ApiModelProperty("结转账单编号")
    private String carriedBillNo;

    @ApiModelProperty("结转账单类型")
    private Integer billType;

    @ApiModelProperty("申请结转时间")
    private LocalDateTime approveTime;

    @ApiModelProperty("结转时间")
    private LocalDateTime carryoverTime;

    @ApiModelProperty("结转状态")
    private Integer state;

    @ApiModelProperty("结转规则")
    private String carryRule;

    @ApiModelProperty("自动结转方式")
    private Integer autoCarryRule;

    @ApiModelProperty("操作人id")
    protected String operator;

    @ApiModelProperty("操作人姓名")
    protected String operatorName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("结转详情")
    private List<CarryoverDetail> carryoverDetail;

    @ApiModelProperty("附件")
    private List<String> fileUrl;
}
