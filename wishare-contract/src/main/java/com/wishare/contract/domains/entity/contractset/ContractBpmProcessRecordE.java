package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractBpmProcessRecordFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 
 * </p>
 *
 * @author jinhui
 * @since 2023-02-24
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_bpm_process_record")
public class ContractBpmProcessRecordE {

    /**
     * 主键
     */
    @TableId(value = ContractBpmProcessRecordFieldConst.ID)
    private Long id;

    /**
     * 流程id
     */
    private String processId;

    /**
     * Bo对象中的id
     */
    private String bpmBoUuid;

    /**
     * 类型（1合同订立支出2合同订立收入）
     */
    private Integer type;

    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 创建人id
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 修改人id
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 修改人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 是否删除（0否1是）
     */
    @TableLogic
    private Boolean deleted;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModify;

    /**
     * 审核状态 0 未提交 1 通过  2 审批中 3 已驳回 4 待审批
     */
    private Integer reviewStatus;

    /**
     * 驳回原因
     */
    private String rejectReason;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 付款明细id/收票明细id,付款流程，需要插入
     */
    private Long paymentId;
}
