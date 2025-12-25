package com.wishare.finance.domains.configure.accountbook.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookMapper;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookOriginMapper;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.wishare.starter.utils.ThreadLocalUtil.curIdentityInfo;

/**
 * @author xujian
 * @date 2022/8/18
 * @Description:
 */
@Service
@Slf4j
public class AccountBookRepository extends ServiceImpl<AccountBookMapper, AccountBookE> {

    @Autowired
    private AccountBookMapper accountBookMapper;

    @Autowired
    private AccountBookOriginMapper accountBookOriginMapper;

    /**
     * 分页查询账簿列表
     *
     * @param form
     * @return
     */
    public Page<AccountBookE> queryPage(PageF<SearchF<?>> form) {
        //SearchF<?> conditions = form.getConditions();
        //List<Field> fields = conditions.getFields();
        //List<Field> relatedList = fields.stream().filter(s -> "cost_center_id".equals(s.getName())).collect(Collectors.toList());
        //Long costCenterId = null;
        //if (CollectionUtils.isNotEmpty(relatedList)) {
        //    costCenterId = (Long) relatedList.get(0).getValue();
        //    fields.removeAll(relatedList);
        //}
        //String applySql = handleQueryModel(conditions);
        //QueryWrapper<?> queryModel = conditions.getQueryModel();
        //if (StringUtils.isNotBlank(applySql)) {
        //    queryModel.or().apply(applySql);
        //}

        QueryWrapper<?> queryModel = handleQueryJson(form.getConditions());
        queryModel.eq("ab.tenant_id",curIdentityInfo().getTenantId());
        log.info("=======分页查询账簿列表使用原生JDBC预执行=======");
        return accountBookOriginMapper.queryPage(Page.of(form.getPageNum(), form.getPageSize(), form.isCount()), queryModel);
    }


    public List<AccountBookE> listByCostAndChargeItem(Long costCenterId, Long chargeItemId,Long statutoryBodyId){
        return baseMapper.selectListByCostAndChargeItem(costCenterId, chargeItemId, statutoryBodyId);
    }

    private QueryWrapper<?> handleQueryJson(SearchF<?> conditions){
        List<Field> fields = conditions.getFields();
        String applySql = "";
        Field field = null;
        HashSet<String> specFieldNames = new HashSet<>();
        String fieldValue = null;
        for (int i = 0; i < fields.size(); i++) {
            field = fields.get(i);
            Map<String, Object> fieldMap = field.getMap();
            if (Objects.nonNull(fieldMap) && !fieldMap.isEmpty()){
                specFieldNames.addAll(fieldMap.keySet());
                fieldValue = (String) fieldMap.values().stream().findAny().orElse("");
            }
            specFieldNames.add(field.getName());
            if (specFieldNames.contains("cost_center_name")){
                applySql += applySql.isBlank() ? "" : " or ";
                applySql += "abg.cost_center -> '$[*].costCenterName' like '%" + (Objects.isNull(fieldValue) ? field.getValue() : fieldValue) + "%'";
                fieldMap.remove("cost_center_name");
                fields.remove(i);
                i--;
            }else if (specFieldNames.contains("charge_item_name")){
                applySql += applySql.isBlank() ? "" : " or ";
                applySql += "abg.charge_item -> '$[*].chargeItemName' like '%" + (Objects.isNull(fieldValue) ? field.getValue() : fieldValue) + "%'";
                fieldMap.remove("charge_item_name");
                fields.remove(i);
                i--;
            }else if (specFieldNames.contains("cost_center_id")){
                applySql += applySql.isBlank() ? "" : " or ";
                applySql += "abg.cost_center -> '$[*].costCenterId' like '%" + (Objects.isNull(fieldValue) ? field.getValue() : fieldValue) + "%'";
                fieldMap.remove("cost_center_id");
                fields.remove(i);
                i--;
            }
        }


        return StringUtils.isNotBlank(applySql) ? conditions.getQueryModel().apply(applySql) : conditions.getQueryModel();
    }

    /**
     * 处理查询条件
     *
     * @param conditions
     * @return
     */
    private String handleQueryModel(SearchF<?> conditions) {
        List<Field> fields = conditions.getFields();
        String applySql = null;
        if (CollectionUtils.isNotEmpty(fields)) {
            for (int i = 0; i < fields.size(); i++) {
                Field field = fields.get(i);
                if (field.getName().equalsIgnoreCase("cost_center_name")) {
                    applySql = "JSON_CONTAINS(abg.cost_center,JSON_OBJECT( 'costCenterName', '"+field.getValue()+"'))";
                    fields.remove(i);
                    i--;
                    continue;
                }

                Map<String, Object> map = field.getMap();
                for (String key : field.getMap().keySet()) {
                    if (key.equalsIgnoreCase("charge_item_name")) {
                        String costCenterName = (String) map.get(key);
                        applySql = "JSON_CONTAINS(abg.cost_center,JSON_OBJECT( 'costCenterName', '"+costCenterName+"'))";
                        map.remove(key,map.get(key));
                        if (map.isEmpty()){
                            fields.remove(i);
                            i--;
                        }
                    }
                    /*if(key.equalsIgnoreCase("abg.charge_item_name")){
                        String chargeItemName = (String) map.get(key);
                        applySql = "JSON_CONTAINS(abg.charge_item,JSON_OBJECT( 'chargeItemName', '"+chargeItemName+"'))";
                        map.remove(key,map.get(key));
                    }*/
                }
            }
        }
        return applySql;
    }


    /**
     * 根据账簿编码获取账簿信息
     *
     * @param code
     * @return
     */
    public AccountBookE getByCode(String code) {
        LambdaQueryWrapper<AccountBookE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookE::getCode, code);
        return accountBookMapper.selectOne(wrapper);
    }

    /**
     * 根据账簿编码列表获取账簿信息
     *
     * @param codes
     * @return
     */
    public List<AccountBookE> getByCodes(List<String> codes) {
        return list(new LambdaQueryWrapper<AccountBookE>().in(AccountBookE::getCode, codes));
    }

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    public AccountBookE detailAccountBookByChargeItemAndCostCenter(Long chargeItemId, Long costCenterId) {
        return accountBookMapper.selectByChargeItemAndCostCenter(chargeItemId, costCenterId);
    }

    /**
     * 根据名称获取账簿
     * @param name
     * @return
     */
    public List<AccountBookE> searchAccountBook(String name) {
        LambdaQueryWrapper<AccountBookE> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(AccountBookE::getCode, name)
                .or()
                .like(AccountBookE::getName, name)
                .eq(AccountBookE::getDeleted, DataDeletedEnum.NORMAL.getCode())
                .eq(AccountBookE::getDisabled, DataDisabledEnum.启用.getCode())
                .last("limit 100");
        } else {
            wrapper.eq(AccountBookE::getDeleted, DataDeletedEnum.NORMAL.getCode())
                .eq(AccountBookE::getDisabled, DataDisabledEnum.启用.getCode())
                .last("limit 100");
        }

        return accountBookMapper.selectList(wrapper);
    }

    public List<AccountBookE> selectListByCostChargeStatutory(Long costCenterId, Long chargeItemId, Long statutoryBodyId) {
        return accountBookMapper.selectListByCostChargeStatutory(costCenterId,chargeItemId,statutoryBodyId);
    }

    public List<AccountBookE> queryList(SearchF<?> form) {
        return accountBookMapper.queryList(form.getQueryModel());
    }
}
