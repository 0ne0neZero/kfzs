package com.wishare.finance.domains.voucher.repository;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.apps.model.voucher.fo.CloseAccountF;
import com.wishare.finance.domains.voucher.entity.CloseAccount;
import com.wishare.finance.domains.voucher.repository.mapper.CloseAccountMapper;
import com.wishare.finance.domains.voucher.repository.mapper.CloseAccountOriginMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.utils.RepositoryUtil;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CloseAccountRepository extends ServiceImpl<CloseAccountMapper, CloseAccount> {

    @Autowired
    private CloseAccountOriginMapper closeAccountOriginMapper;

    public CloseAccount getAccountName(CloseAccount closeAccount) {
        LambdaQueryWrapper<CloseAccount> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CloseAccount::getAccountBookId,closeAccount.getAccountBookId())
                .eq(CloseAccount::getAccountingPeriod,closeAccount.getAccountingPeriod())
                .eq(CloseAccount::getStates,closeAccount.getStates())
                .eq(CloseAccount::getDeleted,closeAccount.getDeleted())
                ;
        return getOne(wrapper);
    }

    public Page<CloseAccount> selectPageBySearch(PageF<SearchF<?>> searchFPageF) {
        return baseMapper.selectPageBySearch(RepositoryUtil.convertMPPage(searchFPageF),
                RepositoryUtil.putLogicDeleted(searchFPageF.getConditions().getQueryModel()));
    }

    public void updateBuyDeleted(CloseAccountF closeAccountF) {
        update(new LambdaUpdateWrapper<CloseAccount>().set(CloseAccount::getDeleted,1).eq(CloseAccount::getId,closeAccountF.getRawId()));

    }

    /**
     * @param bookId
     * @param accountingPeriod 会计期间
     * @return null 表示没关账或者不存在关账记录。 非null：返回关账的最大月份（例子2024-12）
     */
    public CloseAccount getBookIdBuyAccount(Long bookId , String accountingPeriod) {

        List<CloseAccount> list = baseMapper.selectList(Wrappers.<CloseAccount>lambdaQuery()
                .eq(CloseAccount::getAccountBookId,bookId)
                .eq(CloseAccount::getDeleted, DataDeletedEnum.NORMAL.getCode()));
        log.info("getBookIdBuyAccount:{}" , JSON.toJSONString(list));
        if (CollUtil.isEmpty(list)){
            log.error("getBookIdBuyAccount不存在关账信息，bookId-{}",bookId);
            return null;
        }
        return getCloseAccount(accountingPeriod, list);


    }

    @Nullable
    private static CloseAccount getCloseAccount(String accountingPeriod, List<CloseAccount> list) {
        Map<String, CloseAccount> map = list.stream().collect(Collectors.toMap(CloseAccount::getAccountingPeriod, v -> v, (a, b) -> a));
        CloseAccount acc = map.get(accountingPeriod);
        // 当前月未关账
        if (ObjectUtil.isNull(acc) || acc.getStates().equals(0)){
            log.error("getBookIdBuyAccount,月-{}，不存在关账信息，bookId-{}",accountingPeriod,acc);
            return null;
        }

        // 当前月关账，取最近的未关账月份。
        // 例子 2024-05 关账。2024-06 不关账。2024-07 关账  取的是2024-06
        // 例子 2024-05 关账。没有关账记录。2024-07 关账 取的也是 2024-06
        log.error("getBookIdBuyAccount,当前月关账-{}",accountingPeriod);
        String temp = accountingPeriod;
        while (true){
            String s = LocalDate.parse(temp + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            CloseAccount account = map.get(s);
            if (ObjectUtil.isNull(account) || account.getStates().equals(0)){
                // 重置
                acc.setAccountingPeriod(s);
                log.error("getBookIdBuyAccount,当前月关账-{},返回期间为:{}",accountingPeriod,acc.getAccountingPeriod());
                return acc;
            }
            temp = s;
        }
    }

    public void saveBatchCloseAccount(Collection<CloseAccount> closeAccounts) {
        if (CollectionUtils.isNotEmpty(closeAccounts)) {
            closeAccountOriginMapper.saveBatch(closeAccounts);
        }
    }


    public static void main(String[] args) {
        ArrayList<CloseAccount> list = new ArrayList<>();
        list.add(CloseAccount.builder().states(1).accountingPeriod("2024-05").build());
        list.add(CloseAccount.builder().states(1).accountingPeriod("2024-06").build());
        list.add(CloseAccount.builder().states(0).accountingPeriod("2024-07").build());
        list.add(CloseAccount.builder().states(1).accountingPeriod("2024-08").build());
        getCloseAccount("2024-05", list).getAccountingPeriod();

        LocalDate d = LocalDate.parse("2025-05-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        System.out.println(d.getYear());


        ArrayList<CloseAccount> list2 = new ArrayList<>();
        list2.add(CloseAccount.builder().states(1).accountingPeriod("2024-05").build());

        list2.add(CloseAccount.builder().states(1).accountingPeriod("2024-07").build());
        list2.add(CloseAccount.builder().states(1).accountingPeriod("2024-08").build());

        getCloseAccount("2024-05", list2).getAccountingPeriod();

    }


}
