package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 税收商品信息
 * </p>
 *
 * @author dp
 * @since 2023-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tax_item_goods")
public class TaxItemGoodsE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 税收商品名称
     */
    private String goodsName;

    /**
     * 是否商品定义（0默认商品，1自定义商品
     */
    private String goodsFlag;

    /**
     * 商品类型（0：收费项目-默认）
     */
    private String type;

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
            setId(IdentifierFactory.getInstance().generateLongIdentifier("tax_item_id"));
        }
    }

}
