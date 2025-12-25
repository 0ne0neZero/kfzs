package com.wishare.contract.domains.task;

import com.wishare.component.imports.task.ImportTaskType;

/**
 * <p>
 * 仓储管理导入任务枚举
 * </p>
 *
 * @author fufengwen
 * @since 2023/02/15
 */
public enum ImportTaskEnum implements ImportTaskType {
    PAY_CONTRACT_IMPORT("PAY_CONTRACT_IMPORT", "支出合同数据导入"),
    INCOME_CONTRACT_IMPORT("INCOME_CONTRACT_IMPORT", "收入合同数据导入"),
    INCOME_PLAN_IMPORT("INCOME_PLAN_IMPORT", "预估数据导入"),
    CONTRACT_PROJECT_ORDER_IMPORT("CONTRACT_PROJECT_ORDER_IMPORT", "立项订单导入"),
    ;

    private final String code;
    private final String name;

    ImportTaskEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
