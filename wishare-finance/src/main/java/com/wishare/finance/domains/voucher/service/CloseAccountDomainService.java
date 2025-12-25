package com.wishare.finance.domains.voucher.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import com.wishare.finance.apps.model.voucher.fo.CloseAccountF;
import com.wishare.finance.apps.model.voucher.fo.CloseAccountsF;
import com.wishare.finance.apps.model.voucher.vo.CloseAccountV;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookGropuRepository;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookRepository;
import com.wishare.finance.domains.voucher.consts.enums.AccountCloseEnum;
import com.wishare.finance.domains.voucher.entity.CloseAccount;
import com.wishare.finance.domains.voucher.entity.CloseAccountOBV;
import com.wishare.finance.domains.voucher.repository.CloseAccountRepository;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.vo.org.PermissionRV;
import com.wishare.finance.infrastructure.support.ApiData;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.utils.ErrorAssertUtil;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CloseAccountDomainService {

    private final OrgClient orgClient;
    private final CloseAccountRepository closeAccountRepository;
    private final AccountBookGropuRepository accountBookGropuRepository;
    private final AccountBookRepository accountBookRepository;

    /**
     * 新增关账记录
     * @param closeAccount
     * @return
     */
    public Long addCloseAccount(CloseAccount closeAccount) {
        closeAccount.setDeleted(0);
        closeAccount.setStates(1);
        //根据账簿id去查账簿信息
        LambdaQueryWrapper<AccountBookE> wrapper = new LambdaQueryWrapper();
        wrapper.eq(AccountBookE::getId, closeAccount.getAccountBookId());
        AccountBookE book = accountBookRepository.getOne(wrapper);
        if(Objects.nonNull(book)){
            closeAccount.setAccountBookCode(book.getCode());
            closeAccount.setAccountBookName(book.getName());
            CloseAccount accountName = closeAccountRepository.getAccountName(closeAccount);
            ErrorAssertUtil.isNullThrow301(accountName, ErrorMessage.CLOSE_ACCOUNT_FAIL);
            closeAccount.generateIdentifier();
            closeAccountRepository.save(closeAccount);
            return closeAccount.getId();
        }
        return null;
    }


    /**
     * 列表查询
     * @param searchFPageF
     * @return
     */
    public PageV<CloseAccountV> selectPageBySearch(PageF<SearchF<?>> searchFPageF) {
        String userId = ApiData.API.getUserId().orElse("administrator");
        //获取当前用户下的成本中心
        PermissionRV permission = orgClient.permission(2, userId, null);
        List<Long> allNodes = permission.getAllNodes();
        if (CollectionUtils.isEmpty(allNodes)){
            return new PageV<CloseAccountV>();
        }
        //得到所有成本中心对应的账簿id
        List<Long> accountBookId = new ArrayList<>();
        List<AccountBookGroupE> byAccountBookList = accountBookGropuRepository.list();
        if (CollectionUtils.isNotEmpty(byAccountBookList)) {
            for (AccountBookGroupE accountBookGroupE : byAccountBookList) {
                List<CostCenter> costCenter = accountBookGroupE.getCostCenter();
                if (CollectionUtils.isNotEmpty(costCenter)) {
                    for (CostCenter center : costCenter) {
                        if (allNodes.contains(center.getCostCenterId())) {
                            accountBookId.add(accountBookGroupE.getAccountBookId());
                        }
                    }
                }
            }
        }
        List<Field> fields = searchFPageF.getConditions().getFields();
        if (CollectionUtils.isNotEmpty(fields)) {
            for (Field field : fields) {
                for (String s : field.getMap().keySet()) {
                    if (s.equals("end")) {
                        DateTime dateTime = DateUtil.endOfDay(DateUtil.parse((String) field.getMap().get(s), "yyyy-MM-dd"));
                        field.getMap().put("end", dateTime);
                    }
                }
            }
        }
        QueryWrapper<?> queryModel = searchFPageF.getConditions().getQueryModel();
        queryModel.in("account_book_id",accountBookId);
        Page<CloseAccount> closeAccountPage = closeAccountRepository.selectPageBySearch(searchFPageF);
        return RepositoryUtil.convertPage(closeAccountPage, CloseAccountV.class);
    }

    /**
     * 关账/反关账操作
     * @param closeAccountF
     * @return
     */
    public boolean operateCloseAccount(CloseAccountF closeAccountF) {
        //判断原交账记录id
        if (closeAccountF.getRawId() != null){
            //反关账
            if (closeAccountF.getOperateStates() == 0){
                if (closeAccountF.getStates() == 0){
                    ErrorAssertUtil.throw400(ErrorMessage.REVERSED_CLOSE_ACCOUNT_FAIL);
                }else {
                    CloseAccount closeAccount = new CloseAccount();
                    Global.mapperFacade.map(closeAccountF, closeAccount);
                    closeAccount.setDeleted(0);
                    closeAccount.setStates(0);
                    closeAccount.generateIdentifier();
                    closeAccountRepository.save(closeAccount);
                    //修改原交账记录按钮显示状态
                    closeAccountRepository.updateBuyDeleted(closeAccountF);
                }
            //关账
            }else if (closeAccountF.getOperateStates() == 1){
                if (closeAccountF.getStates() == 1){
                    ErrorAssertUtil.throw400(ErrorMessage.CLOSE_ACCOUNT_FAIL);
                }else {
                    CloseAccount closeAccount = new CloseAccount();
                    Global.mapperFacade.map(closeAccountF, closeAccount);
                    closeAccount.setDeleted(0);
                    closeAccount.setStates(1);
                    closeAccount.generateIdentifier();
                    closeAccountRepository.save(closeAccount);
                    //修改原交账记录按钮显示状态
                    closeAccountRepository.updateBuyDeleted(closeAccountF);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 批量关账
     * @param closeAccountsF
     * @return
     */
    public boolean closeAccounts(CloseAccountsF closeAccountsF) {
        List<YearMonth> period = closeAccountsF.getPeriod();
        List<String> periods = DateTimeUtil.getMonthBetweenMonth(period.get(0), period.get(1));
        if (CollectionUtils.isNotEmpty(periods)) {
            List<CloseAccountOBV> closeAccounts = closeAccountsF.getCloseAccounts();
            List<Long> accountBookIds = closeAccounts.stream()
                    .map(CloseAccountOBV::getAccountBookId).collect(Collectors.toList());
            List<CloseAccount> existCloseAccounts = closeAccountRepository.list(
                    new LambdaUpdateWrapper<CloseAccount>()
                            .in(CloseAccount::getAccountBookId, accountBookIds)
                            .in(CloseAccount::getAccountingPeriod, periods)
                            .eq(CloseAccount::getDeleted, DataDeletedEnum.NORMAL.getCode()));
            Set<CloseAccount> closeAccountEs = initCloseAccounts(periods, closeAccounts);
            if (CollectionUtils.isNotEmpty(existCloseAccounts)) {
                // 过滤已存在关账记录
                existCloseAccounts.forEach(closeAccountEs::remove);
                // 在已关账记录中筛选出反关账记录
                List<CloseAccount> notClosedAccounts = existCloseAccounts.stream()
                        .filter(account -> AccountCloseEnum.未关账.equalsByCode(account.getStates()))
                        .peek(account -> account.setStates(AccountCloseEnum.已关账.getCode()))
                        .collect(Collectors.toList());
                closeAccountRepository.updateBatchById(notClosedAccounts);
            }
            closeAccountRepository.saveBatchCloseAccount(closeAccountEs);
        }
        return true;
    }

    public Set<CloseAccount> initCloseAccounts(List<String> periods, List<CloseAccountOBV> closeAccounts) {
        String tenantId = ApiData.API.getTenantId().get();
        String user = ApiData.API.getUserId().orElse("administrator");
        String userName = ApiData.API.getUserName().orElse("系统默认");
        LocalDateTime now = LocalDateTime.now();
        return periods.stream()
                .flatMap(period -> closeAccounts.stream().map(closeAccount -> {
                    CloseAccount closeAccountE = new CloseAccount();
                    closeAccountE.generateIdentifier();
                    closeAccountE.setAccountBookId(closeAccount.getAccountBookId());
                    closeAccountE.setAccountBookCode(closeAccount.getAccountBookCode());
                    closeAccountE.setAccountBookName(closeAccount.getAccountBookName());
                    closeAccountE.setAccountingPeriod(period);
                    closeAccountE.setStates(AccountCloseEnum.已关账.getCode());
                    closeAccountE.setTenantId(tenantId);
                    closeAccountE.setGmtCreate(now);
                    closeAccountE.setCreator(user);
                    closeAccountE.setCreatorName(userName);
                    closeAccountE.setOperator(user);
                    closeAccountE.setOperatorName(userName);
                    closeAccountE.setGmtModify(now);
                    return closeAccountE;
                })).collect(Collectors.toSet());
    }
}
