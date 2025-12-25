package com.wishare.finance.apps.service.configure.accountbook;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookDtoStr;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.configure.accountbook.fo.*;
import com.wishare.finance.domains.configure.accountbook.command.AddAccountBookCommand;
import com.wishare.finance.domains.configure.accountbook.command.UpdateAccountBookCommand;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.domains.configure.accountbook.service.AccountBookDomainService;
import com.wishare.finance.domains.configure.businessunit.entity.BusinessUnitE;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.vo.org.OrgInfoRv;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.interfaces.ApiBase;
import com.wishare.tools.starter.fo.search.SearchF;

import java.util.*;
import java.util.stream.Collectors;

import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Service
public class AccountBookAppService implements ApiBase {

    @Setter(onMethod_ = {@Autowired})
    private AccountBookDomainService accountBookDomainService;

    @Autowired
    private ExternalClient externalClient;

    /**
     * 分页查询账簿列表
     *
     * @param form
     * @return
     */
    public PageV<AccountBookDtoStr> queryPage(PageF<SearchF<?>> form) {
        Page<AccountBookE> accountBookEPage = accountBookDomainService.queryPage(form);
        List<AccountBookDtoStr> accountBookDtoStrs = Global.mapperFacade.mapAsList(accountBookEPage.getRecords(), AccountBookDtoStr.class);
        if (CollectionUtils.isNotEmpty(accountBookDtoStrs)) {
            for (AccountBookDtoStr accountBookDtoStr : accountBookDtoStrs) {
                List<AccountBookGroupE> accountBookGroup = accountBookDomainService.getAccountBookGroup(accountBookDtoStr.getId());
                List<String> groupStrLsit = Lists.newArrayList();
                for (AccountBookGroupE accountBookGroupE : accountBookGroup) {
                    String accountBookGroupStr = handleAccountBookStr(accountBookGroupE.getCostCenter(), accountBookGroupE.getChargeItem());
                    groupStrLsit.add(accountBookGroupStr);
                }
                accountBookDtoStr.setAccountBookGroup(groupStrLsit);
            }
        }
        return PageV.of(form, accountBookEPage.getTotal(), accountBookDtoStrs);
    }

    /**
     * 处理字符串
     *
     * @param costCenters
     * @param chargeItems
     * @return
     */
    private String handleAccountBookStr(List<CostCenter> costCenters, List<ChargeItem> chargeItems) {
        String costCenterName = "";
        if (CollectionUtils.isNotEmpty(costCenters)) {
            for (CostCenter costCenter : costCenters) {
                costCenterName = costCenterName + " " + costCenter.getCostCenterName();
            }
        }
        String chargeItemName = "";
        if (CollectionUtils.isNotEmpty(chargeItems)) {
            for (ChargeItem chargeItem : chargeItems) {
                chargeItemName = chargeItemName + " " + chargeItem.getChargeItemName();
            }
        }
        return "【" + costCenterName + "," + chargeItemName + "】";
    }

    /**
     * 新增账簿
     *
     * @param form
     * @return
     */
    public Long addAccountBook(AddAccountBookF form) {
        AddAccountBookCommand command = form.getAddAccountBookCommand();
        return accountBookDomainService.addAccountBook(command);
    }

    /**
     * 修改账簿
     *
     * @param form
     * @return
     */
    public Long updateAccountBook(UpdateAccountBookF form) {
        UpdateAccountBookCommand command = form.updateAccountBookCommand();
        return accountBookDomainService.updateAccountBook(command);
    }

    /**
     * 删除账簿
     *
     * @param id
     * @return
     */
    public Boolean deleteAccountBook(Long id) {
        return accountBookDomainService.deleteAccountBook(id);
    }

    /**
     * 根据id启用或者禁用账簿
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        return accountBookDomainService.enable(id, disableState);
    }


    /**
     * 根据id获取账簿详情
     *
     * @param id
     * @return
     */
    public AccountbookDTO detailAccountBook(Long id) {
        AccountbookDTO accountbookDTO = accountBookDomainService.detailAccountBook(id);
        return accountbookDTO;
    }

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    public AccountBookE getByChargeItemAndCostCenter(Long chargeItemId, Long costCenterId) {
        return accountBookDomainService.detailAccountBookByChargeItemAndCostCenter(chargeItemId, costCenterId);
    }

    /**
     * 根据名称搜索账簿
     * @param name
     * @return
     */
    public List<AccountBookE> searchAccountBook(String name) {
        return accountBookDomainService.searchAccountBook(name);
    }

    /**
     * 同步账簿信息
     * @param syncAccountBooks 账簿列表
     * @return
     */
    @Transactional
    public boolean syncAccountBook(List<AddAccountBookF> syncAccountBooks) {
        return accountBookDomainService.syncAccountBooks(syncAccountBooks.stream().map(AddAccountBookF::getAddAccountBookCommand).collect(Collectors.toList()));
    }
    @Transactional
    public Map<String,Object> clickSyncAccountBook() {
        HashMap<String, Object> returnMap = new HashMap<>();
        StringBuilder messsage = new StringBuilder("");
        List<AccountBookE> updateAccountBookES = new ArrayList<AccountBookE>();
        List<AccountBookE> createList = new ArrayList<AccountBookE>();
        int size = 200;
        PageF<?> pageF = new PageF<>();
        pageF.setPageSize(size);
        int pageNum = 0;
        boolean flag = true;
        PageV<ExternalAccountingbookF> accountingBookPage = null;
        List<ExternalAccountingbookF> accountingBooks = null;
        while (flag) {
            pageNum++;
            pageF.setPageNum(pageNum);
            accountingBookPage = externalClient.getAccountingbookPage(pageF);
            accountingBooks = accountingBookPage.getRecords();
            if (CollectionUtils.isEmpty(accountingBooks)){
                break;
            }
            List<String> codes = accountingBooks.stream().map(ExternalAccountingbookF::getCode).collect(Collectors.toList());
            List<AccountBookE> accountBookES = accountBookDomainService.queryList(new LambdaQueryWrapper<AccountBookE>().in(AccountBookE::getCode, codes));
            Map<String, List<ExternalAccountingbookF>> newList = accountingBooks.stream().collect(Collectors.groupingBy(ExternalAccountingbookF::getCode));
            if (CollectionUtils.isNotEmpty(accountBookES)){
                for (AccountBookE accountBookE : accountBookES) {
                    List<ExternalAccountingbookF> externalAccountingbookFS = newList.get(accountBookE.getCode());
                    if (CollectionUtils.isNotEmpty(externalAccountingbookFS)){
                        accountBookE.setName(externalAccountingbookFS.get(0).getName());
                        updateAccountBookES.add(accountBookE);
                        newList.remove(accountBookE.getCode());
                    }
                }
            }
            if (newList!=null){
                for (String s : newList.keySet()) {
                    AccountBookE accountBookE = Global.mapperFacade.map(newList.get(s).get(0).getAccountBookECommand(curIdentityInfo()), AccountBookE.class);
                    createList.add(accountBookE);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(updateAccountBookES)){
            accountBookDomainService.updatebatch(updateAccountBookES);
            returnMap.put("updateTotal",updateAccountBookES.size());
            messsage.append("更新"+updateAccountBookES.size()+"条;");
        }
        if (CollectionUtils.isNotEmpty(createList)){
            accountBookDomainService.savebatch(createList);
            returnMap.put("successTotal",createList.size());
            messsage.append("新增"+createList.size()+"条;");
        }
        returnMap.put("success",true);
        returnMap.put("message",messsage.toString());
        return returnMap;
    }

    public List<AccountBookE> selectListByCostChargeStatutory(Long costCenterId, Long chargeItemId, Long statutoryBodyId) {
        List<AccountBookE> accountBookEList = accountBookDomainService.selectListByCostChargeStatutory(costCenterId,chargeItemId,statutoryBodyId);
        return accountBookEList;
    }

    public List<AccountBookDtoStr> queryList(SearchF<?> form) {
       return  Global.mapperFacade.mapAsList(accountBookDomainService.queryList(form), AccountBookDtoStr.class);
    }
}
