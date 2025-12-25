package com.wishare.finance.apps.service.bill;

import com.wishare.finance.apps.model.bill.fo.QueryReconcileGroupF;
import com.wishare.finance.domains.bill.dto.ReconciliationGroupDto;
import com.wishare.finance.domains.bill.repository.BillReconciliationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 对账应用服务
 *
 * @Author dxclay
 * @Date 2022/10/16
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillReconciliationAppService {

    private final BillReconciliationRepository billReconciliationRepository;


    /**
     * 查询所需清分对账的维度分组
     * @return
     */
    public List<ReconciliationGroupDto> getReconcileGroupsClear(QueryReconcileGroupF queryReconcileGroupF){
        List<String> groupFields = new ArrayList<>();
        if (Objects.nonNull(queryReconcileGroupF.getStatutoryBody()) && queryReconcileGroupF.getStatutoryBody().isGroup()){
            groupFields.add("statutory_body_id");
        }
        if (Objects.nonNull(queryReconcileGroupF.getCommunity())&& queryReconcileGroupF.getCommunity().isGroup()){
            groupFields.add("community_id");
        }
        if (Objects.nonNull(queryReconcileGroupF.getCostCenter()) && queryReconcileGroupF.getCostCenter().isGroup()) {
            groupFields.add("cost_center_id");
        }
        if (Objects.nonNull(queryReconcileGroupF.getPayChannel()) && queryReconcileGroupF.getPayChannel().isGroup()){
            groupFields.add("cost_center_id");
        }
        return billReconciliationRepository.getReconcileGroupsClear(groupFields);
    }


}
