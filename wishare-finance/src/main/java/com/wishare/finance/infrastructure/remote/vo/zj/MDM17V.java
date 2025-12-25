package com.wishare.finance.infrastructure.remote.vo.zj;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MDM17V {
    @ApiModelProperty(value = "业务科目id")
    String idExt;

    @ApiModelProperty(value = "业务科目编号")
    String code;

    @ApiModelProperty(value = "业务科目名称")
    String name;

    @ApiModelProperty(value = "业务科目全称")
    String fullName;

    @ApiModelProperty(value = "是否可用")
    String isEnabled;

    @ApiModelProperty(value = "父节点id")
    String parentId;

    @ApiModelProperty(value = "是否明细")
    String isDetail;

    @ApiModelProperty(value = "级数")
    Integer layer;

    @ApiModelProperty(value = "分级码")
    String path;

    @ApiModelProperty(value = "科目属性")
    String accountType;

    @ApiModelProperty(value = "属性")
    String accTitleProp;

    @ApiModelProperty(value = "余额方向")
    String balanceDir;

    @ApiModelProperty(value = "对应总账科目")
    String charToFaCc;

    @ApiModelProperty(value = "全称策略")
    Integer fullNameRole;

    @ApiModelProperty(value = "是否允许下级增加")
    String isAllowLedgerChildAdd;

    @ApiModelProperty(value = "助记码")
    String mnemonicCode;

    @ApiModelProperty(value = "备注")
    String remark;

    @ApiModelProperty(value = "是否公共")
    Integer typeFlag;

    @ApiModelProperty(value = "行政组织编号")
    String accOrgId;

    @ApiModelProperty(value = "坏账准备计提")
    String extendChar01;

    @ApiModelProperty(value = "折现")
    String extendChar02;

    @ApiModelProperty(value = "长期应收款")
    String extendChar03;

    @ApiModelProperty(value = "长期应付款")
    String extendChar04;

    @ApiModelProperty(value = "暂估")
    String extendChar05;

    @ApiModelProperty(value = "专项项目")
    String extendChar06;

    @ApiModelProperty(value = "增减变动，内容为专项类别编号")
    String extendChar07;
}
