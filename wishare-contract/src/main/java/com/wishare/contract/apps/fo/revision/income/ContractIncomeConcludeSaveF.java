package com.wishare.contract.apps.fo.revision.income;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.alibaba.fastjson.annotation.JSONField;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
* <p>
* 收入合同订立信息表 新增请求参数，不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-28
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "收入合同订立信息表新增请求参数", description = "收入合同订立信息表新增请求参数")
public class ContractIncomeConcludeSaveF {

    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    @Length(message = "合同名称不可超过 128 个字符",max = 128)
    private String name;
    /**
    * 合同编号 不可为空
    */
    @ApiModelProperty(value = "合同编号",required = true)
    @Length(message = "合同编号不可超过 40 个字符",max = 40)
    private String contractNo;
    /**
    * 合同性质 1虚拟合同 0非虚拟合同
    */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    /**
    * 合同业务分类Id
    */
    @ApiModelProperty("合同业务分类Id")
    private Long categoryId;
    /**
    * 合同业务分类名称
    */
    @ApiModelProperty("合同业务分类名称")
    @Length(message = "合同业务分类名称不可超过 50 个字符",max = 50)
    private String categoryName;
    /**
    * 关联主合同Id
    */
    @ApiModelProperty("关联主合同Id")
    @Length(message = "关联主合同Id不可超过 50 个字符",max = 50)
    private String pid;
    /**
    * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
    */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    /**
    * 甲方ID 收入类-取客户
    */
    @ApiModelProperty("甲方ID 收入类-取客户")
    @Length(message = "甲方ID 收入类-取客户不可超过 50 个字符",max = 50)
    private String partyAId;
    /**
    * 乙方ID 收入类-取供应商
    */
    @ApiModelProperty("乙方ID 收入类-取供应商")
    @Length(message = "乙方ID 收入类-取供应商不可超过 50 个字符",max = 50)
    private String partyBId;
    /**
    * 甲方名称
    */
    @ApiModelProperty("甲方名称")
    @Length(message = "甲方名称不可超过 50 个字符",max = 50)
    private String partyAName;
    /**
    * 乙方名称
    */
    @ApiModelProperty("乙方名称")
    @Length(message = "乙方名称不可超过 50 个字符",max = 50)
    private String partyBName;
    /**
    * 所属公司ID
    */
    @ApiModelProperty("所属公司ID")
    @Length(message = "所属公司ID不可超过 50 个字符",max = 50)
    private String orgId;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    @Length(message = "所属部门ID不可超过 50 个字符",max = 50)
    private String departId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    @Length(message = "所属部门名称不可超过 50 个字符",max = 50)
    private String departName;
    /**
    * 所属公司名称
    */
    @ApiModelProperty("所属公司名称")
    @Length(message = "所属公司名称不可超过 50 个字符",max = 50)
    private String orgName;
    /**
    * 签约人名称
    */
    @ApiModelProperty("签约人名称")
    @Length(message = "签约人名称不可超过 50 个字符",max = 50)
    private String signPerson;
    /**
    * 签约人ID
    */
    @ApiModelProperty("签约人ID")
    @Length(message = "签约人ID不可超过 50 个字符",max = 50)
    private String signPersonId;
    /**
    * 签约日期
    */
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate signDate;
    /**
    * 成本中心ID
    */
    @ApiModelProperty("成本中心ID")
    @Length(message = "成本中心ID不可超过 50 个字符",max = 50)
    private String costCenterId;
    /**
    * 成本中心名称
    */
    @ApiModelProperty("成本中心名称")
    @Length(message = "成本中心名称不可超过 50 个字符",max = 50)
    private String costCenterName;
    /**
    * 所属项目ID 来源 成本中心
    */
    @ApiModelProperty("所属项目ID 来源 成本中心")
    @Length(message = "所属项目ID 来源 成本中心不可超过 40 个字符",max = 40)
    private String communityId;
    /**
    * 所属项目名称 来源 成本中心
    */
    @ApiModelProperty("所属项目名称 来源 成本中心")
    @Length(message = "所属项目名称 来源 成本中心不可超过 255 个字符",max = 255)
    private String communityName;
    /**
    * 负责人ID
    */
    @ApiModelProperty("负责人ID")
    @Length(message = "负责人ID不可超过 50 个字符",max = 50)
    private String principalId;
    /**
    * 交易类型ID 1关联交易  2非关联交易
    */
    @ApiModelProperty("交易类型ID 1关联交易  2非关联交易")
    @Length(message = "交易类型ID 1关联交易  2非关联交易不可超过 40 个字符",max = 40)
    private String dealTypeId;
    /**
    * 交易类型 1关联交易  2非关联交易
    */
    @ApiModelProperty("交易类型 1关联交易  2非关联交易")
    @Length(message = "交易类型 1关联交易  2非关联交易不可超过 50 个字符",max = 50)
    private String dealType;
    /**
    * 用印类型  1合同专用章 2公司公章
    */
    @ApiModelProperty("用印类型  1合同专用章 2公司公章")
    private Integer sealType;
    /**
    * 用印类型  1合同专用章 2公司用章
    */
    @ApiModelProperty("用印类型  1合同专用章 2公司用章")
    @Length(message = "用印类型  1合同专用章 2公司用章不可超过 50 个字符",max = 50)
    private String sealTypeName;
    /**
    * 负责人名称
    */
    @ApiModelProperty("负责人名称")
    @Length(message = "负责人名称不可超过 50 个字符",max = 50)
    private String principalName;
    /**
    * 合同金额
    */
    @ApiModelProperty("合同金额")
    @Digits(integer = 10,fraction =2,message = "合同金额不正确")
    private BigDecimal contractAmount;
    /**
    * 是否保证金 0 否 1 是
    */
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    /**
    * 保证金额
    */
    @ApiModelProperty("保证金额")
    @Digits(integer = 10,fraction =2,message = "保证金额不正确")
    private BigDecimal bondAmount;
    /**
     * 收款金额
     */
    @ApiModelProperty("收款金额")
    @Digits(integer = 10,fraction =2,message = "收款金额不正确")
    private BigDecimal collectAmount;
    /**
    * 合同开始日期
    */
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    /**
    * 合同到期日期
    */
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    /**
    * 范本ID
    */
    @ApiModelProperty("范本ID")
    private String tempId;
    /**
    * 是否倒签 0 否  1 是
    */
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    /**
    * 范本的filekey
    */
    @ApiModelProperty("范本的filekey")
    @Length(message = "范本的filekey不可超过 255 个字符",max = 255)
    private String tempFilekey;
    /**
    * 签约方式 0 新签 1 补充协议 2 续签 
    */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;
    @ApiModelProperty("合同业务线（1.物管合同；2.代建合同;3.商管合同）")
    private Integer contractBusinessLine;

}
