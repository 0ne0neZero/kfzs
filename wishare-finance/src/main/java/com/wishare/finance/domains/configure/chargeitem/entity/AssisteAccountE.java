package com.wishare.finance.domains.configure.chargeitem.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 辅助核算(AssisteAccount)实体类
 *
 * @author makejava
 * @since 2022-12-02 10:15:00
 */
@Getter
@Setter
@TableName("assiste_account")
public class AssisteAccountE {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 辅助核算编码
     */
    private String asAcCode;
    /**
     * 辅助核算项目
     */
    private String asAcItem;
    /**
     * 辅助核算对象
     */
    private String asAcTarget;
    /**
     * 参照名称
     */
    private String referenceName;
    /**
     * 输入长度
     */
    private String enterLength;
    /**
     * 精度
     */
    private String accuracy;
    /**
     * 统来源：1 收费系统 2合同系统 22 用友ncc
     */
    private Integer sysSource;
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

}

