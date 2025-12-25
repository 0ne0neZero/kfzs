package com.wishare.contract.apps.fo.revision.pay.report;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.domains.bo.BigDecimalConverter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/5/28 16:27
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同台账明细返回参数", description = "支出合同台账明细返回参数")
public class ContractPayReportDetailListV {
    @ApiModelProperty("合同基本信息-区域")
    private String region;
    @ApiModelProperty("合同基本信息-项目ID")
    private String communityId;
    @ApiModelProperty("合同基本信息-项目名称")
    private String communityName;
    @ApiModelProperty("合同基本信息-合同编码")
    private String contractNo;
    @ApiModelProperty("合同基本信息-合同CT码")
    private String conmaincode;
    @ApiModelProperty("合同基本信息-是否NK")
    private String isNK;
    @ApiModelProperty("合同基本信息-合同管理类别")
    private String conmanagetype;
    @ApiModelProperty("合同基本信息-四保一服")
    private String isSbyf;
    @ApiModelProperty("合同基本信息-合同名称")
    private String name;
    @ApiModelProperty("合同基本信息-供应商名称")
    private String qydws;
    @ApiModelProperty("合同基本信息-合同金额")
    private BigDecimal contractAmountNum;
    private String contractAmount;
    @ApiModelProperty("合同基本信息-合同开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    @ApiModelProperty("合同基本信息-合同结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
    @ApiModelProperty("合同基本信息-合同起止时间拼接")
    private String gmtExpireDate;
    @ApiModelProperty("合同基本信息-合同履约状态编码")
    private Integer status;
    @ApiModelProperty("合同基本信息-合同履约状态描述")
    private String statusDesc;
    @ApiModelProperty("合同基本信息-结算周期")
    private String settleCycle;
    @ApiModelProperty("合同基本信息-补充协议是否已拆分")
    private String contractSplit;
    @ApiModelProperty("项目负责人-项目负责人")
    private String projectManager;
    @ApiModelProperty("结算计划金额-结算计划金额")
    private String plannedCollectionAmount;
    private BigDecimal plannedCollectionAmountNum= BigDecimal.ZERO;
    @ApiModelProperty("已发生已结算-应结算金额")
    private String yfsyjsPlannedCollectionAmount;
    private BigDecimal yfsyjsPlannedCollectionAmountNum = BigDecimal.ZERO;
    @ApiModelProperty("已发生已结算-实际结算金额")
    private String yfsyjsActualSettlementAmount;
    private BigDecimal yfsyjsActualSettlementAmountNum= BigDecimal.ZERO;
    @ApiModelProperty("已发生已结算-扣款金额")
    private String yfsyjsDeductionAmount;
    private BigDecimal yfsyjsDeductionAmountNum= BigDecimal.ZERO;
    @ApiModelProperty("已发生未结算-未结算金额")
    private String yfswjsNoSettlementAmount;
    private BigDecimal yfswjsNoSettlementAmountNum= BigDecimal.ZERO;
    @ApiModelProperty("已发生未结算-未结算周期")
    private String yfswjsSettleCycle;
    @ApiModelProperty("已发生未结算-未结算期数合计")
    private Integer yfswjsPeriodsTotal;

    //------后端逻辑使用-------
    private Integer contractServeType;
    //合同ID
    private String contractId;
    //结算周期
    private String splitMode;
    //清单ID
    private String contractPayFundId;
    //计划最小开始时间
    private String costStartTime;
    //计划最大结束时间
    private String costEndTime;
    //计划时间跨度
    private Integer costTimeNum;
    //期数
    private Integer termDate;
    //期数
    private Integer maxTermDate;
    //YJ合同对应的原合同
    private String yjPidContractId;
    //是否YJ
    private Integer nkStatus;

}
