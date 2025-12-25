package com.wishare.finance.apps.enums.imports;

import com.wishare.component.imports.task.ImportTaskType;
import com.wishare.starter.exception.BizException;

public enum ImportTaskEnum implements ImportTaskType {

    蓝票补录导入("FINANCE_BLUE_INVOICE_IMPORT", "蓝票补录导入")
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

    public static ImportTaskEnum valueOfByCode(String code) {
        for (ImportTaskEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        throw new BizException(402, "不支持的导入方式");
    }

}
