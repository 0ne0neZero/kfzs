package com.wishare.contract.domains.vo.revision.projectInitiation.cost;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 费项映射关系树
 */
@Data
public class CostItemNode {

    @ApiModelProperty(value = "节点ID", example = "1671708688168502272")
    private String id;

    @ApiModelProperty(value = "节点名称", example = "合同和成本费项映射关系")
    private String name;

    @ApiModelProperty(value = "父节点ID", example = "1671709433873810432")
    private String parentId;

    @ApiModelProperty(value = "层级", example = "0", allowableValues = "0,1,2,3,4,5")
    private Integer level;

    @ApiModelProperty(value = "成本编码", example = "01-01-01-01-01")
    private String cbCode;

    @ApiModelProperty(value = "成本名称", example = "项目总经理")
    private String cbName;

    @ApiModelProperty(value = "是否合约（1是0否）")
    private String isContract;

    @ApiModelProperty(value = "成本管控方式名称")
    private String costControlTypeName;

    @ApiModelProperty(value = "成本管控方式枚举（10年控；15季控；20月控；30不控；null未设置）")
    private String costControlTypeEnum;

    @ApiModelProperty("子节点列表")
    private List<CostItemNode> children;

}
