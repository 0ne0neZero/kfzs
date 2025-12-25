package com.wishare.finance.domains.bill.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.wishare.finance.domains.bill.consts.enums.CarryoverStateEnum;
import com.wishare.finance.domains.bill.support.ListCarryoverDetailTypeHandler;
import com.wishare.finance.infrastructure.conts.TableNames;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 账单结转记录表(bill_carryover)
 *
 * @author dxclay
 * @since 2022-09-09
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName(value = TableNames.BILL_CARRYOVER, autoResultMap = true)
public class BillCarryoverE {

    /**
     * 主键id
     */
    private Long id;

    /**
     *  账单类型 1-应收账单， 2-预收账单， 3-临时收费账单， 4-应付账单
     */
    private Integer billType;

    /**
     * 结转账单id
     */
    private Long carriedBillId;

    /**
     * 结转账单编号
     */
    private String carriedBillNo;

    /**
     * 结转金额（单位：分）
     */
    private Long carryoverAmount;

    /**
     * 结转方式：1抵扣，2结转预收
     */
    private Integer carryoverType;

    /**
     * 审核记录id
     */
    private Long billApproveId;

    /**
     * 申请结转时间
     */
    private LocalDateTime approveTime;

    /**
     * 结转时间
     */
    private LocalDateTime carryoverTime;

    @ApiModelProperty("结转规则")
    private String carryRule;

    @ApiModelProperty("自动结转方式")
    private Integer autoCarryRule;

    /**
     * 结转详情
     */
    @TableField(typeHandler= ListCarryoverDetailTypeHandler.class)
    private List<CarryoverDetail> carryoverDetail;

    /**
     * 是否结转预收： 0不结转，1结转
     */
    private Integer advanceCarried;

    /**
     * 结转的预收账单id
     */
    private Long advanceBillId;

    /**
     * 结转的预收账单编号
     */
    private String advanceBillNo;

    /**
     * 结转附件文件路径
     */
    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<String> fileUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 结转状态：0待审核，1审核中，2已生效，3未生效
     */
    private Integer state;

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
     * 是否冲销：0未冲销，1已冲销
     */
    private Integer reversed;

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
     * 获取审核中状态
     * @return
     */
    public static List<Integer> getApprovingState(){
        return List.of(CarryoverStateEnum.待审核.getCode(), CarryoverStateEnum.审核中.getCode());
    }

    public void reSetCarryoverAmount() {
        if (CollectionUtils.isEmpty(carryoverDetail)) {
            this.carryoverAmount = 0L;
        }
        this.carryoverAmount = carryoverDetail.stream().map(CarryoverDetail::getActualCarryoverAmount).filter(Objects::nonNull)
                .reduce(Long::sum).orElse(0L);
    }

}
