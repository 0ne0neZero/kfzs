package com.wishare.contract.apps.fo.revision.income;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同订立信息拓展表担保信息实体
 * </p>
 *
 * @author chenglong
 * @since 2023-09-22
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class ContractDbxxF {
    @ApiModelProperty("担保类别")
    private String guarantyclassify;

    @ApiModelProperty("担保类别翻译")
    private String guarantyclassifyname;

    @ApiModelProperty("担保形式")
    private String guarantyform;

    @ApiModelProperty("担保形式翻译")
    private String guarantyformname;

    @ApiModelProperty("担保比例")
    private String guarantyproport;

    @ApiModelProperty("担保金额")
    private String guarantyamt;

    @ApiModelProperty("退还条件")
    private String backrule;
}
