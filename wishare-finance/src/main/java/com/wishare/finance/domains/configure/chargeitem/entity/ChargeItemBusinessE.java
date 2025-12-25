package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 费项业务类型实体
 */
@Getter
@Setter
@TableName("charge_item_business")
public class ChargeItemBusinessE {
    /**
     * 主键id
     */
    @TableId
    private Long id;
    /**
     * 费项id
     */
    private Long chargeItemId;

    /**
     * 费项编码
     */
    private String chargeItemCode;

    /**
     * 费项名称
     */
    private String chargeItemName;

    /**
     * 单据类型id
     */
    private String documentId;

    /**
     * 单据类型编码
     */
    private String documentCode;
    /**
     * 单据类型名称
     */
    private String documentName;

    /**
     * 业务类型id
     */
    private String businessTypeId;
    /**
     * 业务类型编码
     */
    private String businessTypeCode;
    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 变动名称
     */
    private String changeName;

    /**
     * 变动编码
     */
    private String changeCode;

    /**
     * 款项id
     */
    private String paymentId;

    /**
     * 款项名称
     */
    private String paymentName;

    /**
     * 款项编码
     */
    private String paymentCode;

    /**
     * 实签-款项id
     **/
    private String signedPaymentId;

    /**
     * 实签-款项名称-编码
     **/
    private String signedPaymentCode;

    /**
     * 实签-款项名称-名称
     **/
    private String signedPaymentName;

    /**
     * 成本科目口径-款项id
     **/
    private String costPaymentId;

    /**
     * 成本科目口径-编码
     **/
    private String costPaymentCode;

    /**
     * 成本科目口径-名称
     **/
    private String costPaymentName;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

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

}
