package com.wishare.finance.domains.configure.chargeitem.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.domains.configure.chargeitem.command.tax.AddTaxItemGoodsCommand;
import com.wishare.finance.domains.configure.chargeitem.command.tax.UpdateTaxItemGoodsCommand;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemRelationD;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxChargeItemRelationE;
import com.wishare.finance.domains.configure.chargeitem.entity.TaxItemGoodsE;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxChargeItemRelationRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxItemGoodsRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 税收商品信息引用层
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaxItemGoodsDomainService {

    private final TaxItemGoodsRepository taxItemGoodsRepository;

    private final TaxChargeItemRelationRepository taxChargeItemRelationRepository;

    /**
     * 新增税收商品信息
     *
     * @param command 新增税收商品信息
     * @return TaxItemGoodsE
     */
    public TaxItemGoodsE addTaxItem(AddTaxItemGoodsCommand command) {
        List<Long> chargeItemIdList = command.getChargeItemIdList();
        String goodsName =command.getGoodsName();
        TaxItemGoodsE taxItemGoodsE = Global.mapperFacade.map(command, TaxItemGoodsE.class);
        taxItemGoodsE.generateId();

        LambdaQueryWrapper<TaxItemGoodsE> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TaxItemGoodsE::getGoodsName,goodsName);
        lambdaQueryWrapper.eq(TaxItemGoodsE::getGoodsFlag,"1");
        List<TaxItemGoodsE> goodsList = taxItemGoodsRepository.list(lambdaQueryWrapper);
        ErrorAssertUtil.isEmptyThrow400(goodsList, ErrorMessage.TAX_ITEM_GOODS_NAME_EXIST);

        //校验费项是否关联其他税收商品
        if (CollectionUtils.isNotEmpty(chargeItemIdList)) {
            List<TaxChargeItemRelationE> otherTaxRelationCharge = taxChargeItemRelationRepository.queryByGoodsList(chargeItemIdList, taxItemGoodsE.getId());
            ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(otherTaxRelationCharge), ErrorMessage.TAX_CHARGE_ITEM_RELATION_OTHER);
        }

        //保存关联关系
        List<TaxChargeItemRelationE> taxChargeItemRelationList = new ArrayList<>();

        for (Long chargeItemId : chargeItemIdList) {
            TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
            relationE.generateId();
            relationE.setChargeItemId(chargeItemId);
            relationE.setTaxItemId(command.getTaxItemId());
            relationE.setTaxItemGoodsId(taxItemGoodsE.getId());
            taxChargeItemRelationList.add(relationE);
        }
        taxChargeItemRelationRepository.saveBatch(taxChargeItemRelationList);
        taxItemGoodsRepository.save(taxItemGoodsE);
        return taxItemGoodsE;
    }

    /**
     * 更新税收商品信息
     *
     * @param command 更新税收商品信息
     * @return Boolean
     */
    public Boolean update(UpdateTaxItemGoodsCommand command) {
        Long taxItemGoodsId = command.getId();
        String goodsName =command.getGoodsName();
        Long taxItemId = command.getTaxItemId();
        List<Long> chargeItemIdList = command.getChargeItemIdList();

        LambdaQueryWrapper<TaxItemGoodsE> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(TaxItemGoodsE::getGoodsName,goodsName);
        lambdaQueryWrapper.eq(TaxItemGoodsE::getGoodsFlag,"1");
        lambdaQueryWrapper.ne(TaxItemGoodsE::getId,taxItemGoodsId);
        List<TaxItemGoodsE> goodsList = taxItemGoodsRepository.list(lambdaQueryWrapper);
        ErrorAssertUtil.isEmptyThrow400(goodsList, ErrorMessage.TAX_ITEM_GOODS_NAME_EXIST);

        //校验税收商品是否存在
        TaxItemGoodsE taxItemGoodsE = taxItemGoodsRepository.getById(taxItemGoodsId);
        ErrorAssertUtil.notNullThrow300(taxItemGoodsE, ErrorMessage.TAX_ITEM_GOODS_NOT_EXIST);

        TaxItemGoodsE goodsE = Global.mapperFacade.map(command, TaxItemGoodsE.class);

        //校验费项是否关联其他税收商品
        if (CollectionUtils.isNotEmpty(chargeItemIdList)) {
            List<TaxChargeItemRelationE> otherTaxRelationCharge = taxChargeItemRelationRepository.queryByGoodsList(chargeItemIdList, taxItemGoodsE.getId());
            ErrorAssertUtil.isFalseThrow300(CollectionUtils.isNotEmpty(otherTaxRelationCharge), ErrorMessage.TAX_CHARGE_ITEM_GOODS_RELATION_OTHER);
        }

        //获取税收商品和费项关联关系
        List<TaxChargeItemRelationE> taxChargeItemRelationList = taxChargeItemRelationRepository.queryByTaxItemIdList(List.of(taxItemGoodsId));
        List<TaxChargeItemRelationE> deleteRelationList = new ArrayList<>();

        //找出要删除和新增的关联关系
        for (TaxChargeItemRelationE taxChargeItemRelationE : taxChargeItemRelationList) {
            if (!chargeItemIdList.contains(taxChargeItemRelationE.getChargeItemId())) {
                deleteRelationList.add(taxChargeItemRelationE);
            } else {
                chargeItemIdList.remove(taxChargeItemRelationE.getChargeItemId());
            }
        }
        List<TaxChargeItemRelationE> addRelationList = new ArrayList<>();
        for (Long chargeItemId : chargeItemIdList) {
            TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
            relationE.generateId();
            relationE.setTaxItemId(taxItemId);
            relationE.setChargeItemId(chargeItemId);
            relationE.setTaxItemGoodsId(taxItemGoodsId);
            addRelationList.add(relationE);
        }
        if(CollectionUtils.isEmpty(addRelationList)){
            TaxChargeItemRelationE relationE = new TaxChargeItemRelationE();
            relationE.setTaxItemId(taxItemId);
            relationE.setTaxItemGoodsId(taxItemGoodsId);
            LambdaUpdateWrapper<TaxChargeItemRelationE> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(TaxChargeItemRelationE::getTaxItemGoodsId,taxItemGoodsId).
                    set(TaxChargeItemRelationE::getTaxItemId,taxItemId);
            taxChargeItemRelationRepository.update(lambdaUpdateWrapper);
        }
        taxChargeItemRelationRepository.removeBatchByIds(deleteRelationList);
        taxChargeItemRelationRepository.saveBatch(addRelationList);
        return taxItemGoodsRepository.updateById(goodsE);
    }


    /**
     * 根据费项ids查询税收信息
     *
     * @param chargeItemIdList 费项id集合
     * @return List
     */
    public List<TaxItemGoodsD> queryByGoodsList(List<Long> chargeItemIdList) {
        List<TaxChargeItemRelationE> taxChargeItemRelationList = taxChargeItemRelationRepository.queryByGoodsList(chargeItemIdList, null);
        List<Long> taxItemGoodsId = taxChargeItemRelationList.stream().map(TaxChargeItemRelationE::getTaxItemGoodsId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isEmpty(taxItemGoodsId)){
            return new ArrayList<>();
        }
        LambdaQueryWrapper<TaxItemGoodsE> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(TaxItemGoodsE::getId,taxItemGoodsId);
        lambdaQueryWrapper.eq(TaxItemGoodsE::getGoodsFlag,"1");
        List<TaxItemGoodsE> taxItemGoodsES = taxItemGoodsRepository.list(lambdaQueryWrapper);
        if(CollectionUtils.isEmpty(taxItemGoodsES)){
            return new ArrayList<>();
        }
        //根据税收商品id进行分组
        Map<Long, List<TaxItemGoodsE>> collect = taxItemGoodsES.stream().collect(Collectors.groupingBy(TaxItemGoodsE::getId));

        List<TaxItemGoodsD> taxItemGoodsDS = new ArrayList<>();
        for (TaxChargeItemRelationE taxChargeItemRelationE : taxChargeItemRelationList) {
            TaxItemGoodsD taxItemGoodsD = new TaxItemGoodsD();
            List<TaxItemGoodsE> taxItemGoodsDList = collect.get(taxChargeItemRelationE.getTaxItemGoodsId());
            taxItemGoodsD.setGoodsName((CollectionUtils.isEmpty(taxItemGoodsDList) ? null : taxItemGoodsDList.get(0).getGoodsName()));
            taxItemGoodsD.setChargeItemId(taxChargeItemRelationE.getChargeItemId());
            taxItemGoodsDS.add(taxItemGoodsD);
        }
        return taxItemGoodsDS;
    }

    /**
     * 分页查询税收商品信息
     *
     * @param queryF 分页参数
     * @return PageV
     */
    public PageV<TaxItemGoodsD> queryByPage(PageF<SearchF<TaxItemGoodsE>> queryF) {
        QueryWrapper<TaxItemGoodsE> queryWrapper = queryF.getConditions().getQueryModel();
        Page<TaxItemGoodsE> page = new Page<>(queryF.getPageNum(), queryF.getPageSize());
        queryWrapper.eq("tig.deleted", DataDeletedEnum.NORMAL.getCode());
        queryWrapper.orderByDesc("tig.gmt_modify");
        Page<TaxItemGoodsD> taxItemGoodsDPage = taxItemGoodsRepository.queryTaxItemGoodsByPage(page, queryWrapper);
        List<TaxItemGoodsD> taxItemGoodsDS = taxItemGoodsDPage.getRecords();
        List<Long> taxItemGoodsIdList = taxItemGoodsDS.stream().map(TaxItemGoodsD::getId).collect(Collectors.toList());

        List<TaxChargeItemRelationD> taxChargeItemRelationList = taxChargeItemRelationRepository.queryChargeByTaxItemGoodsIdList(taxItemGoodsIdList);

        Map<Long, List<TaxChargeItemRelationD>> collect = taxChargeItemRelationList.stream().collect(Collectors.groupingBy(TaxChargeItemRelationD::getTaxItemGoodsId));
        for (TaxItemGoodsD taxItemGoodsD : taxItemGoodsDS) {
            List<TaxChargeItemRelationD> taxChargeItemRelationDS = collect.get(taxItemGoodsD.getId());
            if(CollectionUtils.isNotEmpty(taxChargeItemRelationDS)){
                taxItemGoodsD.setChargeItemList(taxChargeItemRelationDS);
            }
        }
        return PageV.of(page.getCurrent(), page.getSize(), page.getTotal(), taxItemGoodsDS);
    }

    /**
     * 根据id获取税收商品
     *
     * @param id 税收商品id
     * @return TaxItemGoodsD
     */
    public TaxItemGoodsD queryById(Long id) {
        //校验费项是否存在
        TaxItemGoodsE taxItemGoodsE = taxItemGoodsRepository.getById(id);
        ErrorAssertUtil.notNullThrow300(taxItemGoodsE, ErrorMessage.TAX_ITEM_GOODS_NOT_EXIST);

        TaxItemGoodsD taxItemGoodsD = Global.mapperFacade.map(taxItemGoodsE, TaxItemGoodsD.class);
        List<TaxChargeItemRelationD> taxChargeItemRelationList = taxChargeItemRelationRepository.queryChargeByTaxItemGoodsIdList(List.of(id));
        if (CollectionUtils.isNotEmpty(taxChargeItemRelationList)) {
            taxItemGoodsD.setChargeItemList(taxChargeItemRelationList);
            taxChargeItemRelationList.forEach(e ->{
                if(e.getTaxItemGoodsId().equals(id)){
                    taxItemGoodsD.setCode(e.getCode());
                    taxItemGoodsD.setTaxItemId(e.getTaxItemId());
                }
            });
        }
        return taxItemGoodsD;
    }

    /**
     * 根据id删除税收商品
     *
     * @param id 税收商品id
     * @return Boolean
     */
    public Boolean delete(Long id) {
        taxItemGoodsRepository.removeById(id);
        return taxChargeItemRelationRepository.removeByTaxItemId(id);
    }
}
