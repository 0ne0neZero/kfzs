package com.wishare.finance.apps.service.configure.arrears;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsReasonF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonV;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsReasonE;
import com.wishare.finance.domains.configure.arrears.service.ArrearsReasonDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsReasonAppService implements ApiBase {

    private final ArrearsReasonDomainService arrearsReasonDomainService;

    public boolean create(CreateArrearsReasonF createArrearsReasonF) {
        return arrearsReasonDomainService.create(createArrearsReasonF);
    }

    public PageV<ArrearsReasonV> queryByPage(PageF<SearchF<?>> searchF) {
        return arrearsReasonDomainService.queryByPage(searchF);
    }


    public PageV<ArrearsReasonBillV> queryPageBill(PageF<SearchF<?>> searchF) {
        IPage<ArrearsReasonBillV> page = arrearsReasonDomainService.queryPageBill(searchF);
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());

    }

    /**
     * 查询最新欠费原因
     */
    public List<ArrearsReasonV> queryNewArrearsReason(List<Long> billId){
        List<ArrearsReasonV> arrearsReasonVList = new ArrayList<>();
        List<ArrearsReasonE> arrearsReasonEList = arrearsReasonDomainService.queryNewArrearsReason(billId);
        if(!CollectionUtils.isEmpty(arrearsReasonEList)){
            for (ArrearsReasonE arrearsReasonE : arrearsReasonEList) {
                ArrearsReasonV arrearsReasonV = Global.mapperFacade.map(arrearsReasonE,ArrearsReasonV.class);
                arrearsReasonVList.add(arrearsReasonV);
            }
        }
        return arrearsReasonVList;
    }
}
