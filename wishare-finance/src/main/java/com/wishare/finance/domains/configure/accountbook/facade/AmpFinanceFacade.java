package com.wishare.finance.domains.configure.accountbook.facade;

import com.wishare.finance.apps.model.bill.vo.BillDetailMoreV;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemAppService;
import com.wishare.finance.apps.service.configure.chargeitem.TaxItemGoodsAppService;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxChargeItemD;
import com.wishare.finance.domains.configure.chargeitem.dto.taxitem.TaxItemGoodsD;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.service.ChargeItemDomainService;
import com.wishare.finance.infrastructure.utils.ErrorAssertUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author xujian
 * @date 2022/8/22
 * @Description:
 */
@Service
@Slf4j
public class AmpFinanceFacade {
    @Setter(onMethod_ = {@Autowired})
    private TaxItemAppService taxItemAppService;

    @Setter(onMethod_ = {@Autowired})
    private TaxItemGoodsAppService taxItemGoodsAppService;

    @Setter(onMethod_ = {@Autowired})
    private ChargeItemDomainService chargeItemDomainService;

    /**
     * 根据费项ids查询税目
     *
     * @return
     */
    public Map<Long, List<TaxChargeItemD>> queryByChargeIdList(List<Long> chargeItemIdList){
        List<TaxChargeItemD> taxChargeItemRvs = taxItemAppService.queryByChargeIdList(chargeItemIdList);
        if (CollectionUtils.isNotEmpty(taxChargeItemRvs)) {
            Map<Long, List<TaxChargeItemD>> taxIteamMapByChargeId = taxChargeItemRvs.stream().collect(Collectors.groupingBy(TaxChargeItemD::getChargeItemId));
            return taxIteamMapByChargeId;
        }
        return null;
    }

    /**
     * 根据费项ids查询税收信息
     *
     * @return
     */
    public void queryByGoodsList(List<BillDetailMoreV> billDetailMoreVList){
        //费项id集
        List<Long> chargeIds = billDetailMoreVList.stream().map(BillDetailMoreV::getChargeItemId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        boolean hasOverdue = billDetailMoreVList.stream().anyMatch(detail -> Objects.nonNull(detail.getOverdue()) && detail.getOverdue() == 1);
        ChargeItemE chargeItemE = null;
        // 查询违约金费项对于的税收商品名称
        if (hasOverdue) {
            chargeItemE = chargeItemDomainService.getOverdueChargeItem();
            if (null != chargeItemE){
                chargeIds.add(chargeItemE.getId());
            }
        }
        //根据费项ids查询税收信息
        List<TaxItemGoodsD> taxItemGoodsDS = taxItemGoodsAppService.queryByGoodsList(chargeIds);
        if (CollectionUtils.isEmpty(taxItemGoodsDS)) { return; }
        Map<Long, List<TaxItemGoodsD>> collect = taxItemGoodsDS.stream()
                .collect(Collectors.groupingBy(TaxItemGoodsD::getChargeItemId));
        for (BillDetailMoreV moreV : billDetailMoreVList) {
            // 如果moreV是违约金，直接塞入上面查询的违约金费项税收商品名称
            if (Objects.nonNull(moreV.getOverdue()) && moreV.getOverdue() == 1) {
                if (null != chargeItemE){
                    if (CollectionUtils.isNotEmpty(collect.get(chargeItemE.getId()))) {
                        moreV.setGoodsName(collect.get(chargeItemE.getId()).get(0).getGoodsName());
                        continue;
                    }
                }
            }
            List<TaxItemGoodsD> goodsDS = collect.get(moreV.getChargeItemId());
            if (CollectionUtils.isNotEmpty(goodsDS)) {
                moreV.setGoodsName(goodsDS.get(0).getGoodsName());
            }
        }
    }
}
