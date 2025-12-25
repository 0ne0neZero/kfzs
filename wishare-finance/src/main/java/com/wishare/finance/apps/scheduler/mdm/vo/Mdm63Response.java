package com.wishare.finance.apps.scheduler.mdm.vo;

import cn.hutool.core.date.DateUtil;
import com.wishare.finance.domains.mdm.entity.Mdm63E;
import lombok.Data;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;


/**
 * @author longhuadmin
 */
@Data
@SuppressWarnings("all")
public class Mdm63Response {
    private String ID;
    private String BILLNUM;
    private String BIZDATE;
    private String DQRQ;
    private String ARAPMODULE;
    private String PARTNERID;
    private String PARTNERCODE;
    private String CONTRACTID;
    private String CONTRACTCODE;
    private String PROJECTINFOID;
    private String PROJECTINFOCODE;
    private String FUNDSPROPID;
    private Double CLEARAMOUNT;
    private Double DHXJE;
    private Double AMOUNT;
    private String LASTMODIFIEDTIME;
    private String FTID;
    private String ORGANIZATIONID;
    private String FUNDSPROPNAME;
    private String YBBH;
    private String BWBBH;
    private Double FOREIGNCURRENCY;
    private Double DHXJEYB;
    private Double HXJEYB;
    private String SOURCEBILLCODE;
    private String SOURCEBILLID;
    private String SUMMARY;

    public Mdm63E transferToMdm63E() {
        Mdm63E mdm63E = new Mdm63E();
        mdm63E.setId(ID);
        mdm63E.setBillNum(BILLNUM);
        if (StringUtils.isNotBlank(BIZDATE)){
            mdm63E.setBizDate(LocalDate.parse(BIZDATE));
        }
        if (StringUtils.isNotBlank(DQRQ)){
            mdm63E.setDqRq(LocalDate.parse(DQRQ));
        }
        mdm63E.setArapModule(ARAPMODULE);
        mdm63E.setPartnerId(PARTNERID);
        mdm63E.setPartnerCode(PARTNERCODE);
        mdm63E.setContractId(CONTRACTID);
        mdm63E.setContractCode(CONTRACTCODE);
        mdm63E.setProjectInfoId(PROJECTINFOID);
        mdm63E.setProjectInfoCode(PROJECTINFOCODE);
        mdm63E.setFundsPropId(FUNDSPROPID);
        if (Objects.nonNull(CLEARAMOUNT)){
            mdm63E.setClearAmount(new BigDecimal(CLEARAMOUNT));
        }
        if (Objects.nonNull(DHXJE)){
            mdm63E.setDhxJe(new BigDecimal(DHXJE));
        }
        if (Objects.nonNull(AMOUNT)){
            mdm63E.setAmount(new BigDecimal(AMOUNT));
        }
        // 2024-07-29T08:06:10.000+08:00 转date
        // 先截取 2024-07-29
        if (StringUtils.isNotBlank(LASTMODIFIEDTIME)){
            LASTMODIFIEDTIME = LASTMODIFIEDTIME.substring(0, 10);
            mdm63E.setLastModifiedTime(DateUtil.parse(LASTMODIFIEDTIME, "yyyy-MM-dd"));
        }

        mdm63E.setFtId(FTID);
        mdm63E.setOrganizationId(ORGANIZATIONID);
        mdm63E.setFundsPropName(FUNDSPROPNAME);
        mdm63E.setYbBh(YBBH);
        mdm63E.setBwBbh(BWBBH);
        if (Objects.nonNull(FOREIGNCURRENCY)){
            mdm63E.setForeignCurrency(new BigDecimal(FOREIGNCURRENCY));
        }
        if (Objects.nonNull(DHXJEYB)){
            mdm63E.setDhxJeYb(new BigDecimal(DHXJEYB));
        }
        if (Objects.nonNull(HXJEYB)){
            mdm63E.setHxJeYb(new BigDecimal(HXJEYB));
        }

        mdm63E.setSourceBillCode(SOURCEBILLCODE);
        mdm63E.setSourceBillId(SOURCEBILLID);
        mdm63E.setSummary(SUMMARY);
        return mdm63E;
    }
}
