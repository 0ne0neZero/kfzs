package com.wishare.finance.infrastructure.remote.vo.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
* <p>
* 视图对象
* </p>
*
* @author chenglong
* @since 2023-07-0311
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "客户-简单-内部使用", description = "客户-简单-内部使用")
public class SupplierSimpleV {
    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 客户编码
    */
    @ApiModelProperty("客户编码")
    private String code;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String name;
    /**
    * 客户性质 1企业 2个人
    */
    @ApiModelProperty("客户性质 1企业 2个人")
    private Integer nature;
    /**
     * 主数据编码 外部编码
     */
    @ApiModelProperty("主数据编码 外部编码")
    private String mainDataCode;
}
