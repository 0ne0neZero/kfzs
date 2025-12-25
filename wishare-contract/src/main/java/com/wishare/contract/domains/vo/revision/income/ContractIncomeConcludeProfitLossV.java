package com.wishare.contract.domains.vo.revision.income;

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
* 合同收入损益表视图对象
* </p>
*
* @author chenglong
* @since 2023-10-24
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "合同收入损益表视图对象", description = "合同收入损益表视图对象")
public class ContractIncomeConcludeProfitLossV {

    /**
    * 主键ID
    */
    @ApiModelProperty("主键ID")
    private String id;
    /**
    * 关联合同ID
    */
    @ApiModelProperty("关联合同ID")
    private String contractId;
    /**
    * 收款计划编号
    */
    @ApiModelProperty("收款计划编号")
    private String payNotecode;
    /**
    * 客户id
    */
    @ApiModelProperty("客户id")
    private String customer;
    /**
    * 客户名称
    */
    @ApiModelProperty("客户名称")
    private String customerName;
    /**
    * 合同编号
    */
    @ApiModelProperty("合同编号")
    private String contractNo;
    /**
    * 合同名称
    */
    @ApiModelProperty("合同名称")
    private String contractName;
    /**
    * 期数
    */
    @ApiModelProperty("期数")
    private Integer termDate;
    /**
    * 计划收付款时间
    */
    @ApiModelProperty("计划收付款时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private LocalDate plannedCollectionTime;
    /**
    * 计划收付款金额
    */
    @ApiModelProperty("计划收付款金额")
    private BigDecimal plannedCollectionAmount;
    /**
    * 结算金额
    */
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    /**
    * 减免金额
    */
    @ApiModelProperty("减免金额")
    private BigDecimal deductionAmount;
    /**
    * 开票金额
    */
    @ApiModelProperty("开票金额")
    private BigDecimal invoiceApplyAmount;
    /**
    * 收款金额
    */
    @ApiModelProperty("收款金额")
    private BigDecimal receiptAmount;
    /**
    * 未收款金额
    */
    @ApiModelProperty("未收款金额")
    private BigDecimal noReceiptAmount;
    /**
    * 计划状态 0待提交  1未完成  2已完成
    */
    @ApiModelProperty("计划状态 0待提交  1未完成  2已完成")
    private Integer planStatus;
    /**
    * 结算状态 0未结算  1未完成  2已完成
    */
    @ApiModelProperty("结算状态 0未结算  1未完成  2已完成")
    private Integer paymentStatus;
    /**
    * 开票状态 0未完成  1已完成
    */
    @ApiModelProperty("开票状态 0未完成  1已完成")
    private Integer invoiceStatus;
    /**
    * 审核状态0待提交1审批中2已通过3已拒绝
    */
    @ApiModelProperty("审核状态0待提交1审批中2已通过3已拒绝")
    private Integer reviewStatus;
    /**
    * 部门名称
    */
    @ApiModelProperty("部门名称")
    private String departName;
    /**
    * 拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)
    */
    @ApiModelProperty("拆分方式(一次性:1 按年:2 按半年:3 按季度:4 按月:5)")
    private Integer splitMode;
    /**
    * 费项
    */
    @ApiModelProperty("费项")
    private String chargeItem;
    /**
    * 费项ID
    */
    @ApiModelProperty("费项ID")
    private String chargeItemId;
    /**
    * 税率ID,额外需要维护，不展示
    */
    @ApiModelProperty("税率ID,额外需要维护，不展示")
    private Long taxRateId;
    /**
    * 税率
    */
    @ApiModelProperty("税率")
    private String taxRate;
    /**
    * 不含税金额
    */
    @ApiModelProperty("不含税金额")
    private BigDecimal noTaxAmount;
    /**
    * 税额
    */
    @ApiModelProperty("税额")
    private BigDecimal taxAmount;
    /**
    * 备注
    */
    @ApiModelProperty("备注")
    private String remark;
    /**
    * 第几笔
    */
    @ApiModelProperty("第几笔")
    private Integer howOrder;
    /**
    * 金额比例
    */
    @ApiModelProperty("金额比例")
    private BigDecimal ratioAmount;
    /**
    * 服务类型
    */
    @ApiModelProperty("服务类型")
    private Integer serviceType;
    /**
    * 未计划金额
    */
    @ApiModelProperty("未计划金额")
    private BigDecimal noPlanAmount;
    /**
    * 税率路径
    */
    @ApiModelProperty("税率路径")
    private String taxRateIdPath;
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
     * 成本中心
     */
    @ApiModelProperty("成本中心")
    private String costCenterName;

    /**
     * 收入状态
     */
    @ApiModelProperty("收入状态")
    private Integer acceptStatus;

    /**
     * 收入确认金额(元)
     */
    @ApiModelProperty("收入确认金额(元)")
    private BigDecimal acceptAmount;

}
