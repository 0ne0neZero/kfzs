package com.wishare.contract.domains.entity.revision.income;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_income_conclude_correction")
public class ContractIncomeConcludeCorrectionE {

    @TableId(value = ID)
    private Long id;

    //合同id
    private String contractId;

    //修正状态 0 待提交 1 审批中  2 已通过 3 驳回
    private Integer correctionStatus;

    //BPM流程ID
    private String bpmProcInstId;
    //BPM审批通过时间
    private LocalDateTime bpmApprovalDate;

    /**
     * 原-合同开始日期
     */
    private LocalDate oldGmtExpireStart;

    /**
     * 原-合同到期日期
     */
    private LocalDate oldGmtExpireEnd;

    /**
     * 新-合同开始日期
     */
    private LocalDate newGmtExpireStart;

    /**
     * 新-合同到期日期
     */
    private LocalDate newGmtExpireEnd;

    /**
     * 修正原因
     */
    private String correctionReason;

    /**
     *附件信息
     */
    private String correctionFile;

    /**
     *修正信息
     */
    private String correctionDesc;

    /**
     * 创建人
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
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;


    /**
    * 收入合同订立信息拓展表id
    */
    public static final String ID = "id";

    /**
    * 合同id
    */
    public static final String CONTRACTID = "contractId";
    public static final String CORRECTION_STATUS = "correctionStatus";
    public static final String DELETED = "deleted";
    public static final String GMTCREATE = "gmtCreate";

}
