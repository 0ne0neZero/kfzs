package com.wishare.finance.apps.service.yuanyang;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.yuanyang.fo.YyNccCustomerRelAddF;
import com.wishare.finance.apps.model.yuanyang.fo.YyNccCustomerRelUpdateF;
import com.wishare.finance.apps.model.yuanyang.vo.YyNccCustomerRelV;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.finance.domains.voucher.repository.yuanyang.YyNccCustomerRelRepository;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YyNccCustomerRelAppService implements ApiBase {

    private final YyNccCustomerRelRepository yyNccCustomerRelRepository;

    private static LambdaQueryWrapper<YyNccCustomerRelE> getWrapper() {
        return Wrappers.<YyNccCustomerRelE>lambdaQuery();
    }

    /**
     * 列表查询
     * @param searchFPageF
     * @return
     */
    public PageV<YyNccCustomerRelV> selectPageBySearch(PageF<SearchF<?>> searchFPageF) {
        Page<YyNccCustomerRelE> closeAccountPage = yyNccCustomerRelRepository.selectPageBySearch(searchFPageF);
        return RepositoryUtil.convertPage(closeAccountPage, YyNccCustomerRelV.class);
    }

    public Long addYyNccCustomerRel(YyNccCustomerRelAddF addF){
        // 社会信用唯一
        long count = checkCreditCode(addF.getCreditCode());
        if (count > 0) {
            throw new BizException(402,"提交失败，纳税人识别号重复。");
        }
        count = checkCustomerCode(addF.getNccCustomerCode());
        if (count > 0) {
            throw new BizException(402,"提交失败，NCC客商编码在系统上已存在。");
        }
        YyNccCustomerRelE yyNccCustomerRelE = new YyNccCustomerRelE();
        yyNccCustomerRelE.setCustomerName(addF.getCustomerName());
        yyNccCustomerRelE.setNccCustomerCode(addF.getNccCustomerCode());
        yyNccCustomerRelE.setCreditCode(addF.getCreditCode());
        yyNccCustomerRelE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.YY_NCC_CUSTOMER_REL));

        yyNccCustomerRelRepository.insert(yyNccCustomerRelE);
        return yyNccCustomerRelE.getId();
    }

    private long checkCreditCode(String creditCode) {
        long count = yyNccCustomerRelRepository.count(getWrapper().eq(YyNccCustomerRelE::getCreditCode, creditCode));
        return count;
    }

    private long checkCustomerCode(String nccCustomerCode) {
        long count = yyNccCustomerRelRepository.count(getWrapper().eq(YyNccCustomerRelE::getNccCustomerCode, nccCustomerCode));
        return count;
    }

    public Long updateYyNccCustomerRel(YyNccCustomerRelUpdateF updateF){
        long count = yyNccCustomerRelRepository.count(getWrapper()
                .eq(YyNccCustomerRelE::getCreditCode, updateF.getNccCustomerCode())
                .ne(YyNccCustomerRelE::getId,updateF.getId()));
        if (count > 0) {
            throw new BizException(402,"提交失败，NwCC客商编码在系统上已存在。");
        }
        count = yyNccCustomerRelRepository.count(getWrapper()
                .eq(YyNccCustomerRelE::getCreditCode, updateF.getCreditCode())
                .ne(YyNccCustomerRelE::getId,updateF.getId()));
        if (count > 0) {
            throw new BizException(402,"提交失败，纳税人识别号重复。");
        }
        YyNccCustomerRelE nccCustomerRelE = Global.mapperFacade.map(updateF, YyNccCustomerRelE.class);
        yyNccCustomerRelRepository.updateById(nccCustomerRelE);
        return nccCustomerRelE.getId();
    }

    public void deleteById(Long id){
        yyNccCustomerRelRepository.deleteById(id);
    }

    public void deleteByIds(List<Long> ids) {
        yyNccCustomerRelRepository.removeBatchByIds(ids);
    }

    public YyNccCustomerRelE getById(Long id) {
        return yyNccCustomerRelRepository.getById(id);
    }
}
