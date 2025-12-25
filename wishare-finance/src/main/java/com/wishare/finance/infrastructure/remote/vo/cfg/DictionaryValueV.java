package com.wishare.finance.infrastructure.remote.vo.cfg;


import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 数据项信息表
 * </p>
 *
 * @author wishare
 * @since 2022-04-11
 */
@Getter
@Setter
@Accessors(chain = true)
public class DictionaryValueV {

    @ApiModelProperty("数据项id")
    private String id;
    @ApiModelProperty("数据项名称")
    private String name;
    @ApiModelProperty("数据项code")
    private String code;
    @ApiModelProperty("数据类型code")
    private String dictionaryCode;
    @ApiModelProperty("备注")
    private String remarks;
    @ApiModelProperty("简称编码")
    private String abbrCode;
}
