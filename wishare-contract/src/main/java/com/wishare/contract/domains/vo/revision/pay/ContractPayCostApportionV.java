package com.wishare.contract.domains.vo.revision.pay;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author hhb
 * @describe
 * @date 2025/11/7 11:38
 */
@Data
public class ContractPayCostApportionV {
    //成本-费项编码
    private String accountItemCode;
    //成本-费项名称
    private String accountItemName;
    //成本-费项全码
    private String accountItemFullCode;
    //成本-费项全称
    private String accountItemFullName;
    //成本-本次分摊金额
    private BigDecimal apportionAmount;
    //成本-合约规划可用金额
    private BigDecimal summarySurplusAmount;

    @ApiModelProperty(name = "项目GUID", notes = "项目GUID")
    private String projectGuid;
    @ApiModelProperty(name = "地区公司ID", notes = "地区公司ID")
    private String buGuid;
    @ApiModelProperty(name = "业务线GUID", notes = "业务线GUID")
    private String businessGuid;
    @ApiModelProperty(name = "业务单元编码", notes = "业务单元编码")
    private String businessUnitCode;
    //成本分摊明细
    Map<String, List<ContractPayCostApportionDetailV>> apportionMap;
    //成本管控和方式
    Map<String, String> costControlTypeMap;

}
