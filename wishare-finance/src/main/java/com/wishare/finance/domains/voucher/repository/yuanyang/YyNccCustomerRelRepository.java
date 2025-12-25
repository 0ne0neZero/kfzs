package com.wishare.finance.domains.voucher.repository.yuanyang;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.voucher.entity.yuanyang.YyNccCustomerRelE;
import com.wishare.finance.domains.voucher.repository.yuanyang.mapper.YyNccCustomerRelMapper;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AddCustomerF;
import com.wishare.finance.infrastructure.remote.vo.external.yonyounc.Result;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class YyNccCustomerRelRepository extends ServiceImpl<YyNccCustomerRelMapper, YyNccCustomerRelE> {

    private final ExternalClient externalClient;

    /**
     * 获取ncc的客户编码
     * 若没有在映射表中，则往ncc那边新增
     * @param assisteItem
     * @return
     */
    public String getNccCustomerCode(AssisteItemOBV assisteItem, String creditCode) {
        String nccCustomerCode = baseMapper.getNccCustomerCode(creditCode);
        // 如果没有在映射表，则往ncc里新增客户，不管是供应商还是客户，统一当客户新增
        if (StringUtils.isBlank(nccCustomerCode)) {
            AddCustomerF addCustomerF = new AddCustomerF();
            addCustomerF.setName(assisteItem.getName());
            addCustomerF.setCode(assisteItem.getCode());
            addCustomerF.setPkCustClass("C");
            addCustomerF.setTaxPayerId(creditCode);
            log.info("不存在映射编码，开始请求ncc新增客户:{}", JSON.toJSONString(addCustomerF));
            Result result = externalClient.addCustomer(addCustomerF);
            // 返回示例：{"code":"001","msg":"客户更新成功，客户ID为：1001A21000000000LJMP"}
            log.info("新增NCC客户返回结果:{}", JSON.toJSONString(result));
            if (Objects.nonNull(result) && "001".equals(result.getCode())) {
                saveNccCustomerRel(assisteItem, creditCode);
                return assisteItem.getCode();

            }
            throw BizException.throw300("新增ncc客户失败:" + result.getMsg());
        }
        return nccCustomerCode;
    }

    public void saveNccCustomerRel(AssisteItemOBV assisteItem, String creditCode) {
        YyNccCustomerRelE yyNccCustomerRelE = new YyNccCustomerRelE();
        yyNccCustomerRelE.setCustomerName(assisteItem.getName());
        yyNccCustomerRelE.setNccCustomerCode(assisteItem.getCode());
        yyNccCustomerRelE.setCreditCode(creditCode);
        this.save(yyNccCustomerRelE);
    }

    public Page<YyNccCustomerRelE> selectPageBySearch(PageF<SearchF<?>> searchPageF) {
        Page<YyNccCustomerRelE> yyNccCustomerRelEPage = baseMapper.selectBySearch(RepositoryUtil.convertMPPage(searchPageF),
                searchPageF.getConditions().getQueryModel().orderByDesc("id"));
        return yyNccCustomerRelEPage;
    }


    public YyNccCustomerRelE getById(Long id) {
        return baseMapper.selectById(id);
    }


    public void insert(YyNccCustomerRelE  yyNccCustomerRelE) {
        baseMapper.insert(yyNccCustomerRelE);
    }

    public void deleteById(Long id) {
        baseMapper.deleteById(id);
    }
}
