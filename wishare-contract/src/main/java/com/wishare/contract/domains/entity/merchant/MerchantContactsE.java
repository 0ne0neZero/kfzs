package com.wishare.contract.domains.entity.merchant;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 客商联系人表
 * </p>
 *
 * @author yancao
 * @since 2022-08-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("merchant_contacts")
public class MerchantContactsE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客商联系人id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 客商id
     */
    private Long merchantId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别：1男，2女
     */
    private Integer sex;

    /**
     * 联系方式
     */
    private String contactWay;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 职位
     */
    private String position;

    /**
     * 所在部门
     */
    private String department;

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
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人ID
     */
    private String operator;

    /**
     * 操作人姓名
     */
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;


}
