package com.wishare.finance.domains.configure.chargeitem.command.chargeitem;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 显示/隐藏费项命令
 *
 * @author yancao
 */
@Getter
@Setter
public class ShowedChargeItemCommand {

    /**
     * 费项id
     */
    private List<Long> idList;

    /**
     * 是否显示:0隐藏 1显示
     */
    private Integer showed;

    /**
     * 租户id
     */
    private String tenantId;

    /**
     * 更新人id
     */
    private String operator;

    /**
     * 更新人名称
     */
    private String operatorName;

    /**
     * 更新时间
     */
    private LocalDateTime gmtModify;

}
