package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 付款单明细
 * </p>
 *
 * @author yancao
 * @since 2022-12-22
 */
@Getter
@Setter
@TableName(TableNames.PAY_DETAIL)
public class PayDetail {

    @ApiModelProperty(value = "主键id")
    @TableId
    private Long id;

    @ApiModelProperty(value = "付款单id")
    private Long payBillId;

    @ApiModelProperty(value = "付款单号")
    private String payBillNo;

    @ApiModelProperty(value = "应付单id")
    private Long payableBillId;

    @ApiModelProperty(value = "成本中心id")
    private Long costCenterId;

    @ApiModelProperty(value = "成本中心名称")
    private String costCenterName;

    @ApiModelProperty(value = "费项id")
    private Long chargeItemId;

    @ApiModelProperty(value = "费项名称")
    private String chargeItemName;

    @ApiModelProperty(value = "收费组织id")
    private String cpOrgId;

    @ApiModelProperty(value = "收费组织名称")
    private String cpOrgName;

    @ApiModelProperty(value = "上级收费单元id")
    private String supCpUnitId;

    @ApiModelProperty(value = "上级收费单元名称")
    private String supCpUnitName;

    @ApiModelProperty(value = "收费单元id")
    private String cpUnitId;

    @ApiModelProperty(value = "收费单元名称")
    private String cpUnitName;

    @ApiModelProperty(value = "结算渠道 ")
    private String payChannel;

    @ApiModelProperty(value = "结算方式(0线上，1线下)")
    private Integer payWay;

    @ApiModelProperty(value = "外部支付编号（支付宝单号，银行流水号等）")
    private String outPayNo;

    @ApiModelProperty(value = "应付金额（单位：分）")
    private Long recPayAmount;

    @ApiModelProperty(value = "付款金额（单位：分）(合单付款时，付款金额 > 结算金额)")
    private Long payAmount;

    @ApiModelProperty(value = "收费对象类型")
    private Integer payerType;

    @ApiModelProperty(value = "付款人id")
    private String payerId;

    @ApiModelProperty(value = "付款人名称")
    private String payerName;

    @ApiModelProperty(value = "收款人id")
    private String payeeId;

    @ApiModelProperty(value = "收款人名称")
    private String payeeName;

    @ApiModelProperty(value = "付款时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "收费开始时间")
    private LocalDateTime chargeStartTime;

    @ApiModelProperty(value = "收费结束时间")
    private LocalDateTime chargeEndTime;

    @ApiModelProperty(value = "推凭状态 0-未推凭，1-已推凭")
    private Integer inferenceState;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;
}
