package com.wishare.contract.apps.fo.revision.income;

import com.wishare.contract.infrastructure.utils.PropertyMsg;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/1/15:21
 */
public class ContractIncomeRecordF {

    /**
     * 合同名称
     */
    @PropertyMsg("合同名称")
    private String name;

    /**
     * 合同编号
     */
    @PropertyMsg("合同编号")
    private String contractNo;

    /**
     * 合同业务分类名称
     */
    @PropertyMsg("合同业务分类名称")
    private String categoryName;

    /**
     * 甲方名称
     */
    @PropertyMsg("甲方名称")
    private String partyAName;

    /**
     * 乙方名称
     */
    @PropertyMsg("乙方名称")
    private String partyBName;

    /**
     * 所属部门名称
     */
    @PropertyMsg("所属部门名称")
    private String departName;
    /**
     * 所属公司名称
     */
    @PropertyMsg("所属公司名称")
    private String orgName;
    /**
     * 签约人名称
     */
    @PropertyMsg("签约人名称")
    private String signPerson;
    /**
     * 签约日期
     */
    @PropertyMsg("签约日期")
    private LocalDate signDate;
    /**
     * 成本中心名称
     */
    @PropertyMsg("成本中心名称")
    private String costCenterName;
    /**
     * 所属项目名称 来源 成本中心
     */
    @PropertyMsg("所属项目名称")
    private String communityName;
    /**
     * 合同金额
     */
    @PropertyMsg("合同金额")
    private BigDecimal contractAmount;
    /**
     * 保证金额
     */
    @PropertyMsg("保证金额")
    private BigDecimal bondAmount;
    /**
     * 合同开始日期
     */
    @PropertyMsg("合同开始日期")
    private LocalDate gmtExpireStart;
    /**
     * 合同到期日期
     */
    @PropertyMsg("合同到期日期")
    private LocalDate gmtExpireEnd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPartyAName() {
        return partyAName;
    }

    public void setPartyAName(String partyAName) {
        this.partyAName = partyAName;
    }

    public String getPartyBName() {
        return partyBName;
    }

    public void setPartyBName(String partyBName) {
        this.partyBName = partyBName;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getSignPerson() {
        return signPerson;
    }

    public void setSignPerson(String signPerson) {
        this.signPerson = signPerson;
    }

    public LocalDate getSignDate() {
        return signDate;
    }

    public void setSignDate(LocalDate signDate) {
        this.signDate = signDate;
    }

    public String getCostCenterName() {
        return costCenterName;
    }

    public void setCostCenterName(String costCenterName) {
        this.costCenterName = costCenterName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public BigDecimal getContractAmount() {
        return contractAmount;
    }

    public void setContractAmount(BigDecimal contractAmount) {
        this.contractAmount = contractAmount;
    }

    public BigDecimal getBondAmount() {
        return bondAmount;
    }

    public void setBondAmount(BigDecimal bondAmount) {
        this.bondAmount = bondAmount;
    }

    public LocalDate getGmtExpireStart() {
        return gmtExpireStart;
    }

    public void setGmtExpireStart(LocalDate gmtExpireStart) {
        this.gmtExpireStart = gmtExpireStart;
    }

    public LocalDate getGmtExpireEnd() {
        return gmtExpireEnd;
    }

    public void setGmtExpireEnd(LocalDate gmtExpireEnd) {
        this.gmtExpireEnd = gmtExpireEnd;
    }
}
