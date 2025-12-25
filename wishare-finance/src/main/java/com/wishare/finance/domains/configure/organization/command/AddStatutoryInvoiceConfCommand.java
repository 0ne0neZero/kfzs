package com.wishare.finance.domains.configure.organization.command;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/11/1
 * @Description:
 */
@Getter
@Setter
public class AddStatutoryInvoiceConfCommand {

    /**
     * 法定单位Id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称
     */
    private String statutoryBodyName;

    /**
     * 机器编号（12位盘号）
     */
    private String machineCode;

    /**
     * 分机号
     */
    private Long extensionNumber;

    /**
     * 终端号
     */
    private Long terminalNumber;

    /**
     * 开票人
     */
    private String clerk;

    /**
     * 终端代码
     */
    private String terminalCode;

    /**
     * 用户代码
     */
    private String userCode;
}
