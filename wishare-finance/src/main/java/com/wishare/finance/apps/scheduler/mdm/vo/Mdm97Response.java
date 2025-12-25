package com.wishare.finance.apps.scheduler.mdm.vo;

import cn.hutool.core.date.DateUtil;
import com.wishare.finance.domains.mdm.entity.Mdm97E;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author longhuadmin
 */
@Data
public class Mdm97Response {

    private String ID;

    /**
     * 银行账户ID
     **/
    private String PARENTID;

    /**
     * 授权单位ID
     **/
    private String AUTHORIZEDUNIT;

    /**
     * 状态	1,未启用;2,启用;3,停用
     **/
    private String STARTEDSTATUS;

    /**
     * 最后更新时间 UTC
     **/
    private String lastmodifiedtime;

    public Mdm97E transfer(){
        Mdm97E mdm97E = new Mdm97E();
        mdm97E.setId(ID);
        mdm97E.setParentId(PARENTID);
        mdm97E.setAuthorizedUnit(AUTHORIZEDUNIT);
        mdm97E.setStartedStatus(STARTEDSTATUS);
        if (StringUtils.isNotBlank(lastmodifiedtime)){
            lastmodifiedtime = lastmodifiedtime.substring(0, 10);
            mdm97E.setLastModifiedTime(DateUtil.parse(lastmodifiedtime, "yyyy-MM-dd"));
        }
        mdm97E.setTenantId("13554968497211");
        return mdm97E;
    }
}
