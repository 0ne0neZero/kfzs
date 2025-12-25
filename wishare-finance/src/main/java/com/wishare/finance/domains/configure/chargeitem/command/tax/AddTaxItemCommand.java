package com.wishare.finance.domains.configure.chargeitem.command.tax;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 添加税目命令
 *
 * @author yancao
 */
@Getter
@Setter
public class AddTaxItemCommand {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 税种编码
     */
    private String code;
    /**
     * 税种名称
     */
    private String name;

    /**
     * 费项id
     */
    private List<Long> chargeItemIdList;


}
