package com.wishare.contract.domains.vo.contractset;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author hhb
 * @describe
 * @date 2025/11/6 17:47
 */
@Data
public class ContractPayCostPlanReqV {
    //项目id
    private String communityId;
    //部门ID
    private String departId;
    @ApiModelProperty( "费项ID")
    private Long chargeItemId;
    //合约规划编码
    private String accountItemCode;
    //合约规划全编码
    private String accountItemFullCode;
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

    @ApiModelProperty("不含税金额")
    private BigDecimal amountWithOutRate;
    @ApiModelProperty("扣款金额")
    private BigDecimal deduction;
    @ApiModelProperty("同清单不含税金额之和")
    private BigDecimal amountWithOutRateTotal;

    @ApiModelProperty("款项类型")
    private String typeId;
    @ApiModelProperty("付费方式ID")
    private String payWayId;
    @ApiModelProperty("税率ID")
    private String taxRateId;
    @ApiModelProperty("付费类型ID")
    private String payTypeId;
    @ApiModelProperty("收费标准金额")
    private BigDecimal standAmount;
    @ApiModelProperty("收费标准ID")
    private String standardId;


    @ApiModelProperty("合同ID")
    private String contractId;
    @ApiModelProperty("主合同ID")
    private String contractMainId;
    @ApiModelProperty("成本-分摊明细ID")
    private String cbApportionId;
    @ApiModelProperty("清单ID")
    private String payFundId;

}
