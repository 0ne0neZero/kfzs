package com.wishare.finance.domains.configure.organization.command;

import lombok.Getter;
import lombok.Setter;

/**
 * @author xujian
 * @date 2022/7/28
 * @Description:
 */
@Getter
@Setter
public class StatutoryBodyListCommand {

    /**
     * 法定单位中文名称
     */
    private String nameCn;

    /**
     * 租户id
     */
    private String tenantId;
}
