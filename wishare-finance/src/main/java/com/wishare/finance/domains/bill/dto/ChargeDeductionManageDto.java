package com.wishare.finance.domains.bill.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ChargeDeductionManageDto {

    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("房号")
    private String roomName;

    @ApiModelProperty("费项")
    private String chargeItemName;

    @ApiModelProperty("账单调整id集合")
    private List<Long> adjustIds;

    @ApiModelProperty(value = "减免形式：1.应收减免；2实收减免；3不减免")
    private Integer deductionMethod;

    @ApiModelProperty("减免金额")
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @ApiModelProperty("审核状态（0待审核， 1处理中，2已完成，3未通过）")
    private Integer approveState;

    @ApiModelProperty("减免原因")
    private String mitigateReason;

    @ApiModelProperty("附件信息")
    private String fileVos;

    @ApiModelProperty("发起时间")
    private String approveTime;


    @ApiModelProperty("删除状态：0 未删除，1 已删除")
    private Byte deleted;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("创建人id")
    private String creator;

    @ApiModelProperty("创建人名字")
    private String creatorName;

    @ApiModelProperty("操作人id")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("最后修改时间")
    private LocalDateTime gmtModify;

}
