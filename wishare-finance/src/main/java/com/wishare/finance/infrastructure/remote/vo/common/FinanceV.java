package com.wishare.finance.infrastructure.remote.vo.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author dengjie03
 * @Description
 * @Date 2025-01-10
 */
@Data
@Builder
public class FinanceV implements Serializable {

    private String appInstanceCode ;

    private String unitCode ;

    private String sourceSystem ;

    private String dicCode;

    private WhereConditionV whereCondition;



}
