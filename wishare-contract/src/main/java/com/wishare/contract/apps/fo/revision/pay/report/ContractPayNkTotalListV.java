package com.wishare.contract.apps.fo.revision.pay.report;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.wishare.contract.domains.vo.revision.pay.ContractPayYjSubListV;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * @author hhb
 * @describe
 * @date 2025/8/14 23:47
 */
@Getter
@Setter
public class ContractPayNkTotalListV {

    //区域名称
    private String region;
    //合同id
    private String contractId;
    //合同名称
    private String name;
    //合同编码
    private String contractNo;
    //原合同金额
    private BigDecimal contractAmount;
    //所属项目ID
    private String communityId;
    //所属项目名称
    private String communityName;
    //补充合同名称
    private String bcContractName;
    //补充合同金额
    private BigDecimal bcContractAmount;
    //HY补充合同名称
    private String hyBcContractName;
    //HY补充合同Id
    private String hyBcContractId;
    //HY补充合同金额
    private BigDecimal hyBcContractAmount;
    //NK-已结算周期
    private String nkPperiodsDetail;
    //NK-累计结算金额
    private BigDecimal nkTotalSettlementAmount;
    //YJ-已结算周期
    private String yjPperiodsDetail;
    //YJ-累计结算金额
    private BigDecimal yjTotalSettlementAmount;
    //YJ-累计金额
    private BigDecimal yjTotalAmount;
    //NK-累计金额
    private BigDecimal nkTotaltAmount;
    //累计结算差额
    private BigDecimal ceTotalSettlementAmount;

    //后端计算逻辑使用
    private String nkContractId;
    //合同起始日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireStart;
    //合同终止日期
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JSONField(format = "yyyy-MM-dd")
    private LocalDate gmtExpireEnd;
}
