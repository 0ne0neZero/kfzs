package com.wishare.finance.apps.service.bill.contract;

import com.alibaba.fastjson.JSON;
import com.wishare.finance.apps.model.bill.vo.BillContractV;
import com.wishare.finance.domains.bill.entity.BillContractE;
import com.wishare.finance.domains.bill.entity.TemporaryChargeBill;
import com.wishare.finance.domains.bill.service.BillContractDomainService;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractBasePullF;
import com.wishare.finance.infrastructure.remote.vo.contract.ContractReceivableBill;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceCommunityV;
import com.wishare.starter.Global;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ℳ๓采韵
 * @project wishare-finance
 * @title BillContractAppServiceImpl
 * @date 2024.07.03  14:09
 * @description 合同关联业主
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BillContractAppService {

    private final ExternalClient externalClient;

    private final SpaceClient spaceClient;

    private final BillContractDomainService billContractDomainService;

    /**
     * 根据账单ID获取其对应的合同编号信息
     * @param billId 账单ID
     * @return {@link BillContractV}
     */
    public BillContractV getByBillId(Long billId){
        BillContractE contractE = billContractDomainService.getByBillId(billId);
        return Global.mapperFacade.map(contractE,BillContractV.class);
    }

    /**
     * 临时账单新增同步风梦行财务账单
     * @param billList 临时账单列表
     * @return
     */
    public Boolean syncBill(List<TemporaryChargeBill> billList) {
        List<BillContractE> result = new ArrayList<>();
        billList.forEach(bill->{
            // 构建参数
            Boolean isSuccess;
            ContractBasePullF contractBasePullF = new ContractBasePullF();
            ContractReceivableBill contractReceivableBill = new ContractReceivableBill(bill);
            contractReceivableBill.setOperator(1);
            SpaceCommunityV communityDetail = spaceClient.getCommunityDetail(bill.getCommunityId());
            contractReceivableBill.setProjectCode(communityDetail.getSerialNumber());
            String requestBody = JSON.toJSONString(contractReceivableBill);
            contractBasePullF.setRequestBody(requestBody);
            BillContractE billContractE = new BillContractE(bill);
            try {
                // 发送请求
                log.info("临时新增同步到枫行梦参数体"+requestBody);
                isSuccess = externalClient.contractReceivableBill(contractBasePullF);
                log.info("临时新增同步到枫行梦是否成功" + isSuccess);
                billContractE.setDeleted(isSuccess?0:1);
            }catch (Exception e){
                log.error("临时新增同步到枫行梦失败",e);
                billContractE.setDeleted(1);
                billContractE.setReason(e.getMessage());
            }
            result.add(billContractE);
        });
        // 保存同步记录
        return billContractDomainService.batchAdd(result);
    }
}
