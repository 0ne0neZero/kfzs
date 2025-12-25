package com.wishare.contract.apps.fo.revision.income;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.income.settdetails.ContractIncomeSettDetailsUpdateF;
import com.wishare.contract.apps.fo.revision.income.settlement.ContractIncomeSettlementPeriodF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPayConcludeSettdeductionUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settdetails.ContractPaySettDetailsUpdateF;
import com.wishare.contract.apps.fo.revision.pay.settlement.ContractPaySettlementPeriodF;
import com.wishare.tools.starter.vo.FileVo;
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
import java.util.List;

/**
* <p>
* 支出合同订立信息表 更新请求参数 不会跟着表结构更新而更新
* </p>
*
* @author chenglong
* @since 2023-06-25
*/
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同结算单信息表更新请求参数", description = "支出合同结算单信息表")
public class ContractIncomeSettlementConcludeUpdateF {

    @ApiModelProperty("保存类型")
    private String saveType;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("合同id")
    private String contractId;

    @ApiModelProperty("申请付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate plannedCollectionTime;

    @ApiModelProperty("付款类型  0有票付款  1无票付款")
    private Integer paymentType;

    @ApiModelProperty("付款方式  0现金  1银行转帐  2支票")
    private Integer paymentMethod;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("合同名称")
    private String contractName;

    @ApiModelProperty("客户名称")
    private String customer;

    @ApiModelProperty("V2.12-计量周期")
    private List<ContractIncomeSettlementPeriodF> contractIncomeSettlementPeriodSaveFList;

    @ApiModelProperty("V2.12-成本预估计划id")
    private List<String> planIdList;

    @ApiModelProperty("V2.12-期数")
    private String termDate;

    @ApiModelProperty("V2.12-项目类型 0：住宅项目 1：非住宅项目")
    private Integer communityType;

    @ApiModelProperty("V2.12-结算单标题-V2.12")
    private String title;

    @ApiModelProperty("V2.12-所属区域 0总部 1华北区域 2华南区域 3华东区域 4西部区域 5华中区域")
    private Integer belongRegion;

    @ApiModelProperty("V2.12-确认类型 0中期确收 1最终确收")
    private Integer settlementType;

    @ApiModelProperty("V2.12-确收分类 0基础物管&非业主增值 1业主增值 2综合业务")
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

    @ApiModelProperty("附件(前端传数组)")
    private List<FileVo> attachments;

    @ApiModelProperty("结算明细")
    private List<ContractIncomeSettDetailsUpdateF> contractIncomeSettDetailsSaveFList;

    @ApiModelProperty("结算扣款明细")
    private List<ContractIncomeConcludeSettdeductionUpdateF> contractIncomeConcludeSettdeductionSaveFList;
}
