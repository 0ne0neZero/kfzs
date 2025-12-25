package com.wishare.contract.domains.entity.contractset;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同范本变更记录实体
 * @author ljx
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contract_template_log")
public class ContractTemplateLogE implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 合同范本变更记录id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 合同范本id
     */
    private Long templateId;

    /**
     * 启用时间
     */
    private LocalDateTime enableTime;

    /**
     * 禁用时间
     */
    private LocalDateTime disableTime;

    /**
     * 操作人id
     */
    private String operator;

    /**
     * 操作人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

}
