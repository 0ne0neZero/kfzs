package com.wishare.finance.domains.configure.accountbook.command;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountBookGroupDto;
import com.wishare.finance.apps.model.configure.accountbook.fo.ChargeItem;
import com.wishare.finance.apps.model.configure.accountbook.fo.CostCenter;
import com.wishare.finance.apps.model.configure.accountbook.fo.StatutoryBody;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookGroupE;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.helpers.UidHelper;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description: 新增账簿command
 */
@Getter
@Setter
public class AddAccountBookCommand {

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
     * 法定单位id路径
     */
    private List<Long> statutoryBodyIdPath;

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
     * 是否总账（默认否）
     */
    private Integer isGeneralLedger = 0;

    /**
     * 映射数值
     */
    private List<String> mapValues;

    /**
     * 账单系统来源
     */
    private List<Integer> sysSource;

    /**
     * 构建账簿主表
     * @return
     */
    public AccountBookE getAccountBook() {
        AccountBookE accountBookE = Global.mapperFacade.map(this, AccountBookE.class);
        accountBookE.setId(IdentifierFactory.getInstance().generateLongIdentifier("account_book_id"));
        accountBookE.setStatutoryBodyIdPath(JSON.toJSONString(this.getStatutoryBodyIdPath()));
        accountBookE.setSysSource(this.getSysSource());
        accountBookE.setMapValues(JSON.toJSONString(this.getMapValues()));
        return accountBookE;
    }

    /**
     * 构建
     * @return
     */
    public List<AccountBookGroupE> getAccountGroup(Long accountBookId) {
        List<AccountBookGroupE> accountBookGroupEList = Lists.newArrayList();
        accountBookGroupDtoList.forEach(accountBookGroupDto -> {
            AccountBookGroupE accountBookGroupE = new AccountBookGroupE();
            accountBookGroupE.setId(IdentifierFactory.getInstance().generateLongIdentifier("account_book_group_id"));
            accountBookGroupE.setAccountBookId(accountBookId);
            List<List<Long>> costCenterIdPath = accountBookGroupDto.getCostCenterIdPath();
            List<List<Long>> chargeItemIdPath = accountBookGroupDto.getChargeItemIdPath();
            List<List<Long>> statutoryBodyIdPath = accountBookGroupDto.getStatutoryBodyIdPath();
            accountBookGroupE.setCostCenter(accountBookGroupDto.getCostCenterList());
            accountBookGroupE.setCostCenterPath(CollectionUtils.isNotEmpty(costCenterIdPath)?JSON.toJSONString(costCenterIdPath):null);
            accountBookGroupE.setChargeItem(accountBookGroupDto.getChargeItemList());
            accountBookGroupE.setChargeItemPath(CollectionUtils.isNotEmpty(chargeItemIdPath)?JSON.toJSONString(chargeItemIdPath):null);
            accountBookGroupE.setStatutoryBody(accountBookGroupDto.getStatutoryBodyList());
            accountBookGroupE.setStatutoryBodyPath(CollectionUtils.isNotEmpty(statutoryBodyIdPath)?JSON.toJSONString(statutoryBodyIdPath):null);
            accountBookGroupEList.add(accountBookGroupE);
        });
        return accountBookGroupEList;
    }

    /**
     * 校验map规则
     * @param accountBookGroupDtoList
     */
    public static void checkMap(List<AccountBookGroupDto> accountBookGroupDtoList){
        if (CollectionUtils.isEmpty(accountBookGroupDtoList)){
            return;
        }
        HashSet<Long> costCenterSet = new HashSet<>();
        HashSet<String>   chargeItemSet = new HashSet<>();
        List<CostCenter> costCenterList = null;
        List<ChargeItem> chargeItemList = null;
        boolean checkFlag = false;
        for (AccountBookGroupDto accountBookGroupDto : accountBookGroupDtoList) {
            costCenterList = accountBookGroupDto.getCostCenterList();
            chargeItemList = accountBookGroupDto.getChargeItemList();
            if (CollectionUtils.isNotEmpty(costCenterList)){
                for (CostCenter costCenter : costCenterList) {
                    if (costCenterSet.contains(costCenter.getCostCenterId())){
                        checkFlag = true;
                    }else {
                        costCenterSet.add(costCenter.getCostCenterId());
                    }
                }
            }else {
                checkFlag = true;
            }
            if (CollectionUtils.isNotEmpty(chargeItemList)){
                for (ChargeItem chargeItem : chargeItemList) {
                    if (chargeItemSet.contains(chargeItem.getChargeItemId())){
                        if (checkFlag){
                            throw BizException.throw403(ErrorMessage.ACCOUNT_BOOK_MAP_ERROR.getErrMsg());
                        }
                    }
                }
            }else if (checkFlag){
                throw BizException.throw403(ErrorMessage.ACCOUNT_BOOK_MAP_ERROR.getErrMsg());
            }
        }

    }

    /**
     * 校验组织是否相同
     * @param accountBookGroupDtoList
     */
    public static void checkMaps(List<AccountBookGroupDto> accountBookGroupDtoList){
        if (CollectionUtils.isEmpty(accountBookGroupDtoList)){
            return;
        }
        HashSet<Long> costCenterSet = new HashSet<>();
        HashSet<Long> chargeItemSet = new HashSet<>();
        HashSet<Long> statutoryBodySet = new HashSet<>();
        List<CostCenter> costCenterList =null;
        List<ChargeItem> chargeItemList = null;
        List<StatutoryBody> statutoryBodyList = null;
        boolean check = false;
        boolean checks = false;
        boolean checkss = false;
        for (AccountBookGroupDto accountBookGroupDto:accountBookGroupDtoList) {
            //获取成本中心集合
            costCenterList = accountBookGroupDto.getCostCenterList();
            //获取费项集合
            chargeItemList = accountBookGroupDto.getChargeItemList();
            //获取法定单位集合
            statutoryBodyList = accountBookGroupDto.getStatutoryBodyList();
            //判断成本中心
            if (CollectionUtils.isEmpty(costCenterList)){
                check = true;
            }else {
                //不为空的情况下进行校验
                for (CostCenter costCenter :costCenterList) {
                    if (costCenterSet.contains(costCenter.getCostCenterId())){
                        check = true;
                    }else {
                        costCenterSet.add(costCenter.getCostCenterId());
                    }
                }
            }
            //判断费项
            if (CollectionUtils.isEmpty(chargeItemList)){
                checks = true;
            }else {
                //不为空的情况下进行校验
                for (ChargeItem chargeItem :chargeItemList) {
                    if (chargeItemSet.contains(chargeItem.getChargeItemId())){
                        checks = true;
                    }else {
                        chargeItemSet.add(chargeItem.getChargeItemId());
                    }
                }
            }
            //判断法定单位
            if (CollectionUtils.isEmpty(statutoryBodyList)){
                checkss = true;
            }else {
                //不为空的情况下进行校验
                for (StatutoryBody statutoryBody :statutoryBodyList) {
                    if (statutoryBodySet.contains(statutoryBody.getStatutoryBodyId())){
                        checkss = true;
                    }else {
                        statutoryBodySet.add(statutoryBody.getStatutoryBodyId());
                    }
                }
            }
            //成本中心和费项都相同的时候才会抛出异常
            if (check && checks && checkss){
                throw BizException.throw403(ErrorMessage.ACCOUNT_BOOK_MAP_ERROR.getErrMsg());
            }
        }
    }
}
