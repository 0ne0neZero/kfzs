package com.wishare.finance.infrastructure.remote.vo.space;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @description:
 * @author: pgq
 * @since: 2022/10/20 10:10
 * @version: 1.0.0
 */
@Getter
@Setter
@ApiModel("空间树")
public class ManageTree extends Tree<ManageTree, Long> {

    /**
     * 节点类型，0：组织，1：项目，2：空间
     */
    @ApiModelProperty("节点类型，0：组织，1：项目，2：空间")
    private byte flag;

    /**
     * 	楼层
     */
    @ApiModelProperty("楼层")
    private Integer floor;

    /**
     *	iconUrl链接
     */
    @ApiModelProperty("iconUrl链接")
    private  String iconUrl;
//    private String id;

    /**
     * true：没有子节点，false：有子节点
     */
    @ApiModelProperty("true：没有子节点，false：有子节点")
    private boolean sLeaf;
//    pid;
    /**
     * 允许关联类型：1项目
     */
    @ApiModelProperty("允许关联类型：1项目")
    private Integer supportType;

    /**
     * 节点名
     */
    @ApiModelProperty("节点名")
    private String title;

    /**
     * 	类型id
     */
    @ApiModelProperty("类型id")
    private Long type;

    /**
     * 0启用，1禁用
     */
    @ApiModelProperty("0启用，1禁用")
    private byte unable;

    @ApiModelProperty("value")
    private String value;
}
