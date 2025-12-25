package com.wishare.finance.infrastructure.remote.fo.yonyounc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @description: 项目（成本中心）新增类
 * @author: pgq
 * @since: 2022/12/3 10:29
 * @version: 1.0.0
 */
@ApiModel("项目（成本中心）新增类")
@Getter
@Setter
@ToString
public class AddProjectF {

    /**
     * 项目主键
     */
    @ApiModelProperty("项目主键")
    private String pkProject;

    /**
     * 所属业务单元
     */
    @ApiModelProperty("所属业务单元")
    private String pkOrg = "G";

    /**
     * 项目编码
     */
    @ApiModelProperty(value = "项目编码", required = true)
    private String projectCode;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", required = true)
    private String projectName;

    /**
     * 所属集团 默认集团：远洋亿家集团 G
     */
    @ApiModelProperty("所属集团 默认集团：远洋亿家集团 G")
    private String pkGroup = "G";

    /**
     * 项目简称
     */
    @ApiModelProperty("项目简称")
    private String projectShName;

    /**
     * 项目类型 取NCC项目类型档案-bd_projectclass
     */
    @ApiModelProperty(value = "项目类型 取NCC项目类型档案-bd_projectclass", required = true)
    private String pkProjectClass;

    /**
     * 城市分类 取NCC项目类型档案-bd_defdoc（CSFL）
     */
    @ApiModelProperty(value = "城市分类 取NCC项目类型档案-bd_defdoc（CSFL）", required = true)
    private String def1;

    /**
     * 地区分类 取NCC项目类型档案-bd_areacl
     */
    @ApiModelProperty(value = "地区分类 取NCC项目类型档案-bd_areacl", required = true)
    private String def2;

    /**
     * 项目开发类型 取NCC项目类型档案-bd_defdoc_xmkflx
     */
    @ApiModelProperty(value = "项目开发类型 取NCC项目类型档案-bd_defdoc", required = true)
    private String def3;

    /**
     * 项目业态类别 取NCC项目类型档案-bd_defdoc_xmytlb
     */
    @ApiModelProperty(value = "项目业态类别 取NCC项目类型档案-bd_defdoc", required = true)
    private String def4;

    /**
     * 物业模式 取NCC项目类型档案-bd_defdoc_xmwyms
     */
    @ApiModelProperty(value = "物业模式 取NCC项目类型档案-bd_defdoc", required = true)
    private String def5;

    /**
     * 楼宇管理公司 取NCC项目类型档案-bd_defdoc_xmlyglgs
     */
    @ApiModelProperty(value = "楼宇管理公司 取NCC项目类型档案-bd_defdoc", required = true)
    private String def6;

    /**
     * 管理口径 取NCC项目类型档案-bd_defdoc_xmglkj
     */
    @ApiModelProperty(value = "管理口径 取NCC项目类型档案-bd_defdoc", required = true)
    private String def7;

    /**
     * 项目基本分类 取NCC项目类型档案-pm_eps
     */
    @ApiModelProperty(value = "项目基本分类 取NCC项目类型档案-pm_eps", required = false)
    private String pkEps;

    /**
     * 启用状态 1=未启用; 2=已启用; 3=已停用;
     */
    @ApiModelProperty("启用状态"
        + "1=未启用;\n"
        + "2=已启用;\n"
        + "3=已停用;"
        + "默认3")
    private Integer enableState = 2;

    /**
     * 计划开始日期 格式：2020-12-31
     */
    @ApiModelProperty(value = "计划开始日期 格式：2020-12-31", required = true)
    private String planStartDate;

    /**
     * 计划完成日期 格式：2020-12-31
     */
    @ApiModelProperty(value = "计划完成日期 格式：2020-12-31", required = true)
    private String planFinishDate;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", required = false)
    private String memo;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", required = false)
    private String creator;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", required = false)
    private String creationTime;

}
