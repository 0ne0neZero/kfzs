package com.wishare.finance.domains.configure.chargeitem.command.tax;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddTaxRateCommand {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 税率
     */
    private BigDecimal rate;
    /**
     * 税种id
     */
    private Long taxCategoryId;
    /**
     * 租户id
     */
    private String tenantId;
    /**
     * 是否删除:0未删除，1已删除
     */
    private Integer deleted;
    /**
     * 创建人ID
     */
    private String creator;
    /**
     * 操作人名称
     */
    private String creatorName;
    /**
     * 创建时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtCreate;
    /**
     * 操作人ID
     */
    private String operator;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 修改时间 yyyy-MM-dd HH:mm:ss
     */
    private LocalDateTime gmtModify;

    /**
     * 计税方式 1:一般计税 2:简易计税
     */
    private String taxType;

}
