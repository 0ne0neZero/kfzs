package com.wishare.finance.domains.configure.arrears.service;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.arrearsCategory.fo.CreateArrearsReasonF;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonBillV;
import com.wishare.finance.apps.model.configure.arrearsCategory.vo.ArrearsReasonV;
import com.wishare.finance.domains.bill.dto.ReasonBillTotalDto;
import com.wishare.finance.domains.configure.arrears.entity.ArrearsReasonE;
import com.wishare.finance.domains.configure.arrears.repository.ArrearsReasonRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ArrearsReasonDomainService {

    private final ArrearsReasonRepository arrearsReasonRepository;

    public boolean create(CreateArrearsReasonF createArrearsReasonF) {
        List<Long> billIds = createArrearsReasonF.getBillId();
        if (CollectionUtil.isEmpty(billIds)){
            throw BizException.throw400("账单id不能为空");
        }
        for (Long billId : billIds) {
            ArrearsReasonE arrearsReasonE = new ArrearsReasonE();
            BeanUtils.copyProperties(createArrearsReasonF,arrearsReasonE);
            arrearsReasonE.setBillId(billId);
            arrearsReasonE.setCreator(ThreadLocalUtil.curIdentityInfo().getUserId());
            arrearsReasonE.setGmtCreate(LocalDateTime.now());
            arrearsReasonE.setCreatorName(ThreadLocalUtil.curIdentityInfo().getUserName());
            arrearsReasonRepository.save(arrearsReasonE);
        }

        return true;
    }

    public PageV<ArrearsReasonV> queryByPage(PageF<SearchF<?>> searchF) {
        Page<ArrearsReasonE> page = Page.of(searchF.getPageNum(), searchF.getPageSize());
        QueryWrapper<ArrearsReasonE> queryModel = searchF.getConditions().getQueryModel(ArrearsReasonE.class);
        queryModel.eq("deleted", DataDeletedEnum.NORMAL.getCode())
                .eq("tenant_id", ThreadLocalUtil.curIdentityInfo().getTenantId())
                .orderByDesc("gmt_modify")
                .orderByDesc("id");
        Page<ArrearsReasonE> queryByPage = arrearsReasonRepository.page(page, queryModel);

        return PageV.of(searchF,queryByPage.getTotal(),Global.mapperFacade.mapAsList(queryByPage.getRecords(), ArrearsReasonV.class));
    }

    public IPage<ArrearsReasonBillV> queryPageBill(PageF<SearchF<?>> searchF) {
        return arrearsReasonRepository.queryPageBill(searchF);
    }

    public ReasonBillTotalDto batchReasonBillTotal(PageF<SearchF<?>> searchF) {
        return arrearsReasonRepository.batchReasonBillTotal(searchF);
    }

    public List<ArrearsReasonE> queryNewArrearsReason(List<Long> billId) {
        return arrearsReasonRepository.queryNewArrearsReason(billId);
    }
}
