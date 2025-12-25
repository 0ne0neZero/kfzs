package com.wishare.finance.apps.service.bill.prepay;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.bill.fo.BillPrepayByMchNoUpdateF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoAddF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoQueryF;
import com.wishare.finance.apps.model.bill.fo.BillPrepayInfoUpdateF;
import com.wishare.finance.apps.model.bill.vo.BillAdjustV;
import com.wishare.finance.apps.model.bill.vo.BillPrepayInfoV;
import com.wishare.finance.domains.bill.consts.enums.BillPayStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.entity.Bill;
import com.wishare.finance.domains.bill.entity.BillPrepayInfoE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.service.BillPrepayInfoDomainService;
import com.wishare.finance.domains.bill.service.ReceivableBillDomainService;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.enums.ErrMsgEnum;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillPrepayInfoAppService
 * @date 2023.11.08  10:34
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class BillPrepayInfoAppService {

    private final BillPrepayInfoDomainService billPrepayInfoDomainService;

    private final ReceivableBillDomainService receivableBillDomainService;

    private final ReceivableBillRepository receivableBillRepository;

    /**
     * 获取账单预支付信息
     *
     * @param billPrepayInfoQueryF 查询信息
     * @return List
     */
    public List<BillPrepayInfoV> queryPrepayList(BillPrepayInfoQueryF billPrepayInfoQueryF) {
        return billPrepayInfoDomainService.queryPrepayList(billPrepayInfoQueryF);
    }

    /**
     * 分页获取账单预支付信息 (无租户隔离)
     * @param queryF 分页参数
     * @return {@link PageV}<>{@link BillAdjustV}</>
     */
    public PageV<BillPrepayInfoE> getPrepayListPage(PageF<SearchF<?>> queryF) {
        queryF.getConditions().getFields().stream().filter(field -> "b.pay_state".equals(field.getName())).findAny().orElseGet(() -> {
            queryF.getConditions().getFields().add(new Field("b.pay_state", BillPayStateEnum.支付中.getCode(), 1));return null;
        });
        Page<BillPrepayInfoE> billPrepayInfoPage = billPrepayInfoDomainService.queryPageBySearch(RepositoryUtil.convertMPPage(queryF),
                RepositoryUtil.putLogicDeleted(queryF.getConditions().getQueryModel(), "b"));
        return RepositoryUtil.convertPage(billPrepayInfoPage, BillPrepayInfoE.class);
    }


    /**
     * 账单预支付
     * @param billPrepayInfoAddF 预支付信息
     * @return
     */
    @Transactional
    public Boolean billPrepay(BillPrepayInfoAddF billPrepayInfoAddF) {
//        log.info("添加预支付处理记录, {}", JSON.toJSONString(billPrepayInfoAddF));
        // 查询当前账单状态是否存在支付中数据
        List<BillPrepayInfoV> infoList = queryPrepayList(new BillPrepayInfoQueryF()
                .setBillIds(billPrepayInfoAddF.getBillIds()).setSupCpUnitId(billPrepayInfoAddF.getSupCpUnitId())
                .setPayState(BillPayStateEnum.支付中.getCode()));
        ErrorAssertUtil.isFalseThrow402(!CollectionUtils.isEmpty(infoList), ErrMsgEnum.OPERATION_FAILED
                ,"存在正在结算中的账单，请先完成结算操作");
        List<ReceivableBill> billOjvs = receivableBillDomainService.getList(billPrepayInfoAddF.getBillIds(),billPrepayInfoAddF.getSupCpUnitId());
        ErrorAssertUtil.isFalseThrow402(CollectionUtils.isEmpty(billOjvs), ErrMsgEnum.OPERATION_FAILED,"该批账单已不存在");
        // 校验账单操作状态
        billOjvs.forEach(ReceivableBill::verifyOperate);
        // 预支付处理
        // 1.添加预支付记录
        try {
            billPrepayInfoDomainService.add(billPrepayInfoAddF);
        }catch (Exception e) {
            log.error("添加预支付记录,{}",e.getMessage(), e);
            ErrorAssertUtil.isTrueThrow402(true, ErrMsgEnum.OPERATION_FAILED,"添加预支付记录");
        }

        // 2.修改账单结算状态
        receivableBillDomainService.updateBillInfoByIds(billPrepayInfoAddF.getBillIds(),billPrepayInfoAddF.getSupCpUnitId()
                ,BillSettleStateEnum.结算中.getCode());
        log.info("添加预支付处理记录, {}", JSON.toJSONString(billPrepayInfoAddF));
        return true;
    }


    /**
     * 账单预支付信息更新
     * @param billPrepayInfoUpdateF 更新信息
     * @return {@link Boolean}
     */
    @Transactional
    public Boolean releasePaymentOpr(BillPrepayInfoUpdateF billPrepayInfoUpdateF) {
        // 校验状态是否满足条件
        List<BillPrepayInfoV> infoList = queryPrepayList(new BillPrepayInfoQueryF()
                .setBillIds(billPrepayInfoUpdateF.getBillIds()).setSupCpUnitId(billPrepayInfoUpdateF.getSupCpUnitId())
                .setPayState(BillPayStateEnum.支付中.getCode()));
        if (CollectionUtils.isEmpty(infoList)){
            return false;
        }
        List<ReceivableBill> billOjvs = receivableBillDomainService.getList(billPrepayInfoUpdateF.getBillIds(),billPrepayInfoUpdateF.getSupCpUnitId());
        ErrorAssertUtil.isFalseThrow402(CollectionUtils.isEmpty(billOjvs), ErrMsgEnum.OPERATION_FAILED,"该批账单已不存在");
        // 账单预支付信息更新
        billPrepayInfoDomainService.update(billPrepayInfoUpdateF);
        // 更新账单结算状态
        billOjvs.forEach(ReceivableBill::refresh);

        // 2.修改账单结算状态
        Map<Integer, List<ReceivableBill>> settleMap = billOjvs.stream().collect(Collectors.groupingBy(Bill::getSettleState));
        settleMap.forEach((x,y)->{
            List<Long> billIds = y.stream().map(Bill::getId).collect(Collectors.toList());
            receivableBillRepository.update(new UpdateWrapper<ReceivableBill>().set("settle_state",x)
                    .in("id",billIds).eq("sup_cp_unit_id",billPrepayInfoUpdateF.getSupCpUnitId()));
        });
        return true;
    }

    /**
     * 更新账单列表的预支付状态
     * @param billPrepayInfoUpdateF 更新账单列表的预支付状态
     * @return {@link BillPrepayInfoV}
     */
    public Boolean updateByMchNo(BillPrepayByMchNoUpdateF billPrepayInfoUpdateF) {
        return billPrepayInfoDomainService.updateByMchNo(billPrepayInfoUpdateF);
    }

    /**
     * 根据商户单号查询预支付信息
     *
     * @param mchOrderNo 商户单号
     * @param payState 预支付状态
     * @return @return {@link BillPrepayInfoV}
     */
    public List<BillPrepayInfoV> getByMchNo(String mchOrderNo, Integer payState) {
        return Global.mapperFacade.mapAsList(billPrepayInfoDomainService.getByMchNo(mchOrderNo,payState),BillPrepayInfoV.class);
    }

    /*
        PRIVATE METHOD ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


    /**
     * 账单支付前置处理
     */
    public void checkPrepayment(@Validated BillPrepayInfoQueryF billPrepayInfoQueryF){
        // 获取账单当前支付状态是否满足
        billPrepayInfoQueryF.setPayState(billPrepayInfoQueryF.getPayState()==null?BillPayStateEnum.支付中.getCode():
                billPrepayInfoQueryF.getPayState());
        List<BillPrepayInfoV> result =  queryPrepayList(billPrepayInfoQueryF);
        // 若是存在支付中的账单，则该批次账单不允许再支付
        ErrorAssertUtil.isFalseThrow402(!org.apache.commons.collections4.CollectionUtils.isEmpty(result)
                , ErrMsgEnum.FAILED,"存在正在支付中的账单，请先完成支付");
    }

    @PrepayOprCheck
    public void checkPrepay(List<Long> billIds, String supCpUnitId){};

    @PrepayOprCheck
    public void checkPrepay(Long billId, String supCpUnitId){};
}
