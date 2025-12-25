package com.wishare.finance.domains.mdm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author longhuadmin
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("mdm73")
public class Mdm73E {

    /**
     * id
     **/
    private String id;

    /**
     * 账号
     **/
    private String accountNo;
    /**
     * 中文名称
     **/
    private String accountNameChs;
    /**
     * 英文名称
     **/
    private String accountNameEn;
    /**
     * 繁体名称
     **/
    private String accountNameCht;
    /**
     * 核算单位
     **/
    private String accountUnit;
    /**
     * 账户性质
     **/
    private String accountProperty;
    /**
     * 账户状态 1,未启用;2,启用;3,停用;4,已销户;5,冻结
     **/
    private String accountStatus;
    /**
     * 所属银行字典 即2.3.11银行字典ID
     **/
    private String bank;
    /**
     * 人行联行名称
     **/
    private String bankNameOfChina;
    /**
     * 人行联行号
     **/
    private String bankNoOfChina;
    /**
     * 市
     **/
    private String city;
    /**
     * 最后修改时间
     **/
    private Date lastModifiedTime;
    /**
     * 开户单位
     **/
    private String openAccountUnit;
    /**
     * 开户日期
     **/
    private Date openDate;
    /**
     * 省份
     **/
    private String province;
    /**
     * 国家
     **/
    private String country;

    /**
     * 账户种类 1,内部账户;2,银行账户;3,现金账户
     **/
    private String innerOrOuter;

    private String tenantId;
}
