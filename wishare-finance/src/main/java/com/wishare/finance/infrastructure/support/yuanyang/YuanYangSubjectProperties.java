package com.wishare.finance.infrastructure.support.yuanyang;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 远洋科目配置模块
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/16
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "wishare.finance.yuanyang.subject")
public class YuanYangSubjectProperties {

    /**
     * 其他应付款\员工报销
     */
    private String employeeReimbursement = "217606";

    /**
     * 应交税费\应交增值税\进项税额
     */
    private String inputTax = "21710111";

    /**
     * 应交税费-应交增值税-计算扣除进项税额
     */
    private String calDeductInputTax = "21710115";
    /**
     * 银行存款
     */
    private String bankAccount = "1002";
    /**
     * 备用金
     */
    private String pettyCash = "1171";
    /**
     * 内部往来\代收代付
     */
    private String collectPay = "113202";
    /**
     * 内部往来\上交下拨
     */
    private String allocateTurnover = "113201";

}
