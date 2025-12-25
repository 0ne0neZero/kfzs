package com.wishare.contract.domains.consts;

public class ContractEnum {

    /**
     * 合同范本状态
     */
    public enum ContractTemplateStatus {
        ENABLE(0), // 启用
        DRAFT(1), // 草稿
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
