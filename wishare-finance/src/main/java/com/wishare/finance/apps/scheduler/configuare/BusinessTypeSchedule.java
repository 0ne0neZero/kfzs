package com.wishare.finance.apps.scheduler.configuare;

import com.wishare.finance.apps.model.configure.chargeitem.fo.SyncF;
import com.wishare.finance.domains.configure.chargeitem.service.AssisteAccountDomainService;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author xujian
 * @date 2022/12/7
 * @Description:
 */
//@Component
//@RefreshScope
public class BusinessTypeSchedule {


    //@Value("#{'${ncc.business.tenants}'.split(',')}")
    //private List<String> tenantIds;
    //
    //@Autowired
    //private AssisteAccountDomainService assisteAccountDomainService;
    //
    //@XxlJob("businessTypeSyncHandler")
    //public void businessTypeSyncHandler() {
    //    tenantIds.forEach(tenantId -> {
    //        SyncF syncF = new SyncF();
    //        syncF.setSysSource(SysSourceEnum.用友ncc.getCode());
    //        IdentityInfo identityInfo = new IdentityInfo();
    //        identityInfo.setTenantId(tenantId);
    //        ThreadLocalUtil.set("IdentityInfo", identityInfo);
    //        assisteAccountDomainService.businessTypeSync(syncF);
    //    });
    //}

}
