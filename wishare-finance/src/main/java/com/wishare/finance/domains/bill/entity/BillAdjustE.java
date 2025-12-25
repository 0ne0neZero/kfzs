package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.bill.consts.enums.AdjustStateEnum;
import com.wishare.finance.domains.bill.support.ListFileVoTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.tools.starter.vo.FileVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 账单调整明细表，管理账单调整历史信息记录
 * </p>
 *
 * @author dxclay
 * @since 2022-08-22
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_ADJUST, autoResultMap = true)
public class BillAdjustE implements BillDetailBase {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账单ID
     */
    private Long billId;

    /**
     * 账单类型（1:应收账单，2:预收账单，3:临时收费账单）
     */
    private Integer billType;

    /**
     * 调整方式：1应收调整-单价，2应收调整-应收金额，3应收调整-实测面积，4实收调整-实测面积，5实收调整-空置房打折，6实收调整-优惠券，7实收调整-开发减免，8实收调整-其他
     */
    private Integer adjustWay;

    /**
     * 减免形式：1.应收减免；2实收减免；3不减免
     */
    private Integer deductionMethod;

    /**
     * 调整内容
     */
    private String content;

    /**
     * 调整原因
     */
    private Integer reason;

    /**
     * 原账单金额 (单位： 分)
     */
    private Long billAmount;

    /**
     * 调整金额(减免时为负数，调高时为正数) (单位： 分)
     */
    private Long adjustAmount;

    /**
     * 财务层面减免金额（不包含结转出去的金额）
     */
    private Long actualLostAmount;

    /**
     * 单价（单位：分）
     */
    private BigDecimal unitPrice;

    /**
     * 计费面积 (单位：m²)
     */
    private BigDecimal chargingArea;

    /**
     * 扩展字段（其他调整类型的数量）
     */
    private Long extField1;

    /**
     *调整时拆单的账单编号
     */
    private String separateBillNo;

    /**
     * 调整类型： 1减免，2调高
     */
    private Integer adjustType;

    /**
     * 减免凭证号
     */
    private String voucher;

    /**
     * 调整比例，区间[0.01, 100]
     */
    private BigDecimal adjustRatio;

    /**
     * 审核记录id
     */
    private Long billApproveId;

    /**
     * 外部审批标识
     */
    private String outApproveId;

    /**
     * 调整状态：0待审核，1审核中,2已生效，3未生效
     */
    private Integer state;

    /**
     * 收费对象类型（0:业主，1开发商，2租客）
     */
    private Integer payerType;

    /**
     * 付款方ID
     */
    private String payerId;

    /**
     * 付款方名称
     */
    private String payerName;

    /**
     * 付款方手机号
     */
    private String payerPhone;

    /**
     * 收费对象属性（1:个人，2:企业）
     */
    private Integer payerLabel;

    @ApiModelProperty("源收费对象ID")
    private String originalPayerId;

    @ApiModelProperty("源收费对象名称")
    private String originalPayerName;

    @ApiModelProperty("源收费对象类型")
    private Integer originalPayerType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 申请调整时间
     */
    private LocalDateTime approveTime;

    /**
     * 调整时间
     */
    private LocalDateTime adjustTime;

    /**
     * 调整附件信息
     */
    @TableField(value = "file_info",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> fileVos;

    /**
     * 减免原因说明文件
     */
    @TableField(value = "deduction_file_info",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> deductionFileVos;

    /**
     * 过程催费文件
     */
    @TableField(value = "expediting_file_info",typeHandler= ListFileVoTypeHandler.class)
    private List<FileVo> expeditingFileVos;

    /**
     * 推凭状态 0-未推凭 1-已推凭 默认0
     */
    private Integer inferenceState = 0;

    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;


    @ApiModelProperty(value = "减免方式：1.按固定金额减免，3.按折扣减免（开发代付调整时必填）")
    private Integer derateStrategy;

    @ApiModelProperty(value = "开发商代付调整方式，金额或比例")
    private BigDecimal derateAmount;

    @ApiModelProperty(value = "开发商id")
    private String developerId;

    @ApiModelProperty(value = "开发商名称")
    private String developerName;

    @ApiModelProperty(value = "调整税率值")
    private BigDecimal adjustTaxRate;

    @ApiModelProperty("税率id")
    private Long adjustTaxRateId;

    @ApiModelProperty(value = "违约金比率")
    private BigDecimal overdueRate;

    @ApiModelProperty("违约起算方式,1计费结束日期+1,2应收日+1")
    private Integer breakContractStartMethod;

    @ApiModelProperty("延期天数")
    private Integer lateDays;

    @ApiModelProperty(value = "应收日(包含年月日)")
    private LocalDate receivableDate;

    @ApiModelProperty(value = "违约起算方式是否为应收日（调整应收日使用）")
    private Boolean isReceivableDateBreakContractStartMethod;

    @ApiModelProperty("调整后的费项id")
    private Long changeChargeItemId;

    /**
     *调整金额是否准确（账单金额是否按照计费方式计算）1 是 2 否
     */
    private Integer isExact;

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
     * 获取审核中权限
     * @return List
     */
    public static List<Integer> getApprovingState(){
        return List.of(AdjustStateEnum.待审核.getCode(), AdjustStateEnum.审核中.getCode());
    }

}
