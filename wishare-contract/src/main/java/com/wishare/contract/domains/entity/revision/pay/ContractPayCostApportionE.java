package com.wishare.contract.domains.entity.revision.pay;

import com.baomidou.mybatisplus.annotation.*;
import com.wishare.contract.domains.entity.revision.BaseE;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author hhb
 * @describe
 * @date 2025/11/7 16:59
 */
@Data
@Accessors(chain = true)
@TableName("contract_pay_cost_apportion")
public class ContractPayCostApportionE{

    @TableId(value = ID)
    private String id;
    //合同ID
    private String contractId;
    //父ID
    private String pid;
    //成本-费项编码
    private String accountItemCode;
    //成本-费项名称
    private String accountItemName;
    //成本-费项全码
    private String accountItemFullCode;
    //成本-费项全称
    private String accountItemFullName;
    //成本-管控方式枚举
    private Integer costControlTypeEnum;
    //成本-管控方式名称
    private String costControlTypeName;

    @ApiModelProperty("业务单元编号")
    private String businessUnitCode;

    @ApiModelProperty("地区公司GUID")
    private String buGuid;

    @ApiModelProperty("项目GUID")
    private String projectGuid;

    @ApiModelProperty("业务线GUID")
    private String businessGuid;
    //本次分摊金额
    private BigDecimal apportionAmount;
    @ApiModelProperty("动态成本GUID")
    private String dynamicCostGuid;
    @ApiModelProperty("分摊类型")
    private Integer apportionType;
    @ApiModelProperty("年份")
    private String year;
    @ApiModelProperty(name = "当年合计", notes = "年度")
    private BigDecimal yearSurplus;
    @ApiModelProperty(name = "一月", notes = "一月")
    private BigDecimal janSurplus;
    @ApiModelProperty(name = "二月", notes = "二月")
    private BigDecimal febSurplus;
    @ApiModelProperty(name = "三月", notes = "三月")
    private BigDecimal marSurplus;
    @ApiModelProperty(name = "四月", notes = "四月")
    private BigDecimal aprSurplus;
    @ApiModelProperty(name = "五月", notes = "五月")
    private BigDecimal maySurplus;
    @ApiModelProperty(name = "六月", notes = "六月")
    private BigDecimal junSurplus;
    @ApiModelProperty(name = "七月", notes = "七月")
    private BigDecimal julSurplus;
    @ApiModelProperty(name = "八月", notes = "八月")
    private BigDecimal augSurplus;
    @ApiModelProperty(name = "九月", notes = "九月")
    private BigDecimal sepSurplus;
    @ApiModelProperty(name = "十月", notes = "十月")
    private BigDecimal octSurplus;
    @ApiModelProperty(name = "十一月", notes = "十一月")
    private BigDecimal novSurplus;
    @ApiModelProperty(name = "十二月", notes = "十二月")
    private BigDecimal decSurplus;


    private String tenantId;

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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;

    /**
     * 更新人名称
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

    /**
     * 是否删除  0 正常 1 删除
     */
    @TableLogic
    private Integer deleted;

    public static final String ID = "id";
    public static final String PID = "pid";
    public static final String TENANT_ID = "tenantId";
    public static final String CONTRACT_ID = "contractId";
    public static final String DELETED = "deleted";
    public static final String YEAR = "year";
    public static final String APPORTION_TYPE = "apportionType";
}
