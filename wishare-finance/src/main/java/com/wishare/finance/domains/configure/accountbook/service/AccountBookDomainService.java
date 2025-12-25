package com.wishare.finance.domains.configure.accountbook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookDtoStr;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookGroupDto;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.domains.configure.accountbook.command.AddAccountBookCommand;
import com.wishare.finance.domains.configure.accountbook.command.UpdateAccountBookCommand;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookGropuRepository;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookRepository;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.infrastructure.conts.ErrMsgEnum;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.exception.BizException;
import com.wishare.tools.starter.fo.search.SearchF;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class AccountBookDomainService {

    private final ChargeItemRepository chargeItemRepository;
    private final AccountBookRepository accountBookRepository;

    private final AccountBookGropuRepository accountBookGropuRepository;



    /**
     * 分页查询账簿列表
     *
     * @param form
     * @return
     */
    public Page<AccountBookE> queryPage(PageF<SearchF<?>> form) {
        return accountBookRepository.queryPage(form);
    }

    /**
     * 新增账簿
     *
     * @param command
     * @return
     */
    @Transactional
    public Long addAccountBook(AddAccountBookCommand command) {
        AddAccountBookCommand.checkMaps(command.getAccountBookGroupDtoList());
        AccountBookE accountBookEBefore = accountBookRepository.getByCode(command.getCode());
        if (null != accountBookEBefore) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_CODE_EXISTS.getErrMsg());
        }
        if (accountBookRepository.count(new LambdaQueryWrapper<AccountBookE>()
                .eq(AccountBookE::getCode, command.getCode())
                .eq(AccountBookE::getDisabled, 0)) > 0) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_NAME_EXISTS.getErrMsg());
        }
        AccountBookE accountBookE = command.getAccountBook();
        List<AccountBookGroupE> accountBookGroupEs = command.getAccountGroup(accountBookE.getId());
        if (CollectionUtils.isNotEmpty(accountBookGroupEs)){
            for (AccountBookGroupE accountBookGroupE : accountBookGroupEs) {
                accountBookGroupE.setChargeItem(convertChargeItem(accountBookGroupE.getChargeItem()));
            }
        }
        accountBookRepository.save(accountBookE);
        accountBookGropuRepository.saveBatch(accountBookGroupEs);
        return accountBookE.getId();
    }

    /**
     * 批量同步账簿信息
     * @param commands
     * @return
     */
    public boolean syncAccountBooks(List<AddAccountBookCommand> commands){
        Map<String, AccountBookE>  accountBookMap = new HashMap<>();
        Map<String, List<AccountBookGroupE>>  accountBookGroupMap = new HashMap<>();
        AccountBookE accountBook = null;
        for (AddAccountBookCommand command : commands) {
            accountBook = command.getAccountBook();
            accountBookMap.put(accountBook.getCode(), accountBook);
            accountBookGroupMap.put(accountBook.getCode(), command.getAccountGroup(accountBook.getId()));
        }
        List<AccountBookE> accountBookES = accountBookRepository.getByCodes(accountBookMap.keySet().stream().collect(Collectors.toList()));

        if (CollectionUtils.isNotEmpty(accountBookES)){
            String code = null;
            for (AccountBookE bookE : accountBookES) {
                code = bookE.getCode();
                accountBook = accountBookMap.get(code);
                if (Objects.nonNull(accountBook)){
                    accountBook.setId(bookE.getId());
                    accountBook.setMapValues(bookE.getMapValues());
                    accountBook.setSysSource(bookE.getSysSource());
                    accountBookGroupMap.remove(code); //账簿映射详情已本系统为准，同步的账簿不需要插入
                }
            }
        }

        log.info("更新账簿其中一条数据:{}", JSONObject.toJSONString(accountBookMap.values().iterator().next()));
        accountBookRepository.saveOrUpdateBatch(accountBookMap.values());
        List<AccountBookGroupE> accountBookGroupES = new ArrayList<>();
        for (List<AccountBookGroupE> abgs : accountBookGroupMap.values()) {
            accountBookGroupES.addAll(abgs);
        }
        accountBookGropuRepository.saveBatch(accountBookGroupES);
        return true;
    }


    /**
     * 修改账簿
     *
     * @param command
     * @return
     */
    @Transactional
    public Long updateAccountBook(UpdateAccountBookCommand command) {
//        AddAccountBookCommand.checkMap(command.getAccountBookGroupDtoList());
        // 校验组织是否相同
        AddAccountBookCommand.checkMaps(command.getAccountBookGroupDtoList());

        AccountBookE accountBookE = accountBookRepository.getById(command.getId());
        if (null == accountBookE) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_NO_EXISTS.getErrMsg());
        }
        //校验账簿名称是否重复
        if (!command.getCode().equals(accountBookE.getCode())&&accountBookRepository.count(new LambdaQueryWrapper<AccountBookE>()
                .eq(AccountBookE::getCode, command.getCode())
                .eq(AccountBookE::getDisabled, 0)) > 0) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_NAME_EXISTS.getErrMsg());
        }
        AccountBookE newAccountBookE = command.getAccountBook();
        accountBookRepository.updateById(newAccountBookE);
        //修改先删除原有的，再新增进去
        accountBookGropuRepository.removeByAccountBookId(command.getId());
        List<AccountBookGroupE> accountBookGroupEs = command.getAccountGroup(accountBookE.getId());
        if (CollectionUtils.isNotEmpty(accountBookGroupEs)){
            for (AccountBookGroupE accountBookGroupE : accountBookGroupEs) {
                accountBookGroupE.setChargeItem(convertChargeItem(accountBookGroupE.getChargeItem()));
            }
        }
        accountBookGropuRepository.saveBatch(accountBookGroupEs);
        return accountBookE.getId();
    }

    /**
     * 转换费项
     * @param chargeItems
     * @return
     */
    public  List<ChargeItem> convertChargeItem(List<ChargeItem> chargeItems){
        if (CollectionUtils.isNotEmpty(chargeItems)){
            List<ChargeItem> convertChargeItems = new ArrayList<>();
            for (ChargeItem chargeItem : chargeItems) {
                //获取当前费项及其子费项信息
                List<ChargeItemE> chargeItemByPath = chargeItemRepository.getChargeItemByPath(List.of(chargeItem.getChargeItemId()), null);
                if (CollectionUtils.isNotEmpty(chargeItemByPath)){
                    convertChargeItems.addAll(chargeItemByPath.stream().map(item -> new ChargeItem(item.getId(), item.getName())).collect(Collectors.toList()));
                }
            }
            return convertChargeItems;
        }
        return chargeItems;
    }

    /**
     * 删除账簿
     *
     * @param id
     * @return
     */
    @Transactional
    public Boolean deleteAccountBook(Long id) {
        AccountBookE accountBookE = accountBookRepository.getById(id);
        if (null == accountBookE) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_NO_EXISTS.getErrMsg());
        }
        accountBookRepository.removeById(id);
        accountBookGropuRepository.removeByAccountBookId(id);
        return Boolean.TRUE;
    }

    /**
     * 根据id启用或者禁用账簿
     *
     * @param id
     * @param disableState
     * @return
     */
    public Boolean enable(Long id, Integer disableState) {
        AccountBookE accountBookE = accountBookRepository.getById(id);
        if (null == accountBookE) {
            throw BizException.throw400(ErrMsgEnum.ACCOUNT_BOOK_NO_EXISTS.getErrMsg());
        }
        accountBookE.setDisabled(disableState);
        return accountBookRepository.updateById(accountBookE);
    }

    @Autowired
    private OrgClient orgClient;

    /**
     * 根据id获取账簿详情
     *
     * @param id
     * @return
     */
    public AccountbookDTO detailAccountBook(Long id) {
        AccountBookE accountBookE = accountBookRepository.getById(id);
        if (accountBookE == null) {
            return null;
        }
        //获取法定单位树级结构
        List<Long> statutoryBodyIdPath = orgClient.orgFinanceOrgByPid(accountBookE.getStatutoryBodyId());

        List<AccountBookGroupE> accountBookGroupEList = accountBookGropuRepository.getByAccountBookId(id);
        List<AccountBookGroupDto> accountBookGroupDtoList = Lists.newArrayList();
        accountBookGroupEList.forEach(accountBookGroupE -> {
            AccountBookGroupDto accountBookGroupDto = new AccountBookGroupDto();
            accountBookGroupDto.setChargeItemList(accountBookGroupE.getChargeItem());
            accountBookGroupDto.setCostCenterList(accountBookGroupE.getCostCenter());
            accountBookGroupDto.setStatutoryBodyList(accountBookGroupE.getStatutoryBody());
            accountBookGroupDto.setCostCenterIdPath(handlePath(accountBookGroupE.getCostCenterPath()));
            accountBookGroupDto.setChargeItemIdPath(handlePath(accountBookGroupE.getChargeItemPath()));
            accountBookGroupDto.setStatutoryBodyIdPath(handlePath(accountBookGroupE.getStatutoryBodyPath()));
            accountBookGroupDtoList.add(accountBookGroupDto);
        });
        AccountbookDTO accountbookDTO = new AccountbookDTO().handleAccountBookE(accountBookE, statutoryBodyIdPath);
        accountbookDTO.setAccountBookGroupDtoList(accountBookGroupDtoList);
        return accountbookDTO;
    }

    /**
     * 处理路径
     *
     * @param path
     * @return
     */
    private List<List<Long>> handlePath(String path) {
        if (StringUtils.isNotBlank(path)) {
            JSONArray pathArray = JSON.parseArray(path);
            List<List<Long>> objects = Lists.newArrayList();
            for (Object o : pathArray) {
                List<Long> longs = JSON.parseArray(JSON.toJSONString(o), Long.class);
                objects.add(longs);
            }
            return objects;
        }
        return null;
    }


    /**
     * 根据账簿id获取账簿组合
     *
     * @param accountBookId
     * @return
     */
    public List<AccountBookGroupE> getAccountBookGroup(Long accountBookId) {
        return accountBookGropuRepository.getByAccountBookId(accountBookId);
    }

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    public AccountBookE detailAccountBookByChargeItemAndCostCenter(Long chargeItemId, Long costCenterId) {
        return accountBookRepository.detailAccountBookByChargeItemAndCostCenter(chargeItemId, costCenterId);
    }

    /**
     * 根据名称获取账簿
     * @param name
     * @return
     */
    public List<AccountBookE> searchAccountBook(String name) {
        return accountBookRepository.searchAccountBook(name);
    }

    public AccountBookE getAccountBookByCode(String accountBookCode) {
        return accountBookRepository.getByCode(accountBookCode);
    }

    public List<AccountBookE> getAccountBookByCodes(List<String> accountBookCodes) {
        return accountBookRepository.getByCodes(accountBookCodes);
    }

    public List<AccountBookE> selectListByCostChargeStatutory(Long costCenterId, Long chargeItemId, Long statutoryBodyId) {
        return accountBookRepository.listByCostAndChargeItem(costCenterId, chargeItemId, statutoryBodyId);
    }

    public List<AccountBookE> queryList(LambdaQueryWrapper queryWrapper) {
        return accountBookRepository.list(queryWrapper);
    }

    public Boolean update(LambdaUpdateWrapper updateWrapper) {
        return accountBookRepository.update(updateWrapper);
    }

    public Boolean updatebatch(List<AccountBookE> list) {
        return accountBookRepository.updateBatchById(list);
    }
    public Boolean savebatch(List<AccountBookE> list) {
        return accountBookRepository.saveBatch(list);
    }

    public List<AccountBookE> queryList(SearchF<?> form) {
        return accountBookRepository.queryList(form);
    }
}
