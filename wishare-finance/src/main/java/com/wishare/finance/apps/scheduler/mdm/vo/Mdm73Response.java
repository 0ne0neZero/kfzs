package com.wishare.finance.apps.scheduler.mdm.vo;

import cn.hutool.core.date.DateUtil;
import com.wishare.finance.domains.mdm.entity.Mdm73E;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Data
public class Mdm73Response {

    /**
     * ID
     **/
    private String ID;
    /**
     * 账号
     **/
    private String ACCOUNTNO;
    /**
     * 中文名称
     **/
    private String ACCOUNTNAME_CHS;
    /**
     * 英文名称
     **/
    private String ACCOUNTNAME_en;
    /**
     * 繁体名称
     **/
    private String ACCOUNTNAME_cht;
    /**
     * 核算单位
     **/
    private String AccountUnit;
    /**
     * 账户性质
     **/
    private String AccountProperty;
    /**
     * 账户状态 1,未启用;2,启用;3,停用;4,已销户;5,冻结
     **/
    private String AccountStatus;
    /**
     * 所属银行字典 即2.3.11银行字典ID
     **/
    private String BANK;
    /**
     * 人行联行名称
     **/
    private String BankNameOfChina;
    /**
     * 人行联行号
     **/
    private String BankNoOfChina;
    /**
     * 市
     **/
    private String City;
    /**
     * 最后修改时间
     **/
    private String lastmodifiedtime;
    /**
     * 开户单位
     **/
    private String OpenAccountUnit;
    /**
     * 开户日期
     **/
    private String OpenDate;
    /**
     * 省份
     **/
    private String Province;
    /**
     * 国家
     **/
    private String COUNTRY;
    /**
     * 账户种类 1,内部账户;2,银行账户;3,现金账户
     **/
    private String INNEROROUTER;

    public Mdm73E transferToMdm73E() {
        Mdm73E mdm73E = new Mdm73E();
        mdm73E.setId(this.ID);
        mdm73E.setAccountNo(this.ACCOUNTNO);
        mdm73E.setAccountNameChs(this.ACCOUNTNAME_CHS);
        mdm73E.setAccountNameEn(this.ACCOUNTNAME_en);
        mdm73E.setAccountNameCht(this.ACCOUNTNAME_cht);
        mdm73E.setAccountUnit(this.AccountUnit);
        mdm73E.setAccountProperty(this.AccountProperty);
        mdm73E.setAccountStatus(this.AccountStatus);
        mdm73E.setBank(this.BANK);
        mdm73E.setBankNameOfChina(this.BankNameOfChina);
        mdm73E.setBankNoOfChina(this.BankNoOfChina);
        mdm73E.setCity(this.City);
        mdm73E.setOpenAccountUnit(this.OpenAccountUnit);
        mdm73E.setProvince(this.Province);
        mdm73E.setCountry(this.COUNTRY);
        mdm73E.setInnerOrOuter(this.INNEROROUTER);
        if (StringUtils.isNotBlank(lastmodifiedtime)){
            lastmodifiedtime = lastmodifiedtime.substring(0, 10);
            mdm73E.setLastModifiedTime(DateUtil.parse(lastmodifiedtime, "yyyy-MM-dd"));
        }
        if (StringUtils.isNotBlank(OpenDate)){
            OpenDate = OpenDate.substring(0, 10);
            mdm73E.setOpenDate(DateUtil.parse(OpenDate, "yyyy-MM-dd"));
        }
        mdm73E.setTenantId("13554968497211");
        return mdm73E;
    }


}
