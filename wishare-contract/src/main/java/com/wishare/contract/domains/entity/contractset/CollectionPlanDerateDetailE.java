package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.CollectionPlanDerateDetailFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 收款计划减免明细
 * </p>
 *
 * @author ljx
 * @since 2022-11-07
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("collection_plan_derate_detail")
public class CollectionPlanDerateDetailE{

    /**
     * id
     */
    @TableId(value = CollectionPlanDerateDetailFieldConst.ID)
    private Long id;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 收款计划id
     */
    private Long collectionPlanId;

    /**
     * 减免金额（元）
     */
    private BigDecimal derateAmount;

    /**
     * 减免原因
     */
    private String derateReason;

    /**
     * 审核状态  0通过  1审核中  2未通过
     */
    private Integer auditStatus;

    /**
     * 审批编号
     */
    private String auditCode;

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
    private LocalDateTime gmtCreate;


}
