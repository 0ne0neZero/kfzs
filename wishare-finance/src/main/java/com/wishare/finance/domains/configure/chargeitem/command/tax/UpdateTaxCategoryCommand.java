package com.wishare.finance.domains.configure.chargeitem.command.tax;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateTaxCategoryCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 税种编码
     */
    private String code;
    /**
     * 税种名称
     */
    private String name;

    /**
     * 是否启用
     */
    private Integer disabled;
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
}
