package com.wishare.contract.apps.fo.revision;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@ApiModel(value = "同步合同信息到空间资源实体")
public class ContractInfoToSpaceResourceF {

    @ApiModelProperty(value = "临时表单 ID")
    private String formId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同 ID")
    private String agreementId;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同编号")
    private String agreementNo;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同名称")
    private String agreementName;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同类型：0：服务类合同 1：销售类（流水）合同")
    private Integer agreementType;

    /**
     * 必填
     */
    @ApiModelProperty(value = "我方名称")
    private String companyName;

    /**
     * 必填
     */
    @ApiModelProperty(value = "区域")
    private String teamName;

    /**
     * 必填
     */
    @ApiModelProperty(value = "市")
    private String cityName;

    /**
     * 必填
     */
    @ApiModelProperty(value = "我方经办人")
    private String managerNickname;

    /**
     * 必填
     */
    @ApiModelProperty(value = "我方经办人电话")
    private String managerPhone;

    /**
     * 必填
     */
    @ApiModelProperty(value = "关联的项目 id")
    private List<String> agreementProjectIds;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "关联合同归档编号")
    private String agreementRelateNo;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同生效日期")
    private LocalDate agreementStartDate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同到期时间")
    private LocalDate agreementEndDate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "业务航道 0：社区资源业务合同 1：")
    private Integer channel;

    /**
     * 必填
     */
    @ApiModelProperty(value = "对方类型 0： '企业', 1： '个人', 2： '个体户'")
    private Integer businessType;

    /**
     * 必填
     */
    @ApiModelProperty(value = "对方名称")
    private String partyName;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "对方联系人名称")
    private String partyNickname;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "对方联系人电话")
    private String partyPhone;

    /**
     * 非必填
     */
    @ApiModelProperty(value = "对方类型名称")
    private String businessTypeName;

    /**
     * 对方类型为个人/个体户时必填
     */
    @ApiModelProperty(value = "对方证件号码")
    private String certNumber;

    /**
     * 必填
     */
    @ApiModelProperty(value = "我司分成比例，以 0.01%计数")
    private BigDecimal companyRate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "业主分层比例")
    private BigDecimal ownerRate;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同总金额，以分计算")
    private BigDecimal totalAmount;

    /**
     * 必填
     */
    @ApiModelProperty(value = "合同总金额，以元计算")
    private BigDecimal totalAmountAvg;
}
