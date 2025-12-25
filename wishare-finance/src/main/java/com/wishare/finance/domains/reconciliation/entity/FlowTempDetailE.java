package com.wishare.finance.domains.reconciliation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 暂存记录、收款单关联表
 */
@Setter
@Getter
@TableName("flow_temp_detail")
public class FlowTempDetailE {

    /**
     * 主键id
     */
    @TableId
    private Long id;


    private Long flowTempRecordId;


    private  Long billId;


    private String statutoryBodyName;


    private String gatherDetailIds;

    /**
     * 费项名称
     */
    private String chargeItemName;


    /**
     * 上级收费单元名称
     */
    private String supCpUnitName;

    /**
     * 收费单元名称
     */
    private String cpUnitName;


    /**
     * 账单编号
     */
    private String billNo;


    /**
     * 账单金额
     */
    private Long totalAmount = 0L;

    /**
     * 缴费时间
     */
    private LocalDateTime payTime;

    /**
     * 收费对象名称
     */
    private String customerName;

    @ApiModelProperty("金额单位")
    @TableField(exist = false)
    private String amountUnit = "分";

    @ApiModelProperty("结算方式")
    private String payChannel;

    @ApiModelProperty("结算方式名称")
    private String payChannelName;

    /**
     * 成本中心id
     */
    private Long costCenterId;

    /**
     * 成本中心名称
     */
    private String costCenterName;

    private Integer billType;

    /**
     * 系统来源 1收费系统，2合同系统，3民宿管理，101亿家优选系统，102BPM系统
     */
    private Integer sysSource;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
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

    public void init() {
        if (Objects.isNull(id)){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("flow_temp_detail"));
        }
    }
}
