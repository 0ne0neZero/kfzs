package com.wishare.finance.domains.configure.organization.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 银行账户与分成费项关联实体类
 *
 * @author dongpeng
 * @version 1.0
 * @since 2023/7/22
 */
@Getter
@Setter
@TableName("share_charge_cost_org")
public class ShareChargeCostOrgE {

    /**
     * 成本中心id
     */
    private Long costOrgId;
    /**
     * 分成费项id
     */
    private Long shareChargeId;
    /**
     * 分成比例
     */
    private BigDecimal shareProportion;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 创建人id
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
    @TableField(fill = FieldFill.INSERT)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT)
    private String operatorName;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtModify;

}
