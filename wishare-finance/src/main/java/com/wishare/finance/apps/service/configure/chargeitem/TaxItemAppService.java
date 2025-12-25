package com.wishare.finance.apps.service.configure.chargeitem;

import com.wishare.finance.apps.model.configure.chargeitem.fo.AddTaxItemF;
import com.wishare.finance.apps.model.configure.chargeitem.fo.UpdateTaxItemF;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxItemCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxItemCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemE;
import com.wishare.finance.domains.configure.chargeitem.service.TaxItemDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 税目应用层
 *
 * @author yancao
 */
@Service
public class TaxItemAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private TaxItemDomainService taxItemDomainService;

    /**
     * 新增税目
     *
     * @param addTaxItemF 新增税目入参
     * @return Long
     */
    public Long add(AddTaxItemF addTaxItemF) {
        return taxItemDomainService.addTaxItem(Global.mapperFacade.map(addTaxItemF, AddTaxItemCommand.class)).getId();
    }

    /**
     * 更新税目
     *
     * @param updateTaxItemF 更新税目入参
     * @return Boolean
     */
    public Boolean update(UpdateTaxItemF updateTaxItemF) {
        return taxItemDomainService.update(Global.mapperFacade.map(updateTaxItemF, UpdateTaxItemCommand.class));
    }

    /**
     * 根据费项id查询税目
     *
     * @param chargeItemIdList 费项id集合
     * @return List
     */
    public List<TaxChargeItemD> queryByChargeIdList(List<Long> chargeItemIdList) {
        return taxItemDomainService.queryByChargeIdList(chargeItemIdList);
    }


    /**
     * 分页查询税目
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<TaxItemD> queryByPage(PageF<SearchF<TaxItemE>> queryF) {
        return taxItemDomainService.queryByPage(queryF);
    }

    /**
     * 根据id获取税目
     *
     * @param id 税目id
     * @return TaxItemD
     */
    public TaxItemD queryById(Long id) {
        return taxItemDomainService.queryById(id);
    }

    /**
     * 根据id删除税目
     *
     * @param id 税目id
     * @return Boolean
     */
    @Transactional
    public Boolean delete(Long id) {
        return taxItemDomainService.delete(id);
    }

    /**
     * 导入税目
     *
     * @param file file
     * @return Boolean
     */
    public Boolean importTaxItem(MultipartFile file) {
        return taxItemDomainService.importTaxItem(file);
    }

    /**
     * 查询税目信息列表
     *
     * @return List<TaxItemD>
     */
    public List<TaxItemD> queryTaxItemList() {
        return taxItemDomainService.queryTaxItemList();
    }
}
