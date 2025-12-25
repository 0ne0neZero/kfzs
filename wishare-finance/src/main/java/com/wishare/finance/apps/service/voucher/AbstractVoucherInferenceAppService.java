package com.wishare.finance.apps.service.voucher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wishare.finance.apps.model.bill.fo.InvoiceBillDto;
import com.wishare.finance.apps.model.bill.fo.SettleChannelAndIdsF;
import com.wishare.finance.apps.model.bill.vo.BillInferenceV;
import com.wishare.finance.apps.model.bill.vo.BillSettleChannelV;
import com.wishare.finance.apps.model.bill.vo.BillSettleV;
import com.wishare.finance.apps.model.configure.accountbook.dto.AccountbookDTO;
import com.wishare.finance.apps.model.configure.chargeitem.vo.AssisteAccountV;
import com.wishare.finance.apps.model.configure.organization.vo.StatutoryBodyAccountV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectDetailV;
import com.wishare.finance.apps.model.configure.subject.vo.SubjectV;
import com.wishare.finance.apps.model.invoice.invoice.vo.InvoiceReceiptDetailV;
import com.wishare.finance.apps.service.bill.BillSettleAppService;
import com.wishare.finance.apps.service.configure.accountbook.AccountBookAppService;
import com.wishare.finance.apps.service.configure.organization.StatutoryBodyAccountAppService;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.facade.BillFacade;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.facade.AccountOrgFacade;
import com.wishare.finance.domains.configure.cashflow.entity.CashFlowE;
import com.wishare.finance.domains.configure.cashflow.service.CashFlowDomainService;
import com.wishare.finance.domains.configure.taxrate.entity.NccTaxRate;
import com.wishare.finance.domains.configure.taxrate.service.NccTaxRateDomainService;
import com.wishare.finance.domains.invoicereceipt.entity.invoicing.InvoiceReceiptDetailE;
import com.wishare.finance.domains.invoicereceipt.service.InvoiceDomainService;
import com.wishare.finance.domains.voucher.consts.enums.ActionEventEnum;
import com.wishare.finance.domains.voucher.consts.enums.BillSourceEnum;
import com.wishare.finance.domains.voucher.consts.enums.EventTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherE;
import com.wishare.finance.domains.voucher.entity.VoucherInferenceRecordE;
import com.wishare.finance.domains.voucher.entity.VoucherRuleE;
import com.wishare.finance.domains.voucher.facade.OldVoucherFacade;
import com.wishare.finance.domains.voucher.model.PrefabricationVoucher;
import com.wishare.finance.domains.voucher.model.SimpleVoucherBill;
import com.wishare.finance.domains.voucher.model.SupItem;
import com.wishare.finance.domains.voucher.model.VoucherDetail;
import com.wishare.finance.domains.voucher.model.VoucherRuleCondition;
import com.wishare.finance.domains.voucher.service.VoucherDomainService;
import com.wishare.finance.domains.voucher.service.VoucherInferenceRecordDomainService;
import com.wishare.finance.domains.voucher.service.OldVoucherRuleDomainService;
import com.wishare.finance.infrastructure.conts.ErrorMessage;
import com.wishare.finance.infrastructure.identifier.IdentifierFactory;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.AssF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.CashFlowF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.DetailsF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.VoucherF;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.VoucherHeadF;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.starter.beans.IdentityInfo;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.utils.ThreadLocalUtil;
import com.wishare.tools.starter.fo.search.Field;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pgq
 * @since: 2022/11/15 14:25
 * @version: 1.0.0
 */
@Service
@Slf4j
public abstract class AbstractVoucherInferenceAppService implements IVoucherInferenceAppService{

    @Resource
    @Lazy
    VoucherInferenceAppServiceFactory voucherInferenceAppServiceFactory;
    @Autowired
    protected BillFacade billFacade;
    @Autowired
    protected OldVoucherFacade oldVoucherFacade;
    @Autowired
    protected AccountOrgFacade accountOrgFacade;
    @Autowired
    protected InvoiceDomainService invoiceDomainService;
    @Autowired
    protected VoucherDomainService voucherDomainService;
    @Autowired
    private OldVoucherRuleDomainService oldVoucherRuleDomainService;
    @Autowired
    protected VoucherInferenceRecordDomainService voucherInferenceRecordDomainService;
    @Autowired
    private BillSettleAppService billSettleAppService;
    @Autowired
    private AccountBookAppService accountBookAppService;
    @Autowired
    private StatutoryBodyAccountAppService statutoryBodyAccountAppService;
    @Autowired
    private CashFlowDomainService cashFlowDomainService;
    @Autowired
    private NccTaxRateDomainService nccTaxRateDomainService;

    @Value("${spring.profiles.active}")
    private String springProfilesActive;
    @Value("${ncc.testAccBook:false}")
    private boolean testAccBook;

    /**
     * 事件类型
     * @return
     */
    @Override
    public EventTypeEnum getEventType() {
        return null;
    }

    /**
     * 推凭公共的操作
     * @param record
     * @param isSingle
     */
    @Override
    public void inference(VoucherRuleE record, boolean isSingle){

        setTenantId(record.getTenantId());

        String conditions = record.getConditions();
        if (StringUtils.isEmpty(conditions)) {
            return;
        }
        List<Field> fieldList = new ArrayList<>();
        getStatutoryBodyId(conditions, fieldList, "b");
        getCommunityId(conditions, fieldList, "b");
        getChargeItemId(conditions, record, fieldList, "b");
//        getPayChannel(conditions, fieldList, "b");
        getCustomer(conditions, fieldList, "b");
        getStartTime(conditions, fieldList, "b");
        getEndTime(conditions, fieldList, "b");

        voucherInferenceAppServiceFactory.getInstance(EventTypeEnum.valueOfByCodeByEvent(record.getEventType())).getBillStatus(record, fieldList);


        voucherInferenceAppServiceFactory.getInstance(EventTypeEnum.valueOfByCodeByEvent(record.getEventType()))
            .inferenceByBillType(record.getEventType(), fieldList, conditions, record, isSingle);
//        advanceInference(record.getEventType(), record.getChargeItemId(), fieldList, conditions, record, isSingle);
//
//        receivableInference(record.getEventType(), record.getChargeItemId(), fieldList, conditions, record, isSingle);
//
//        temporaryInference(record.getEventType(), record.getChargeItemId(), fieldList, conditions, record, isSingle);

    }

    // 客商
    protected void getCustomer(String entries, List<Field> fieldList, String alia) {
        // todo 改客商
        addBillSearchFiled(entries, fieldList, "5", alia + ".customer_id");
    }

    // 支付渠道
    protected void getPayChannel(String entries, List<Field> fieldList, String alia) {
        addBillSearchFiled(entries, fieldList, "13", alia + ".pay_channel");
    }

    // 计费周期
    protected void getStartTime(String entries, List<Field> fieldList, String alia) {
        addBillSearchFiledByTime(entries, fieldList, "19", alia + ".start_time");
    }

    // 计费周期
    protected void getEndTime(String entries, List<Field> fieldList, String alia) {
        addBillSearchFiledByTime(entries, fieldList, "20", alia + ".end_time");
    }

    // 结算时间
    protected void getPayTime(String entries, List<Field> fieldList, String alia) {
        addBillSearchFiledByTime(entries, fieldList, "18", alia + ".pay_time");
    }

    // 收款银行账户
    protected void getIncomeBankAccount(String entries, List<Field> fieldList, String alia, String fieldName) {
        addBillSearchFiled(entries, fieldList, "9", alia + "." + fieldName);
    }

    // 付款银行账户
    protected void getPayBankAccount(String entries, List<Field> fieldList, String alia, String fieldName) {
        addBillSearchFiled(entries, fieldList, "10", alia + "." + fieldName);
    }

    @Override
    public void inferenceByBillType(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        advanceInference(record.getEventType(), fieldList, conditions, record, isSingle);

        receivableInference(record.getEventType(), fieldList, conditions, record, isSingle);

        temporaryInference(record.getEventType(), fieldList, conditions, record, isSingle);
    }

    /**
     * 给ThreadLocal加入 租户id，用于整个线程
     * @param tenantId
     */
    protected void setTenantId(String tenantId) {

        IdentityInfo identityInfo = new IdentityInfo();
        identityInfo.setTenantId(tenantId);
        ThreadLocalUtil.set("IdentityInfo", identityInfo);

    }

    /**
     * 构造法定单位
     * @param jsonStr
     * @param fieldList
     * @param alia
     */
    protected void getStatutoryBodyId(String jsonStr, List<Field> fieldList, String alia) {
        addBillSearchFiled(jsonStr, fieldList, "1", alia + ".statutory_body_id");
    }

    /**
     * 构造费项
     * @param jsonStr
     * @param record
     * @param fieldList
     * @param alia
     */
    protected void getChargeItemId(String jsonStr, VoucherRuleE record, List<Field> fieldList, String alia) {
        //if (StringUtils.isNotBlank(record.getChargeItemId())) {
        //    List<Long> list = JSONArray.parseArray(record.getChargeItemId(), Long.class);
        //    if (!CollectionUtils.isEmpty(list)) {
        //        Optional<VoucherRuleCondition> conditionOptional = getCondition(jsonStr, "14");
        //
        //        if (conditionOptional.isPresent()) {
        //            Field field = new Field();
        //            VoucherRuleCondition condition = conditionOptional.get();
        //            field.setMethod(getMethodNum(condition.getCompare()));
        //            field.setValue(list);
        //            field.setName(alia + ".charge_item_id");
        //            fieldList.add(field);
        //        }
        //    }
        //}
    }

    /**
     * 构造成本中心
     * @param entries
     * @param fieldList
     * @param alia
     */
    protected void getCommunityId(String entries, List<Field> fieldList, String alia) {
        addBillSearchFiled(entries, fieldList, "2", alia + ".cost_center_id");
    }

    /**
     * 构造账单查询字段条件
     * @param jsonStr
     * @param fieldList
     * @param key
     * @param name
     */
    private void addBillSearchFiled(String jsonStr, List<Field> fieldList, String key, String name) {
        Optional<VoucherRuleCondition> conditionOptional = getCondition(jsonStr, key);

        if (conditionOptional.isPresent()) {
            VoucherRuleCondition condition = conditionOptional.get();
            JSONArray value = (JSONArray) condition.getValue();
            if (!CollectionUtils.isEmpty(value)) {
                Field field = new Field();
                field.setMethod(getMethodNum(condition.getCompare()));
                field.setValue(getValue(key, value, field.getMethod()));
                field.setName(name);
                fieldList.add(field);
            }
        }
    }

    /**
     * 构造账单查询字段条件 - 时间选择器类型
     * @param jsonStr
     * @param fieldList
     * @param key
     * @param name
     */
    private void addBillSearchFiledByTime(String jsonStr, List<Field> fieldList, String key, String name) {
        Optional<VoucherRuleCondition> conditionOptional = getCondition(jsonStr, key);

        if (conditionOptional.isPresent()) {
            VoucherRuleCondition<String> condition = conditionOptional.get();
            String value = condition.getValue();
            if (StringUtils.isBlank(value)) {
                Field field = new Field();
                field.setMethod(getMethodNum(condition.getCompare()));
                field.setValue(value);
                field.setName(name);
                fieldList.add(field);
            }
        }
    }

    /**
     * 获取条件
     * @return
     */
    protected Optional<VoucherRuleCondition> getCondition(String jsonStr, String key) {
        Map<String, VoucherRuleCondition> conditionMap = conditionList2Map(JSONArray.parseArray(jsonStr, VoucherRuleCondition.class));


        if (!CollectionUtils.isEmpty(conditionMap) && conditionMap.containsKey(key)) {
           return Optional.of(conditionMap.get(key));
        }
        return Optional.empty();
    }

    protected Map<String, VoucherRuleCondition> conditionList2Map(List<VoucherRuleCondition> conditionList) {
        if (CollectionUtils.isEmpty(conditionList)) {
            return null;
        }
        return conditionList.stream().collect(Collectors.toMap(VoucherRuleCondition::getConditions, Function.identity()));
    }

    // 条件转化
    protected Integer getMethodNum(String compare) {
        switch (compare) {
            case "0": // 等于
                return 1;
            case "1": // in
                return 15;
            case "2": // not in
                return 16;
            case "3": // 大于等于
                return 4;
            case "4": // 小于等于
                return 6;
            default:
                return 1;
        }
    }

    // 取值
    protected Object getValue(String key, JSONArray value, Integer compare) {
        if (value.isEmpty()) {
            return null;
        }

        // 当条件是等于的时候，只取第一个值
        if (compare == 1) {
            if ("11".equals(key)) {
                for (int i = 0; i < value.size(); i++) {
                    JSONArray jsonArray = value.getJSONArray(i);
                    if (!jsonArray.isEmpty()) {
                        return value.getString(jsonArray.size() - 1);
                    }
                }
            }
            return value.getString(value.size() - 1);
        }
        // 条件为包含或者不包含时
        if ("11".equals(key)) {
            List<Long> list = new ArrayList();
            for (int i = 0; i < value.size(); i++) {
                JSONArray jsonArray = value.getJSONArray(i);
                if (!jsonArray.isEmpty()) {
                    list.addAll(jsonArray.toJavaList(Long.class));
                }
            }
            return list;
        }
        return new ArrayList(value);
    }

    /**
     * 临时账单
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    protected void temporaryInference(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.临时收费账单, isSingle);
    }

    /**
     * 应收账单
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    protected void receivableInference(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.应收账单, isSingle);
    }

    /**
     * 预收账单
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    protected void advanceInference(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, boolean isSingle) {
        billInference(eventType, fieldList, conditions, record, BillTypeEnum.预收账单, isSingle);
    }

    /**
     * 账单推凭
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    protected void billInference(Integer eventType, List<Field> fieldList,
        String conditions, VoucherRuleE record, BillTypeEnum billTypeEnum, boolean isSingle) {
        long pageNum = 1;
        boolean isLast = false;
        List<VoucherRuleCondition> conditionList = JSONArray.parseArray(conditions, VoucherRuleCondition.class);

        do {
            PageV<BillInferenceV> page = billFacade.listBillInferenceInfo(eventType, fieldList, billTypeEnum, pageNum);
//            log.info("eventType【{}】 -  billTypeEnum【{}】 ： total 【{}】", eventType, billTypeEnum, page.getTotal());
            pageNum++;
            List<BillInferenceV> records = page.getRecords();
            if (records != null && !records.isEmpty()) {
                records = filterBill(records, conditionList);

//                log.info("after filter ============== eventType【{}】 -  billTypeEnum【{}】 ： total 【{}】", eventType, billTypeEnum, page.getTotal());
                if (records != null && !records.isEmpty()) {
                    if (isSingle) {
                        writeDownList(records, eventType, billTypeEnum, record);
                    } else {
                        // 根据项目、费项、成本中心联合分组
                        Map<String, List<BillInferenceV>> collect = groupByBase(records);
                        // todo 根据辅助核算进行分组
                        batchWriteDownListGroupBySup(collect, record,
                            EventTypeEnum.valueOfByCodeByEvent(eventType), billTypeEnum, false);
//                        // 根据项目分组 第二版
//                        Map<String, List<BillInferenceV>> communityMap = records.stream()
//                            .collect(Collectors.groupingBy(BillInferenceV::getCommunityId));
//                        if (!CollectionUtils.isEmpty(communityMap)) {
//                            Iterator<Entry<String, List<BillInferenceV>>> communityIt = communityMap.entrySet().iterator();
//                            while (communityIt.hasNext()) {
//                                // 根据费项分组
//                                Map<Long, List<BillInferenceV>> map = communityIt.next().getValue().stream()
//                                    .collect(Collectors.groupingBy(BillInferenceV::getChargeItemId));
//                                if (!CollectionUtils.isEmpty(map)) {
//                                    Iterator<Entry<Long, List<BillInferenceV>>> it = map.entrySet()
//                                        .iterator();
//                                    while (it.hasNext()) {
//                                        // 根据成本中心分组
//                                        Map<Long, List<BillInferenceV>> costMap = it.next()
//                                            .getValue()
//                                            .stream()
//                                            .collect(
//                                                Collectors.groupingBy(
//                                                    BillInferenceV::getCostCenterId));
//                                        if (!CollectionUtils.isEmpty(costMap)) {
//                                            Iterator<Entry<Long, List<BillInferenceV>>> costIt = costMap.entrySet()
//                                                .iterator();
//                                            while (costIt.hasNext()) {
//                                                batchWriteDownList(costIt.next().getValue(), record,
//                                                    EventTypeEnum.valueOfByCodeByEvent(eventType),
//                                                    billTypeEnum,
//                                                    false);
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
            isLast = page.isLast();
        } while (!isLast);
    }

    /**
     * 根据费项、项目、成本中心分类
     * @param records
     */
    private Map<String, List<BillInferenceV>> groupByBase(List<BillInferenceV> records) {
        return records.stream().collect(Collectors.groupingBy(
            item ->
                String.format("%s#%s#%s#%s", item.getCommunityId(), item.getChargeItemId(),
                    item.getCostCenterId(), item.getChargeItemName())
        ));
    }

    /**
     * 根据辅助核算分组
     * @param map
     * @param record
     * @param eventTypeEnum
     * @param billTypeEnum
     * @param isSingle
     */
    private List<String> batchWriteDownListGroupBySup(Map<String, List<BillInferenceV>> map, VoucherRuleE record, EventTypeEnum eventTypeEnum, BillTypeEnum billTypeEnum, boolean isSingle) {
        if (StringUtils.isBlank(record.getEntries())) {
            throw BizException.throw404("找不到分录信息");
        }
        List <JSONObject> subjects = JSONArray.parseArray(record.getEntries(), JSONObject.class);

        Set<String> set = new HashSet<>();
        for (JSONObject subject : subjects) {
            if (StringUtils.isNotBlank(subject.getString("supItemName"))) {
                set.addAll(List.of(subject.getString("supItemName").split(";")));
            }
        }

        List<VoucherE> list = new ArrayList<>();
        Map<String, List<BillInferenceV>> detailMap = new HashMap<>();
        for (List<BillInferenceV> value : map.values()) {
            if (!CollectionUtils.isEmpty(map.values())) {
                record = dealVoucherSubjects(record, value.get(0));
                for (BillInferenceV billInferenceV : value) {
                    billInferenceV.setOutBillNo("");
                    dealVoucherSubjects(set, billInferenceV, billTypeEnum, record);
                }
                Map<String, List<BillInferenceV>> collect = value.stream()
                    .collect(Collectors.groupingBy(BillInferenceV::getOutBillNo));

                for (List<BillInferenceV> billInferenceVS : collect.values()) {
                    VoucherE voucherE = BatchGeneratePrefabricationVoucher(billInferenceVS,
                        billTypeEnum, eventTypeEnum, record);
                    List<Long> concatIds = billInferenceVS.stream().map(BillInferenceV::getConcatId).collect(Collectors.toList());
                    List<Long> inferIds = billFacade.batchInsertInference(billInferenceVS.stream().map(BillInferenceV::getId).collect(Collectors.toList()),
                        concatIds, billTypeEnum.getCode(), eventTypeEnum.getEvent());
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("inferIds", inferIds);
                    jsonObject.put("concatIds", concatIds);
                    jsonObject.put("eventTypeEnum", eventTypeEnum.getEvent());
                    jsonObject.put("billTypeEnum", billTypeEnum.getCode());
                    voucherE.setBillInferIds(JSON.toJSONString(jsonObject));
                    list.add(voucherE);
                    detailMap.put(voucherE.getVoucherNo(), billInferenceVS);
                }
            }
        }

        voucherDomainService.batchInsert(list);
        VoucherInferenceRecordE voucherInferenceRecordE = buildInferRecordForAuto(list, map.keySet(), record);
        list.forEach(item -> batchInsertVoucherBills(detailMap.get(item.getVoucherNo()), billTypeEnum, Collections.singletonList(item.getId())));
        return list.stream().map(VoucherE::getVoucherNo).collect(Collectors.toList());
    }

    private void dealVoucherSubjects(Set<String> supItemNames, BillInferenceV bill, BillTypeEnum billTypeEnum, VoucherRuleE voucherRule) {

        StringBuilder stringBuilder = new StringBuilder();
        StringJoiner joiner = new StringJoiner("#");
        for (String supItemName : supItemNames) {
            stringBuilder.append("【").append(supItemName).append("】");
            if (bill == null) {
                continue;
            }
            switch (supItemName) {
                case "客商":
                    if (bill.getCustomerLabel() != null && bill.getCustomerLabel() == 2) {
                        if (BillTypeEnum.应收账单 == billTypeEnum || BillTypeEnum.预收账单 == billTypeEnum
                            || BillTypeEnum.收款单 == billTypeEnum
                            || BillTypeEnum.临时收费账单 == billTypeEnum) {
                            stringBuilder.append(bill.getPayerName());
                            joiner.add(bill.getPayerName());
                        } else {
                            stringBuilder.append(bill.getPayeeName());
                            joiner.add(bill.getPayeeName());
                        }
                    } else {
                        String s = "业主";
//                        if (BillTypeEnum.收款单 == billTypeEnum || BillTypeEnum.付款单 == billTypeEnum) {
//                            s = bill.getPayerName();
//                        }
                        stringBuilder.append(s);
                        joiner.add(s);
                    }
                    if (EventTypeEnum.未认领暂收款.getEvent() == voucherRule.getEventType()) {
                        stringBuilder.append("临时客商");
                        joiner.add("临时客商");
                    }
                    break;
                case "项目":
                case "部门":
                    if (bill.getCostCenterName() == null) {
                        joiner.add("");
                        break;
                    }
                    stringBuilder.append(bill.getCostCenterName());
                    joiner.add(bill.getCostCenterName());
                    break;
                case "业务类型":
                    joiner.add("");
                    break;
                case "银行账户":
                    if (bill.getSbAccountId() == null) {
                        joiner.add("");
                        break;
                    }
                    StatutoryBodyAccountV statutoryBodyAccountV = statutoryBodyAccountAppService.detailStatutoryBodyAccount(bill.getSbAccountId());
                    if (Optional.ofNullable(statutoryBodyAccountV).isPresent()) {
                        stringBuilder.append(statutoryBodyAccountV.getName());
                        joiner.add(statutoryBodyAccountV.getName());
                    }
                    break;
                case "存款账户性质":
                    stringBuilder.append("活期");
                    joiner.add("活期");
                    break;
                case "坏账准备增减方式":
                    stringBuilder.append("资产核销减少");
                    joiner.add("资产核销减少");
                    break;
                case "增值税税率":
                    if (null != bill.getTaxRate()) {
                        stringBuilder.append(bill.getTaxRate().doubleValue() * 100).append("%");
                        joiner.add(bill.getTaxRate().doubleValue() * 100 + "%");
                    } else {
                        stringBuilder.append("0%");
                        joiner.add("0%");
                    }
                    break;
                default:
                    joiner.add("");
                    break;
            }
            stringBuilder.append(";");
        }
        // 无用字段，暂存数据
        if (bill != null) {
            bill.setOutBusNo(stringBuilder.toString());
            bill.setOutBillNo(joiner.toString());
        }
    }

    /**
     * 预制凭证
     * @param billInferenceVS
     * @param billTypeEnum
     * @param eventTypeEnum
     * @param voucherRule
     */
    private VoucherE BatchGeneratePrefabricationVoucher(List<BillInferenceV> billInferenceVS,
        BillTypeEnum billTypeEnum, EventTypeEnum eventTypeEnum, VoucherRuleE voucherRule) {
        VoucherE voucherE = new VoucherE();
        List<SimpleVoucherBill> billList = billInferenceVS.stream()
            .map(item -> new SimpleVoucherBill(item.getId(), item.getBillNo(), billTypeEnum.getCode()))
            .collect(Collectors.toList());
        voucherE.setBillList(JSON.toJSONString(billList));
        voucherE.setBillId(0L);
        voucherE.setBillNo("");
        voucherE.setBillType(billTypeEnum.getCode());
        voucherE.setVoucherNo(IdentifierFactory.getInstance().serialNumber("voucher", "PZ", 20));
        Long amount = billInferenceVS.stream()
            .mapToLong(bill -> voucherInferenceAppServiceFactory.getInstance(eventTypeEnum).generateInferenceAmount(bill, billTypeEnum))
            .sum();
        voucherE.setVoucherType("记账凭证");
        voucherE.setAmount(amount);
        voucherE.setInferenceState(0);

        BillInferenceV bill = billInferenceVS.get(0);
        voucherE.setStatutoryBodyId(bill.getStatutoryBodyId());
        voucherE.setStatutoryBodyName(bill.getStatutoryBodyName());
        voucherE.setMaker("系统");
        voucherE.setMadeType("自动录制");
        voucherE.setReason("");

        voucherE.setDetails(buildDetailForPrefabrication(voucherRule, voucherE, billTypeEnum, bill, true));
        voucherE.setPrefabricationDetails(buildPrefabricationVoucher(voucherRule, voucherE, billTypeEnum, bill, true));

        return voucherE;
    }

    private String buildPrefabricationVoucher(VoucherRuleE voucherRule, VoucherE voucherE, BillTypeEnum billTypeEnum, BillInferenceV bill, boolean b) {
        PrefabricationVoucher preVoucher = new PrefabricationVoucher();
        preVoucher.setVoucherType("记账凭证");
        preVoucher.setBillNo("");
        preVoucher.setBillId(0L);
        preVoucher.setMaker("系统");
        preVoucher.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now()));
        Optional<AccountBookE> accountBookE = mapAccountBook(bill.getChargeItemId(),
            bill.getCostCenterId());
        preVoucher.setAccountBookId(0L);
        preVoucher.setAccountBookCode("010010010A-0001");
        accountBookE.ifPresent(item ->  {
            preVoucher.setAccountBookId(item.getId());
            preVoucher.setAccountBookCode(item.getCode());
        });

        List<VoucherDetail> list = new ArrayList<>();
        String detailsStr = voucherE.getDetails();
        if (StringUtils.isNotBlank(detailsStr)) {
            JSONArray array = JSONArray.parseArray(detailsStr);
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                VoucherDetail voucherDetail = new VoucherDetail();
                voucherDetail.setType(jsonObject.getString("type"));
                voucherDetail.setSubjectName(jsonObject.getString("subjectName"));
                voucherDetail.setSubjectCode(jsonObject.getString("subjectCode"));
                voucherDetail.setSubjectId(jsonObject.getLong("subjectId"));
                voucherDetail.setRuleRemark(jsonObject.getString("ruleRemark"));
                if ("debit".equals(voucherDetail.getType())) {
                    voucherDetail.setDebitAmount(AmountUtils.toDecimal(jsonObject.getLong("amount")));
                    voucherDetail.setCreditAmount(BigDecimal.ZERO);
                } else {
                    voucherDetail.setCreditAmount(AmountUtils.toDecimal(jsonObject.getLong("amount") / 100.0));
                    voucherDetail.setDebitAmount(BigDecimal.ZERO);

                }
                List<String> auxiliaryCountList;
                List<SupItem> supItems = new ArrayList<>();
                if (StringUtils.isNotBlank(jsonObject.getString("auxiliaryCount")) &&
                    !CollectionUtils.isEmpty(auxiliaryCountList = JSONArray.parseArray(jsonObject.getString("auxiliaryCount"), String.class))) {
                    for (String code : auxiliaryCountList) {
                        SupItem supItem = new SupItem();
                        supItem.setCode(buildValue(code, bill, billTypeEnum, voucherRule));
                        supItem.setName("");
                        supItem.setType(code);
                        supItem.setValueName("");
                        supItems.add(supItem);
                    }
                }

                voucherDetail.setSupItems(supItems);

                list.add(voucherDetail);
            }
        }

        preVoucher.setDetails(list);
        return JSON.toJSONString(preVoucher);
    }

    /**
     *
     * @param voucherRule
     * @param voucherE
     * @param billTypeEnum
     * @param bill
     * @param isSingle
     * @return
     */
    private String buildDetailForPrefabrication(VoucherRuleE voucherRule, VoucherE voucherE,
        BillTypeEnum billTypeEnum, BillInferenceV bill, boolean isSingle) {
        if (StringUtils.isEmpty(voucherRule.getEntries())) {
            return "";
        }
        JSONArray subjects = JSONArray.parseArray(voucherRule.getEntries());
        if (subjects.isEmpty()) {
            return "";
        }
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            subject.put("current", "人民币");
            subject.put("voucherType", "记账凭证");
            if (!subject.containsKey("subjectId")) {
                subject.put("subjectName", "");
            } else {
                Long subjectId = subject.getLong("subjectId");
                map.put(subjectId, i);
            }
        }

        // 批量查科目分录
        List<SubjectV> subjectRVS = oldVoucherFacade.listByIds(new ArrayList<>(map.keySet()));
        List<SubjectV> rateSubjects = subjectRVS.stream()
            .filter(item -> 1 == item.getExistTax()).collect(Collectors.toList());
        boolean exitRate = !CollectionUtils.isEmpty(rateSubjects);
        BigDecimal decimal = new BigDecimal(voucherE.getAmount());
        boolean debitRate = false;
        boolean creditRate = false;
        Map<Long, SubjectV> subjectVMap = subjectRVS.stream()
            .collect(Collectors.toMap(SubjectV::getId, Function.identity()));
        if (exitRate) {
            List<Long> collect = rateSubjects.stream().map(SubjectV::getId)
                .collect(Collectors.toList());

            for (int i = 0; i < subjects.size(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                if (!subject.containsKey("subjectId")) {
                    subject.put("subjectName", "");
                } else {
                    Long subjectId = subject.getLong("subjectId");
                    map.put(subjectId, i);
                    SubjectV subjectRV = subjectVMap.get(subjectId);
                    if (1 == subjectRV.getExistTax()) {
                        if (null != bill.getTaxRate()) {
                            decimal = new BigDecimal(voucherE.getAmount()).divide(
                                new BigDecimal(1).add(bill.getTaxRate()), RoundingMode.HALF_EVEN);
                            subject.put("amount", voucherE.getAmount() - decimal.longValue());
                        } else {
                            subject.put("amount", 0);
                        }

                        filterSomeSubject(subject, subjectRV, bill, voucherRule);

                        if ("debit".equals(subject.getString("type"))) {
                            subject.put("localDebit", subject.get("amount"));
                            subject.put("localCredit", 0);
                            debitRate = true;
                        } else {
                            subject.put("localDebit", 0);
                            subject.put("localCredit", subject.get("amount"));
                            creditRate = true;
                        }
                    }
                }
            }
//            for (SubjectV subjectRV : rateSubjects) {
//                JSONObject subject = subjects.getJSONObject(map.get(subjectRV.getId()));
//
//                if (null != bill.getTaxRate()) {
//                    decimal = new BigDecimal(voucherE.getAmount()).divide(
//                        new BigDecimal(1).add(bill.getTaxRate()), RoundingMode.HALF_EVEN);
//                    subject.put("amount", voucherE.getAmount() - decimal.longValue());
//                } else {
//                    subject.put("amount", 0);
//                }
//
//                filterSomeSubject(subject, subjectRV, bill, voucherRule);
//
//                if ("debit".equals(subject.getString("type"))) {
//                    subject.put("localDebit", subject.get("amount"));
//                    subject.put("localCredit", 0);
//                    debitRate = true;
//                } else {
//                    subject.put("localDebit", 0);
//                    subject.put("localCredit", subject.get("amount"));
//                    creditRate = true;
//                }
//            }
        }
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            if (!subject.containsKey("subjectId")) {
                subject.put("subjectName", "");
            } else {
                Long subjectId = subject.getLong("subjectId");
//                map.put(subjectId, i);
                SubjectV subjectRV = subjectVMap.get(subjectId);
                subject.put("subjectName", buildSubjectName(subjectRV));
                if (0 == subjectRV.getExistTax()) {

                    filterSomeSubject(subject, subjectRV, bill, voucherRule);

                    if ("debit".equals(subject.getString("type")) && debitRate) {
                        subject.put("amount", decimal.longValue());
                        subject.put("localDebit", subject.get("amount"));
                        subject.put("localCredit", 0);
                    } else if ("icredit".equals(subject.getString("type")) && creditRate) {
                        subject.put("amount", decimal.longValue());
                        subject.put("localDebit", 0);
                        subject.put("localCredit", subject.get("amount"));
                    } else if ("icredit".equals(subject.getString("type"))) {
                        subject.put("amount", voucherE.getAmount());
                        subject.put("localDebit", 0);
                        subject.put("localCredit", subject.get("amount"));
                    } else  {
                        subject.put("amount", voucherE.getAmount());
                        subject.put("localDebit", subject.get("amount"));
                        subject.put("localCredit", 0);
                    }
                }
            }
        }
//        for (SubjectV subjectRV : subjectRVS) {
//            if (0 == subjectRV.getExistTax()) {
//                JSONObject subject = subjects.getJSONObject(map.get(subjectRV.getId()));
//
//                filterSomeSubject(subject, subjectRV, bill, voucherRule);
//
//                if ("debit".equals(subject.getString("type")) && debitRate ||
//                    "icredit".equals(subject.getString("type")) && creditRate) {
//                    subject.put("amount", decimal.longValue());
//                    subject.put("localDebit", subject.get("amount"));
//                    subject.put("localCredit", 0);
//                } else {
//                    subject.put("amount", voucherE.getAmount());
//                    subject.put("localDebit", 0);
//                    subject.put("localCredit", subject.get("amount"));
//                }
//            }
//        }
//        voucherRule.setEntries(JSON.toJSONString(subjects));
        return JSON.toJSONString(subjects);
    }

    /**
     * 构建科目名称
     * @param subjectV
     * @return
     */
    private String buildSubjectName(SubjectV subjectV) {
        if (CollectionUtils.isEmpty(subjectV.getSubjectPath())) {
            return subjectV.getSubjectName();
        }

        List<SubjectV> subjectRVS = oldVoucherFacade.listByIds(subjectV.getSubjectPath());
        Map<Long, String> map = subjectRVS.stream()
            .collect(Collectors.toMap(SubjectV::getId, SubjectV::getSubjectName));
        StringJoiner joiner = new StringJoiner("/");
        for (Long subjectId : subjectV.getSubjectPath()) {
            if (map.containsKey(subjectId)) {
                joiner.add(map.get(subjectId));
            }
        }
        joiner.add(subjectV.getSubjectName());

        return joiner.toString();
    }

    /**
     * 填充属性
     * @param subject
     * @param subjectRV
     * @param bill
     * @param voucherRule
     */
    private void filterSomeSubject(JSONObject subject, SubjectV subjectRV,
        BillInferenceV bill, VoucherRuleE voucherRule) {
        subject.put("subjectCode", subjectRV.getSubjectCode());
        subject.put("auxiliaryCount", subjectRV.getAuxiliaryCountList());
        subject.put("ruleRemark", dealRuleMark(voucherRule.getRuleRemark(), bill.getChargeItemName()));
        subject.put("supItemName", replaceByStr(subject.getString("supItemName"), bill.getOutBusNo()));
    }

    /**
     * 辅助核算项替代
     * @param subjectName
     * @param outBusNo
     * @return
     */
    private String replaceByStr(String subjectName, String outBusNo) {
        if (StringUtils.isBlank(outBusNo) || StringUtils.isBlank(subjectName)) {
            return subjectName;
        }
        List<String> collect = Arrays.stream(outBusNo.split(";")).collect(Collectors.toList());

        StringJoiner joiner = new StringJoiner(";");
        for (String name : subjectName.split(";")) {
            for (String s : collect) {
                if (s.contains("【" + name + "】")) {
                    joiner.add(s);
                }
            }
        }
        return joiner.toString();
    }


    /**
     * 过滤数据
     * @param records
     * @param conditionList
     */
    private List<BillInferenceV> filterBill(List<BillInferenceV> records, List<VoucherRuleCondition> conditionList) {

        Map<String, VoucherRuleCondition> conditionMap = conditionList2Map(conditionList);
        if (!CollectionUtils.isEmpty(conditionMap)) {
            // 判断账单来源
            records = filterBySource(records, conditionMap);

            // 结算方式
            records = filterBySettleType(records, conditionMap);

            // 票据类型
            records = filterByInvoiceType(records, conditionMap);

            // 税率
            return filterByTaxRate(records, conditionMap);
        }
        return records;
    }


    // 税率
    private List<BillInferenceV> filterByTaxRate(List<BillInferenceV> records, Map<String, VoucherRuleCondition> conditionMap) {

        if (conditionMap.containsKey("11") && !records.isEmpty()) {
            VoucherRuleCondition<JSONArray> taxRate = conditionMap.get("11");
            JSONArray values = taxRate.getValue();
            if (!values.isEmpty()) {
                List<InvoiceReceiptDetailE> invoiceReceiptDetailList = invoiceDomainService.getBillIdsByBillIdsAndTaxRate(
                    records.stream().map(BillInferenceV::getId).collect(Collectors.toList()),
                    getMethodNum(taxRate.getCompare()), values);
                List<Long> billIds = invoiceReceiptDetailList.stream()
                    .map(InvoiceReceiptDetailE::getBillId).collect(Collectors.toList());
                records = records.stream().filter(t -> billIds.contains(t.getId()))
                    .collect(Collectors.toList());
            }
        }
        return records;
    }


    // 票据类型
    private List<BillInferenceV> filterByInvoiceType(List<BillInferenceV> records, Map<String, VoucherRuleCondition> conditionMap) {

        if (conditionMap.containsKey("4") && !records.isEmpty()) {
            VoucherRuleCondition<JSONArray> invoiceType = conditionMap.get("4");
            JSONArray values = invoiceType.getValue();
            if (!values.isEmpty()) {
                List<InvoiceReceiptDetailV> invoiceReceiptDetailList = invoiceDomainService.getBillIdsByType(
                    records.stream().map(BillInferenceV::getId).collect(Collectors.toList()),
                    getMethodNum(invoiceType.getCompare()), values);

                List<Long> billIds = invoiceReceiptDetailList.stream()
                    .map(InvoiceReceiptDetailV::getBillId).collect(Collectors.toList());

                records = records.stream().filter(t -> billIds.contains(t.getId()))
                    .collect(Collectors.toList());
            }
        }
        return records;
    }

    // 结算方式
    private List<BillInferenceV> filterBySettleType(List<BillInferenceV> records, Map<String, VoucherRuleCondition> conditionMap) {

        if (conditionMap.containsKey("3") && !records.isEmpty()) {
            VoucherRuleCondition<JSONArray> settleType = conditionMap.get("3");
            JSONArray values = settleType.getValue();
            if (!values.isEmpty()) {
                SettleChannelAndIdsF form = new SettleChannelAndIdsF();
                form.setBillIds(records.stream().map(BillInferenceV::getId).collect(
                    Collectors.toList()));
                Map<String, Object> map = new HashMap<>();
                map.put("method", getMethodNum(settleType.getCompare()));
                map.put("value", settleType.getValue());
                form.setParams(JSON.toJSONString(map));
                List<Long> billIds = billFacade.listBillIdsByIdsAndChannel(form);
                records = records.stream().filter(t -> billIds.contains(t.getId())).collect(
                    Collectors.toList());
            }
        }
        return records;
    }

    // 判断账单来源
    private List<BillInferenceV> filterBySource(List<BillInferenceV> records, Map<String, VoucherRuleCondition> conditionMap) {

        if (conditionMap.containsKey("6")) {
            VoucherRuleCondition<JSONArray> billSource = conditionMap.get("6");
            Integer compare = getMethodNum(billSource.getCompare());
            List<String> values = billSource.getValue().toJavaList(String.class);
            if (!values.isEmpty()) {
                if (compare == 1) {
                    records = records.stream().filter(t -> t.getSource()
                        .contains(BillSourceEnum.getDescByCode(values.get(0)))).collect(
                        Collectors.toList());
                } else if (compare == 15) {
                    records = records.stream().filter(t -> {
                        for (String value : values) {
                            if (t.getSource().contains(BillSourceEnum.getDescByCode(value))) {
                                return true;
                            }
                        }
                        return false;
                    }).collect(
                        Collectors.toList());
                } else if (compare == 16) {
                    records = records.stream().filter(t -> {
                        for (String value : values) {
                            if (t.getSource().contains(BillSourceEnum.getDescByCode(value))) {
                                return false;
                            }
                        }
                        return true;
                    }).collect(
                        Collectors.toList());
                }
            }
        }
        return records;
    }

    /**
     * 数据写入
     * @param records
     * @param eventType
     * @param billTypeEnum
     * @param voucherRule
     */
    @Transactional
    public void writeDownList(List<BillInferenceV> records, Integer eventType, BillTypeEnum billTypeEnum, VoucherRuleE voucherRule) {
        // 批量插入凭证
        EventTypeEnum eventTypeEnum = EventTypeEnum.valueOfByCodeByEvent(eventType);
        if (eventTypeEnum == null) {
            return;
        }

        dealVoucherSubjects(voucherRule, records.get(0));

        List<VoucherE> list = new ArrayList<>();
        // 预制凭证编号
        String serialNumber = IdentifierFactory.getInstance().serialNumber("voucher", "PZ", 20);
        records.forEach(t -> list.add(generateVoucher(t, eventTypeEnum, voucherRule, billTypeEnum, false, serialNumber)));
        batchInsertVouchers(list);
        List<Long> voucherIds = list.stream().map(VoucherE::getId).collect(Collectors.toList());

        BillInferenceV bill = records.get(0);
        VoucherInferenceRecordE record = new VoucherInferenceRecordE();
        record.setChargeItemId(bill.getChargeItemId());
        record.setChargeItemName(bill.getChargeItemName());
        // TODO 数据待填入
        record.setVoucherSystem("用友NCC");

        record.setSuccessState(0);
        record.setVoucherRuleName(voucherRule.getRuleName());
        record.setVoucherRuleId(voucherRule.getId());
        record.setEventType(eventTypeEnum.getEvent());
        record.setVoucherIds(JSON.toJSONString(Collections.singletonList(bill.getId())));

        long amount = list.stream().mapToLong(VoucherE::getAmount).sum();
        record.setDebitAmount(amount);
        record.setCreditAmount(amount);

        voucherInferenceRecordDomainService.insert(record);
        List<Long> concatIds = records.stream().map(BillInferenceV::getConcatId).collect(Collectors.toList());
        // 批量
        List<Long> inferIds = billFacade.batchInsertInference(records.stream().map(BillInferenceV::getId).collect(Collectors.toList()),
            concatIds, billTypeEnum.getCode(), eventType);

        // 批量处理快照
        batchInsertVoucherBills(records, billTypeEnum, voucherIds);
    }



    public VoucherE generateVoucher(BillInferenceV bill, EventTypeEnum eventTypeEnum,
        VoucherRuleE voucherRule, BillTypeEnum billTypeEnum, boolean isBatch, String serialNumber) {
        VoucherE voucherE = new VoucherE();
        voucherE.setBillNo(bill.getBillNo());
        voucherE.setBillId(bill.getId());
        voucherE.setBillType(billTypeEnum.getCode());
        voucherE.setVoucherNo(serialNumber);
        Long amount = voucherInferenceAppServiceFactory.getInstance(eventTypeEnum).generateInferenceAmount(bill, billTypeEnum);
        voucherE.setVoucherType("记账凭证");
        voucherE.setAmount(amount);
        voucherE.setInferenceState(0);
        voucherE.setStatutoryBodyId(bill.getStatutoryBodyId());
        voucherE.setStatutoryBodyName(bill.getStatutoryBodyName());

        voucherE.setDetails(getRuleDetails(voucherRule, voucherE, billTypeEnum, Collections.singletonList(bill), true, null));
        if (!isBatch) {
//            SendresultV sendresultV = externalInference(bill, voucherRule, billTypeEnum, voucherE);
//            // todo 凭证编号待处理 具体数据需对比
//            voucherE.setVoucherNo(sendresultV.getPkVoucher());
        }
        if (EventTypeEnum.付款结算 == eventTypeEnum || EventTypeEnum.收款结算 == eventTypeEnum) {
            // TODO 调用徐剑 零税通方法
        }
        return voucherE;
    }

    /**
     * 保留账单快照
     * @param list
     * @param billTypeEnum
     * @param voucherIds
     */
    public void batchInsertVoucherBills(List<BillInferenceV> list, BillTypeEnum billTypeEnum, List<Long> voucherIds) {
        // 数据是映射而来导致，id也是映射的。 需要处理
        list.forEach(billInferenceV -> billInferenceV.setId(null));
        voucherDomainService.batchInsertVoucherBills(list, billTypeEnum, voucherIds);
    }

    /**
     * 构造凭证分录详情
     * @param voucherRule
     * @param voucher
     * @param billTypeEnum
     * @param bills
     * @param isSingle
     * @param list
     * @return
     */
    public String getRuleDetails(VoucherRuleE voucherRule, VoucherE voucher,
        BillTypeEnum billTypeEnum, List<BillInferenceV> bills, boolean isSingle, List<VoucherE> list) {
        if (StringUtils.isEmpty(voucherRule.getEntries())) {
            return "";
        }
        JSONArray subjects = JSONArray.parseArray(voucherRule.getEntries());
        if (subjects.isEmpty()) {
            return "";
        }
        // 计算分录金额
        Map<Integer, Long> map = new HashMap<>();
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            subject.put("current", "人民币");
            subject.put("voucherType", "记账凭证");
            if (!subject.containsKey("subjectId")) {
                subject.put("subjectName", "");
            } else {
                Long subjectId = subject.getLong("subjectId");
                map.put(i, subjectId);
            }
            if (StringUtils.isNotBlank(subject.getString("supItemName"))) {
                StringBuilder stringBuilder = new StringBuilder();
                String[] supItemNames = subject.getString("supItemName").split(";");
                for (String supItemName : supItemNames) {
                    stringBuilder.append("【").append(supItemName).append("】");
                    switch (supItemName) {
                        case "客商" :
                            if (bills.get(0).getCustomerLabel() != null && bills.get(0).getCustomerLabel() == 2) {
                                if (BillTypeEnum.应收账单 == billTypeEnum || BillTypeEnum.预收账单 == billTypeEnum
                                    || BillTypeEnum.收款单 == billTypeEnum || BillTypeEnum.临时收费账单 == billTypeEnum) {
                                    stringBuilder.append(bills.get(0).getPayerName());
                                } else {
                                    stringBuilder.append(bills.get(0).getPayeeName());
                                }
                            } else {
                                String s = "业主";
                                if (BillTypeEnum.收款单 == billTypeEnum || BillTypeEnum.付款单 == billTypeEnum) {
                                    s = bills.get(0).getPayerName();
                                }
                                stringBuilder.append(s);
                            }
                            if (EventTypeEnum.未认领暂收款.getEvent() == voucherRule.getEventType()) {
                                stringBuilder.append("临时客商");
                            }
                            break;
                        case "项目":
                            stringBuilder.append(bills.get(0).getCostCenterName());
                            break;
                        case "部门":
                            break;
                        case "业务类型":
                            break;
                        case "银行账户":
                            StatutoryBodyAccountV statutoryBodyAccountV = statutoryBodyAccountAppService.detailStatutoryBodyAccount(
                                bills.get(0).getSbAccountId());
                            if (Optional.ofNullable(statutoryBodyAccountV).isPresent()) {
                                stringBuilder.append(statutoryBodyAccountV.getName());
                            }
                            break;
                        case "存款账户性质":
                            stringBuilder.append("活期");
                            break;
                        case "坏账准备增减方式":
                            stringBuilder.append("资产核销减少");
                            break;
                        case "增值税税率":
                            if (null != bills.get(0) && null != bills.get(0).getTaxRate()) {
                                stringBuilder.append(bills.get(0).getTaxRate().doubleValue() * 100).append("%");
                            } else {
                                stringBuilder.append("0%");
                            }
                            break;
                        default:
                            break;
                    }
                    stringBuilder.append(";");
                }
                subject.put("supItemName", stringBuilder.toString());
            }
        }

        // 批量查科目分录
        List<SubjectV> subjectRVS = oldVoucherFacade.listByIds(new ArrayList<>(map.values()));
        Map<Long, SubjectV> subjectDTOMap = subjectRVS.stream().collect(Collectors.toMap(SubjectV::getId, Function.identity()));
        // 科目中含税处理
        if (subjectRVS.stream().anyMatch(subjectDTO -> subjectDTO.getExistTax() == 1) && !CollectionUtils.isEmpty(bills)) {
            if (isSingle) {
                generateSubjectsByTax(map, subjects, subjectDTOMap, voucher.getAmount(), voucherRule, bills, billTypeEnum, isSingle);
            } else {
                generateSubjectsByVouchers(map, subjects, subjectDTOMap, voucher.getAmount(), voucherRule, bills, billTypeEnum, list);
            }
        } else {
            generateSubjects(map, subjects, subjectDTOMap, voucher.getAmount(), voucherRule, bills.get(0).getChargeItemName());
        }
        return JSON.toJSONString(subjects);
    }

    /**
     *
     * 带税的构造 推凭用
     * @param map
     * @param subjects
     * @param subjectRVMap
     * @param amount
     * @param voucherRule
     * @param bills
     * @param list
     */
    private void generateSubjectsByVouchers(Map<Integer, Long> map, JSONArray subjects, Map<Long, SubjectV> subjectRVMap,
        Long amount, VoucherRuleE voucherRule, List<BillInferenceV> bills, BillTypeEnum billTypeEnum, List<VoucherE> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        for (Integer index : map.keySet()) {
            JSONObject subject = subjects.getJSONObject(index);
            SubjectV subjectRV = subjectRVMap.get(map.get(index));
            AtomicReference<Long> totalAmount = new AtomicReference<>(0L);
            list.forEach(voucher -> {
                JSONArray array = JSONArray.parseArray(voucher.getDetails());
                totalAmount.updateAndGet(v -> v + array.getJSONObject(index).getLong("amount"));
            });
            subject.put("subjectCode", subjectRV.getSubjectCode());
            subject.put("amount", totalAmount);
            subject.put("auxiliaryCount", subjectRV.getAuxiliaryCountList());
            subject.put("ruleRemark", dealRuleMark(voucherRule.getRuleRemark(), bills.get(0).getChargeItemName()));

            if ("debit".equals(subject.getString("type"))) {
                subject.put("localDebit", subject.get("amount"));
                subject.put("localCredit", 0);
            } else {
                subject.put("localDebit", 0);
                subject.put("localCredit", subject.get("amount"));
            }
        }
    }

    /**
     * 带税的构造
     * @param map
     * @param subjects
     * @param subjectRVMap
     * @param amount
     * @param voucherRule
     * @param bills
     * @param billTypeEnum
     */
    private void generateSubjectsByTax(Map<Integer, Long> map, JSONArray subjects, Map<Long, SubjectV> subjectRVMap,
        Long amount, VoucherRuleE voucherRule, List<BillInferenceV> bills, BillTypeEnum billTypeEnum, boolean isSingle) {
        if (CollectionUtils.isEmpty(bills)) {
            return;
        }
        // 过滤带税的科目
        Map<Long, SubjectV> taxMap = subjectRVMap.values().stream()
            .filter(subjectDTO -> subjectDTO.getExistTax() == 1)
            .collect(Collectors.toMap(SubjectV::getId, Function.identity()));
        Long debitTax = 0L;
        Long creditTax = 0L;

        for (Integer index : map.keySet()) {
            if (taxMap.containsKey(map.get(index))) {
                JSONObject subject = subjects.getJSONObject(index);
                SubjectV subjectRV = subjectRVMap.get(map.get(index));
                if (Objects.nonNull(subjectRV)) {
                    subject.put("subjectCode", subjectRV.getSubjectCode());
                    subject.put("isTax", true);
                    long subjectAmount;
                    if (isSingle) {
                        BigDecimal taxRate = bills.get(0).getTaxRate() == null ? new BigDecimal(0) : bills.get(0).getTaxRate();
                        subjectAmount = new BigDecimal(amount).subtract(
                             new BigDecimal(amount).divide(taxRate.add(new BigDecimal("1")),
                             RoundingMode.FLOOR)).longValue();
                    } else {
                        subjectAmount = bills.stream()
                            .mapToLong(bill -> {
                                BigDecimal taxRate = bill.getTaxRate() == null ? new BigDecimal(0) : bill.getTaxRate();
                                return new BigDecimal(amount).subtract(new BigDecimal(amount).
                                divide(taxRate.add(new BigDecimal("1")), RoundingMode.FLOOR)).longValue();
                            }).sum();
                    }
//                    Long subjectAmount = calcSubjectAmount(bill.getId(), billTypeEnum);
                    if ("debit".equals(subject.getString("type"))) {
                        debitTax += subjectAmount;
                    } else {
                        creditTax += subjectAmount;
                    }
                    subject.put("amount", subjectAmount);
                    subject.put("auxiliaryCount", subjectRV.getAuxiliaryCountList());
                    subject.put("ruleRemark", dealRuleMark(voucherRule.getRuleRemark(), bills.get(0).getChargeItemName()));

                    if ("debit".equals(subject.getString("type"))) {
                        subject.put("localDebit", subject.get("amount"));
                        subject.put("localCredit", 0);
                    } else {
                        subject.put("localDebit", 0);
                        subject.put("localCredit", subject.get("amount"));
                    }
                }
            }
        }
        // 过滤不带税的科目
        Map<Long, SubjectV> noTaxMap = subjectRVMap.values().stream()
            .filter(subjectRV -> subjectRV.getExistTax() == 0)
            .collect(Collectors.toMap(SubjectV::getId, Function.identity()));
        for (Integer index : map.keySet()) {
            if (noTaxMap.containsKey(map.get(index))) {
                JSONObject subject = subjects.getJSONObject(index);
                SubjectV subjectDTO = noTaxMap.get(map.get(index));
                if (Objects.nonNull(subjectDTO)) {
                    subject.put("subjectCode", subjectDTO.getSubjectCode());
                    if ("debit".equals(subject.getString("type"))) {
                        subject.put("amount", amount - debitTax);
                    } else {
                        subject.put("amount", amount - creditTax);
                    }
                    subject.put("auxiliaryCount", subjectDTO.getAuxiliaryCountList());
                    subject.put("ruleRemark", dealRuleMark(voucherRule.getRuleRemark(), bills.get(0).getChargeItemName()));

                    if ("debit".equals(subject.getString("type"))) {
                        subject.put("localDebit", subject.get("amount"));
                        subject.put("localCredit", 0);
                    } else {
                        subject.put("localDebit", 0);
                        subject.put("localCredit", subject.get("amount"));
                    }
                }
            }
        }

    }

    /**
     * 处理摘要
     * @param ruleRemark 摘要
     * @param chargeItemName 费项
     * @return
     */
    private Object dealRuleMark(String ruleRemark, String chargeItemName) {
        LocalDate now = LocalDate.now();
        return ruleRemark.replaceAll("\\[费项名称\\]", chargeItemName).replaceAll("\\[应结算月\\]",  now.getMonthValue() + "");
    }

    /**
     * 构造分录
     * @param map
     * @param subjects
     * @param SubjectRVMap
     * @param amount
     * @param voucherRule
     */
    private void generateSubjects(Map<Integer, Long> map, JSONArray subjects, Map<Long, SubjectV> SubjectRVMap, Long amount, VoucherRuleE voucherRule, String chargeItemName) {
        for (Integer index : map.keySet()) {
            JSONObject subject = subjects.getJSONObject(index);
            SubjectV subjectRV = SubjectRVMap.get(map.get(index));
            if (Objects.nonNull(subjectRV)) {
                subject.put("subjectCode", subjectRV.getSubjectCode());
                subject.put("amount", amount);
                subject.put("auxiliaryCount", subjectRV.getAuxiliaryCountList());
                subject.put("ruleRemark", dealRuleMark(voucherRule.getRuleRemark(), chargeItemName));

                if ("debit".equals(subject.getString("type"))) {
                    subject.put("localDebit", subject.get("amount"));
                    subject.put("localCredit", 0);
                } else {
                    subject.put("localDebit", 0);
                    subject.put("localCredit", subject.get("amount"));
                }
            }
        }
    }

    /**
     *  计算分录金额
     * @param billId
     * @return
     */
    private Long calcSubjectAmount(Long billId, BillTypeEnum billType) {
        Optional<Map<Long, List<InvoiceBillDto>>> billInvoiceMap = invoiceDomainService.getBillInvoiceMap(
            Collections.singletonList(billId), billType.getCode());
        if (billInvoiceMap.isEmpty()) {
            return 0L;
        }
        List<InvoiceBillDto> list = billInvoiceMap.get().get(billId);
        double taxAmount = 0.0;
        for (InvoiceBillDto invoiceBillDto : list) {
            taxAmount += invoiceBillDto.getInvoiceAmount() * Double.parseDouble(invoiceBillDto.getTaxRate());
        }
        return Math.round(taxAmount);
    }

    /**
     * 调用三方开票推凭
     * todo 优化代码 将己方数据转化为NCC接口所需的字段
     */
    @Transactional
    public SendresultV externalInference(BillInferenceV bill, VoucherRuleE voucherRule, BillTypeEnum billTypeEnum, VoucherE voucherE) {
        if (!"dev".equals(springProfilesActive)) {
            return null;
        }
        UfinterfaceF form = new UfinterfaceF().create();

        VoucherF voucher = new VoucherF();
        voucher.setId("ncc_" + bill.getId());
        form.setVoucher(voucher);

        VoucherHeadF voucherHead = new VoucherHeadF();
        voucherHead.setPk_voucher("");                          // 新增不传
        voucherHead.setPk_vouchertype("01");                    // 等待提供了类型 01表示记账凭证  一般都只用这个
        voucherHead.setYear(bill.getGmtCreate().getYear() + ""); // 归纳的年份
        voucherHead.setPk_system("GL");                         // 来源系统 默认GL
        voucherHead.setVoucherkind("0");                        // 凭证类型 写死
        voucherHead.setPk_accountingbook(mapAccountBookCode(bill.getChargeItemId(), bill.getCostCenterId()));             // 等待nc的编码
        voucherHead.setDiscardflag("N");
        voucherHead.setPeriod(bill.getGmtCreate().getMonthValue() + ""); // 凭证所属的月份
        voucherHead.setNo("");                                           //
        voucherHead.setAttachment("");
        voucherHead.setPrepareddate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(bill.getGmtCreate())); // 账单生成时间
        voucherHead.setPk_prepared("liuyancui");                      // 等待nc提供  可能默认使用一个人，也可能需根据人员档案看
        voucherHead.setPk_casher("");
        voucherHead.setSignflag("Y");
        voucherHead.setPk_checked("");
        voucherHead.setTallydate(""); // 记账日期
        voucherHead.setPk_manager(""); // 记账人
        voucherHead.setMemo1("");
        voucherHead.setMemo2("");
        voucherHead.setReserve1("");
        voucherHead.setReserve2("");
        voucherHead.setSiscardflag("");
        voucherHead.setPk_org("010010010A");                           // 等待nc 系统有组织档案
        voucherHead.setPk_org_v("");                            // 不用填
        voucherHead.setPk_group("G");                         // nc系统所建集团编码

        voucherHead.setDetails(generateVoucherHeadDetails(voucherE, voucherRule, bill, billTypeEnum));

        voucher.setVoucher_head(voucherHead);

        return oldVoucherFacade.externalInference(form);
    }

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    private String mapAccountBookCode(Long chargeItemId, Long costCenterId) {
        if (testAccBook) {
            AccountBookE accountBook = accountBookAppService.getByChargeItemAndCostCenter(
                chargeItemId, costCenterId);
            if (Optional.ofNullable(accountBook).isPresent()) {
                return accountBook.getCode();
            }
        }
        return "010010010A-0001";
    }

    /**
     * 根据费项id和成本中心映射费项
     * @param chargeItemId 费项id
     * @param costCenterId 成本中心id
     * @return
     */
    private Optional<AccountBookE> mapAccountBook(Long chargeItemId, Long costCenterId) {
        AccountBookE accountBook = accountBookAppService.getByChargeItemAndCostCenter(
            chargeItemId, costCenterId);
       return Optional.ofNullable(accountBook);
    }

    /**
     * 构造凭证详情
     * @param voucherE
     * @param voucherRule
     * @param billInferenceV
     * @return
     */
    private List<DetailsF> generateVoucherHeadDetails(VoucherE voucherE, VoucherRuleE voucherRule, BillInferenceV billInferenceV, BillTypeEnum billTypeEnum) {
        List<DetailsF> details = new ArrayList<>();
        if (StringUtils.isNotEmpty(voucherE.getDetails())) {
            JSONArray subjects = JSONArray.parseArray(voucherE.getDetails());
            for (int i = 0; i < subjects.size(); i++) {
                JSONObject subject = subjects.getJSONObject(i);
                DetailsF detail = new DetailsF();
                detail.setDetailindex((i + 1) + "");                        // 循环增加
                detail.setExplanation(voucherRule.getRuleName());         // 规则的摘要
                detail.setVerifydate("");
                detail.setPrice("");
                detail.setExcrate2("1");
                detail.setPk_accasoa(subject.getString("subjectCode"));     // todo 需ncc换取 需对齐ncc的会计科目
                Long amount = subject.getLong("amount");
                if ("debit".equals(subject.getString("type"))) {
//                    detail.setPk_accasoa("54020301"); // todo 这里要删除
                    detail.setDebitquantity("");
                    detail.setDebitamount(amount / 100.00 + "");                                  // 原来的钱 （使用的币种）
                    detail.setLocaldebitamount(amount / 100.00 + "");                             // 所在国家的币种
                    detail.setGroupdebitamount("");
                    detail.setGlobaldebitamount("");
                } else {
                    // 借贷返回pk_accasoa不一样
//                    detail.setPk_accasoa("1002"); // todo 这里要删除
                    detail.setCreditquantity("");
                    detail.setCreditamount(amount / 100.00 + "");
                    detail.setLocalcreditamount(amount / 100.00 + "");
                    detail.setGroupcreditamount("");
                    detail.setGlobalcreditamount("");
                }
                detail.setPk_currtype("CNY");                    //
                detail.setPk_unit("");
                detail.setPk_unit_v("");
                details.add(detail);

                detail.setAss(generateAssFList(subject.getJSONArray("auxiliaryCount"),
                    subject.getString("type"), billInferenceV, billTypeEnum, voucherRule));
            }

        }
        return details;
    }

    /**
     * 生成辅助核算
     * todo 请求接口获取辅助核算
     * @param auxiliaryCounts
     * @param type
     * @return
     */
    private List<AssF> generateAssFList(JSONArray auxiliaryCounts, String type,
        BillInferenceV billInferenceV, BillTypeEnum billTypeEnum, VoucherRuleE voucherRule) {
        List<AssF> assFList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(auxiliaryCounts)) {
            for (int i = 0; i < auxiliaryCounts.size(); i++) {
                String auxiliaryCount = auxiliaryCounts.getString(i);
                AssF ass = new AssF();
                ass.setPk_Checktype(auxiliaryCount);        // 部门 0001，客商 0004，人员档案 0002，项目（自定义档案） 0049
                ass.setPk_Checkvalue(buildValue(auxiliaryCount, billInferenceV, billTypeEnum, voucherRule));       // 具体值
                assFList.add(ass);
            }
        }

        // todo 这一期未对接辅助核算接口  先只用一个(需查询ncc对应的辅助核算接口，循环添加返回的值)
//        if ("debit".equals(type)) {
//            AssF ass = new AssF();
//            ass.setPk_Checktype("0001");        // 部门 0001，客商 0004，人员档案 0002，项目（自定义档案） 0049 暂时写死测试
//            ass.setPk_Checkvalue("002");       // 具体值 测试
//            AssF ass1 = new AssF();
//            ass1.setPk_Checktype("0010");        // 部门 0001，客商 0004，人员档案 0002，项目（自定义档案） 0049 暂时写死测试
//            ass1.setPk_Checkvalue("01SH00000001");       // 具体值 测试
//            assFList.add(ass);
//            assFList.add(ass1);
//        } else {
//            AssF ass = new AssF();
//            ass.setPk_Checktype("0011");        // 部门 0001，客商 0004，人员档案 0002，项目（自定义档案） 0049 暂时写死测试
//            ass.setPk_Checkvalue("6200220000001");       // 具体值 测试
//            AssF ass1 = new AssF();
//            ass1.setPk_Checktype("0065");        // 部门 0001，客商 0004，人员档案 0002，项目（自定义档案） 0049 暂时写死测试
//            ass1.setPk_Checkvalue("01");       // 具体值 测试
//            assFList.add(ass);
//            assFList.add(ass1);
//        }

        return assFList;
    }

    /**
     * 获取对应的 辅助核算code
     * @param auxiliaryCount
     * @return
     */
    private String buildValue(String auxiliaryCount, BillInferenceV billInferenceV, BillTypeEnum billTypeEnum, VoucherRuleE voucherRule) {
        switch (auxiliaryCount) {
            case "0001": // 部门
                return billInferenceV.getCostCenterId().toString();
            case "0010": // 项目
                return billInferenceV.getCostCenterId().toString();
            case "0004": // 客商
                if (EventTypeEnum.未认领暂收款.getEvent() == voucherRule.getEventType()) {
//                    stringBuilder.append("临时客商");
                    // todo nc 新增个 临时客商
                }
                if (billInferenceV.getCustomerLabel() != null && billInferenceV.getCustomerLabel() == 2) {
                    if (BillTypeEnum.应收账单 == billTypeEnum || BillTypeEnum.预收账单 == billTypeEnum
                        || BillTypeEnum.收款单 == billTypeEnum || BillTypeEnum.临时收费账单 == billTypeEnum) {
                        return accountOrgFacade.getMerchantByName(billInferenceV.getPayerName());
                    } else {
                        return accountOrgFacade.getMerchantByName(billInferenceV.getPayeeName());
                    }
                } else {
                    if (BillTypeEnum.收款单 == billTypeEnum || BillTypeEnum.付款单 == billTypeEnum) {
                        return accountOrgFacade.getMerchantByName(billInferenceV.getPayerName());
                    }
                    return "0000001C";
                }
            case "0063":  // 业务类型
                return "0";
            case "0011":  // 银行账户
                return billInferenceV.getSbAccountId() + "";
            case "0065":  // 存款账户性质
                return "";
            case "0054":  // 坏账准备增减方式
                return "";
            case "0066":  // 增值税率

                return getRateCode(billInferenceV.getTaxRate().doubleValue());
            default:
                return "";
        }
    }

    /**
     * 根据获取增值税税率code
     * @param rate
     */
    private String getRateCode(double rate) {
        List<NccTaxRate> list = nccTaxRateDomainService.listRate();
        Optional<NccTaxRate> first = list.stream()
            .filter(rateRv -> (rate + "%").equals(rateRv.getName())).findFirst();
        if (first.isPresent()) {
            return first.get().getCode();
        }
        return "";
    }

    /**
     * 根据账单类型转化为NCC的账单类型
     * 默认 vouchergl
     *
     * @param billTypeEnum
     * @return
     */
    private String getNCCBillType(BillTypeEnum billTypeEnum) {
//        switch (billTypeEnum) {
//            case 应收账单:
//                return "";
//            case 预收账单:
//                return "advance";
//            case 临时收费账单:
//                return "temp";
//            default:
                return "vouchergl";
//        }
    }

    /**
     * 批量新增凭证 且 留下账单快照
     * @param list
     */
    public void batchInsertVouchers(List<VoucherE> list) {
        voucherDomainService.batchInsert(list);
    }

    /**
     * 运行推凭规则
     * @param ruleId
     * @return
     */
    public Boolean runInferenceByRule(Long ruleId) {
        VoucherRuleE voucherRule = oldVoucherRuleDomainService.getById(ruleId);
        if (2 == voucherRule.getExecuteType()) {
            inference(voucherRule, false);
        } else {
            throw BizException.throw400(ErrorMessage.VOUCHER_RULE_EXECUTE_TYPE_NOT_HAND.msg());
        }
        return Boolean.TRUE;
    }



    /**
     * 单个推凭 即时推送
     * @param billId  账单id
     * @param billTypeEnum   账单类型参考 {@link BillTypeEnum}
     * @param actionEventEnum 时间类型参考 {@link ActionEventEnum}
     * @param amount 外来金额（推凭的金额可传递）  默认按规则查询
     * @return 凭证号
     */
    public String singleInference(Long billId, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, String supCpUnitId) {

        List<BillInferenceV> billInfo = billFacade.getBillInferenceInfo(billId, billTypeEnum, actionEventEnum.getCode(), supCpUnitId);

//        List<BillOjv> billInfo = billFacade.getBillInfo(Collections.singletonList(billId), billTypeEnum.getCode());
        if (billInfo.isEmpty()) {
            return null;
        }
        if (ActionEventEnum.结算 == actionEventEnum) {
            List<String> sendresultVList = groupBillByChargeItemAndInfer(null, actionEventEnum,
                billInfo, billTypeEnum, false);
            return JSON.toJSONString(sendresultVList);
        }
        BillInferenceV bill = billInfo.get(0);

        EventTypeEnum eventTypeEnum = inferEventType(billTypeEnum, actionEventEnum);

        if (voucherInferenceAppServiceFactory.getInstance(eventTypeEnum).judgeSingleBillStatus(bill, billTypeEnum, supCpUnitId)) {
            return null;
        }
        // 获取账单对应费项的推凭规则
        List<VoucherRuleE> list = oldVoucherRuleDomainService.listByChargeItemIdAndEventTypeAndExecuteType(
            Collections.singleton(bill.getChargeItemId()), eventTypeEnum, 1);

        if (list.isEmpty()) {
            return null;
        }

        VoucherRuleE voucherRule = null;
        for (VoucherRuleE voucherRuleE : list) {
            if (voucherRuleE.getExecuteType() == 1 && doSingleInference(bill, voucherRuleE)) {
                voucherRule = voucherRuleE;
                break;
            }
        }
        if (voucherRule == null) {
            return null;
        }
        return writeDown(bill, eventTypeEnum, voucherRule, billTypeEnum, amount);
    }

    /**
     * 批量即时推凭
     * @param billIds  账单id集合
     * @param billTypeEnum   账单类型参考 {@link BillTypeEnum}
     * @param actionEventEnum 时间类型参考 {@link ActionEventEnum}
     * @param amount 外来金额（推凭的金额可传递）  默认按规则查询
     * @param needBack 是否需要返回值
     *                 需要 true 整个流程会采用同步
     *                 不需要 false 推凭会采用异步
     * @return
     */
    @Override
    public List<String> batchSingleInference(List<Long> billIds, BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum, Long amount, boolean needBack, String supCpUnitId) {

        List<BillInferenceV> billInfo = billFacade.getBillInferenceInfoByIds(billIds, billTypeEnum, actionEventEnum.getCode(), supCpUnitId);
        if (billInfo.isEmpty()) {
            return null;
        }

        List<String> list;
        // 筛选符合的账单
        if (ActionEventEnum.计提 != actionEventEnum && ActionEventEnum.结算 != actionEventEnum) {
            list = groupBillByChargeItemAndInfer(null, actionEventEnum, billInfo, billTypeEnum, needBack);
        } else {
            list = groupBillByChargeItemAndInfer(1, actionEventEnum, billInfo, billTypeEnum, needBack);

            if (list != null) {
                list.addAll(Objects.requireNonNull(groupBillByChargeItemAndInfer(2, actionEventEnum, billInfo, billTypeEnum, needBack)));
            } else {
                list = groupBillByChargeItemAndInfer(2, actionEventEnum, billInfo, billTypeEnum, needBack);
            }
        }

        return list;
    }

    /**
     * 将账单根据费项分组后推凭
     * @param chargeItemType
     * @param actionEventEnum
     * @param billInfo
     * @param billTypeEnum
     * @param needBack
     * @return
     */
    private List<String> groupBillByChargeItemAndInfer(Integer chargeItemType, ActionEventEnum actionEventEnum, List<BillInferenceV> billInfo,
        BillTypeEnum billTypeEnum, boolean needBack) {
        EventTypeEnum eventTypeEnum = inferEventType(billTypeEnum, actionEventEnum);
        List<BillInferenceV> inferenceRVList = billInfo.stream().filter(
                bill -> voucherInferenceAppServiceFactory.getInstance(eventTypeEnum).judgeSingleBillStatus(bill, billTypeEnum, billInfo.get(0).getCommunityId()))
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(inferenceRVList)) {
            return null;
        }
        List<VoucherRuleE> voucherRuleList = oldVoucherRuleDomainService
            .listByChargeItemIdAndEventTypeAndExecuteType(
                inferenceRVList.stream().map(BillInferenceV::getChargeItemId).collect(Collectors.toSet()),
                eventTypeEnum, 1);

        if (CollectionUtils.isEmpty(voucherRuleList)) {
            return null;
        }

        List<String> sendresultVList = new ArrayList<>();

        Iterator<VoucherRuleE> it = voucherRuleList.iterator();
        while (it.hasNext()) {
            if (CollectionUtils.isEmpty(inferenceRVList)) {
                break;
            }
            sendresultVList.addAll(batchDoInferenceRightNow(it.next(), inferenceRVList, eventTypeEnum, billTypeEnum, needBack));
        }
        return sendresultVList;
    }

    /**
     * 批量判断账单是否可以立即推凭
     * @param rule
     * @param billInferenceRVS
     * @param eventTypeEnum
     * @param billTypeEnum
     * @param needBack
     */
    private List<String> batchDoInferenceRightNow(VoucherRuleE rule, List<BillInferenceV> billInferenceRVS,
        EventTypeEnum eventTypeEnum, BillTypeEnum billTypeEnum, boolean needBack) {
        if (CollectionUtils.isEmpty(billInferenceRVS)) {
            return Collections.emptyList();
        }

//        getStatutoryBodyId(conditions, fieldList);
//        getCommunityId(conditions, fieldList);
        List<BillInferenceV> filter = new ArrayList<>();
        List<VoucherRuleCondition> conditionList = JSONArray.parseArray(rule.getConditions(), VoucherRuleCondition.class);
        if (StringUtils.isNotEmpty(rule.getConditions()) && !CollectionUtils.isEmpty(conditionList)) {
            Map<String, VoucherRuleCondition> conditionMap = conditionList2Map(conditionList);
            if (!CollectionUtils.isEmpty(conditionMap)) {
                filter = filterByBodyId(billInferenceRVS, conditionMap);
                filter = filterByCostCenterId(filter, conditionMap);
                filter = filterBill(filter, conditionList);
            }
        } else {
            log.error(" infer error 解析推凭条件失败, conditions: {} ", rule.getConditions());
        }
        Map<String, List<BillInferenceV>> baseGroup = groupByBase(filter);
        return batchWriteDownListGroupBySup(baseGroup, rule, eventTypeEnum, billTypeEnum, false);
    }

    /**
     * 批量形式推凭（按凭证维度）
     * @param records
     * @param voucherRule
     * @param eventTypeEnum
     * @param billTypeEnum
     * @param needBack
     */
    @Transactional
    public List<String> batchWriteDownList(List<BillInferenceV> records, VoucherRuleE voucherRule,
        EventTypeEnum eventTypeEnum, BillTypeEnum billTypeEnum, boolean needBack) {

        if (CollectionUtils.isEmpty(records)) {
            return null;
        }

        dealVoucherSubjects(voucherRule, records.get(0));

        List<VoucherE> list = new ArrayList<>();
        // 预制凭证编号
        String serialNumber = IdentifierFactory.getInstance().serialNumber("voucher", "PZ", 20);
        records.forEach(t -> list.add(generateVoucher(t, eventTypeEnum, voucherRule, billTypeEnum, true, serialNumber)));
        batchInsertVouchers(list);
        List<Long> voucherIds = list.stream().map(VoucherE::getId).collect(Collectors.toList());

        BillInferenceV bill = records.get(0);
        VoucherInferenceRecordE record = new VoucherInferenceRecordE();
        record.setChargeItemId(bill.getChargeItemId());
        record.setChargeItemName(bill.getChargeItemName());
        // TODO 数据待填入
        record.setVoucherSystem("用友NCC");

        record.setSuccessState(0);
        record.setVoucherRuleName(voucherRule.getRuleName());
        record.setVoucherRuleId(voucherRule.getId());
        record.setEventType(eventTypeEnum.getEvent());
        record.setVoucherIds(JSON.toJSONString(voucherIds));

        long amount = list.stream().mapToLong(VoucherE::getAmount).sum();
        record.setDebitAmount(amount);
        record.setCreditAmount(amount);

        voucherInferenceRecordDomainService.insert(record);

        List<Long> concatIds = records.stream().map(BillInferenceV::getConcatId).collect(Collectors.toList());
        // 批量
        List<Long> inferIds = billFacade.batchInsertInference(records.stream().map(BillInferenceV::getId).collect(Collectors.toList()),
            concatIds, billTypeEnum.getCode(), eventTypeEnum.getEvent());

        // 批量处理快照
        batchInsertVoucherBills(records, billTypeEnum, voucherIds);
        if (needBack) {
            batchExternalInference(list, records, voucherRule, record, billTypeEnum, inferIds, eventTypeEnum, concatIds);
            return list.stream().map(VoucherE::getVoucherNo).collect(Collectors.toList());
        }
        //  todo 改成异步调用
        batchExternalInference(list, records, voucherRule, record, billTypeEnum, inferIds, eventTypeEnum, concatIds);
        return null;
    }

    /**
     * 处理规则分录一级问题
     * @param voucherRule
     * @param billInferenceRV
     */
    protected VoucherRuleE dealVoucherSubjects(VoucherRuleE voucherRule, BillInferenceV billInferenceRV) {
        if (StringUtils.isEmpty(voucherRule.getEntries())) {
            return voucherRule;
        }
        JSONArray subjects = JSONArray.parseArray(voucherRule.getEntries());
        if (CollectionUtils.isEmpty(subjects)) {
            return voucherRule;
        }
        for (int i = 0; i < subjects.size(); i++) {
            JSONObject subject = subjects.getJSONObject(i);
            if (subject.containsKey("subjectId")) {
                Long subjectId = subject.getLong("subjectId");
                SubjectDetailV subjectRV = oldVoucherFacade.getSubjectById(subjectId);
                if (subjectRV.getLeaf() == 0) {
                    Long tailSubjectId =
                        CollectionUtils.isEmpty(subjectRV.getSubjectPath()) ? subjectRV.getId()
                            : subjectRV.getSubjectPath().get(0);
                    SubjectV tailSubject = oldVoucherFacade.getSubjectByChargeItemIdAndHeadSubjectId(tailSubjectId, billInferenceRV.getChargeItemId());
                    if (Optional.ofNullable(tailSubject).isEmpty()) {
                        throw BizException.throw404("分录科目中存在非末级科目且未设置映射关系的科目！");
                    }
                    subject.put("subjectId", tailSubject.getId());
                    subject.put("subjectName", oldVoucherFacade.getSubjectName(tailSubject.getId()));
                    subject.put("auxiliaryCount", tailSubject.getAuxiliaryCountList());

                    List<AssisteAccountV> assistAccountList = oldVoucherFacade.listAssistAccount(tailSubject.getAuxiliaryCountList());
                    if (!CollectionUtils.isEmpty(assistAccountList)) {
                        subject.put("supItemName", assistAccountList.stream().map(AssisteAccountV::getAsAcItem).collect(Collectors.joining(";")));
                    }
                } else {
                    subject.put("subjectName", oldVoucherFacade.getSubjectName(subjectRV.getId()));
                    subject.put("auxiliaryCount", subjectRV.getAuxiliaryCountList());
                }
            }
        }
        voucherRule.setEntries(JSON.toJSONString(subjects));
        return voucherRule;
    }

    /**
     * 合并推凭
     * @param list 凭证列表（用于添加凭证的数据）
     * @param records 推凭的账单信息
     * @param voucherRule 推凭规则（用于推凭摘要）
     * @param record 推凭记录（用于回调填入凭证号）
     * @param billTypeEnum 账单类型
     * @param inferIds
     * @param eventTypeEnum
     * @param concatIds
     */
    @Async
    public List<SendresultV> batchExternalInference(List<VoucherE> list, List<BillInferenceV> records,
        VoucherRuleE voucherRule, VoucherInferenceRecordE record, BillTypeEnum billTypeEnum,
        List<Long> inferIds, EventTypeEnum eventTypeEnum, List<Long> concatIds) {
        if (!"dev".equals(springProfilesActive)) {
            return null;
        }

        Map<String, List<VoucherE>> voucherEMap = list.stream()
            .filter(voucherE -> voucherE.getAmount() != 0)
            .collect(Collectors.groupingBy(VoucherE::getBillNo));

        // 推凭周期只接受一个点（年月） 需要将知道按月分组
//        Map<String, List<BillInferenceV>> billMap = records.stream().collect(Collectors.groupingBy(
//            billInference -> billInference.getGmtCreate().getYear() + "_" + billInference.getGmtCreate().getMonthValue()));
//
//        Iterator<Entry<String, List<BillInferenceV>>> it = billMap.entrySet().iterator();
//
//
        List<SendresultV> sendresultVList = new ArrayList<>();
//        while (it.hasNext()) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int moth = now.getMonthValue();


        UfinterfaceF form = new UfinterfaceF().create();

        VoucherF voucher = new VoucherF();
        String voucherId = year + "_" + moth + Instant.now().getEpochSecond();

        voucher.setId(voucherId);
        form.setVoucher(voucher);

        VoucherHeadF voucherHead = new VoucherHeadF().create();
        voucherHead.setYear(year + ""); // 归纳的年份
        voucherHead.setPk_system("GL");                         // 来源系统 默认GL
        voucherHead.setPk_accountingbook(
            mapAccountBookCode(records.get(0).getChargeItemId(), records.get(0).getCostCenterId()));             // 等待nc的编码
        voucherHead.setPeriod(moth + ""); // 凭证所属的月份
        voucherHead.setPrepareddate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())); // 账单生成时间
        voucherHead.setPk_prepared("liuyancui");                      // todo 等待nc提供  可能默认使用一个人，也可能需根据人员档案看
        voucherHead.setTallydate(""); // 记账日期
        voucherHead.setPk_manager(""); // 记账人
        voucherHead.setPk_org("010010010A");                           // todo 等待nc 系统有组织档案
        voucherHead.setPk_org_v("");                            // 不用填
        voucherHead.setPk_group("G");                         // nc系统所建集团编码


        long amount = records.stream().mapToLong(billInferenceRV ->
            voucherEMap.get(billInferenceRV.getBillNo()) == null ?
                0L : voucherEMap.get(billInferenceRV.getBillNo()).isEmpty() ?
                0L : voucherEMap.get(billInferenceRV.getBillNo()).stream().mapToLong(VoucherE::getAmount).sum()).sum();
        VoucherE voucherForInfer = new VoucherE();
        voucherForInfer.setAmount(amount);
        voucherForInfer.setDetails(getRuleDetails(voucherRule, voucherForInfer, billTypeEnum, records, false, list));
        voucherHead.setDetails(generateVoucherHeadDetails(voucherForInfer, voucherRule, records.get(0), billTypeEnum));

        voucher.setVoucher_head(voucherHead);

        log.info("推凭开始 参数: {}", JSON.toJSONString(form));
        SendresultV sendresultV = null;
        try {
            sendresultV = oldVoucherFacade.externalInference(form);
            log.info("凭证推送成功 结果: {}", JSON.toJSONString(sendresultV));

            sendresultVList.add(sendresultV);
            // 填充凭证数据中的凭证编号 预制凭证
            fillVoucherNo(list, sendresultV);
//        }
            // 填充推凭记录数据
            fillVoucherRecord(record, sendresultV);

            dealInferFail(inferIds, sendresultV, eventTypeEnum, billTypeEnum, concatIds);
        } catch (Exception e) {
            log.error("凭证推送失败，推送参数为： {}", JSON.toJSONString(form));
            log.error("凭证推送失败 ---------> : ", e);

            dealInferFail(inferIds, null, eventTypeEnum, billTypeEnum, concatIds);
        }
//        SendresultV sendresultV = null;

        return sendresultVList;
    }

    public void dealInferFail(List<Long> inferIds, SendresultV sendresultV, EventTypeEnum eventTypeEnum, BillTypeEnum billTypeEnum, List<Long> concatIds) {
        boolean success = false;
        if (Objects.nonNull(sendresultV)) {
            success = 1 == sendresultV.getResultcode();
        }
        if (!success) {
            billFacade.delBillInferencesByIds(inferIds, eventTypeEnum, billTypeEnum, concatIds);
        }
    }

    /**
     * 填充凭证数据中的凭证编号
     * @param list
     * @param sendresultV
     */
    private void fillVoucherNo(List<VoucherE> list, SendresultV sendresultV) {
        boolean success = false;
        String pkVoucher = "";
        if (Objects.nonNull(sendresultV)) {
            pkVoucher = sendresultV.getPkVoucher();
            success = 1 == sendresultV.getResultcode();
        }
        voucherDomainService.updateVoucherNoByIds(list.stream().map(VoucherE::getId).collect(Collectors.toList()),
            pkVoucher, success);
    }

    /**
     * 填充推凭记录数据
     * @param record
     * @param sendresultV
     */
    private void fillVoucherRecord(VoucherInferenceRecordE record, SendresultV sendresultV) {
        boolean result = false;
        if (Objects.nonNull(sendresultV)) {
            result = 1 == sendresultV.getResultcode();
        }
        record.setSuccessState(result ? 1 : 2);
        // 修改数据状态
        voucherInferenceRecordDomainService.updateById(record);
    }

    /**
     * 根据成本中心过滤
     * @param billInferenceRVS
     * @param conditionMap
     * @return
     */
    private List<BillInferenceV> filterByBodyId(List<BillInferenceV> billInferenceRVS, Map<String, VoucherRuleCondition> conditionMap) {
        if (CollectionUtils.isEmpty(billInferenceRVS)) {
            return null;
        }
        if (conditionMap.containsKey("1")) {
            VoucherRuleCondition<JSONArray> statutoryBody = conditionMap.get("1");
            JSONArray values = statutoryBody.getValue();
            if (!values.isEmpty()) {
                int compare = getMethodNum(statutoryBody.getCompare());
                List<Long> statutoryBodyIds = values.toJavaList(Long.class);
                if (compare == 1 || compare == 2) {
                    return billInferenceRVS.stream().filter(bill -> statutoryBodyIds.contains(bill.getStatutoryBodyId())).collect(Collectors.toList());
                } else if (compare == 3) {
                    return billInferenceRVS.stream().filter(bill -> !statutoryBodyIds.contains(bill.getStatutoryBodyId())).collect(Collectors.toList());
                }
            }
        }
        return billInferenceRVS;
    }

    /**
     * 根据项目过滤
     * @param billInferenceRVS
     * @param conditionMap
     * @return
     */
    private List<BillInferenceV> filterByCostCenterId(List<BillInferenceV> billInferenceRVS, Map<String, VoucherRuleCondition> conditionMap) {
        if (CollectionUtils.isEmpty(billInferenceRVS)) {
            return null;
        }
        if (conditionMap.containsKey("2")) {
            VoucherRuleCondition<JSONArray> community = conditionMap.get("2");
            JSONArray values = community.getValue();
            List<Long> costCenterIds = values.toJavaList(Long.class);
            if (!values.isEmpty()) {
                int compare = getMethodNum(community.getCompare());
                if (compare == 1 || compare == 2) {
                    return billInferenceRVS.stream().filter(bill -> costCenterIds.contains(bill.getCostCenterId())).collect(Collectors.toList());
                } else if (compare == 3) {
                    return billInferenceRVS.stream().filter(bill -> !costCenterIds.contains(bill.getCostCenterId())).collect(Collectors.toList());
                }
            }
        }
        return billInferenceRVS;
    }

    /**
     * 精确推断触发事件
     * @param billTypeEnum
     * @param actionEventEnum
     * @return
     */
    private EventTypeEnum inferEventType(BillTypeEnum billTypeEnum, ActionEventEnum actionEventEnum) {
        switch (actionEventEnum) {
            case 计提:
                if (BillTypeEnum.应收账单 == billTypeEnum) {
                    return EventTypeEnum.应收计提;
                } else {
                    return EventTypeEnum.应付计提;
                }
            case 冲销:
                return EventTypeEnum.冲销作废;
            case 结算:
                if (BillTypeEnum.收款单 == billTypeEnum) {
                    return EventTypeEnum.收款结算;
                } else {
                    return EventTypeEnum.付款结算;
                }
            case 调整:
                return EventTypeEnum.账单调整;
            case 开票:
                return EventTypeEnum.账单开票;
            case 销账:
                return EventTypeEnum.预收应收核销;
            case 收票:
                return EventTypeEnum.收票结算;
            default:
                break;
        }
        return null;
    }

    /**
     * 单个凭证
     * @param bill
     * @param voucherRuleE
     * @return
     */
    private boolean doSingleInference(BillInferenceV bill, VoucherRuleE voucherRuleE) {
        // 判断规则是否是即时推凭
//        if (voucherRuleE.getExecuteType() != 0) {
//            return false;
//        }
        List<VoucherRuleCondition> conditionList = JSONArray.parseArray(
            voucherRuleE.getConditions(), VoucherRuleCondition.class);
        Map<String, VoucherRuleCondition> conditionMap = conditionList2Map(conditionList);
        if (CollectionUtils.isEmpty(conditionMap)) {
            return true;
        }
        // 判断法定单位
        boolean flag = doStatutoryBody(conditionMap, bill);
        if (!flag) {
            return false;
        }

        // 判断成本中心
        flag = doCommunity(conditionMap, bill);
        if (!flag) {
            return flag;
        }

        // 判断结算方式
        flag = doSettleType(conditionMap, bill);
        if (!flag) {
            return flag;
        }

        // 判断票据
        flag = doInvoiceType(conditionMap, bill);
        if (!flag) {
            return flag;
        }

        // 判断账单来源
        flag = doBillSource(conditionMap, bill);
        if (!flag) {
            return flag;
        }

        // 判断税率
        flag = doTaxRate(conditionMap, bill);

        return flag;
    }

    // 判断法定单位
    private boolean doStatutoryBody(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("1")) {
            VoucherRuleCondition<JSONArray> statutoryBody = conditionMap.get("1");
            JSONArray values = statutoryBody.getValue();
            if (!values.isEmpty()) {
                Integer compare = getMethodNum(statutoryBody.getCompare());
                if (compare == 1 || compare == 15) {
                    return values.toJavaList(Long.class).contains(bill.getStatutoryBodyId());
                } else if (compare == 16) {
                    return !values.toJavaList(Long.class).contains(bill.getStatutoryBodyId());
                }
            }
            return false;
        }
        return true;
    }

    // 判断成本中心
    private boolean doCommunity(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("2")) {
            VoucherRuleCondition<JSONArray> community = conditionMap.get("2");
            JSONArray values = community.getValue();
            if (!values.isEmpty()) {
                Integer compare = getMethodNum(community.getCompare());
                if (compare == 1 || compare == 15) {
                    return values.toJavaList(Long.class).contains(bill.getCostCenterId());
                } else if (compare == 16) {
                    return !values.toJavaList(Long.class).contains(bill.getCostCenterId());
                }
                return false;
            }
        }
        return true;
    }

    // 判断结算方式
    private boolean doSettleType(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("3")) {
            VoucherRuleCondition<JSONArray> settleType = conditionMap.get("3");
            JSONArray values = settleType.getValue();
            if (!values.isEmpty()) {
                Integer compare = getMethodNum(settleType.getCompare());
                Optional<Map<Long, List<String>>> billSettleChannelList = getBillSettleChannelList(Collections.singletonList(bill.getId()), bill.getCommunityId());
                if (billSettleChannelList.isEmpty()) {
                    return false;
                }
                List<String> list = billSettleChannelList.get().get(bill.getId());
                if (list.isEmpty()) {
                    return false;
                }
                boolean f = false;
                if (compare == 1 || compare == 15) {
                    for (String s : list) {
                        if (values.toJavaList(String.class).contains(s)) {
                            f = true;
                            break;
                        }
                    }
                } else if (compare == 16) {
                    f = true;
                    for (String s : list) {
                        if (values.toJavaList(String.class).contains(s)) {
                            f = false;
                            break;
                        }
                    }
                }
                return f;
            }
        }
        return true;
    }

    // 判断票据
    private boolean doInvoiceType(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("4")) {
            VoucherRuleCondition<JSONArray> billSource = conditionMap.get("4");
            JSONArray values = billSource.getValue();
            if (!values.isEmpty()) {
                List<InvoiceReceiptDetailV> list = invoiceDomainService.getBillIdsByType(Collections.singletonList(bill.getId()),
                    getMethodNum(billSource.getCompare()), values);
                if (!list.isEmpty()) {
                    List<Long> billIds = list.stream().map(InvoiceReceiptDetailV::getBillId).collect(Collectors.toList());
                    return billIds.contains(bill.getId());
                }
                return false;
            }
        }
        return true;
    }

    // 判断账单来源
    private boolean doBillSource(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("6")) {
            VoucherRuleCondition<JSONArray> billSource = conditionMap.get("6");
            List<String> values = billSource.getValue().toJavaList(String.class);
            if (!values.isEmpty()) {
                Integer compare = getMethodNum(billSource.getCompare());
                if (compare == 1 || compare == 15) {
                    boolean f = false;
                    for (String s : values) {
                        if (bill.getSource().contains(BillSourceEnum.getDescByCode(s))) {
                            f = true;
                        }
                        break;
                    }
                    return f;
                } else if (compare == 16) {
                    boolean f = true;
                    for (String s : values) {
                        if (bill.getSource().contains(BillSourceEnum.getDescByCode(s))) {
                            f = false;
                        }
                        break;
                    }
                    return f;
                }
                return false;
            }
        }
        return true;
    }

    // 判断税率
    private boolean doTaxRate(Map<String, VoucherRuleCondition> conditionMap, BillInferenceV bill) {
        if (conditionMap.containsKey("11")) {
            VoucherRuleCondition<JSONArray> taxRate = conditionMap.get("11");
            JSONArray values = taxRate.getValue();
            if (!values.isEmpty()) {
                // todo 优化税率
                List<InvoiceReceiptDetailE> list = invoiceDomainService.getBillIdsByBillIdsAndTaxRate(
                    Collections.singletonList(bill.getId()),
                    getMethodNum(taxRate.getCompare()), values);
                if (!list.isEmpty()) {
                    List<Long> billIds = list.stream().map(InvoiceReceiptDetailE::getBillId).collect(
                        Collectors.toList());
                    return billIds.contains(bill.getId());
                }
                return false;
            }
        }
        return true;
    }
    /**
     * 记录需要的数据
     * @param bill
     * @param eventTypeEnum
     * @param voucherRule
     * @param billTypeEnum
     * @param fromAmount 外部带来的金额
     */
    @Transactional
    public String writeDown(BillInferenceV bill, EventTypeEnum eventTypeEnum, VoucherRuleE voucherRule, BillTypeEnum billTypeEnum, Long fromAmount) {

        Long amount = 0L;
        if (fromAmount == null || fromAmount <= 0) {
            amount = voucherInferenceAppServiceFactory.getInstance(eventTypeEnum).generateInferenceAmount(bill, billTypeEnum);
        } else {
            amount = fromAmount;
        }

        dealVoucherSubjects(voucherRule, bill);

        VoucherE voucherE = BatchGeneratePrefabricationVoucher(Collections.singletonList(bill), billTypeEnum, eventTypeEnum, voucherRule);
        List<VoucherE> voucherES = Collections.singletonList(voucherE);

        voucherDomainService.batchInsert(voucherES);
        List<Long> voucherIds = voucherES.stream().map(VoucherE::getId).collect(Collectors.toList());
        billFacade.insertInference(bill.getId(), bill.getConcatId(), billTypeEnum.getCode(), eventTypeEnum.getEvent());
        batchInsertVoucherBills(Collections.singletonList(bill), billTypeEnum, voucherIds);
        return voucherE.getVoucherNo();
    }

    /**
     * 获取账单的结算信息
     * @param billId
     * @param billTypeEnum
     * @return
     */
    protected List<BillSettleV> getBillSettleList(Long billId, BillTypeEnum billTypeEnum, String supCpUnitId) {
        return billFacade.getBillSettle(Collections.singletonList(billId), billTypeEnum, supCpUnitId);
    }

    protected boolean isNotNormalBill(BillInferenceV bill) {
        return bill.getState() != 0 || bill.getVerifyState() == 1 || bill.getReversed() == 1;
    }

    /**
     * 根据明细筛选账单（结算用）
     * @param eventType
     * @param fieldList
     * @param conditions
     * @param record
     * @param isSingle
     */
    protected void inferenceDetailByBillType(Integer eventType, List<Field> fieldList, String conditions, VoucherRuleE record, boolean isSingle, BillTypeEnum billTypeEnum) {

            long pageNum = 1;
            boolean isLast = false;
        List<VoucherRuleCondition> conditionList = JSONArray.parseArray(conditions, VoucherRuleCondition.class);
        do {
                PageV<BillInferenceV> page = billFacade.listBillInferenceInfoByDetail(eventType, fieldList, billTypeEnum, pageNum);
                pageNum++;
                List<BillInferenceV> records = page.getRecords();
                if (records != null && !records.isEmpty()) {
                    records = filterBill(records, conditionList);
                    if (records != null && !records.isEmpty()) {
                        if (isSingle) {
                            writeDownList(records, eventType, billTypeEnum, record);
                        } else {
                            Map<String, List<BillInferenceV>> groupByBase = groupByBase(records);
                            batchWriteDownListGroupBySup(groupByBase, record, EventTypeEnum.valueOfByCodeByEvent(eventType), billTypeEnum, false);
//                            Map<Long, List<BillInferenceV>> map = records.stream()
//                                .collect(Collectors.groupingBy(BillInferenceV::getChargeItemId));
//                            Iterator<Entry<Long, List<BillInferenceV>>> it = map.entrySet().iterator();
//                            while (it.hasNext()) {
//                                batchWriteDownList(it.next().getValue(), record, EventTypeEnum.valueOfByCodeByEvent(eventType), billTypeEnum, false);
//                            }
                        }
                    }
                }
                isLast = page.isLast();
            } while (!isLast);
    }

    /**
     * 根据账单id获取账单收款方式
     * @param billIds 账单id
     * @return
     */
    public Optional<Map<Long, List<String>>> getBillSettleChannelList(List<Long> billIds, String supCpUnitId) {

        List<BillSettleChannelV> billSettleChannelList = billSettleAppService.listBillSettleChannelByIds(billIds, supCpUnitId);
        if (billSettleChannelList.isEmpty()) {
            return Optional.empty();
        }
        Map<Long, List<String>> map = new HashMap<>();
        for (BillSettleChannelV billSettleChannelRV : billSettleChannelList) {
            List<String> list = map.getOrDefault(billSettleChannelRV.getBillId(), new ArrayList<>());
            list.add(billSettleChannelRV.getSettleChannel());
            map.put(billSettleChannelRV.getBillId(), list);
        }
        return Optional.of(map);
    }

    @Override
    public void doAfterInfer() {

    }


    /**
     * 填充现金流量
     */
    public List<CashFlowE> fillCashFlow(Long subjectId) {
        List<CashFlowE> list = cashFlowDomainService.listCashFlowBySubjectId(subjectId, true);
        return list;
    }

    /**
     * 推送预制凭证
     * @param voucherList 预制凭证集合
     * @return
     */
    @Override
    public Map<String, Object> inferPrefabricationVoucher(List<VoucherE> voucherList) {

        voucherDomainService.updateVoucherNoInferStateByIds(voucherList.stream().map(VoucherE::getId).collect(Collectors.toList()), 4);
        int successCount = 0;
        List<Long> errorList = new ArrayList<>();
        for (VoucherE prefabricationVoucher : voucherList) {

            if (StringUtils.isBlank(prefabricationVoucher.getPrefabricationDetails())) {
                return null;
            }
            PrefabricationVoucher prefabrication = JSON.parseObject(prefabricationVoucher.getPrefabricationDetails(), PrefabricationVoucher.class);

            AccountbookDTO accountbookDTO = accountBookAppService.detailAccountBook(
                prefabrication.getAccountBookId());


            UfinterfaceF form = new UfinterfaceF().create();
            VoucherF voucher = new VoucherF();
            String voucherId = prefabrication.getTime() + Instant.now().getEpochSecond();

            voucher.setId(voucherId);
            form.setVoucher(voucher);

            VoucherHeadF voucherHead = new VoucherHeadF().create();
            voucherHead.setYear(prefabrication.getTime().substring(0, prefabrication.getTime().indexOf("-"))); // 归纳的年份
            voucherHead.setPk_system("GL");                         // 来源系统 默认GL
            voucherHead.setPk_accountingbook(StringUtils.isBlank(prefabrication.getAccountBookCode()) ?
                accountbookDTO.getCode() : prefabrication.getAccountBookCode());             // 等待nc的编码
            voucherHead.setPeriod(prefabrication.getTime().substring(prefabrication.getTime().indexOf("-") + 1)); // 凭证所属的月份
            voucherHead.setPrepareddate(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())); // 账单生成时间
            voucherHead.setPk_prepared("liuyancui");                      // todo 等待nc提供  可能默认使用一个人，也可能需根据人员档案看
            voucherHead.setTallydate(""); // 记账日期
            voucherHead.setPk_manager(""); // 记账人
            voucherHead.setPk_org("010010010A");                           // todo 等待nc 系统有组织档案
            voucherHead.setPk_org_v("");                            // 不用填
            voucherHead.setPk_group("G");                         // nc系统所建集团编码

            voucherHead.setDetails(generateVoucherHeadDetailsByPrefabrication(prefabricationVoucher));

            voucher.setVoucher_head(voucherHead);

            log.info("推凭开始 参数: {}", JSON.toJSONString(form));
            SendresultV sendresultV = null;
            try {
                sendresultV = oldVoucherFacade.externalInference(form);
                log.info("凭证推送成功 结果: {}", JSON.toJSONString(sendresultV));
                boolean result;
                String reason = "";
                if (null != sendresultV && 1 == sendresultV.getResultcode()) {
                    result = true;
                } else {
                    result = false;
                    if (null != sendresultV) {
                        reason = dealReason(sendresultV.getResultdescription());
                    } else {
                        reason = "出现未知错误，请快速定位处理！";
                    }
                }
                voucherDomainService.updateVoucherNoByIds(
                    Collections.singletonList(prefabricationVoucher.getId()), reason, result);
                successCount++;
            } catch (Exception e) {
                log.error("凭证推送失败，推送参数为： {}", JSON.toJSONString(form));
                log.error("凭证推送失败 ---------> : ", e);
                prefabricationVoucher.setReason("请求nc凭证保存接口失败或者超时！");
                voucherDomainService.updateVoucherNoByIds(
                    Collections.singletonList(prefabricationVoucher.getId()), prefabricationVoucher.getReason(), false);
                if (StringUtils.isNotBlank(prefabricationVoucher.getBillInferIds())) {
                    JSONObject parse = JSONObject.parseObject(prefabricationVoucher.getBillInferIds());
                    billFacade.delBillInferencesByIds(JSONArray.parseArray(parse.getString("inferIds"), Long.class),
                        EventTypeEnum.valueOfByCodeByEvent(parse.getIntValue("eventTypeEnum")), BillTypeEnum.valueOfByCode(parse.getIntValue("billTypeEnum")),
                        JSONArray.parseArray(parse.getString("concatIds"), Long.class));
                }
                errorList.add(prefabrication.getId());
            }
        }
//        fillVoucherRecordByPrefabrication(record, successCount > 0);
        Map<String, Object> result = new HashMap<>();
        if (successCount == voucherList.size()) {
            result.put("level", "success");
            result.put("successTotal", successCount);
            result.put("errorTotal", 0);
        } else if (successCount > 0) {
            result.put("level", "warn");
            result.put("successTotal", successCount);
            result.put("errorTotal", voucherList.size() - successCount);
            result.put("errorList", errorList);
        } else {
            result.put("level", "error");
            result.put("successTotal", 0);
            result.put("errorTotal", voucherList.size());
            result.put("errorList", errorList);
        }
        return result;
    }

    /**
     * 截取需要的异常
     * @param resultdescription
     * @return
     */
    private String dealReason(String resultdescription) {
        if (resultdescription.contains("异常信息:")) {
            return resultdescription.substring(resultdescription.indexOf("异常信息:") + 5);
        } else if (resultdescription.contains("处理错误:")) {
            return resultdescription.substring(resultdescription.indexOf("处理错误:") + 5);
        } else {
            return resultdescription;
        }
    }

    /**
     * 给推凭记录返回结果
     * @param record
     * @param success
     */
    private void fillVoucherRecordByPrefabrication(VoucherInferenceRecordE record, boolean success) {
        record.setSuccessState(success ? 1 : 2);
        // 修改数据状态
        voucherInferenceRecordDomainService.updateById(record);
    }

    /**
     * 生成凭证推送记录
     * @param voucherList
     * @return
     */
    private VoucherInferenceRecordE buildInferRecord(List<VoucherE> voucherList) {

        VoucherInferenceRecordE record = new VoucherInferenceRecordE();
        record.setChargeItemId(0L);
        record.setChargeItemName("");
        // TODO 数据待填入
        record.setVoucherSystem("用友NCC");

        record.setSuccessState(0);
        record.setVoucherRuleName("");
        record.setVoucherRuleId(0L);
        record.setEventType(EventTypeEnum.手动生成.getEvent());
        record.setVoucherIds(JSON.toJSONString(voucherList.stream().map(VoucherE::getId).collect(Collectors.toList())));

        long amount = voucherList.stream().mapToLong(VoucherE::getAmount).sum();
        record.setDebitAmount(amount);
        record.setCreditAmount(amount);

        voucherInferenceRecordDomainService.insert(record);
        return record;
    }

    /**
     * 生成凭证推送记录
     * @param voucherList
     * @param baseGroup
     * @param rule
     * @return
     */
    private VoucherInferenceRecordE buildInferRecordForAuto(List<VoucherE> voucherList,
        Set<String> baseGroup, VoucherRuleE rule) {

        VoucherInferenceRecordE record = new VoucherInferenceRecordE();
        record.setChargeItemId(0L);
        StringJoiner joiner = new StringJoiner(";");
        baseGroup.forEach(item -> {
            joiner.add(item.substring(item.lastIndexOf("#") + 1));
        });
        record.setChargeItemName(joiner.toString());
        // TODO 数据待填入
        record.setVoucherSystem("用友NCC");

        record.setSuccessState(0);
        record.setVoucherRuleName(rule.getRuleName());
        record.setVoucherRuleId(rule.getId());
        record.setEventType(rule.getEventType());
        record.setVoucherIds(JSON.toJSONString(voucherList.stream().map(VoucherE::getId).collect(Collectors.toList())));

        long amount = voucherList.stream().mapToLong(VoucherE::getAmount).sum();
        record.setDebitAmount(amount);
        record.setCreditAmount(amount);

        voucherInferenceRecordDomainService.insert(record);
        return record;
    }

    /**
     * 预制凭证推凭
     * @param voucherE
     * @return
     */
    private List<DetailsF> generateVoucherHeadDetailsByPrefabrication(VoucherE voucherE) {

        if (StringUtils.isBlank(voucherE.getPrefabricationDetails())) {
            return Collections.emptyList();
        }
        PrefabricationVoucher prefabrication = JSON.parseObject(voucherE.getPrefabricationDetails(), PrefabricationVoucher.class);
        List<DetailsF> details = new ArrayList<>();

        if (!CollectionUtils.isEmpty(prefabrication.getDetails())) {
            int index = 1;
            for (VoucherDetail voucherDetail : prefabrication.getDetails()) {
                DetailsF detail = new DetailsF();
                detail.setDetailindex((index++) + "");                        // 循环增加
                detail.setExplanation(voucherDetail.getRuleRemark());         // 规则的摘要
                detail.setVerifydate("");
                detail.setPrice("");
                detail.setExcrate2("1");
                detail.setPk_accasoa(voucherDetail.getSubjectCode());     // todo 需ncc换取 需对齐ncc的会计科目
                List<CashFlowE> cashFlowES = fillCashFlow(voucherDetail.getSubjectId());
                if ("debit".equals(voucherDetail.getType())) {
                    detail.setDebitquantity("");
                    detail.setDebitamount(voucherDetail.getDebitAmount().divide(new BigDecimal( 100)) + "");                                  // 原来的钱 （使用的币种）
                    detail.setLocaldebitamount(voucherDetail.getDebitAmount().divide(new BigDecimal( 100)) + "");                             // 所在国家的币种
                    detail.setGroupdebitamount("");
                    detail.setGlobaldebitamount("");
                    // todo 现在全用主表，附表不可用   手动后期会使用附表的情况
                    List<CashFlowE> debitCashFlows = cashFlowES.stream()
                        .filter(item -> "2".equals(item.getItemType())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(debitCashFlows)) {
                        detail.setCashFlow(buildCashFlows(debitCashFlows, voucherDetail.getDebitAmount().divide(new BigDecimal( 100)).doubleValue()));
                    }
                } else {
                    // 借贷返回pk_accasoa不一样
//                    detail.setPk_accasoa("1002"); // todo 这里要删除
                    detail.setCreditquantity("");
                    detail.setCreditamount(voucherDetail.getCreditAmount().divide(new BigDecimal( 100)) + "");
                    detail.setLocalcreditamount(voucherDetail.getCreditAmount().divide(new BigDecimal( 100)) + "");
                    detail.setGroupcreditamount("");
                    detail.setGlobalcreditamount("");
                    // todo 现在全用主表，附表不可用   手动后期会使用附表的情况
                    List<CashFlowE> creditCashFlows = cashFlowES.stream()
                        .filter(item -> "1".equals(item.getItemType())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(creditCashFlows)) {
                        detail.setCashFlow(buildCashFlows(creditCashFlows, voucherDetail.getDebitAmount().divide(new BigDecimal( 100)).doubleValue()));
                    }
                }
                detail.setPk_currtype("CNY");                    //
                detail.setPk_unit("");
                detail.setPk_unit_v("");
                details.add(detail);

                detail.setAss(generateAssFListByPrefabrication(voucherDetail.getSupItems()));
            }

        }
        return details;
    }

    protected List<CashFlowF> buildCashFlows(List<CashFlowE> debitCashFlows, double amount) {
        List<CashFlowF> list = new ArrayList<>();
        CashFlowF cashFlowF = new CashFlowF();
        cashFlowF.setPk_innercorp("cc01");
        cashFlowF.setPk_cashflow(IdentifierFactory.getInstance().serialNumber("cashFlow", "CF", 20));
        cashFlowF.setMoneymain(new BigDecimal(amount));
        cashFlowF.setMoney(new BigDecimal(amount));
        cashFlowF.setMoneygroup(new BigDecimal(amount));
        cashFlowF.setMoneyglobal(new BigDecimal(amount));
        cashFlowF.setM_pk_currtype("CNY");
        return list;
    }

    /**
     * 预制凭证推凭辅助核算
     * @param supItems
     * @return
     */
    private List<AssF> generateAssFListByPrefabrication(List<SupItem> supItems) {
        List<AssF> list = new ArrayList<>();

        if (!CollectionUtils.isEmpty(supItems)) {
            supItems.forEach(item -> {
                AssF ass = new AssF();
                ass.setPk_Checktype(item.getCode());
                ass.setPk_Checkvalue(item.getValue());
                list.add(ass);
            });
        }
        return list;
    }

}
