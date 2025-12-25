package com.wishare.finance.apps.service.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxItemGoodsF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxItemGoodsF;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxItemGoodsCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxItemGoodsCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemGoodsE;
import com.wishare.finance.domains.configure.chargeitem.service.TaxItemGoodsDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 税目应用层
 *
 * @author yancao
 */
@Service
public class TaxItemGoodsAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private TaxItemGoodsDomainService taxItemDomainService;

    /**
     * 新增税目
     *
     * @param addTaxItemGoodsF 新增税目入参
     * @return Long
     */
    public Long add(AddTaxItemGoodsF addTaxItemGoodsF) {
        return taxItemDomainService.addTaxItem(Global.mapperFacade.map(addTaxItemGoodsF, AddTaxItemGoodsCommand.class)).getId();
    }

    /**
     * 更新税目
     *
     * @param updateTaxItemF 更新税目入参
     * @return Boolean
     */
    public Boolean update(UpdateTaxItemGoodsF updateTaxItemF) {
        return taxItemDomainService.update(Global.mapperFacade.map(updateTaxItemF, UpdateTaxItemGoodsCommand.class));
    }

    /**
     * 根据费项ids查询税收信息
     *
     * @param chargeItemIdList 费项id集合
     * @return List
     */
    public List<TaxItemGoodsD> queryByGoodsList(List<Long> chargeItemIdList) {
        return taxItemDomainService.queryByGoodsList(chargeItemIdList);
    }

    /**
     * 分页查询税收商品信息
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<TaxItemGoodsD> queryByPage(PageF<SearchF<TaxItemGoodsE>> queryF) {
        return taxItemDomainService.queryByPage(queryF);
    }

    /**
     * 根据id获取税收商品
     *
     * @param id 税收商品id
     * @return TaxItemGoodsD
     */
    public TaxItemGoodsD queryById(Long id) {
        return taxItemDomainService.queryById(id);
    }

    /**
     * 根据id删除税收商品
     *
     * @param id 税收商品id
     * @return Boolean
     */
    @Transactional
    public Boolean delete(Long id) {
        return taxItemDomainService.delete(id);
    }

}
