package com.wishare.finance.apps.model.configure.organization.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author xujian
 * @date 2022/7/26
 * @Description:
 */
@Getter
@Setter
@ApiModel("成本中心反参")
public class CostCenterV {

    @ApiModelProperty("成本中心id")
    private Long id;

    @ApiModelProperty("成本中心编码")
    private String code;

    @ApiModelProperty("关联对象类型:0组织；1项目")
    private Integer relationType;

    @ApiModelProperty("成本中心名称-中文")
    private String nameCn;

    @ApiModelProperty("成本中心名称-英文")
    private String nameEn;

    @ApiModelProperty("成本中心类型：1费用；2基本生产；3辅助生产")
    private Integer type;

    @ApiModelProperty("负责人名称")
    private String directorName;

    @ApiModelProperty("关联组织id")
    private Long orgId;

    @ApiModelProperty("是否禁用：0启用，1禁用")
    private Integer disabled;

    @ApiModelProperty("是否与主数据库同步：0否 1是")
    private Integer dataSyn;

    @ApiModelProperty("租户id")
    private String tenantId;

    @ApiModelProperty("是否删除:0未删除，1已删除")
    private Integer deleted;

    @ApiModelProperty("创建人ID")
    private String creator;

    @ApiModelProperty("创建人姓名")
    private String creatorName;

    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("操作人ID")
    private String operator;

    @ApiModelProperty("操作人姓名")
    private String operatorName;

    @ApiModelProperty("修改时间")
    private LocalDateTime gmtModify;
}
