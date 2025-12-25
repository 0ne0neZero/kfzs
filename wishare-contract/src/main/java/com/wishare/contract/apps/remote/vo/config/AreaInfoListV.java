package com.wishare.contract.apps.remote.vo.config;

import com.wishare.starter.beans.Tree;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 查询省市区树形结构返回类
 *
 * @Author :慧享 王蕊
 * @Date: 2022/4/6 17:20
 */
@Getter
@Setter
@Accessors(chain = true)

public class AreaInfoListV extends Tree<AreaInfoListV, Long> {

    //    @ApiModelProperty("主键")
//    private Long id;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("区域code")
    private Long adCode;
    @ApiModelProperty("父级id")
    private Long parentId;
    @ApiModelProperty("等级英文代号")
    private String levelName;
    @ApiModelProperty("层级数字代号")
    private Integer levelCode;
    @ApiModelProperty("经纬度，经度，纬度")
    private String center;
    @ApiModelProperty("简称代码")
    private String abbrCode;
}
