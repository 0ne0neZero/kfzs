package com.wishare.contract.domains.vo.revision.pay;

import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractSettFundV;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundInfoV;
import com.wishare.contract.domains.vo.revision.pay.fund.ContractPayFundV;
import com.wishare.contract.domains.vo.revision.pay.settdetails.ContractPaySettDetailsV;
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
 * @Date: 2023/7/13/15:37
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
@ApiModel(value = "支出合同结算单表视图对象", description = "支出合同结算单表视图对象")
public class ContractPaySettlementInfoV {

    @ApiModelProperty("供应商名称")
    private String merchantName;
    @ApiModelProperty("合同名称")
    private String contractName;
    @ApiModelProperty("合同编号")
    private String contractNo;
    @ApiModelProperty("结算单编号")
    private String payFundNumber;
    @ApiModelProperty("合同金额")
    private BigDecimal contractSum;
    @ApiModelProperty("变更合同金额")
    private BigDecimal changeContractSum;
    @ApiModelProperty("变更合同金额字符串")
    private String changeContractSumString;
    @ApiModelProperty("本期合同金额")
    private BigDecimal thisContractSum;
    @ApiModelProperty("本期扣款金额")
    private BigDecimal thisDeductionSum;
    @ApiModelProperty("本期汇总合同金额")
    private BigDecimal thisGathContractSum;
    @ApiModelProperty("至上期末统计")
    private BigDecimal fromContractSum;
    @ApiModelProperty("至上期末统计字符串")
    private String fromContractSumString;
    @ApiModelProperty("至上期末扣款金额统计")
    private BigDecimal fromDeductionSum;
    @ApiModelProperty("至上期末扣款金额统计字符串")
    private String fromDeductionSumString;
    @ApiModelProperty("至上期末汇总统计")
    private BigDecimal fromGathContractSum;
    @ApiModelProperty("至上期末汇总统计字符串")
    private String fromGathContractSumString;
    @ApiModelProperty("至本期末统计")
    private BigDecimal toContractSum;
    @ApiModelProperty("至本期末扣款金额统计")
    private BigDecimal toDeductionSum;
    @ApiModelProperty("至本期末汇总统计")
    private BigDecimal toGathContractSum;
    @ApiModelProperty("付款合同清单")
    private List<ContractPayFundInfoV> contractPayFundVList;
}
