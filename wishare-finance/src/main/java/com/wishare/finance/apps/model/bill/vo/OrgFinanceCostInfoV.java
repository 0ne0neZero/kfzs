package com.wishare.finance.apps.model.bill.vo;

import java.math.BigDecimal;
import java.util.List;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ℳ๓采韵
 * @project wishare-org
 * @title OrgFinanceCostInfoV
 * @date 2024.06.17  17:26
 * @description
 */
@Getter
@Setter
@ApiModel("成本中心分页查询结果")
public class OrgFinanceCostInfoV {

    @ApiModelProperty("主键（成本id）")
    private Long id;

    @ApiModelProperty("成本中心形式 1 项目 2行政组织")
    private Integer costForm;

    @ApiModelProperty("成本中心形式名称")
    private String costFormValue;

    @ApiModelProperty("项目/行政组织名称")
    private String name;

    @ApiModelProperty("关联项目id")
    private String communityId;

    @ApiModelProperty("关联项目")
    private String communityName;

    @ApiModelProperty("行政组织名称")
    private String orgName;

    @ApiModelProperty("成本中心中文名称")
    private String nameCn;

    @ApiModelProperty("成本中心英文名称")
    private String nameEn;

    @ApiModelProperty("期区集合")
    private List<BlockEntity> blockList;

    @ApiModelProperty("期区名称")
    private String blockName;

    @ApiModelProperty("成本中心编码")
    private String costCode;

    @ApiModelProperty("关联银行账户")
    private String bankAccountCode;

    @ApiModelProperty("启用状态")
    private Integer disabled;

    @ApiModelProperty("启用状态值")
    private String disabledValue;

    @ApiModelProperty("公区收益分成比例")
    private BigDecimal shareProportion;

    @ApiModelProperty("分成费项名称")
    private String shareChargeName;

    @ApiModelProperty("银联商户号")
    private String unionPayMerchantNo;

}
