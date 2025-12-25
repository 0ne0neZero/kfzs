package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.wishare.contract.domains.consts.contractset.ContractSpaceResourcesFieldConst;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
/**
 * <p>
 * 合同空间资源信息
 * </p>
 *
 * @author wangrui
 * @since 2022-12-28
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@TableName("contract_space_resources")
public class ContractSpaceResourcesE {

    /**
     * 主键
     */
    @TableId(value = ContractSpaceResourcesFieldConst.ID)
    private Long id;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 资源编码
     */
    private String code;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 资源分类
     */
    private String category;

    /**
     * 资源位置
     */
    private String position;

    /**
     * 资源业态
     */
    private String businessType;

    /**
     * 定价标准
     */
    private String pricingStandard;

    /**
     * 资源单价
     */
    private String resourceRates;

    /**
     * 资源单位
     */
    private String resourceUnit;

    /**
     * 数量/面积
     */
    private String quantityArea;

    /**
     * 总价（元）
     */
    private BigDecimal totalPrice;

    /**
     * 开始日期
     */
    private LocalDate startTime;

    /**
     * 结束日期
     */
    private LocalDate endTime;

    /**
     * 资源状态
     */
    private String state;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建人名称
     */
    @TableField(fill = FieldFill.INSERT)
    private String creatorName;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 操作人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 操作人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 操作时间
     */
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;


}
