package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.domains.bill.consts.enums.AdjustStateEnum;
import com.wishare.finance.domains.bill.consts.enums.CarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.JumpStateEnum;
import com.wishare.finance.domains.bill.support.ListFileVoTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 账单跳收明细表，管理账单跳收历史信息记录
 * </p>
 *
 * @author zhenghui
 * @since 2023-07-14
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_JUMP, autoResultMap = true)
public class BillJumpE implements BillDetailBase {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单ID
     */
    private Long billId;

    /**
     * 上级收费单元id
     */
    private String supCpUnitId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单,7收款单）
     */
    private Integer billType;

    /**
     * 跳收状态：0-审核中,1-已审核,2-拒绝
     */
    private Integer state;

    /**
     * 审核记录id
     */
    private Long billApproveId;

    /**
     * 外部跳收编号（bpm流程id）
     */
    private String outJumpNo;

    /**
     * 调整原因
     */
    private String reason;

    /**
     * 跳收对接来源：0-系统内部,1-远洋bpm
     */
    private Integer jumpSource;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 获取跳收审核中状态
     * @return
     */
    public static List<Integer> getJumpApprovingState(){
        return List.of(JumpStateEnum.跳收审核中.getCode());
    }

}
