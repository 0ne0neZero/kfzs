package com.wishare.finance.domains.configure.subject.facade;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.StatutoryBodyRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceTreeRv;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author xujian
 * @date 2022/8/22
 * @Description:
 */
@Service
@Slf4j
public class SubjectOrgFacade {

    @Setter(onMethod_ = {@Autowired})
    private OrgClient orgClient;


    /**
     * 分页获取法定单位
     *
     * @param queryPageF queryPageF
     * @return List
     */
    public PageV<StatutoryBodyRv> getOrgFinancePage(PageF<SearchF<?>> queryPageF) {
        QueryWrapper<?> queryModel = queryPageF.getConditions().getQueryModel();
        return orgClient.getOrgFinancePage(queryPageF);
    }

    /**
     * 批量获取组织信息
     *
     * @param orgIdList 组织id
     * @return List
     */
    public List<OrgFinanceTreeRv> getOrgListById(List<Long> orgIdList) {
        return orgClient.getOrgListById(orgIdList);
    }
}
