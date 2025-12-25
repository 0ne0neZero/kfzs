package com.wishare.finance.apps.model.bill.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

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
public class BillJumpV {

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
    private String tenantId;


    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 修改人姓名
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;


}
