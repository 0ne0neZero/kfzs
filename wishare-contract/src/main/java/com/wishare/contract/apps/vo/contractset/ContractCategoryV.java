package com.wishare.contract.apps.vo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 合同分类返回信息
 *
 * @author yancao
 */
@Data
@ApiModel("合同分类返回信息")
public class ContractCategoryV {

    @ApiModelProperty("合同分类id")
    private Long id;

    @ApiModelProperty("合同分类名称")
    private String name;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("是否具有采购事项，1是，0否")
    private Integer isBuy;

    @ApiModelProperty("中中交合同业务类型编码")
    private String bizCode;

    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;
    @ApiModelProperty("简称编码")
    private String abbrCode;

}
