package com.wishare.contract.domains.consts;

public class ContractInvoiceEnum {

    /**
     * 开票状态
     */
    public enum ContractTemplateStatus {
        成功(0), // 启用
        开票中(1), // 草稿
        DISABLE(2), // 禁用
        ;

        private int value;

        ContractTemplateStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
