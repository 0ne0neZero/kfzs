package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表保证金信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractBzjxxF {
    @ApiModelProperty("保证金类型")
    private String guarantytype;

    @ApiModelProperty("保证金类型翻译")
    private String guarantytypename;

    @ApiModelProperty("保证金比例")
    private String guarantyproport;

    @ApiModelProperty("保证金金额")
    private String guarantyamt;

    @ApiModelProperty("保证金退还条件")
    private String guarantyxjzfbhthtj;
}
