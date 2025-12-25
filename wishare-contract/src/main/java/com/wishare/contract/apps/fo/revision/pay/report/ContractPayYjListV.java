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
public class ContractPayYjListV {

    //区域名称
    private String region;
    //所属部门ID
    private String departId;
    //所属部门ID
    private String departName;
    //所属项目ID
    private String communityId;
    //所属项目名称
    private String communityName;
    //合同id
    private String contractId;
    //合同名称
    private String name;
    //合同编码
    private String contractNo;
    //补充合同编码
    //private String bcContractNo;
    //合同类别
    private String conmanagetype;
    //四保一服
    private String isSbyf;
    //供应商ID
    private String merchant;
    //供应商名称
    private String merchantName;
    //我方单位Id
    private String ourPartyId;
    //我方单位-名称
    private String ourParty;
    //合同主要内容
    //private String content;
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
    //原合同金额
    private BigDecimal contractAmount;
    //变更后合同金额
    private BigDecimal changContractAmount;
    //YJ后合同金额
    //private BigDecimal yjContractAmount;
    //合同履约状态
    private Integer status;
    //合同履约状态描述
    private String statusDesc;
    private List<ContractPayYjSubListV> children;

    private String nkContractId;
    //HY补充协议名称
    private String hyContractName;
    //HY金额
    private BigDecimal hyContractAmount;

    //周期明细
    private String periodsDetail;
    //原合同-本期结算金额
    private BigDecimal bqSettlementAmount;
    //原合同-累计结算金额
    private BigDecimal totalSettlementAmount;
    //YJ后-本期结算金额
    private BigDecimal yjBqSettlementAmount;
    //YJ后-累计结算金额
    private BigDecimal yjTotalSettlementAmount;
    //差额-本期结算金额
    private BigDecimal ceBqSettlementAmount;
    //差额-累计结算金额
    private BigDecimal ceTotalSettlementAmount;
}
