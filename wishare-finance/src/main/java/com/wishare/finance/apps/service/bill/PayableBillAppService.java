package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.AddBillSettleF;
import com.wishare.finance.apps.model.bill.fo.AddPayableBillF;
import com.wishare.finance.domains.bill.command.AddBillSettleCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.entity.PayableBill;
import com.wishare.finance.domains.bill.service.PayableBillDomainService;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 *
 * @author yancao
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PayableBillAppService extends BillAppServiceImpl<PayableBillDomainService, PayableBill> {

    @Override
    public BillTypeEnum getBillType() {
        return BillTypeEnum.应付账单;
    }

    /**
     * 批量结算应收账单
     *
     * @param form 结算参数
     * @return Boolean
     */
    @Transactional
    public Long settleBatch(List<AddBillSettleF> form) {
        return baseBillDomainService.settleBatch(Global.mapperFacade.mapAsList(form, AddBillSettleCommand.class));
    }

    /**
     * 应付账单冲销
     *
     * @param payableBillId
     * @return
     */
    public Boolean reverse(Long payableBillId,String extField1) {
        return baseBillDomainService.reverse(payableBillId,extField1,null);
    }

    /**
     * 导出应付单
     *
     * @param queryF  查询参数
     * @param response response
     */
    public void export(PageF<SearchF<?>> queryF, HttpServletResponse response) {
        AddPayableBillF addPayableBillF = new AddPayableBillF();
        baseBillDomainService.export(queryF,response);
    }

    /**
     * 获取付费对象数据
     * @param addPayableBillList
     * @return
     */
    public void setCustomerInfo(List<AddPayableBillF> addPayableBillList){
        addPayableBillList.forEach(addPayableBillF -> {
            addPayableBillF.setCustomerId(addPayableBillF.getPayeeId());
            addPayableBillF.setCustomerName(addPayableBillF.getPayeeName());
            addPayableBillF.setCustomerType(addPayableBillF.getPayeeType());
        });
    }
}
