package com.wishare.contract.apps.vo.contractset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 合同分类详细信息返回信息
 *
 * @author yancao
 */
@Data
@ApiModel("合同分类详细信息返回信息")
public class ContractCategoryDetailV {

    @ApiModelProperty("合同分类id")
    private Long id;

    @ApiModelProperty("合同分类名称")
    private String name;

    @ApiModelProperty("合同分类路径")
    private List<Long> pathList;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("中台业务类型id")
    private Long natureTypeId;
    @ApiModelProperty("中台业务类型code")
    private String natureTypeCode;
    @ApiModelProperty("中台业务类型名称")
    private String natureTypeName;

    @ApiModelProperty("是否具有采购事项，1是，0否")
    private Integer isBuy;

}
