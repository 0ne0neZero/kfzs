package com.wishare.finance.domains.configure.accountbook.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookGroupMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xujian
 * @date 2022/8/19
 * @Description:
 */
@Service
public class AccountBookGropuRepository extends ServiceImpl<AccountBookGroupMapper, AccountBookGroupE> {

    @Autowired
    private AccountBookGroupMapper accountBookGroupMapper;

    /**
     * 根据账簿id删除账簿所属数据
     *
     * @param accountBookId
     */
    public void removeByAccountBookId(Long accountBookId) {
        LambdaQueryWrapper<AccountBookGroupE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookGroupE::getAccountBookId, accountBookId);
        this.remove(wrapper);
        accountBookGroupMapper.delete(wrapper);
    }

    /**
     * 根据账簿id获取账簿组合数据
     *
     * @param accountBookId
     * @return
     */
    public List<AccountBookGroupE> getByAccountBookId(Long accountBookId) {
        LambdaQueryWrapper<AccountBookGroupE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookGroupE::getAccountBookId, accountBookId);
       return accountBookGroupMapper.selectList(wrapper);
    }

    /**
     * 根据账簿id获取账簿所属数据
     *
     * @param accountBookId
     * @return
     */
    /*public AccountBookBelongE getByAccountBookId(Long accountBookId) {
        LambdaQueryWrapper<AccountBookBelongE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookBelongE::getAccountBookId, accountBookId);
        return accountBookGroupMapper.selectOne(wrapper);
    }

    *//**
     * 根据账簿id修改账簿所属数据
     *
     * @param accountBookBelongE
     *//*
    public void updateByAccountBookId(AccountBookBelongE accountBookBelongE) {
        LambdaQueryWrapper<AccountBookBelongE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookBelongE::getAccountBookId, accountBookBelongE.getAccountBookId());
        accountBookGroupMapper.update(accountBookBelongE, wrapper);
    }

    */
}
