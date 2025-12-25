package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 税目费项关联表
 * </p>
 *
 * @author yancao
 * @since 2022-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tax_charge_item_relation")
public class TaxChargeItemRelationE  {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 税目id
     */
    private Long taxItemId;

    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 税收商品id
     */
    private Long taxItemGoodsId;

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
     * 生成id
     */
    public void generateId(){
        if (Objects.isNull(getId())){
            setId(IdentifierFactory.getInstance().generateLongIdentifier("tax_charge_item_relation"));
        }
    }

}
