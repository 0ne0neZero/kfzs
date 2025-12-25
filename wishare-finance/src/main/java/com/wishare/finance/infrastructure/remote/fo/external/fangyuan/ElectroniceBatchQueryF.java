package com.wishare.finance.infrastructure.remote.fo.external.fangyuan;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author dongpeng
 * @date 2023/6/19 17:19
 * 查询电子发票信息接口（多张） 实体信息
 */
@Getter
@Setter
@ApiModel("批量查询发票")
public class ElectroniceBatchQueryF {

    @ApiModelProperty(value = "购方名称")
    private String GHF_MC;

    @ApiModelProperty(value = "购方税号")
    private String GHF_NSRSBG;

    @ApiModelProperty(value = "销货方税号",required = true)
    @NotBlank(message = "销货方税号不可为空")
    private String XHF_NSRSBH;

    @ApiModelProperty(value = "发票代码")
    private String FP_DM;

    @ApiModelProperty(value = "发票号码")
    private String FP_HM;

    @ApiModelProperty(value = "开票起始日期,必填，格式：yyyy-MM-dd ")
    private String KPRQ_from;

    @ApiModelProperty(value = "开票结束日期,必填，格式：yyyy-MM-dd ")
    private String KPRQ_to;
}
