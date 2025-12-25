package com.wishare.finance.infrastructure.remote.vo.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
@ApiModel(value = "合同-结算计划 相关信息 后端内部使用")
public class ContractPayPlanInnerInfoV {

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("合同编码")
    private String contractNo;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("中交合同编码-CT码")
    private String conMainCode;

    @ApiModelProperty("中交合同-ID")
    private String fromId;

    @ApiModelProperty("合同类型")
    private Integer splitMode;

    @ApiModelProperty("签约单位id")
    private String oppositeOneId;

    @ApiModelProperty("签约单位名称")
    private String oppositeOne;

    @ApiModelProperty("支出方id-默认取第一个")
    private String payeeId;

    @ApiModelProperty("支出方名称-默认取第一个")
    private String payee;

    @ApiModelProperty("收入方id-默认取第一个")
    private String draweeId;

    @ApiModelProperty("收入方名称-默认取第一个")
    private String drawee;

    @ApiModelProperty("收入方信息-JSON")
    private String skdwxx;

    @ApiModelProperty("支出方信息-JSON")
    private String fkdwxx;

    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gmtExpireStart;

    @ApiModelProperty("合同结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date gmtExpireEnd;

    @ApiModelProperty("逻辑内容-合同结束日期+一个顺推结算周期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireNextEndDate;

    @ApiModelProperty(" 项目id")
    private String communityId;

    @ApiModelProperty("项目名称")
    private String communityName;

    @ApiModelProperty("区域")
    private String region;


    @ApiModelProperty("合同类别")
    private String conmanagetypename;

    @ApiModelProperty("合同金额")
    private BigDecimal contractAmountOriginalRate;

    @ApiModelProperty("变更后合同金额")
    private BigDecimal changeContractAmount;

    @ApiModelProperty("合同服务类型,0:其它 1:四保一服")
    private Integer contractServeType;

}
