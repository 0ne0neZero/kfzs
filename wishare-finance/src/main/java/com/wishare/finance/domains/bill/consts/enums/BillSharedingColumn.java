package com.wishare.finance.domains.bill.consts.enums;

/**
 * 分表对应的字段
 */
public enum BillSharedingColumn {
    /**
     * 应收账单表、分表字段
     */
    应收账单("receivable_bill", "sup_cp_unit_id"),
    收款账单("gather_bill", "sup_cp_unit_id"),
    收款明细("gather_detail", "sup_cp_unit_id"),
    交账信息("bill_account_hand", "sup_cp_unit_id"),
    审核信息("bill_approve", "sup_cp_unit_id");
    private String tableName;

    private String columnName;

    BillSharedingColumn(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
