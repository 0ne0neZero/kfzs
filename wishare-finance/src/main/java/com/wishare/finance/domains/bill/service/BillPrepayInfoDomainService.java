package com.wishare.finance.domains.bill.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.BillPrepayByMchNoUpdateF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoAddF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoQueryF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillPrepayInfoV;
import com.wishare.finance.domains.bill.consts.enums.BillPayStateEnum;
import com.wishare.finance.domains.bill.entity.BillPrepayInfoE;
import com.wishare.finance.domains.bill.repository.BillPrepayInfoRepository;
import com.wishare.finance.domains.bill.repository.mapper.BillPrepayInfoMapper;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.consts.Const;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoDomainService
 * @date 2023.11.08  10:19
 * @description domainService
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BillPrepayInfoDomainService {

    private final BillPrepayInfoRepository billPrepayInfoRepository;

    private final BillPrepayInfoMapper billPrepayInfoMapper;

    /**
     * 获取账单列表的预支付信息
     * @param billPrepayInfoQueryF 预支付传参
     * @return {@link BillPrepayInfoV}
     */
    public List<BillPrepayInfoV> queryPrepayList(BillPrepayInfoQueryF billPrepayInfoQueryF) {
        List<BillPrepayInfoE> list = billPrepayInfoRepository.list(new QueryWrapper<BillPrepayInfoE>()
                .in("bill_id", billPrepayInfoQueryF.getBillIds())
                .eq("sup_cp_unit_id", billPrepayInfoQueryF.getSupCpUnitId())
                .eq("deleted", Const.State._0)
                .eq(Objects.nonNull(billPrepayInfoQueryF.getPayState()),"pay_state", billPrepayInfoQueryF.getPayState()));
        return CollectionUtils.isEmpty(list)?List.of(): Global.mapperFacade.mapAsList(list,BillPrepayInfoV.class);
    }

    /**
     * 分页获取账单预支付信息 (无租户隔离)
     * @param searchF searchF
     * @param queryWrapper queryWrapper
     * @return {@link BillPrepayInfoE}
     */
    public Page<BillPrepayInfoE> queryPageBySearch(Page<SearchF<?>> searchF, QueryWrapper<?> queryWrapper) {
        return billPrepayInfoMapper.queryPageBySearch(searchF,queryWrapper);
    }

    /**
     * 添加账单列表的预支付信息
     * @param billPrepayInfoAddF 预支付
     * @return {@link BillPrepayInfoV}
     */
    public Boolean add(BillPrepayInfoAddF billPrepayInfoAddF) {
        return billPrepayInfoRepository.saveBatch(buildAddPrepayInfo(billPrepayInfoAddF));
    }

    /**
     * 更新账单列表的预支付信息
     * @param billPrepayInfoUpdateF 预支付
     * @return {@link BillPrepayInfoV}
     */
    public Boolean update(BillPrepayInfoUpdateF billPrepayInfoUpdateF) {
        return billPrepayInfoRepository.update(new UpdateWrapper<BillPrepayInfoE>()
                .set("pay_state",billPrepayInfoUpdateF.getPayState())
                .set(StringUtils.isNotBlank(billPrepayInfoUpdateF.getReason()),"reason",billPrepayInfoUpdateF.getReason())
                .in("bill_id",billPrepayInfoUpdateF.getBillIds())
                .eq("sup_cp_unit_id",billPrepayInfoUpdateF.getSupCpUnitId())
                .eq("pay_state",BillPayStateEnum.支付中.getCode()));
    }


    /**
     * 更新账单列表的预支付状态
     * @param billPrepayByMchNoUpdateF 更新账单列表的预支付状态
     * @return {@link BillPrepayInfoV}
     */
    public Boolean updateByMchNo(BillPrepayByMchNoUpdateF billPrepayByMchNoUpdateF) {
        return billPrepayInfoRepository.update(new UpdateWrapper<BillPrepayInfoE>()
                .set("pay_state", billPrepayByMchNoUpdateF.getPayStatus())
                .eq("mch_order_no", billPrepayByMchNoUpdateF.getMchOrderNo())
                .eq("sup_cp_unit_id", billPrepayByMchNoUpdateF.getSupCpUnitId()));
    }


    /*
        PRIVATE METHOD ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * 转化添加预支付参数
     * @param billPrepayInfoAddF 预支付参数
     * @return {@link BillPrepayInfoE}
     */
    private List<BillPrepayInfoE> buildAddPrepayInfo(BillPrepayInfoAddF billPrepayInfoAddF) {
        List<BillPrepayInfoE> result = new ArrayList<>();
        billPrepayInfoAddF.getBillIds().forEach(id->{
            BillPrepayInfoE prepayInfoE = Global.mapperFacade.map(billPrepayInfoAddF, BillPrepayInfoE.class);
            prepayInfoE.setId(IdentifierFactory.getInstance().generateLongIdentifier(TableNames.BILL_PREPAY_INFO));
            prepayInfoE.setBillId(id);
            prepayInfoE.setSupCpUnitId(billPrepayInfoAddF.getSupCpUnitId());
            prepayInfoE.setPayState(BillPayStateEnum.支付中.getCode());
            result.add(prepayInfoE);
        });
        return result;
    }


    /**
     * 根据商户单号查询预支付信息
     *
     * @param mchOrderNo 商户单号
     * @param payState 预支付状态
     * @return @return {@link BillPrepayInfoE}
     */
    public List<BillPrepayInfoE> getByMchNo(String mchOrderNo, Integer payState) {
        return billPrepayInfoRepository.list(new LambdaQueryWrapper<BillPrepayInfoE>()
                .eq(BillPrepayInfoE::getMchOrderNo,mchOrderNo)
                .eq(Objects.nonNull(payState),BillPrepayInfoE::getPayState, payState));
    }
}
