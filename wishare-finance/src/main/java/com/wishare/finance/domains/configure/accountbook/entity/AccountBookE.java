package com.wishare.finance.domains.configure.accountbook.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 账簿表(AccountBook)实体类
 *
 * @author makejava
 * @since 2022-08-18 09:37:11
 */
@Getter
@Setter
@TableName(value = "account_book", autoResultMap = true)
public class AccountBookE  {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 凭证系统
     */
    private Integer voucherSys;
    /**
     * 账簿编码
     */
    private String code;
    /**
     * 账簿名称
     */
    private String name;
    /**
     * 法定单位id
     */
    private Long statutoryBodyId;
    /**
     * 法定单位名称
     */
    private String statutoryBodyName;
    /**
     * 法定单位id路径
     */
    private String statutoryBodyIdPath;
    /**
     * 映射数值
     */
    private String mapValues;
    /**
     * 账单系统来源
     */
    @TableField(typeHandler= JacksonTypeHandler.class)
    private List<Integer> sysSource;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否总账：0不是，1是
     */
    private Integer isGeneralLedger;
    /**
     * 是否启用：0未启用，1启用
     */
    private Integer disabled;
    /**
     * 资产金额
     */
    private Long assetsAmount;
    /**
     * 费用金额
     */
    private Long costAmount;
    /**
     * 负债金额
     */
    private Long liabilitiesAmount;
    /**
     * 所有者权益金额
     */
    private Long equitiesAmount;
    /**
     * 收入金额
     */
    private Long incomeAmount;
    /**
     * 租户id
     */
    @TableField(fill = FieldFill.INSERT)
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    @TableLogic(value = "0", delval = "1")
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
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operator;
    /**
     * 操作人姓名
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModify;

}

