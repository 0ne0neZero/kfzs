package com.wishare.finance.apps.service.voucher;

import com.wishare.finance.apps.model.fangyuan.vo.BusinessUnitSyncDto;
import com.wishare.finance.apps.model.voucher.fo.AssisteBizTypeF;
import com.wishare.finance.apps.model.voucher.fo.AssisteInoutclassF;
import com.wishare.finance.apps.model.voucher.fo.AssisteOrgDeptF;
import com.wishare.finance.apps.model.voucher.fo.AssisteOrgF;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.entity.AssisteInoutclass;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrg;
import com.wishare.finance.domains.configure.subject.entity.AssisteOrgDept;
import com.wishare.finance.domains.configure.subject.service.AssisteItemDomainService;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 辅助核算领域服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/3/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AssisteItemAppService {

    private final AssisteItemDomainService assisteItemDomainService;

    /**
     * 批量新增业务类型
     * @param assisteBizTypeFS
     * @return
     */
    public boolean syncBatchAssisteBizType(List<AssisteBizTypeF> assisteBizTypeFS) {
        return assisteItemDomainService.syncBatchAssisteBizType(Global.mapperFacade.mapAsList(assisteBizTypeFS, AssisteBizType.class));
    }

    /**
     *  wishare-external（定时调用) 功能：同步业务单元
     * @param assisteOrgFS
     * @return
     */
    public List<BusinessUnitSyncDto> syncBatchAssisteOrg(List<AssisteOrgF> assisteOrgFS) {
        return assisteItemDomainService.syncBatchAssisteOrg(Global.mapperFacade.mapAsList(assisteOrgFS, AssisteOrg.class));
    }

    /**
     * 批量新增辅助核算（部门）
     * @param assisteOrgDeptFS
     * @return
     */
    public boolean syncBatchAssisteOrgDept(List<AssisteOrgDeptF> assisteOrgDeptFS) {
        return assisteItemDomainService.syncBatchAssisteOrgDept(Global.mapperFacade.mapAsList(assisteOrgDeptFS, AssisteOrgDept.class));
    }

    /**
     * 批量新增辅助核算（收支项目）
     * @param assisteInoutclassFS
     * @return
     */
    public boolean syncBatchAssisteInoutclass(List<AssisteInoutclassF> assisteInoutclassFS) {
        return assisteItemDomainService.syncBatchAssisteInoutclass(Global.mapperFacade.mapAsList(assisteInoutclassFS, AssisteInoutclass.class));
    }

    /**
     * 获取辅助核算信息
     * @param name 名称
     * @param code 编码
     * @param type 类型
     * @param sbId 法定单位id
     * @return 辅助核算信息列表
     */
    public List<AssisteItemOBV> getBaseAssisteItem(String name, String code, Integer type, String sbId){
        return assisteItemDomainService.getBaseAssisteItem(name, code, type, sbId);
    }

}
