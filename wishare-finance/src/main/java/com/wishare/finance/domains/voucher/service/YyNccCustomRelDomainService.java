package com.wishare.finance.domains.voucher.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.yuanyang.vo.YyNccCustomerRelV;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccCustomerRelRepository;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YyNccCustomRelDomainService {

    private final YyNccCustomerRelRepository yyNccCustomerRelRepository;

    /**
     * 列表查询
     * @param searchFPageF
     * @return
     */
    public PageV<YyNccCustomerRelV> selectPageBySearch(PageF<SearchF<?>> searchFPageF) {
        Page<YyNccCustomerRelE> closeAccountPage = yyNccCustomerRelRepository.selectPageBySearch(searchFPageF);
        return RepositoryUtil.convertPage(closeAccountPage, YyNccCustomerRelV.class);
    }

    public void insert(YyNccCustomerRelE yyNccCustomerRelE) {
        yyNccCustomerRelRepository.save(yyNccCustomerRelE);
    }

    public void updateById(YyNccCustomerRelE yyNccCustomerRelE) {
        yyNccCustomerRelRepository.updateById(yyNccCustomerRelE);
    }
    public void deleteById(Long id) {
        yyNccCustomerRelRepository.deleteById(id);
    }
}
