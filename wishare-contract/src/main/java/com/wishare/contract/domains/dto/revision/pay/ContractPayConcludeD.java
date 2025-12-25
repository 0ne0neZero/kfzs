package com.wishare.contract.domains.dto.revision.pay;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;

/**
* <p>
* 支出合同订立信息表
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "contract_pay_conclude请求对象", description = "支出合同订立信息表")
public class ContractPayConcludeD {

    @ApiModelProperty("合同id")
    private String id;
    @ApiModelProperty("合同名称")
    private String name;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    @ApiModelProperty("合同业务分类Id")
    private Long categoryId;
    /**
     * 合同业务分类path
     */
    @ApiModelProperty("合同业务分类path")
    private String categoryPath;
    @ApiModelProperty("合同业务分类名称")
    private String categoryName;
    @ApiModelProperty("关联主合同Id")
    private String pid;
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    @ApiModelProperty("甲方ID 支出类-取客户")
    private String partyAId;
    @ApiModelProperty("乙方ID 支出类-取供应商")
    private String partyBId;
    @ApiModelProperty("甲方名称")
    private String partyAName;
    @ApiModelProperty("乙方名称")
    private String partyBName;
    @ApiModelProperty("所属公司ID")
    private String orgId;
    @ApiModelProperty("所属部门ID")
    private String departId;
    @ApiModelProperty("所属部门名称")
    private String departName;
    @ApiModelProperty("所属公司名称")
    private String orgName;
    @ApiModelProperty("签约人名称")
    private String signPerson;
    @ApiModelProperty("签约人ID")
    private String signPersonId;
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate signDate;
    @ApiModelProperty("成本中心ID")
    private String costCenterId;
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    @ApiModelProperty("所属项目ID 来源 成本中心")
    private String communityId;
    @ApiModelProperty("所属项目名称 来源 成本中心")
    private String communityName;
    @ApiModelProperty("负责人ID")
    private String principalId;
    @ApiModelProperty("负责人名称")
    private String principalName;
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    @ApiModelProperty("保证金额")
    private BigDecimal bondAmount;
    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("范本ID")
    private String tempId;
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    @ApiModelProperty("范本的filekey")
    private String tempFilekey;
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    @ApiModelProperty("审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 ")
    private Integer reviewStatus;
    @ApiModelProperty("合同状态")
    private Integer status;
    @ApiModelProperty("租户id")
    private String tenantId;
    @ApiModelProperty("创建人")
    private String creator;
    @ApiModelProperty("创建人名称")
    private String creatorName;
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("操作人")
    private String operator;
    @ApiModelProperty("操作人名称")
    private String operatorName;
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("是否删除  0 正常 1 删除")
    private Integer deleted;

}
