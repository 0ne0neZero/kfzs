package com.wishare.finance.infrastructure.remote.vo.contract;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
* <p>
* 支出合同订立信息表视图对象
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同订立信息表视图对象", description = "支出合同订立信息表视图对象")
public class ContractPayConcludeV {

    /**
    * 合同id
    */
    @ApiModelProperty("合同id")
    private String id;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String name;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    private String contractNo;

    /**
     * 合同CT码
     */
    @ApiModelProperty("合同CT码")
    private String conmaincode;

    /**
     * 合同名称-合同编号
     */
    @ApiModelProperty("合同编号")
    private String nameNo;
    /**
    * 合同性质 1虚拟合同 0非虚拟合同
    */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private Integer contractNature;
    /**
     * 合同性质 1虚拟合同 0非虚拟合同
     */
    @ApiModelProperty("合同性质 1虚拟合同 0非虚拟合同")
    private String contractNatureName;
    /**
    * 合同业务分类Id
    */
    @ApiModelProperty("合同业务分类Id")
    private Long categoryId;
    /**
     * 合同业务分类path
     */
    @ApiModelProperty("合同业务分类path")
    private String categoryPath;
    /**
    * 合同业务分类名称
    */
    @ApiModelProperty("合同业务分类名称")
    private String categoryName;
    /**
    * 关联主合同Id
    */
    @ApiModelProperty("关联主合同Id")
    private String pid;
    /**
    * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
    */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private Integer contractType;
    /**
     * 合同属性0普通合同 1框架合同 2补充协议 3结算合同
     */
    @ApiModelProperty("合同属性0普通合同 1框架合同 2补充协议 3结算合同")
    private String contractTypeName;
    /**
    * 甲方ID 支出类-取客户
    */
    @ApiModelProperty("甲方ID 支出类-取客户")
    private String partyAId;
    /**
    * 乙方ID 支出类-取供应商
    */
    @ApiModelProperty("乙方ID 支出类-取供应商")
    private String partyBId;
    /**
    * 甲方名称
    */
    @ApiModelProperty("甲方名称")
    private String partyAName;
    /**
    * 乙方名称
    */
    @ApiModelProperty("乙方名称")
    private String partyBName;
    /**
    * 所属公司ID
    */
    @ApiModelProperty("所属公司ID")
    private String orgId;
    /**
    * 所属部门ID
    */
    @ApiModelProperty("所属部门ID")
    private String departId;
    /**
    * 所属部门名称
    */
    @ApiModelProperty("所属部门名称")
    private String departName;
    /**
    * 所属公司名称
    */
    @ApiModelProperty("所属公司名称")
    private String orgName;
    /**
    * 签约人名称
    */
    @ApiModelProperty("签约人名称")
    private String signPerson;
    /**
    * 签约人ID
    */
    @ApiModelProperty("签约人ID")
    private String signPersonId;
    /**
    * 签约日期
    */
    @ApiModelProperty("签约日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate signDate;
    /**
    * 成本中心ID
    */
    @ApiModelProperty("成本中心ID")
    private String costCenterId;
    /**
    * 成本中心名称
    */
    @ApiModelProperty("成本中心名称")
    private String costCenterName;
    /**
    * 所属项目ID 来源 成本中心
    */
    @ApiModelProperty("所属项目ID 来源 成本中心")
    private String communityId;
    /**
    * 所属项目名称 来源 成本中心
    */
    @ApiModelProperty("所属项目名称 来源 成本中心")
    private String communityName;
    /**
    * 负责人ID
    */
    @ApiModelProperty("负责人ID")
    private String principalId;
    /**
    * 负责人名称
    */
    @ApiModelProperty("负责人名称")
    private String principalName;
    /**
    * 合同金额
    */
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    /**
     * 变更后合同履约金额
     */
    @ApiModelProperty("变更后合同履约金额")
    private BigDecimal changContractAmount;
    /**
    * 是否保证金 0 否 1 是
    */
    @ApiModelProperty("是否保证金 0 否 1 是")
    private Boolean bond;
    /**
    * 保证金额
    */
    @ApiModelProperty("保证金额")
    private BigDecimal bondAmount;
    /**
    * 付款金额
    */
    @ApiModelProperty("付款金额")
    private BigDecimal payAmount;
    /**
     * 未付款金额
     */
    @ApiModelProperty("未付款金额")
    private BigDecimal unPayAmount;
    /**
     * 未收票金额
     */
    @ApiModelProperty("未收票金额")
    private BigDecimal unInvoiceAmount;
    /**
     * 扣款金额
     */
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    /**
    * 合同开始日期
    */
    @ApiModelProperty("合同开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    /**
    * 合同到期日期
    */
    @ApiModelProperty("合同到期日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    /**
    * 范本ID
    */
    @ApiModelProperty("范本ID")
    private String tempId;
    /**
     * 区域
     */
    @ApiModelProperty("区域")
    private String region;
    /**
    * 是否倒签 0 否  1 是
    */
    @ApiModelProperty("是否倒签 0 否  1 是")
    private Integer isBackDate;
    /**
    * 范本的filekey
    */
    @ApiModelProperty("范本的filekey")
    private String tempFilekey;
    /**
    * 签约方式 0 新签 1 补充协议 2 续签 
    */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private Integer signingMethod;
    /**
     * 签约方式 0 新签 1 补充协议 2 续签
     */
    @ApiModelProperty("签约方式 0 新签 1 补充协议 2 续签 ")
    private String signingMethodName;
    /**
    * 合同预警状态 0正常 1 临期 2 已到期
    */
    @ApiModelProperty("合同预警状态 0正常 1 临期 2 已到期")
    private Integer warnState;
    /**
    * 审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 
    */
    @ApiModelProperty("审核状态 0 待提交 1 审批中  2 已通过 3 已拒绝 ")
    private Integer reviewStatus;
    @ApiModelProperty("合同状态  0 待提交， 1 审批中， 2 已拒绝， 3 未开始， 4 执行中， 5 已到期， 6 已终止")
    private Integer status;
    /**
    * 租户id
    */
    @ApiModelProperty("租户id")
    private String tenantId;
    /**
    * 创建人
    */
    @ApiModelProperty("创建人")
    private String creator;
    /**
    * 创建人名称
    */
    @ApiModelProperty("创建人名称")
    private String creatorName;
    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtCreate;
    /**
    * 操作人
    */
    @ApiModelProperty("操作人")
    private String operator;
    /**
    * 操作人名称
    */
    @ApiModelProperty("操作人名称")
    private String operatorName;
    /**
    * 操作时间
    */
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime gmtModify;
    /**
     * 是否展示编辑按钮
     */
    @ApiModelProperty("是否展示编辑按钮")
    private Boolean showBtnEdit;
    /**
     * 是否展示变更按钮
     */
    @ApiModelProperty("是否展示变更按钮")
    private Boolean showBtnChange;
    /**
     * 是否展示续签按钮
     */
    @ApiModelProperty("是否展示续签按钮")
    private Boolean showBtnRenewal;
    /**
     * 是否展示终止按钮
     */
    @ApiModelProperty("是否展示终止按钮")
    private Boolean showBtnEnd;
    /**
     * 是否展示推送
     */
    @ApiModelProperty("是否展示推送")
    private Boolean showBtnPull;
    /**
     * 是否展示删除按钮
     */
    @ApiModelProperty("是否展示删除按钮")
    private Boolean showBtnDelete;
    /**
     * 是否展示变更记录按钮
     */
    @ApiModelProperty("是否展示变更记录按钮")
    private Boolean showBtnChangeRecord;
    /**
     * 是否展示反审按钮
     */
    @ApiModelProperty("是否展示反审按钮")
    private Boolean showBtnBackReview;




    @ApiModelProperty("对方单位1-ID")
    private String oppositeOneId;
    @ApiModelProperty("对方单位1-名称")
    private String oppositeOne;
    @ApiModelProperty("对方单位2-ID")
    private String oppositeTwoId;
    @ApiModelProperty("对方单位2-名称")
    private String oppositeTwo;
    @ApiModelProperty("我方单位-ID")
    private String ourPartyId;
    @ApiModelProperty("我方单位-名称")
    private String ourParty;
    @ApiModelProperty("币种编码")
    private String currencyCode;
    @ApiModelProperty("币种")
    private String currency;
    @ApiModelProperty("汇率")
    private String exRate;
    @ApiModelProperty("合同总额 原币含税")
    private BigDecimal contractAmountOriginalRate;
    @ApiModelProperty("合同总额 原币不含税")
    private BigDecimal contractAmountOriginal;
    @ApiModelProperty("合同总额 本币含税")
    private BigDecimal contractAmountLocalRate;
    @ApiModelProperty("合同总额 本币不含税")
    private BigDecimal contractAmountLocal;
    @ApiModelProperty("结算金额 原币含税")
    private BigDecimal settleAmountRate;
    @ApiModelProperty("结算金额 原币不含税")
    private BigDecimal settleAmount;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceAmount;
    @ApiModelProperty("是否为反审(0 否   1 是)")
    private Integer isBackReview;
    /**
     * 终止日期
     */
    @ApiModelProperty("终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @JSONField(format = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @ApiModelProperty("未计划金额")
    private BigDecimal noPlanAmount;

    @ApiModelProperty("未计划金额损益")
    private BigDecimal noPlanAmountLoss;

    @ApiModelProperty("合同承办人姓名")
    private String conundertaker;

    @ApiModelProperty("变更后合同金额字符串")
    private String changContractAmountPj;

    @ApiModelProperty("结算周期")
    private String splitMode;

}
