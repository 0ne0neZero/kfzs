package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 账单跳收明细表
 * </p>
 *
 * @author jinwh
 * @since 2023-07-20
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_FREEZE, autoResultMap = true)
public class BillFreezeE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 跳收状态：0-审核中,1-已审核,2-拒绝,3-处理中
     */
    @TableField(fill = FieldFill.INSERT)
    private Integer state;

    /**
     * 调整原因
     */
    @TableField
    private String reason;

    /*  *//**
     * 调整附件信息
     *//*
    @TableField(value = "file_info",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> fileVos;*/


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
    @TableField(fill = FieldFill.INSERT,value = "gmt_create")
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

    /**
     * 冻结账单明细记录
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "jump_records_extJson" )
    private String jumpRecordsExtJson;

    /**
     * 冻结总额
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long freezeAmount;

    /**
     * 文件key集合
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "fileList" )
    private String fileList;

    /**
     * 房间名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "room_name")
    private String roomName;

    /**
     * 费项名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "charge_item_name")
    private String chargeItemName;

    /**
     * 项目名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "community_name")
    private String communityName;

    /**
     * 费项id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "charge_item_id")
    private String chargeItemId;

    /**
     * 项目id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "community_id")
    private String communityId;

    /**
     * 房号id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "room_id")
    private String roomId;

    /**
     * 是否解冻:0表示解冻,1表示冻结
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "is_refreeze")
    private Integer isRefreeze;

    /**
     * 冻结类型 1跳收 2代扣
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "freeze_type")
    private Integer freezeType;
}
