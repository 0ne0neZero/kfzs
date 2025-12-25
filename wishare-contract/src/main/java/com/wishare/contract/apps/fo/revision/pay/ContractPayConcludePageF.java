package com.wishare.contract.apps.fo.revision.pay;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* 支出合同订立信息表 分页请求参数
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息表分页请求参数", description = "支出合同订立信息表")
public class ContractPayConcludePageF {

    /**
    * name
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;
    /**
    * contractNo
    */
    @ApiModelProperty("合同编号")
    @Length(message = "合同编号不可超过 40 个字符",max = 40)
    private String contractNo;
    /**
    * contractNature
    */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    /**
    * categoryId
    */
    @ApiModelProperty("合同业务分类Id")
    private Long categoryId;
    /**
     * 合同业务分类path
     */
    @ApiModelProperty("合同业务分类path")
    private String categoryPath;
    /**
    * categoryName
    */
    @ApiModelProperty("合同业务分类名称")
    @Length(message = "合同业务分类名称不可超过 50 个字符",max = 50)
    private String categoryName;
    /**
    * pid
    */
    @ApiModelProperty("关联主合同Id")
    @Length(message = "关联主合同Id不可超过 50 个字符",max = 50)
    private String pid;
    /**
    * contractType
    */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    /**
    * partyAId
    */
    @ApiModelProperty("甲方ID 支出类-取客户")
    @Length(message = "甲方ID 支出类-取客户不可超过 50 个字符",max = 50)
    private String partyAId;
    /**
    * partyBId
    */
    @ApiModelProperty("乙方ID 支出类-取供应商")
    @Length(message = "乙方ID 支出类-取供应商不可超过 50 个字符",max = 50)
    private String partyBId;
    /**
    * partyAName
    */
    @ApiModelProperty("甲方名称")
    @Length(message = "甲方名称不可超过 50 个字符",max = 50)
    private String partyAName;
    /**
    * partyBName
    */
    @ApiModelProperty("乙方名称")
    @Length(message = "乙方名称不可超过 50 个字符",max = 50)
    private String partyBName;
    /**
    * orgId
    */
    @ApiModelProperty("所属公司ID")
    @Length(message = "所属公司ID不可超过 50 个字符",max = 50)
    private String orgId;
    /**
    * departId
    */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 50 个字符",max = 50)
    private String departId;
    /**
    * departName
    */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String departName;
    /**
    * orgName
    */
    @ApiModelProperty("所属公司名称")
    @Length(message = "所属公司名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
    * signPerson
    */
    @ApiModelProperty("签约人名称")
    @Length(message = "签约人名称不可超过 50 个字符",max = 50)
    private String signPerson;
    /**
    * signPersonId
    */
    @ApiModelProperty("签约人ID")
    @Length(message = "签约人ID不可超过 50 个字符",max = 50)
    private String signPersonId;
    /**
    * signDate
    */
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate signDate;
    /**
    * costCenterId
    */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
    * costCenterName
    */
    @ApiModelProperty("成本中心名称")
    @Length(message = "成本中心名称不可超过 50 个字符",max = 50)
    private String costCenterName;
    /**
    * communityId
    */
    @ApiModelProperty("所属项目ID 来源 成本中心")
    @Length(message = "所属项目ID 来源 成本中心不可超过 40 个字符",max = 40)
    private String communityId;
    /**
    * communityName
    */
    @ApiModelProperty("所属项目名称 来源 成本中心")
    @Length(message = "所属项目名称 来源 成本中心不可超过 255 个字符",max = 255)
    private String communityName;
    /**
    * principalId
    */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 50 个字符",max = 50)
    private String principalId;
    /**
    * principalName
    */
    @ApiModelProperty("负责人名称")
    @Length(message = "负责人名称不可超过 50 个字符",max = 50)
    private String principalName;
    /**
    * contractAmount
    */
    @ApiModelProperty("合同金额")
    @Digits(integer = 10,fraction =2,message = "合同金额不正确")
    private BigDecimal contractAmount;
    /**
    * bond
    */
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    /**
    * bondAmount
    */
    @ApiModelProperty("保证金额")
    @Digits(integer = 10,fraction =2,message = "保证金额不正确")
    private BigDecimal bondAmount;
    /**
    * payAmount
    */
    @ApiModelProperty("付款金额")
    @Digits(integer = 10,fraction =2,message = "付款金额不正确")
    private BigDecimal payAmount;
    /**
    * gmtExpireStart
    */
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    /**
    * gmtExpireEnd
    */
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    /**
    * tempId
    */
    @ApiModelProperty("范本ID")
    private String tempId;
    /**
    * isBackDate
    */
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    /**
    * tempFilekey
    */
    @ApiModelProperty("范本的filekey")
    @Length(message = "范本的filekey不可超过 255 个字符",max = 255)
    private String tempFilekey;
    /**
    * signingMethod
    */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;
    /**
    * warnState
    */
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    /**
    * reviewStatus
    */
    @ApiModelProperty("审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 ")
    private Integer reviewStatus;
    /**
    * tenantId
    */
    @ApiModelProperty("租户id")
    @Length(message = "租户id不可超过 40 个字符",max = 40)
    private String tenantId;
    /**
    * creator
    */
    @ApiModelProperty("创建人")
    @Length(message = "创建人不可超过 40 个字符",max = 40)
    private String creator;
    /**
    * creatorName
    */
    @ApiModelProperty("创建人名称")
    @Length(message = "创建人名称不可超过 40 个字符",max = 40)
    private String creatorName;
    /**
    * gmtCreate
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * operator
    */
    @ApiModelProperty("操作人")
    @Length(message = "操作人不可超过 40 个字符",max = 40)
    private String operator;
    /**
    * operatorName
    */
    @ApiModelProperty("操作人名称")
    @Length(message = "操作人名称不可超过 40 个字符",max = 40)
    private String operatorName;
    /**
    * gmtModify
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    @ApiModelProperty("需要查询返回的字段，不传时返回全部，可选字段列表如下"
        + "[\"id\",\"name\",\"contractNo\",\"contractNature\",\"categoryId\",\"categoryName\",\"pid\",\"contractType\",\"partyAId\",\"partyBId\",\"partyAName\",\"partyBName\",\"orgId\",\"departId\",\"departName\",\"orgName\",\"signPerson\",\"signPersonId\",\"signDate\",\"costCenterId\",\"costCenterName\",\"communityId\",\"communityName\",\"principalId\",\"principalName\",\"contractAmount\",\"bond\",\"bondAmount\",\"payAmount\",\"gmtExpireStart\",\"gmtExpireEnd\",\"tempId\",\"isBackDate\",\"tempFilekey\",\"signingMethod\",\"warnState\",\"reviewStatus\",\"tenantId\",\"creator\",\"creatorName\",\"gmtCreate\",\"operator\",\"operatorName\",\"gmtModify\",\"deleted\"]"
        + "id 合同id"
        + "name 合同名称"
        + "contractNo 合同编号"
        + "contractNature 合同性质 1虚拟合同 0非虚拟合同"
        + "categoryId 合同业务分类Id"
        + "categoryName 合同业务分类名称"
        + "pid 关联主合同Id"
        + "contractType 合同属性0普通合同 1框架合同 2补充协议 3结算合同"
        + "partyAId 甲方ID 支出类-取客户"
        + "partyBId 乙方ID 支出类-取供应商"
        + "partyAName 甲方名称"
        + "partyBName 乙方名称"
        + "orgId 所属公司ID"
        + "departId 所属部门ID"
        + "departName 所属部门名称"
        + "orgName 所属公司名称"
        + "signPerson 签约人名称"
        + "signPersonId 签约人ID"
        + "signDate 签约日期"
        + "costCenterId 成本中心ID"
        + "costCenterName 成本中心名称"
        + "communityId 所属项目ID 来源 成本中心"
        + "communityName 所属项目名称 来源 成本中心"
        + "principalId 负责人ID"
        + "principalName 负责人名称"
        + "contractAmount 合同金额"
        + "bond 是否保证金 0 否 1 是"
        + "bondAmount 保证金额"
        + "payAmount 付款金额"
        + "gmtExpireStart 合同开始日期"
        + "gmtExpireEnd 合同到期日期"
        + "tempId 范本ID"
        + "isBackDate 是否倒签 0 否  1 是"
        + "tempFilekey 范本的filekey"
        + "signingMethod 签约方式 0 新签 1 补充协议 2 续签 "
        + "warnState 合同预警状态 0正常 1 临期 2 已到期"
        + "reviewStatus 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 "
        + "tenantId 租户id"
        + "creator 创建人"
        + "creatorName 创建人名称"
        + "gmtCreate 创建时间"
        + "operator 操作人"
        + "operatorName 操作人名称"
        + "gmtModify 操作时间"
        + "deleted 是否删除  0 正常 1 删除")
    private List<String> fields;


}
