package com.wishare.finance.domains.configure.accountbook.command;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookGroupDto;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.HashSet;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description: 修改账簿command
 */
@Getter
@Setter
public class UpdateAccountBookCommand {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 账簿编码
     */
    private String code;

    /**
     * 账簿名称
     */
    private String name;

    /**
     * 法定单位id
     */
    private Long statutoryBodyId;

    /**
     * 法定单位名称
     */
    private String statutoryBodyName;

    /**
     * 账簿组合
     */
    private List<AccountBookGroupDto> accountBookGroupDtoList;

    /**
     * 备注
     */
    private String remark;

    /**
     * 凭证系统：1 用友ncc
     */
    private Integer voucherSys;

    /**
     * 是否禁用：0启用，1禁用
     */
    private Integer disabled;

    /**
     * 映射数值
     */
    private List<String> mapValues;

    /**
     * 账单系统来源
     */
    private List<Integer> sysSource;

    /**
     * 构建
     * @return
     */
    public List<AccountBookGroupE> getAccountGroup(Long accountBookId) {
        List<AccountBookGroupE> accountBookGroupEList = Lists.newArrayList();
        List<List<Long>> costCenterIdPath = null;
        List<List<Long>> chargeItemIdPath = null;
        List<List<Long>> statutoryBodyIdPath = null;
        for (AccountBookGroupDto accountBookGroupDto : accountBookGroupDtoList) {
            AccountBookGroupE accountBookGroupE = new AccountBookGroupE();
            accountBookGroupE.setId(IdentifierFactory.getInstance().generateLongIdentifier("account_book_group_id"));
            accountBookGroupE.setAccountBookId(accountBookId);
            costCenterIdPath = accountBookGroupDto.getCostCenterIdPath();
            chargeItemIdPath = accountBookGroupDto.getChargeItemIdPath();
            statutoryBodyIdPath = accountBookGroupDto.getStatutoryBodyIdPath();
            accountBookGroupE.setCostCenter(accountBookGroupDto.getCostCenterList());
            accountBookGroupE.setCostCenterPath(JSON.toJSONString(costCenterIdPath));
            accountBookGroupE.setChargeItem(accountBookGroupDto.getChargeItemList());
            accountBookGroupE.setChargeItemPath(JSON.toJSONString(chargeItemIdPath));
            accountBookGroupE.setStatutoryBody(accountBookGroupDto.getStatutoryBodyList());
            accountBookGroupE.setStatutoryBodyPath(JSON.toJSONString(statutoryBodyIdPath));
            accountBookGroupEList.add(accountBookGroupE);
        }
        return accountBookGroupEList;
    }

    public AccountBookE getAccountBook() {
        AccountBookE newAccountBookE = Global.mapperFacade.map(this, AccountBookE.class);
        newAccountBookE.setSysSource(this.getSysSource());
        newAccountBookE.setMapValues(JSON.toJSONString(this.getMapValues()));
        return newAccountBookE;
    }

}
