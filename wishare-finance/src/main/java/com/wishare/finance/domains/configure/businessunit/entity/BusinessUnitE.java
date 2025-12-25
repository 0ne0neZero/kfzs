package com.wishare.finance.domains.configure.businessunit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 业务单元实体
 *
 * @author yancao
 */
@Getter
@Setter
@TableName("business_unit")
public class BusinessUnitE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 业务单元编码
     */
    private String code;

    /**
     * 业务单元名称
     */
    private String name;

    /**
     * 父业务单元id
     */
    private Long parentId;

    /**
     * 业务单元id路径 如：[1,2]
     */
    private String path;

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

}
