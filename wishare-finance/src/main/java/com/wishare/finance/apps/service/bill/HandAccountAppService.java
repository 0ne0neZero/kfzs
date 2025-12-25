package com.wishare.finance.apps.service.bill;

import com.wishare.finance.domains.bill.command.AccountHandCommand;
import com.wishare.finance.domains.bill.command.BatchAccountHandCommand;
import com.wishare.finance.domains.bill.command.BatchRefreshAccountHandCommand;
import com.wishare.finance.domains.bill.command.RefreshAccountHandCommand;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.dto.BillBatchResultDto;
import com.wishare.finance.domains.bill.dto.HandAccountDto;
import com.wishare.finance.domains.bill.entity.BillHandAccountRule;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 交账应用服务
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/1/11
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HandAccountAppService {

    private final HandAccountDomainService handAccountDomainService;

    /**
     * 发起交账
     * @param billId 账单id
     * @param billType 账单类型
     * @return
     */
    @Transactional
    public HandAccountDto handAccount(Long billId, Integer billType,String supCpUnitId) {
        return handAccountDomainService.handAccount(new AccountHandCommand(billId, BillTypeEnum.valueOfByCode(billType),supCpUnitId));
    }

    /**
     * 发起批量交账
     * @param query   查询条件
     * @param billIds 账单id
     * @return
     */
    @Transactional
    public BillBatchResultDto batchHandAccount(PageF<SearchF<?>> query, List<Long> billIds,String supCpUnitId){
        return handAccountDomainService.batchHandAccount(new BatchAccountHandCommand(query, billIds,supCpUnitId));
    }

    /**
     * 反交账
     * @param billId
     * @param billType
     * @return
     */
    public boolean reversal(Long billId, Integer billType, String supCpUnitId) {
        return handAccountDomainService.reversal(billId, BillTypeEnum.valueOfByCode(billType), supCpUnitId);
    }

    /**
     * 更新交账信息
     *
     * @param command  更新交账命令
     * @return
     */
    public boolean refreshOrAddAccountHand(RefreshAccountHandCommand command){
       return handAccountDomainService.refreshOrAddAccountHand(command) != null;
    }

    /**
     * 删除交账账单
     * @param command
     * @return
     */
    public boolean updateAccountHandDelete(RefreshAccountHandCommand command){
        return handAccountDomainService.updateAccountHandDelete(command);
    }



    /**
     * 批量更新交账账单
     * @param batchRefreshAccountHandCommand
     */
    @Transactional
    public void batchRefreshOrAddAccountHand(BatchRefreshAccountHandCommand batchRefreshAccountHandCommand) {
        handAccountDomainService.batchRefreshOrAddAccountHand(batchRefreshAccountHandCommand);
    }

    /**
     * 获取规则
     * @param ruleId
     * @return
     */
    public BillHandAccountRule getHandAccountRuleBySpcId(Long ruleId){
        return handAccountDomainService.getOrCreateHandAccountRule(ruleId);
    }

    /**
     * 更新收费规则
     * @param billHandAccountRule 规则信息
     * @return
     */
    public boolean updateHandAccountRule(BillHandAccountRule billHandAccountRule){
        return handAccountDomainService.updateHandAccountRule(billHandAccountRule);
    }

    /**
     * 流水认领时自动交账
     * @param refreshAccountHandCommand
     */
    public boolean flowClaimAutoAccountHand(RefreshAccountHandCommand refreshAccountHandCommand) {
        return handAccountDomainService.flowClaimAutoAccountHand(refreshAccountHandCommand);
    }
}
