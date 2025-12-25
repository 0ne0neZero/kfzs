package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 费项实体
 *
 * @author yancao
 */
@Getter
@Setter
@TableName("charge_item")
public class ChargeItemE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 费项编码
     */
    private String code;

    /**
     * 费项名称
     */
    private String name;

    /**
     * 费项属性：  1收入,2支出,3代收代付及其他
     */
    private Integer attribute;

    /**
     * 父费项id
     */
    private Long parentId;

    /**
     * 父费项名称 一级费项
     */
    @TableField(exist = false)
    private String parentName;
    /**
     * 费项id路径 如：[1,2]
     */
    private String path;

    /**
     * 费项类型：1常规收费类型、2临时收费类型、3押金收费类型、4常规付费类型、5押金付费类型
     */
    private Integer type;

    /**
     * 业务标识
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String businessFlag;

    /**
     * 分成费项编码id
     */
    private String shareChargeId;
    /**
     * 分成费项父id
     */
    private Long shareParentId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否显示:0隐藏 1显示
     */
    private Integer showed;

    /**
     * 是否启用暂估收人:0未启用，1启用
     */
    private Integer estimated;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 是否末级：0否,1是
     */
    private Integer lastLevel;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    /**
     * 创建人ID
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

    /**
     * 父费项编码（导入费项）
     */
    @TableField(exist = false)
    private String parentCode;

    /**
     * 税率id路径 如：[1,2]
     */
    private String taxRatePath;


    /**
     * 是否校验唯一性,1:关闭唯一性校验,其他:开启唯一性校验
     */
    private Integer isUnique;

    /**
     * 是否为违约金 0 否 1 是
     */
    private Integer isOverdue;

    /**
     * 关联的税率信息
     */
    private String taxRateInfo;

}
