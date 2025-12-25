package com.wishare.contract.domains.entity.contractset;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 业务类型和bmp系统对应关系表
 * @author wanp
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("contract_business_def")
public class ContractBusinessDefE implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 业务类型id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * bpm系统支出流程id
     */
    private String expendDefId;

    /**
     * bpm系统非支出流程id
     */
    private String noExpendDefId;

    /**
     * 业务类型，0合同订立，1合同变更,2付款
     */
    private String busType;
    /**
     * 业务类型名称
     */
    private String businessName;
    /**
     * 区域名称
     */
    private String areaName;


}
