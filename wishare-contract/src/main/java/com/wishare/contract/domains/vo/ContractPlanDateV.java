package com.wishare.contract.domains.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 收付款计划，点击拆分方式后要调用的接口，所要使用的属性；因为具体计划是后端计算的
 *
 * @author 龙江锋
 * @date 2023/8/18 14:27
 */
@Data
@Accessors(chain = true)
@ApiModel("计算收款计划返回")
public class ContractPlanDateV {
    /**
     * 计划收付款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;

    /**
     * 计划收付款金额
     */
    private BigDecimal plannedCollectionAmount;

    /**
     * 金额比例
     */
    private BigDecimal ratioAmount;

    /**
     * 期数
     */
    private Integer termDate;
}
