package com.wishare.contract.apps.fo.revision.pay.report;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/5/28 14:51
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同台账返回参数", description = "支出合同台账返回参数")
public class ContractPayReportListV {
    @ApiModelProperty("区域")
    private String region;
    @ApiModelProperty("汇总-未结算金额")
    private String totalNoSettlementAmount;
    private BigDecimal totalNoSettlementAmountNum = BigDecimal.ZERO;
    @ApiModelProperty("汇总-合同数量")
    private int totalContractNum = 0;
    @ApiModelProperty("汇总-合同数量对应合同")
    private List<String> contractList = new ArrayList<>();
    @ApiModelProperty("汇总-项目数量")
    private Long totalProjectNum = 0L;
    @ApiModelProperty("汇总-供方数量")
    private Long totalSupplierNum =  0L;
    @ApiModelProperty("汇总-补充协议未拆分数量")
    private Long totalNoSplitNum =  0L;
    @ApiModelProperty("汇总-补充协议未拆分数量对应合同")
    private List<String> contractNoSplitList = new ArrayList<>();
    @ApiModelProperty("四保一服-未结算金额")
    private String sbyfNoSettlementAmount;
    private BigDecimal sbyfNoSettlementAmountNum = BigDecimal.ZERO;
    @ApiModelProperty("四保一服-合同数量")
    private Integer sbyfContractNum = 0;
    @ApiModelProperty("四保一服-合同数量对应合同")
    private List<String> sbyfContractList = new ArrayList<>();
    @ApiModelProperty("四保一服-项目数量")
    private Long sbyfProjectNum = 0L;
    @ApiModelProperty("四保一服-供方数量")
    private Long sbyfSupplierNum = 0L;
    @ApiModelProperty("四保一服(超3个月及以上)-未结算金额")
    private String sbyfThreeNoSettlementAmount;
    private BigDecimal sbyfThreeNoSettlementAmountNum = BigDecimal.ZERO;
    @ApiModelProperty("四保一服(超3个月及以上)--合同数量")
    private Integer sbyfThreeContractNum = 0;
    @ApiModelProperty("四保一服(超3个月及以上)-合同数量对应合同")
    private List<String> sbyfContractThreeList = new ArrayList<>();
    @ApiModelProperty("四保一服(超3个月及以上)--项目数量")
    private Long sbyfThreeProjectNum = 0L;
    @ApiModelProperty("四保一服(超3个月及以上)--供方数量")
    private Long sbyfThreeSupplierNum = 0L;
    @ApiModelProperty("四保一服(超5个月及以上)-未结算金额")
    private String sbyfFiveNoSettlementAmount;
    private BigDecimal sbyfFiveNoSettlementAmountNum = BigDecimal.ZERO;
    @ApiModelProperty("四保一服(超5个月及以上)--合同数量")
    private Integer sbyfFiveContractNum = 0;
    @ApiModelProperty("四保一服(超5个月及以上)-合同数量对应合同")
    private List<String> sbyfContractFiveList = new ArrayList<>();
    @ApiModelProperty("四保一服(超5个月及以上)--项目数量")
    private Long sbyfFiveProjectNum = 0L;
    @ApiModelProperty("四保一服(超5个月及以上)--供方数量")
    private Long sbyfFiveSupplierNum = 0L;
}
