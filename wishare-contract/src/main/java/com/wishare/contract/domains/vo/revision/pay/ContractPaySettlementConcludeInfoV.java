package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/7/7/10:12
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同结算单表视图对象V", description = "支出合同结算单表视图对象V")
public class ContractPaySettlementConcludeInfoV {

    @ApiModelProperty("主键")
    private String id;
    @ApiModelProperty("结算单编号")
    private String payFundNumber;
    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编码")
    private String contractNo;
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同金额")
    private BigDecimal contractAmount;
    @ApiModelProperty("期数")
    private String termDate;
    @ApiModelProperty("计划付款日期")
    private LocalDate plannedCollectionTime;
    @ApiModelProperty("计划付款金额")
    private BigDecimal plannedCollectionAmount;
    @ApiModelProperty("结算时间")
    private LocalDateTime paymentDate;
    @ApiModelProperty("结算金额")
    private BigDecimal settlementAmount;
    @ApiModelProperty("扣款金额")
    private BigDecimal deductionAmount;
    @ApiModelProperty("付款状态")
    private Integer paymentStatus;
    @ApiModelProperty("付款状态名称")
    private String paymentStatusName;
    @ApiModelProperty("结算状态")
    private Integer settleStatus;
    @ApiModelProperty("结算状态名称")
    private String settleStatusName;
    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;
    @ApiModelProperty("收票金额")
    private BigDecimal invoiceApplyAmount;
    @ApiModelProperty("收票状态")
    private Integer invoiceStatus;
    @ApiModelProperty("收票状态名称")
    private String invoiceStatusName;
    @ApiModelProperty("付款金额")
    private String paymentAmount;
    @ApiModelProperty("计划状态")
    private Integer planStatus;
    @ApiModelProperty("审核状态")
    private Integer reviewStatus;
    @ApiModelProperty("创建人")
    private String creatorName;
    @ApiModelProperty("创建时间")
    private LocalDateTime gmtCreate;
    @ApiModelProperty("更新时间")
    private LocalDateTime gmtModify;

    @ApiModelProperty("V2.12-备注")
    private String remark;

    @ApiModelProperty("V2.12-项目类型 0：住宅项目 1：非住宅项目")
    private Integer communityType;

    @ApiModelProperty("V2.12-结算单标题-V2.12")
    private String title;

    @ApiModelProperty("V2.12-所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域")
    private Integer belongRegion;

    @ApiModelProperty("V2.12-结算类型 0中期结算 1最终结算")
    private Integer settlementType;

    @ApiModelProperty("V2.12-结算分类 0工程类 1秩序类(保安) " +
            "2环境类(保洁、绿化、垃圾清运、不含化粪池清掏) " +
            "3消防维保 4电梯维保 5设备设施维修 6案场类 " +
            "7智能化、信息化 8房屋租赁类 9劳务派遣类 10增值类 11猎头委托、培训类 12其他")
    private Integer settlementClassify;

    @ApiModelProperty("V2.12-增值类型 0空间运营 1到家服务 2零售业务 3美居业务 4资产业务 5餐饮业务 6业态运营")
    private Integer additionType;

    @ApiModelProperty("V2.12-所属层级 0项目 1区域公司")
    private Integer belongLevel;

    @ApiModelProperty(value = "V2.12-实际结算金额")
    private BigDecimal actualSettlementAmount;

    @ApiModelProperty(value = "V2.12-本期结算百分比")
    private String currentSettleRatio;

    @ApiModelProperty(value = "V2.12-结算审批完成时间")
    private LocalDateTime approveCompletedTime;

    @ApiModelProperty(value = "V2.12-累计结算金额")
    private BigDecimal totalSettledAmount;

    @ApiModelProperty(value = "V2.12-累计结算百分比")
    private String totalSettledRatio;
}
