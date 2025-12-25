package com.wishare.contract.domains.service.revision.invoice;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.contract.domains.entity.revision.pay.bill.ContractSettlementsBillE;
import com.wishare.contract.domains.mapper.revision.pay.bill.ContractSettlementsBillMapper;
import com.wishare.contract.domains.vo.revision.pay.bill.ContractPayBillV;
import com.wishare.owl.enhance.IOwlApiBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: zhangfuyu
 * @Date: 2023/8/1/20:53
 */
@Service
@Slf4j
public class ContractInvoiceService extends ServiceImpl<ContractSettlementsBillMapper, ContractSettlementsBillE> implements IOwlApiBase {


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractSettlementsBillMapper contractPayBillMapper;

    /**
     *
     * @param id 根据Id删除
     * @return 删除结果
     */
    public boolean removeById(String id){
        contractPayBillMapper.deleteById(id);
        return true;
    }

    public List<ContractPayBillV> getDetailsByIdSettle(String oppositeId){
        return contractPayBillMapper.getDetailsByIdSettle(oppositeId);
    }

}
