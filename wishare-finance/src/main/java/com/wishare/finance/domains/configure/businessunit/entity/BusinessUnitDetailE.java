package com.wishare.finance.domains.configure.businessunit.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 业务单元关联实体
 *
 * @author yancao
 */
@Getter
@Setter
@TableName("business_unit_detail")
public class BusinessUnitDetailE {

    /**
     * 主键id
     */
    @TableId
    private Long id;

    /**
     * 关联类型
     */
    private Integer type;

    /**
     * 业务单元id
     */
    private Long businessUnitId;

    /**
     * 关联id
     */
    private Long relevanceId;

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
