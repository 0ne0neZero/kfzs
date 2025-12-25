package com.wishare.finance.domains.voucher.facade;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wishare.finance.apis.common.FinanceConstants;
import com.wishare.finance.apps.service.bill.SharedBillAppService;
import com.wishare.finance.domains.bill.consts.enums.AdjustStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillAdjustWayEnum;
import com.wishare.finance.domains.bill.consts.enums.BillApproveStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillRefundStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillReverseStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillSettleStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillStateEnum;
import com.wishare.finance.domains.bill.consts.enums.BillTypeEnum;
import com.wishare.finance.domains.bill.consts.enums.BillVerifyStateEnum;
import com.wishare.finance.domains.bill.consts.enums.CarryoverStateEnum;
import com.wishare.finance.domains.bill.consts.enums.SettleChannelEnum;
import com.wishare.finance.domains.bill.entity.BillAdjustE;
import com.wishare.finance.domains.bill.entity.BillCarryoverDetailE;
import com.wishare.finance.domains.bill.entity.ReceivableBill;
import com.wishare.finance.domains.bill.repository.AdvanceBillRepository;
import com.wishare.finance.domains.bill.repository.GatherBillRepository;
import com.wishare.finance.domains.bill.repository.PayBillRepository;
import com.wishare.finance.domains.bill.repository.PayableBillRepository;
import com.wishare.finance.domains.bill.repository.ReceivableBillRepository;
import com.wishare.finance.domains.bill.repository.mapper.AdvanceBillMapper;
import com.wishare.finance.domains.bill.repository.mapper.BillAdjustMapper;
import com.wishare.finance.domains.bill.repository.mapper.BillCarryoverDetailMapper;
import com.wishare.finance.domains.bill.repository.mapper.GatherBillMapper;
import com.wishare.finance.domains.bill.repository.mapper.ReceivableBillMapper;
import com.wishare.finance.domains.configure.accountbook.entity.AccountBookE;
import com.wishare.finance.domains.configure.accountbook.repository.AccountBookRepository;
import com.wishare.finance.domains.configure.accountbook.repository.mapper.AccountBookMapper;
import com.wishare.finance.domains.configure.chargeitem.entity.ChargeItemE;
import com.wishare.finance.domains.configure.chargeitem.repository.ChargeItemRepository;
import com.wishare.finance.domains.configure.chargeitem.repository.TaxRateRepository;
import com.wishare.finance.domains.configure.organization.dto.ShareChargeCostOrgDto;
import com.wishare.finance.domains.configure.organization.repository.ShareChargeCostOrgRepository;
import com.wishare.finance.domains.configure.subject.consts.enums.AssisteItemTypeEnum;
import com.wishare.finance.domains.configure.subject.entity.AssisteBizType;
import com.wishare.finance.domains.configure.subject.entity.AssisteItemOBV;
import com.wishare.finance.domains.configure.subject.entity.SubjectE;
import com.wishare.finance.domains.configure.subject.repository.AssisteBizTypeRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectMapUnitDetailRepository;
import com.wishare.finance.domains.configure.subject.repository.SubjectRepository;
import com.wishare.finance.domains.configure.subject.strategy.AssisteItemContext;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceLineEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.InvoiceReceiptStateEnum;
import com.wishare.finance.domains.invoicereceipt.consts.enums.SysSourceEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBillCustomerTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherBookModeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherEventTypeEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherPayWayEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionMethodEnum;
import com.wishare.finance.domains.voucher.consts.enums.VoucherRuleConditionTypeEnum;
import com.wishare.finance.domains.voucher.entity.VoucherBook;
import com.wishare.finance.domains.voucher.entity.VoucherBusinessDetail;
import com.wishare.finance.domains.voucher.entity.VoucherMakeError;
import com.wishare.finance.domains.voucher.entity.VoucherRule;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherRuleConditionOptionOBV;
import com.wishare.finance.domains.voucher.entity.VoucherStatutoryBody;
import com.wishare.finance.domains.voucher.repository.mapper.VoucherRuleMapper;
import com.wishare.finance.domains.voucher.strategy.core.AbstractVoucherStrategy;
import com.wishare.finance.domains.voucher.strategy.core.ManualVoucherStrategyCommand;
import com.wishare.finance.domains.voucher.strategy.core.VoucherBusinessBill;
import com.wishare.finance.domains.voucher.strategy.core.VoucherRuleConditionUtils;
import com.wishare.finance.domains.voucher.strategy.core.VoucherStrategyCommand;
import com.wishare.finance.infrastructure.conts.DataDeletedEnum;
import com.wishare.finance.infrastructure.conts.DataDisabledEnum;
import com.wishare.finance.infrastructure.conts.TableNames;
import com.wishare.finance.infrastructure.remote.clients.base.ChargeClient;
import com.wishare.finance.infrastructure.remote.clients.base.ExternalClient;
import com.wishare.finance.infrastructure.remote.clients.base.OrgClient;
import com.wishare.finance.infrastructure.remote.clients.base.SpaceClient;
import com.wishare.finance.infrastructure.remote.fo.yonyounc.UfinterfaceF;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceCostRv;
import com.wishare.finance.infrastructure.remote.vo.org.OrgFinanceRv;
import com.wishare.finance.infrastructure.remote.vo.space.SpaceDetails;
import com.wishare.finance.infrastructure.remote.vo.yonyounc.SendresultV;
import com.wishare.finance.infrastructure.utils.AmountUtils;
import com.wishare.finance.infrastructure.utils.DateTimeUtil;
import com.wishare.finance.infrastructure.utils.WrapperUtils;
import com.wishare.starter.Global;
import com.wishare.starter.exception.BizException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 凭证防腐层
 *
 * @author dxclay
 * @version 1.0
 * @since 2023/4/5
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VoucherFacade {

    private final OrgClient orgClient;
    private final ExternalClient externalClient;
    private final ChargeClient chargeClient;
    private final TaxRateRepository taxRateRepository;
    private final SubjectRepository subjectRepository;
    private final ChargeItemRepository chargeItemRepository;
    private final AccountBookRepository accountBookRepository;
    private final AccountBookMapper accountBookMapper;
    private final ReceivableBillRepository receivableBillRepository;
    private final ReceivableBillMapper receivableBillMapper;
    private final BillCarryoverDetailMapper carryoverDetailMapper;
    private final SubjectMapUnitDetailRepository subjectMapUnitDetailRepository;
    private final GatherBillRepository gatherBillRepository;
    private final GatherBillMapper gatherBillMapper;
    private final AdvanceBillRepository advanceBillRepository;
    private final AdvanceBillMapper advanceBillMapper;
    private final PayableBillRepository payableBillRepository;
    private final PayBillRepository payBillRepository;
    private final AssisteBizTypeRepository assisteBizTypeRepository;
    private static final Logger log = LoggerFactory.getLogger(AbstractVoucherStrategy.class);
    private final SpaceClient spaceClient;
    private final ShareChargeCostOrgRepository shareChargeCostOrgRepository;
    private final SharedBillAppService sharedBillAppService;
    private final BillAdjustMapper billAdjustMapper;
    private final VoucherRuleMapper voucherRuleMapper;


    /**
     * 推送凭证
     * @param form
     * @return
     */
    public SendresultV pushVoucher(UfinterfaceF form) {
        return externalClient.addVouchers(form);
    }

    /**
     * 获取票据类型
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getInvoiceTypesOptions(){
        return Arrays.stream(InvoiceLineEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), i.getNuonuoCode(), i.getDes())).collect(Collectors.toList());
    }

    /**
     * 获取支付渠道
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getPayWayOptions(){
        return Arrays.stream(SettleChannelEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), i.getCode(), i.getValue())).collect(Collectors.toList());
    }

    /**
     * 获取支付渠道
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getAdjustTypeOptions(){
        return Arrays.stream(BillAdjustWayEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
    }

    /**
     * 获取调整类型
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getAdjustWayOptions(){
        return Arrays.stream(BillAdjustTypeEnum.values()).map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getCode()), String.valueOf(i.getCode()), i.getValue())).collect(Collectors.toList());
    }

    /**
     * 获取税率信息
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getTaxRateOptions(){
        return taxRateRepository.listWithSize(1000).stream().map(i -> new VoucherRuleConditionOptionOBV(String.valueOf(i.getId()), i.getCode(), i.getRate().toString() + "%")).collect(Collectors.toList());
    }

    /**
     * 获取业务类型选项信息
     * @return
     */
    public List<VoucherRuleConditionOptionOBV> getAssisteBizTypeOptions() {
        LambdaQueryWrapper<AssisteBizType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AssisteBizType::getDisabled, DataDisabledEnum.启用.getCode())
                .eq(AssisteBizType::getDeleted, DataDeletedEnum.NORMAL.getCode())
                .orderByAsc(AssisteBizType::getCode)
                ;
        List<AssisteBizType> assisteBizTypes = assisteBizTypeRepository.list(wrapper);
        return assisteBizTypes.stream().map(e ->
                new VoucherRuleConditionOptionOBV(String.valueOf(e.getId()), String.valueOf(e.getCode()), e.getName())
        ).collect(Collectors.toList());
    }

    /**
     * 获取应收计提业务单据信息
     * @param queryWrapper
     * @return
     */
    public List<VoucherBusinessBill> getBusinessBillByReceive(QueryWrapper<?> queryWrapper) {
        int size = 500;
        int index = 1;
        List<VoucherBusinessBill> voucherBusinessBills = new ArrayList<>();
        List<ReceivableBill> receivableBills;
        Page<ReceivableBill> receivableBillPage;
        boolean isLast = false;
        while (isLast) {
            receivableBillPage = receivableBillRepository.pageByWrapper(Page.of(index, size), queryWrapper);
            receivableBills = receivableBillPage.getRecords();
            if (CollectionUtils.isEmpty(receivableBills) || receivableBills.size() < size){
                break;
            }
            voucherBusinessBills.addAll(receivableBills.stream().map(item -> {
                VoucherBusinessBill voucherBusinessBill = new VoucherBusinessBill();
                //voucherBusinessBill.setId();
                //voucherBusinessBill.setBusinessBillId();
                //voucherBusinessBill.setBusinessBillType();
                //voucherBusinessBill.setFee();
                //voucherBusinessBill.setTotalAmount();
                //voucherBusinessBill.setReceivableAmount();
                //voucherBusinessBill.setDeductibleAmount();
                //voucherBusinessBill.setDiscountAmount();
                //voucherBusinessBill.setActualPayAmount();
                //voucherBusinessBill.setTaxAmount();
                //voucherBusinessBill.setTaxRate();
                return voucherBusinessBill;
            }).collect(Collectors.toList()));
        }
        return voucherBusinessBills;

    }


    /**
     * 根据成本中心和费项获取账簿信息
     * @param costCenterId 成本中心id
     * @param chargeItemId 费项id
     * @param statutoryBodyId 法定单位id
     * @return
     */
    public List<VoucherBook> getBook(Long costCenterId, Long chargeItemId,Long statutoryBodyId) {
        List<AccountBookE> accountBookES = accountBookRepository.listByCostAndChargeItem(costCenterId, chargeItemId,statutoryBodyId);
        return CollectionUtils.isNotEmpty(accountBookES) ? accountBookES.stream().map(i -> {
            VoucherBook voucherBook = new VoucherBook();
            voucherBook.setBookId(i.getId());
            voucherBook.setBookCode(i.getCode());
            voucherBook.setBookName(i.getName());
            return voucherBook;
        }).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * 根据凭证条件查询项目id
     * @param conditions
     * @return
     */
    public List<String> getCommunityIds(List<VoucherRuleConditionOBV> conditions, VoucherMakeError voucherMakeError) {
        Set<String> communityIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(conditions)) {
            conditions.forEach(condition -> {
                VoucherRuleConditionTypeEnum ruleConditionTypeEnum = VoucherRuleConditionTypeEnum.valueOfByCode(condition.getType());
                switch (ruleConditionTypeEnum) {
                    case 成本中心:
                        List<String> ids = condition.getValues().stream().map(VoucherRuleConditionOptionOBV::getId).collect(Collectors.toList());
                        ids.forEach(id -> {
                            OrgFinanceCostRv cost = getOrgFinanceCostByIdNoExe(Long.valueOf(id));
                            if (Objects.nonNull(cost)) {
                                if (StringUtils.isNotBlank(cost.getCommunityId())) {
                                    communityIdSet.add(cost.getCommunityId());
                                } else {
                                    setVoucherMakeErrorMessage(voucherMakeError, " 成本中心未找到对应项目:成本中心名称：" + cost.getNameCn() + "，成本中心id：" + cost.getId());
                                }
                            } else {
                                setVoucherMakeErrorMessage(voucherMakeError, " 找不到对应成本中心，对应id:" + id);
                            }
                        });
                        break;
                    case 法定单位:
                        communityIdSet.add("default");
                        break;
                }
            });
        }
        return new ArrayList<>(communityIdSet);
    }

    private OrgFinanceCostRv getOrgFinanceCostByIdNoExe(Long id){

        try {
            return orgClient.getOrgFinanceCostById(Long.valueOf(id));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("getOrgFinanceCostByIdNoExe获取成本中心异常:{}",e.getMessage());
        }
        return null;

    }

    public void setVoucherMakeErrorMessage(VoucherMakeError voucherMakeError, String errorMessage) {
        if (Objects.nonNull(voucherMakeError)) {
            voucherMakeError.addMessage(errorMessage);
        }
    }

    /**
     * 根据凭证条件查询项目id
     * @param conditions
     * @return
     */
    public List<String> getCommunityIds(List<VoucherRuleConditionOBV> conditions) {
        return getCommunityIds(conditions, null);
    }

    public HashMap<String, Set<Long>> getBookIdByCommunityId( List<String> comms){
        Map<String, List<Long>> map = chargeClient.getBusinessUnitsByCommunityId(new HashSet<>(comms));
        HashMap<String, Set<Long>> vo = new HashMap<String, Set<Long>>();
        map.forEach((communityId,businessUnits)->{
            List<Long> longList = accountBookMapper.getIdByBusinessUnits(businessUnits);
            if (CollUtil.isNotEmpty(longList)){
                vo.put(communityId,new HashSet<>(longList));
            }
        });

        log.info("getBookIdByCommunityId-{}",vo);
        return vo;
    }



    /**
     * 根据业务单元id获取账簿信息
     * @param businessUnitId 费项id
     * @return
     */
    public List<VoucherBook> business_unit_id(Long businessUnitId) {
        LambdaQueryWrapper<AccountBookE> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AccountBookE::getStatutoryBodyId,businessUnitId)
        .eq(AccountBookE::getDeleted,DataDeletedEnum.NORMAL.getCode());
        List<AccountBookE> accountBookES = accountBookRepository.list(wrapper);
        return CollectionUtils.isNotEmpty(accountBookES) ? accountBookES.stream().map(i -> {
            VoucherBook voucherBook = new VoucherBook();
            voucherBook.setBookId(i.getId());
            voucherBook.setBookCode(i.getCode());
            voucherBook.setBookName(i.getName());
            return voucherBook;
        }).collect(Collectors.toList()) : new ArrayList<>();
    }

    /**
     * 获取凭证法定单位信息
     * @param statutoryBodyId 法定单位id
     * @return 法定单位信息
     */
    public VoucherStatutoryBody getVoucherStatutoryBody(Long statutoryBodyId){
        OrgFinanceRv orgFinance = orgClient.getOrgFinanceById(statutoryBodyId);
        if (Objects.nonNull(orgFinance)){
            VoucherStatutoryBody voucherStatutoryBody = new VoucherStatutoryBody();
            voucherStatutoryBody.setStatutoryBodyId(orgFinance.getId());
            voucherStatutoryBody.setStatutoryBodyCode(orgFinance.getCode());
            voucherStatutoryBody.setStatutoryBodyName(orgFinance.getNameCn());
            return voucherStatutoryBody;
        }
        return null;
    }

    /**
     * 根据科目编码获取科目信息
     * @param subjectCodes
     * @return
     */
    public List<SubjectE> getSubjects(List<String> subjectCodes){
        return subjectRepository.listByCodes(subjectCodes);
    }

    /**
     * 根据科目id获取科目信息
     * @param subjectCode 科目id
     * @return 科目信息
     */
    public SubjectE getSubjectByCode(String subjectCode){
        return subjectRepository.getByCode(subjectCode);
    }

    /**
     * 是否叶子节点
     * @param subjectId
     * @return
     */
    public SubjectE getSubject(Long subjectId) {
        return subjectRepository.getSubject(subjectId);
    }

    /**
     * 根据费项获取映射科目信息
     * @param subjectId
     * @param chargeItemId
     * @return
     */
    public SubjectE getSubjectByChargeItem(Long subjectId, Long chargeItemId) {
       return subjectMapUnitDetailRepository.getSubject(subjectId, chargeItemId, 1);
    }

    /**
     * 获取费项信息
     * @param chargeItemId
     * @return
     */

    public ChargeItemE getChargeItemById(Long chargeItemId){
       return chargeItemRepository.getById(chargeItemId);
    }


    /**
     * 获取辅助核算信息
     * @param aiId 业务辅助核算id
     * @param assisteItemType 辅助核算类型
     * @return
     */
    public AssisteItemOBV getAssisteItem(String aiId, AssisteItemTypeEnum assisteItemType){
       return AssisteItemContext.getStrategy(assisteItemType).getById(aiId);
    }

    public Set<Long> getAccountBookId(Long ruleId){
        VoucherRule voucherRule = voucherRuleMapper.selectById(ruleId);
        Integer mode = voucherRule.getBookMode();
        if (mode == VoucherBookModeEnum.指定账簿.getCode()){
            VoucherBook book = voucherRule.getBook();
            if (ObjectUtil.isNull(book)){
                throw new BizException(401,"规则"+ruleId+"指定账簿为空");
            }
            return Collections.singleton(book.getBookId());
        }
        return null;
    }

    private String getVoucherBusinessDetailKey(Long accountBookId){
        return "voucher_business_detail_" +Math.abs(accountBookId.hashCode() % 512);
    }

    /**
     * 获取凭证应收计提及应收计提冲回业务单据（应收单、临时单）信息
     *
     * @param conditions 查询条件
     * @param command
     * @return 应收单据
     */
    public List<VoucherBusinessBill> getReceivableList(List<VoucherRuleConditionOBV> conditions, VoucherStrategyCommand command, Integer sceneType) {
        // 判断是否有来源 收费系统 规则过滤条件
        boolean sourceSystem = checkSourceSystem(conditions);
        List<String> communityIds = getCommunityIds(conditions);
        List<VoucherBusinessBill> result = new ArrayList<>();
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }
        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }

            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                
                String tableName = getVoucherBusinessDetailKey(bookId);
                // 查询应收单
                QueryWrapper<?> receivableWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
                receivableWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.应收账单.getCode())
                        .le("b.account_date", DateUtil.endOfMonth(DateUtil.date()).toDateStr())
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .ne("b.description","开发代付")
                        .ne("b.state", BillStateEnum.作废.getCode())
                        .ne("b.refund_state",BillRefundStateEnum.已退款.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                if(VoucherEventTypeEnum.应收计提.getCode() == sceneType){
                    receivableWrapper.isNull("b.bill_label");
                }
                if(VoucherEventTypeEnum.应收计提冲回.getCode() == sceneType){
                    receivableWrapper.lt("b.receivable_amount", 0);
                }
                WrapperUtils.logWrapper(command,receivableWrapper);
                List<VoucherBusinessBill> receivableList = receivableBillRepository.listByQuery(receivableWrapper, sceneType,tableName);
                if (CollectionUtils.isNotEmpty(receivableList)) {
                    result.addAll(receivableList);
                }

                // 查询临时单
                QueryWrapper<?> tempWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
                tempWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        // 临时单
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                        .eq("gd.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("gd.available", DataDisabledEnum.启用.getCode())
                        .le("b.account_date", DateUtil.endOfMonth(DateUtil.date()).toDateStr())
                        .ne("b.description","开发代付")
                        .ne("b.state",BillStateEnum.作废.getCode())
                        .ne("b.refund_state",BillRefundStateEnum.已退款.getCode())
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                if(VoucherEventTypeEnum.应收计提冲回.getCode() == sceneType){
                    tempWrapper.lt("b.receivable_amount", 0);
                }
                WrapperUtils.logWrapper(command,tempWrapper);
                List<VoucherBusinessBill> tempList = receivableBillRepository.tempBillListByQuery(tempWrapper, sceneType,tableName);
                if (CollectionUtils.isNotEmpty(tempList)) {
                    result.addAll(tempList);
                }
            }
        }
        List<VoucherBusinessBill> voucherBusinessBills = replaceBusinessBill(result,sceneType);
        // 过滤空置房条件(如果有收费系统来源条件)
        return sourceSystem ? filterByCustomerType(voucherBusinessBills) : voucherBusinessBills;
    }

    /**
     * 替换分成费项及分成比例
     * @param tempList
     * @return
     */
    private List<VoucherBusinessBill> replaceBusinessBill(List<VoucherBusinessBill> tempList,Integer sceneType) {
        if(VoucherEventTypeEnum.应收计提.getCode() != sceneType && VoucherEventTypeEnum.收款结算.getCode() != sceneType){
            return tempList;
        }
        List<VoucherBusinessBill> tempListOne = new ArrayList<>();
        final HashMap<String, ShareChargeCostOrgDto> costOrgDtoHashMap = new HashMap<>(64);
        Map<String, ChargeItemE> shareChargeItemMap = new HashMap<>(64);
        for (VoucherBusinessBill voucherBusinessBill : tempList) {
            if(Optional.ofNullable(voucherBusinessBill.getCostCenterId()).isPresent()){
                ShareChargeCostOrgDto shareChargeInfo = getShareChargeCostOrgMap(costOrgDtoHashMap, voucherBusinessBill);
                if(Optional.ofNullable(shareChargeInfo).isPresent()){
                    log.info("存在分成费项:{}, 单据信息{}", JSON.toJSONString(shareChargeInfo), JSON.toJSONString(voucherBusinessBill));
                    String shareKey = shareChargeInfo.getShareChargeId() + "-" + voucherBusinessBill.getChargeItemId();
                    ChargeItemE chargeItemE = null;
                    if (shareChargeItemMap.containsKey(shareKey)) {
                        chargeItemE = shareChargeItemMap.get(shareKey);
                    } else {
                        // 通过分摊信息找到分摊的另一个费项
                        LambdaQueryWrapper<ChargeItemE> wrapper = new LambdaQueryWrapper<>();
                        wrapper.eq(ChargeItemE::getDisabled,DataDisabledEnum.启用.getCode()).
                                eq(ChargeItemE::getDeleted,DataDeletedEnum.NORMAL.getCode()).
                                eq(ChargeItemE::getId,voucherBusinessBill.getChargeItemId());
                        ChargeItemE parentChargeItem = chargeItemRepository.getOne(wrapper);
                        if (Objects.nonNull(parentChargeItem) && StringUtils.isNotBlank(parentChargeItem.getShareChargeId())) {
                            String[] shareIds = parentChargeItem.getShareChargeId().split(",");
                            if (CollectionUtils.size(shareIds) >= 2) {
                                for (String shareId : shareIds) {
                                    if (!StringUtils.equals(shareId, String.valueOf(shareChargeInfo.getShareChargeId()))) {
                                        chargeItemE = chargeItemRepository.getById(shareId);
                                        shareChargeItemMap.put(shareKey, chargeItemE);
                                    }
                                }
                            }
                        }
                    }
                    if(Objects.nonNull(chargeItemE)){
                        VoucherBusinessBill voucherBusinessBillOne = Global.mapperFacade.map(voucherBusinessBill, VoucherBusinessBill.class);
                        VoucherBusinessBill voucherBusinessBillTwo = Global.mapperFacade.map(voucherBusinessBill, VoucherBusinessBill.class);
                        BigDecimal receivableAmount = AmountUtils.longToDecimal(voucherBusinessBill.getReceivableAmount()).multiply(shareChargeInfo.getShareProportion().
                                divide(new BigDecimal(100), RoundingMode.HALF_UP));
                        BigDecimal actualPayAmount = AmountUtils.longToDecimal(voucherBusinessBill.getActualPayAmount()).multiply(shareChargeInfo.getShareProportion().
                                divide(new BigDecimal(100), RoundingMode.HALF_UP));
                        voucherBusinessBillOne.setReceivableAmount(receivableAmount.longValue());
                        voucherBusinessBillOne.setActualPayAmount(actualPayAmount.longValue());
                        voucherBusinessBillOne.setChargeItemId(shareChargeInfo.getShareChargeId());
                        voucherBusinessBillOne.setChargeItemName(shareChargeInfo.getShareChargeName());

                        voucherBusinessBillTwo.setReceivableAmount(AmountUtils.longToDecimal(voucherBusinessBill.getReceivableAmount()).subtract(receivableAmount).longValue());
                        voucherBusinessBillTwo.setActualPayAmount(AmountUtils.longToDecimal(voucherBusinessBill.getActualPayAmount()).subtract(actualPayAmount).longValue());
                        voucherBusinessBillTwo.setChargeItemId(chargeItemE.getId());
                        voucherBusinessBillTwo.setChargeItemName(chargeItemE.getName());
                        tempListOne.add(voucherBusinessBillOne);
                        tempListOne.add(voucherBusinessBillTwo);
                    }else {
                        tempListOne.add(voucherBusinessBill);
                    }
                }else {
                    tempListOne.add(voucherBusinessBill);
                }
            }else {
                tempListOne.add(voucherBusinessBill);
            }
        }
        return tempListOne;
    }

    /**
     * 查询费项下对应分成费项和占比
     * @param costOrgDtoHashMap
     * @param voucherBusinessBill
     * @return
     */
    private ShareChargeCostOrgDto getShareChargeCostOrgMap( HashMap<String, ShareChargeCostOrgDto> costOrgDtoHashMap,VoucherBusinessBill voucherBusinessBill){
        Long costCenterId = voucherBusinessBill.getCostCenterId();
        Long chargeItemId = voucherBusinessBill.getChargeItemId();
        String key = costCenterId+"-"+chargeItemId;

        if (costOrgDtoHashMap.containsKey(key)){
           return costOrgDtoHashMap.get(key);
        }else {
            ShareChargeCostOrgDto d = shareChargeCostOrgRepository.getShareChargeInfo(voucherBusinessBill.getCostCenterId(),
                    voucherBusinessBill.getChargeItemId());
            costOrgDtoHashMap.put(key,d);
            return d;
        }
    }

    public void receivableModifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        // 应收计提修改成功做凭证的账单为已推凭状态
        // 应收计提只会获取应收单和临时单，直接更新同一个表即可
        if (CollectionUtils.isNotEmpty(voucherBusinessDetails)) {
            Map<String, List<Long>> businessIdGroupMap = voucherBusinessDetails.stream().collect(Collectors.groupingBy(VoucherBusinessDetail::getSupCpUnitId, Collectors.mapping(VoucherBusinessDetail::getBusinessBillId, Collectors.toList())));
            for (Map.Entry<String, List<Long>> entry : businessIdGroupMap.entrySet()) {
                String supCpUnitId = entry.getKey();
                receivableBillRepository.receivableModifyInferenceStatus(entry.getValue(), supCpUnitId);
            }
//            List<Long> idList = voucherBusinessDetails.stream().map(VoucherBusinessDetail::getBusinessBillId).collect(Collectors.toList());
        }
    }


    private boolean checkSpecialConditionsExist( List<VoucherRuleConditionOBV> conditions){
        AtomicBoolean f = new AtomicBoolean(false);
        conditions.forEach(v->{
            if (v.getType().equals(VoucherRuleConditionTypeEnum.结算方式.getCode())){
                List<VoucherRuleConditionOptionOBV> list = v.getValues();
                for (VoucherRuleConditionOptionOBV obv : list) {
                    if (obv.getCode().equals(VoucherPayWayEnum.结转.getCode())){
                        f.set(true);
                    }
                    if (obv.getCode().equals(VoucherPayWayEnum.冲销结转.getCode())){
                        f.set(true);
                    }
                }
            }
        });
        return f.get();
    }
    /**
     * 查询收款单和预收单的凭证业务单据信息
     * @return 收款结算
     */
    public List<VoucherBusinessBill> getSettleBillList(VoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> voucherBusinessBills = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return voucherBusinessBills;
        }
        // 检查条件是否存在，特殊收款结算
        boolean special = checkSpecialConditionsExist(conditions);

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                //判断是否来自合同系统
                if(checkSourceSystemFromContract(conditions)){
                    QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.收款单);
                    wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                            .eq("rb.state", BillStateEnum.正常.getCode())
                            .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                            .eq("b.sup_cp_unit_id", communityId)
                            .eq("gd.sup_cp_unit_id", communityId)
                            .eq("rb.sup_cp_unit_id", communityId)
                            .eq("rb.business_unit_id",statutoryBodyId)
                            //.le("rb.account_date", DateTimeUtil.nowDate())
                            .isNull("vbd.id");
                    WrapperUtils.logWrapperPro(command,wrapper,"getSettleBillList合同系统-"+BillTypeEnum.收款单.getValue());
                    List<VoucherBusinessBill> gatherVoucherBusinessBills = gatherBillRepository.listVoucherBillByQueryFromContract(wrapper, VoucherEventTypeEnum.收款结算.getCode());
                    voucherBusinessBills.addAll(gatherVoucherBusinessBills);
                }

                // 查询收款单(对应应收、临时、预收)
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.收款单);
                // b-收款单,rb-账单,gd-收款单详情
                wrapper.eq("rb.deleted", DataDisabledEnum.启用.getCode())
                        .in("rb.state",Arrays.asList(BillStateEnum.正常.getCode(),BillStateEnum.冻结.getCode()))
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq("rb.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("gd.available", BillStateEnum.正常.getCode())
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("rb.sup_cp_unit_id", communityId)
                        .eq("rb.business_unit_id",statutoryBodyId)
                        //.le("rb.account_date", DateTimeUtil.nowDate())
                        .isNull("vbd.id");
                WrapperUtils.logWrapperPro(command,wrapper,"getSettleBillList-"+BillTypeEnum.收款单.getValue());
                List<VoucherBusinessBill> gatherVoucherBusinessBills =
                        gatherBillRepository.listVoucherBillByQuery(wrapper,
                                VoucherEventTypeEnum.收款结算.getCode(),special,tableName);
                voucherBusinessBills.addAll(gatherVoucherBusinessBills);

                // ---------预收查询--------
                QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.预收账单);
                advanceWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("gd.available", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        //.le("b.account_date",DateTimeUtil.nowDate())
                        .isNull("vbd.id");
                WrapperUtils.logWrapperPro(command,advanceWrapper,"getSettleBillList-"+BillTypeEnum.预收账单.getValue());

                List<VoucherBusinessBill> advanceVoucherBusinessBills = advanceBillRepository
                        .listVoucherBillByQuery(advanceWrapper, VoucherEventTypeEnum.收款结算.getCode(), special,tableName);
                voucherBusinessBills.addAll(advanceVoucherBusinessBills);
            }
        }
        return replaceBusinessBill(voucherBusinessBills,VoucherEventTypeEnum.收款结算.getCode());
    }

    private boolean checkSourceSystemFromContract(List<VoucherRuleConditionOBV> conditions) {
        if(CollectionUtils.isEmpty(conditions)){
            return false;
        }
        List<VoucherRuleConditionOBV> sourceConditions = conditions.stream().filter(e -> Integer.valueOf(VoucherRuleConditionTypeEnum.单据来源.getCode()).equals(e.getType())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(sourceConditions)){
            return false;
        }
        VoucherRuleConditionOBV sourceCondition = sourceConditions.get(0);
        long sourceChargeCount = 0L;
        switch (VoucherRuleConditionMethodEnum.valueOfByCode(sourceCondition.getMethod())){
            case 包含:
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.合同系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount > 0;
            case 不包含:
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.合同系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount <= 0;
            default:
                return false;
        }

    }

    public void settleModifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {

        if (CollUtil.isEmpty(voucherBusinessDetails)) {
          return;
        }
        //receivable
        Map<String, Set<Long>> idMaps = voucherBusinessDetails.stream().filter(v-> Objects.equals(v.getBusinessBillType(), BillTypeEnum.收款单.getCode()))
                .collect(Collectors.groupingBy(VoucherBusinessDetail::getSupCpUnitId,
                        Collectors.mapping(VoucherBusinessDetail::getReceivableBillId, Collectors.toSet())));
        idMaps.forEach((communityId, billIds) -> {
            if (CollUtil.isNotEmpty(billIds)){
                gatherBillRepository.updateReceivableBillInferenceStateByGatherBillIds(new ArrayList<>(billIds),communityId);
            }

        });
        // 更新advance表
        // BillTypeEnum.预收账单 VoucherBusinessDetail#businessBillId是advance表Id
        Set<Long> advanceIdList = voucherBusinessDetails.stream()
                .filter(v-> Objects.equals(v.getBusinessBillType(), BillTypeEnum.预收账单.getCode())).map(VoucherBusinessDetail::getAdvanceId).collect(Collectors.toSet());
        if (CollUtil.isNotEmpty(advanceIdList)){
            advanceBillRepository.updateInferenceState(new ArrayList<>(advanceIdList));
        }

        // 更新 gather_detail
        Map<String, Set<String>> sceneIdListMap = voucherBusinessDetails.stream()
                .collect(Collectors.groupingBy(VoucherBusinessDetail::getSupCpUnitId,
                        Collectors.mapping(VoucherBusinessDetail::getSceneId, Collectors.toSet())));
        sceneIdListMap.forEach((communityId, sceneIds) -> {
            if (CollUtil.isNotEmpty(sceneIds)){
                gatherBillMapper.updateGatherDetail(new ArrayList<>(sceneIds),communityId);
            }

        });

    }

    /**
     * 查询收款单和预收单的已和银行对账过的凭证业务单据信息
     * @return
     */
    public List<VoucherBusinessBill> getBankSettleBillList(ManualVoucherStrategyCommand command,List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.收款单);
                wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        // 分表键
                        .eq("rb.sup_cp_unit_id", communityId)
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("rb.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,wrapper);
                List<VoucherBusinessBill> gatherVoucherBusinessBills = gatherBillRepository.listVoucherBankBillByQuery(wrapper,tableName);
                if (CollectionUtils.isNotEmpty(gatherVoucherBusinessBills)) {
                    result.addAll(gatherVoucherBusinessBills);
                }

                QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.预收账单);
                advanceWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,advanceWrapper);
                List<VoucherBusinessBill> advanceVoucherBusinessBills = advanceBillRepository.listVoucherBankBillByQuery(advanceWrapper,tableName);
                if (CollectionUtils.isNotEmpty(advanceVoucherBusinessBills)) {
                    result.addAll(advanceVoucherBusinessBills);
                }
            }
        }

        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.结算认领}
     * @return
     */
    public List<VoucherBusinessBill> getClaimBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
            //手动算出分表表名
            String gatherBillTableName = sharedBillAppService.getShareTableName(communityId, TableNames.GATHER_BILL);
            String gatherDetailTableName = sharedBillAppService.getShareTableName(communityId, TableNames.GATHER_DETAIL);
            String receivableBillTableName = sharedBillAppService.getShareTableName(communityId, TableNames.RECEIVABLE_BILL);
            QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.收款单);
            wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                    .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .eq("b.state", BillStateEnum.正常.getCode())
                    .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                    .eq("b.sup_cp_unit_id", communityId)
                    .eq("b.business_unit_id",statutoryBodyId)
                    .isNull("vbd.id");
            WrapperUtils.logWrapper(command,wrapper);
            result.addAll(gatherBillRepository.listVoucherClaimBillByQuery(wrapper,gatherBillTableName,gatherDetailTableName,receivableBillTableName,tableName));
            QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.预收账单);
            advanceWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                    .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                    .eq("b.state", BillStateEnum.正常.getCode())
                    .eq("b.sup_cp_unit_id", communityId)
                    .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                    .eq("b.business_unit_id",statutoryBodyId)
                    .isNull("vbd.id");
            WrapperUtils.logWrapper(command,advanceWrapper);
            result.addAll(advanceBillRepository.listVoucherClaimBillByQuery(advanceWrapper,tableName));
            }
        }
        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.账单减免}
     */
    public List<VoucherBusinessBill> getReducedBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                // 应收减免 是否要过滤模板中已推凭的账单
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
                wrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        // 添加分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        // 过滤已生效的调整记录
                        .eq("ba.state", AdjustStateEnum.已生效.getCode())
                        // 固定调整
                        .eq("ba.adjust_type", BillAdjustTypeEnum.减免.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        // 测试只用一条账单来生成凭证
//                .eq("b.id", "1332637919264002")
//                .eq("b.id", "134487518211308")
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,wrapper);
                List<VoucherBusinessBill> voucherBusinessBills = receivableBillRepository.reducedListByQuery(wrapper,tableName);
                if (CollectionUtils.isNotEmpty(voucherBusinessBills)) {
                    result.addAll(voucherBusinessBills);
                }
            }
        }
        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.收入退款}
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getRefundBillList(ManualVoucherStrategyCommand command,List<VoucherRuleConditionOBV> conditions){
        List<VoucherBusinessBill> result = new ArrayList<>(64);
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.预收账单);
                advanceWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,advanceWrapper);
                List<VoucherBusinessBill> advanceVoucherBusinessBills = advanceBillRepository.listVoucherRefundBillByQuery(advanceWrapper,tableName);
                result.addAll(advanceVoucherBusinessBills);

                QueryWrapper<?> ReceivableRefundWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                ReceivableRefundWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,ReceivableRefundWrapper);
                List<VoucherBusinessBill> receivableRefundBillByQuery = receivableBillRepository.listVoucherReceivableRefundBillByQuery(ReceivableRefundWrapper,tableName);
                if (CollectionUtils.isNotEmpty(receivableRefundBillByQuery)) {
                    result.addAll(receivableRefundBillByQuery);
                }
                QueryWrapper<?> temporaryRefundWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.临时收费账单);
                temporaryRefundWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.scene_id");
                WrapperUtils.logWrapper(command,temporaryRefundWrapper);
                List<VoucherBusinessBill> temporaryRefundBillByQuery = receivableBillRepository.listVoucherTemporaryRefundBillByQuery(temporaryRefundWrapper,tableName);
                if (CollectionUtils.isNotEmpty(temporaryRefundBillByQuery)) {
                    result.addAll(temporaryRefundBillByQuery);
                }
            }
        }
        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.应付计提,VoucherEventTypeEnum.应付计提冲回}
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getPayableBillList(ManualVoucherStrategyCommand command,List<VoucherRuleConditionOBV> conditions,Integer sceneType){
        QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
        List<VoucherBusinessBill> result = new ArrayList<>(64);
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .le("b.account_date", DateTimeUtil.nowDate())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.scene_id");
                if (VoucherEventTypeEnum.应付计提冲回.getCode() == sceneType) {
                    wrapper.lt("b.receivable_amount", 0);
                }
                WrapperUtils.logWrapper(command, wrapper);
                List<VoucherBusinessBill> bill = payableBillRepository.listVoucherPayableBillByQuery(wrapper, sceneType,tableName);

                result.addAll(bill);
            }
        }

        return result;
    }

    public void payableModifyInferenceStatus(List<VoucherBusinessDetail> voucherBusinessDetails) {
        // 修改成功做了凭证的应付单状态为已推凭
        if (CollectionUtils.isNotEmpty(voucherBusinessDetails)) {
            List<Long> idList = voucherBusinessDetails.stream().map(VoucherBusinessDetail::getBusinessBillId).collect(Collectors.toList());
            payableBillRepository.updateInferenceState(idList);
        }
    }

    /**
     * {@linkplain VoucherEventTypeEnum.账单开票}
     *
     * @param command
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getInvoiceReceiptBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions){

        QueryWrapper<?> receivableWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
        List<VoucherBusinessBill> result = new ArrayList<>(64);
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                receivableWrapper.eq("b.deleted", DataDisabledEnum.启用.getCode())
                        .eq("b.state", InvoiceReceiptStateEnum.开票成功.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.scene_id");
                WrapperUtils.logWrapper(command,receivableWrapper);
                List<VoucherBusinessBill> query = receivableBillRepository.listVoucherInvoiceReceiptBillByQuery(receivableWrapper,tableName);

                result.addAll(query);
            }

        }
        return result;
    }



    private HashMap<Long, Boolean> isReserveByCustomerTypeChange(Set<Long> billIdSet){
        List<BillAdjustE> list =billAdjustMapper.selectList(Wrappers.<BillAdjustE>lambdaQuery()
                .select(BillAdjustE::getId,BillAdjustE::getPayerType,BillAdjustE::getOriginalPayerType)
                .in(BillAdjustE::getBillId,billIdSet)
                .orderByAsc(BillAdjustE::getGmtCreate));

        Map<Long, List<BillAdjustE>> collect = list.stream().collect(Collectors.groupingBy(BillAdjustE::getBillId));
         final HashMap<Long, Boolean> map = new HashMap<>(collect.size());

         collect.forEach((billId,ll)->{
             boolean b = isReserveByCustomerTypeChange(ll, billId);
             map.put(billId,b);
         });

         return map;
    }

    private static boolean isReserveByCustomerTypeChange(List<BillAdjustE> list, Long billId) {
        if (CollUtil.isEmpty(list)){
            return false;
        }

        //开发商调整成业主
        int oldPay = ObjectUtil.isNull(list.get(0).getOriginalPayerType()) ? VoucherBillCustomerTypeEnum.业主.getCode() : list.get(0).getOriginalPayerType();
        Integer newPay = list.size() == 1 ? list.get(0).getPayerType() : getLastPayerType(list);
        if (ObjectUtil.isNotNull(newPay) && newPay ==0 && oldPay == VoucherBillCustomerTypeEnum.开发商.getCode() ){
            log.info(FinanceConstants.customer_type_change+"账单id:{},oldType-:{},newType-{},完整调整记录:{}",billId,oldPay,newPay, JSONObject.toJSONString(list));
            return true;
        }
        return false;
    }

    private static Integer getLastPayerType(List<BillAdjustE> list){
        Integer integer = null;
        for (int i = list.size()-1; i>=0;i-- ) {
            BillAdjustE e = list.get(i);
            if (ObjectUtil.isNotNull(e.getPayerType())){
                integer = e.getPayerType();
                break;
            }
        }
        return integer;
    }

    private boolean getChargeObjectChange(ManualVoucherStrategyCommand command,List<VoucherRuleConditionOBV> conditions){
        AtomicBoolean f = new AtomicBoolean(false);
        conditions.forEach(v->{
            if (v.getType().equals(VoucherRuleConditionTypeEnum.调整方式.getCode())){
                List<VoucherRuleConditionOptionOBV> list = v.getValues();
                for (VoucherRuleConditionOptionOBV obv : list) {
                    if (obv.getCode().equals(String.valueOf(BillAdjustWayEnum.CHARGE_OBJECT.getCode()))){
                        log.info(FinanceConstants.customer_type_change+"规则id-{},存在收费对象调整,条件:{}",
                                command.getVoucherRuleId(),JSONObject.toJSONString(obv));
                        f.set(true);
                    }
                }

            }
        });
        return f.get();
    }

    /**
     * {@linkplain VoucherEventTypeEnum.账单调整}
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getReceivableBillList(ManualVoucherStrategyCommand command,List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        // 检查条件是否存在，收费对象调整
        boolean change = getChargeObjectChange(command,conditions);
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.应收账单);
                advanceWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        // 添加分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq(change,"ba.original_payer_type",VoucherBillCustomerTypeEnum.开发商.getCode())
                        .eq(change,"ba.payer_type",VoucherBillCustomerTypeEnum.业主.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,advanceWrapper);
                List<VoucherBusinessBill> advanceVoucherBusinessBills = receivableBillRepository.listReceivableBillByQuery(advanceWrapper,change,tableName);
                if (CollectionUtils.isNotEmpty(advanceVoucherBusinessBills)) {
                    result.addAll(advanceVoucherBusinessBills);
                }

                QueryWrapper<?> gatherWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.预收账单);
                gatherWrapper.eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq(change,"ba.original_payer_type",VoucherBillCustomerTypeEnum.开发商.getCode())
                        .eq(change,"ba.payer_type",VoucherBillCustomerTypeEnum.业主.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,gatherWrapper);
                List<VoucherBusinessBill> gatherVoucherBusinessBills = advanceBillRepository.listAdvanceBillByQuery(gatherWrapper,change,tableName);
                if (CollectionUtils.isNotEmpty(gatherVoucherBusinessBills)) {
                    result.addAll(gatherVoucherBusinessBills);
                }
            }
        }

        return result;
    }


    /**
     * {@linkplain VoucherEventTypeEnum.作废} 做应收计提的反转
     * @return
     */
    public List<VoucherBusinessBill> getCancellationBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }


        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);

                // 应收账单，通过 vbd.scene_id = b.id
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
                wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .and(e->e.eq("b.state", BillStateEnum.作废.getCode())
                                .or().eq("b.refund_state",BillRefundStateEnum.已退款.getCode()))
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,wrapper);
                List<VoucherBusinessBill> gatherVoucherBusinessBills = receivableBillRepository.queryCancellationBillList(wrapper,tableName);
                if (CollectionUtils.isNotEmpty(gatherVoucherBusinessBills)) {
                    gatherVoucherBusinessBills.forEach(v->v.setAccountBookId(bookId));
                    result.addAll(gatherVoucherBusinessBills);
                }


                // 临时表，通过 vbd.scene_id = gd.id

                QueryWrapper<?> wrapperTmp = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
                wrapperTmp.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .and(e->e.eq("b.state", BillStateEnum.作废.getCode())
                                .or().eq("b.refund_state",BillRefundStateEnum.已退款.getCode()))
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapper(command,wrapperTmp);
                List<VoucherBusinessBill> sceneVersion = receivableBillMapper.queryCancellationSen(wrapperTmp,tableName);
                if (CollectionUtils.isNotEmpty(sceneVersion)) {
                    sceneVersion.forEach(v->v.setAccountBookId(bookId));
                    result.addAll(sceneVersion);
                }
            }
        }

        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.冲销}
     * @param eventType 要冲销的事件类型
     * @param chargeAgainstEventType 对应的冲销事件
     * @return
     */

    public List<VoucherBusinessBill> getChargeAgainstBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions, int eventType, int chargeAgainstEventType) {
        log.info("冲销事件类型：{}, 冲销事件：{}",eventType,chargeAgainstEventType);
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.应收账单);
                wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("gd.available", 1)
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gb.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapperPro(command,wrapper,"getChargeAgainstBillList"+BillTypeEnum.应收账单.getValue());
                List<VoucherBusinessBill> gatherVoucherBusinessBills = receivableBillMapper.chargeAgainstBillList(wrapper, tableName, eventType, chargeAgainstEventType);
                if (CollectionUtils.isNotEmpty(gatherVoucherBusinessBills)) {
                    gatherVoucherBusinessBills.forEach(v->v.setAccountBookId(bookId));
                    result.addAll(gatherVoucherBusinessBills);
                }

                QueryWrapper<?> advanceWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions, BillTypeEnum.预收账单);
                advanceWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("gd.available", 1)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.id");
                WrapperUtils.logWrapperPro(command,advanceWrapper,"getChargeAgainstBillList"+BillTypeEnum.预收账单.getValue());
                List<VoucherBusinessBill> advanceVoucherBusinessBills = advanceBillMapper.chargeAgainstBillList(advanceWrapper,tableName,eventType,chargeAgainstEventType);
                if (CollectionUtils.isNotEmpty(advanceVoucherBusinessBills)) {
                    advanceVoucherBusinessBills.forEach(v->v.setAccountBookId(bookId));
                    result.addAll(advanceVoucherBusinessBills);
                }

            }
        }

        return result;
    }


    /** {@linkplain VoucherEventTypeEnum.款项结转}
     * @param command
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> moneyCarriedForwardList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<VoucherBusinessBill> result = new ArrayList<>();
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)){
            return result;
        }
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        // communityId - accountBookIdSet
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }
        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                // 查询临时单
                QueryWrapper<?> tempWrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);
                tempWrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())

                        // 临时单
                        .eq("b.reversed", BillReverseStateEnum.未冲销.getCode())
                        .eq("b.bill_type", BillTypeEnum.临时收费账单.getCode())
                        .eq("br.state", CarryoverStateEnum.已生效.getCode())

                        .in("b.state", Arrays.asList(BillStateEnum.正常.getCode(),BillStateEnum.冻结.getCode()))

                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.scene_id");

                WrapperUtils.logWrapper(command,tempWrapper);

                List<VoucherBusinessBill> tempList = receivableBillMapper.moneyCarriedForwardList(tempWrapper,VoucherEventTypeEnum.款项结转.getCode(),tableName);

                result.addAll(tempList);
            }
        }


        return result;
    }

    public Collection<Long> getTempToAdvanceBill(Set<Long> tempSet){
        if (CollUtil.isEmpty(tempSet)) {

            return Collections.emptySet();
        }

        Map<Long, Long> map = carryoverDetailMapper.selectList(Wrappers.<BillCarryoverDetailE>lambdaQuery()
                .select(BillCarryoverDetailE::getCarriedBillId, BillCarryoverDetailE::getTargetBillId)
                .in(BillCarryoverDetailE::getCarriedBillId, tempSet)
                .orderByAsc(BillCarryoverDetailE::getId)
        ).stream().collect(
                Collectors.toMap(BillCarryoverDetailE::getCarriedBillId, BillCarryoverDetailE::getTargetBillId,
                        (oldVal, newVal) -> newVal));
        return map.values();


    }


    public void hitSet(String communityId, List<VoucherBusinessBill> tempList){
        if (CollUtil.isEmpty(tempList)){
            return;
        }

        // K:原账单id,v:目标账单id
        Map<Long, Long> map = carryoverDetailMapper.selectList(Wrappers.<BillCarryoverDetailE>lambdaQuery()
                .select(BillCarryoverDetailE::getCarriedBillId, BillCarryoverDetailE::getTargetBillId)
                .in(BillCarryoverDetailE::getCarriedBillId, tempList.stream().map(VoucherBusinessBill::getReceivableBillId).collect(Collectors.toSet()))
                .orderByAsc(BillCarryoverDetailE::getId)
        ).stream().collect(
                Collectors.toMap(BillCarryoverDetailE::getCarriedBillId, BillCarryoverDetailE::getTargetBillId,
                        (oldVal, newVal) -> newVal));

        if (CollUtil.isEmpty(map)){
            return;
        }

        // 目标账单是结转为 应收的
        Set<Long> hitSet = receivableBillMapper.moneyCarriedForwardSet(communityId, map.values());
        HashSet<Long> res = new HashSet<>(hitSet.size());
        map.forEach((k,v)->{
            if (hitSet.contains(v)){
                res.add(k);
            }
        });

        printLog(tempList,communityId,res);
        tempList.removeIf(v->res.contains(v.getBusinessBillId()));
    }

    private void printLog(List<VoucherBusinessBill> tempList,String communityId,HashSet<Long> res){
        Set<Long> list = new HashSet<>(8);
        for (VoucherBusinessBill bill : tempList) {
            if (res.contains(bill.getBusinessBillId())){
                list.add(bill.getReceivableBillId());
            }
        }
        log.info("特殊收款结算,communityId-{},A->B,B为应收账单:{}",communityId,list);
    }

    /**
     * {@linkplain VoucherEventTypeEnum.付款结算}
     *
     * @param command
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getPayBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions){
        QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions,BillTypeEnum.付款单);

        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)) {
            return new ArrayList<>();
        }
        List<VoucherBusinessBill> result = new ArrayList<>(64);

        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                wrapper.eq("b.deleted", DataDeletedEnum.NORMAL.getCode())
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("b.approved_state", BillApproveStateEnum.已审核.getCode())
                        .eq("b.verify_state", BillVerifyStateEnum.未核销.getCode())
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd.scene_id");
                WrapperUtils.logWrapper(command,wrapper);
                List<VoucherBusinessBill> payableVoucherBusinessBills = payBillRepository.listVoucherPayBillByQuery(wrapper,tableName);

                if (CollUtil.isNotEmpty(payableVoucherBusinessBills)){
                    result.addAll(payableVoucherBusinessBills);
                }
            }

        }

        return result;
    }

    /**
     * {@linkplain VoucherEventTypeEnum.预收应收核销}
     *
     * @param command
     * @param conditions
     * @return
     */
    public List<VoucherBusinessBill> getReversedBillList(ManualVoucherStrategyCommand command, List<VoucherRuleConditionOBV> conditions) {
        List<String> communityIds = getCommunityIds(conditions);
        if (CollUtil.isEmpty(communityIds)) {
           return new ArrayList<>();
        }

        List<VoucherBusinessBill> result = new ArrayList<>(64);
        Set<Long> accountBookIds = getAccountBookId(command.getRuleId());
        HashMap<String, Set<Long>> hashMap = new HashMap<>(1);
        if (CollUtil.isEmpty(accountBookIds)){
            hashMap = getBookIdByCommunityId(communityIds);
        }

        for (String communityId : communityIds) {
            if (CollUtil.isEmpty(accountBookIds)){
                accountBookIds = hashMap.get(communityId);
                if (CollUtil.isEmpty(accountBookIds)){
                    log.error("communityId-{},规则id:{}不存在账簿",communityId,command.getRuleId());
                    continue;
                }
            }
            for (Long bookId : accountBookIds) {
                QueryWrapper<?> wrapper = VoucherRuleConditionUtils.parseConditionToQuery(conditions);

                Long statutoryBodyId = accountBookMapper.getStatutoryBodyIdByBookId(bookId);
                if (ObjectUtil.isNull(statutoryBodyId)){
                    log.error("getStatutoryBodyIdByBookIdLimit1-找不到业务单元-bookId-{}",bookId);
                   continue;
                }
                String tableName = getVoucherBusinessDetailKey(bookId);
                // 审核状态未初始审核的待审核账单
                wrapper.ne("b.approved_state", BillApproveStateEnum.未通过.getCode())
                        //结算状态为：已结算，部分结算
                        .in("b.settle_state", Arrays.asList(BillSettleStateEnum.已结算.getCode(),BillSettleStateEnum.部分结算.getCode()))
                        // 不是初始化账单
                        .eq("b.is_init", 0)
                        // 账单状态正常
                        .eq("b.state", BillStateEnum.正常.getCode())
                        .eq("gd.available", 0)
                        // 归属月小于等于当前月
                        .le("b.account_date", DateTimeUtil.nowDate())
                        // 分表键
                        .eq("b.sup_cp_unit_id", communityId)
                        .eq("gb.sup_cp_unit_id", communityId)
                        .eq("gd.sup_cp_unit_id", communityId)
                        .eq("b.business_unit_id",statutoryBodyId)
                        .isNull("vbd2.id");
                WrapperUtils.logWrapper(command,wrapper);

                // 应收账单
                List<VoucherBusinessBill> advanceBillList = advanceBillRepository.listVoucherReversedBillByQuery(wrapper,tableName);
                if (CollUtil.isNotEmpty(advanceBillList)){
                    result.addAll(advanceBillList);
                }
                // 临时账单
                List<VoucherBusinessBill> tempList = advanceBillRepository.listVoucherReversedTempBillByQuery(wrapper,tableName);
                if (CollectionUtils.isNotEmpty(tempList)) {
                    result.addAll(tempList);
                }

            }

        }
        return result;
    }

    public boolean checkSourceSystem(List<VoucherRuleConditionOBV> conditions) {
        if (CollectionUtils.isEmpty(conditions)) {
            return false;
        }
        // 先过滤出来是 单据来源 查询条件的conditions
        List<VoucherRuleConditionOBV> sourceConditions = conditions.stream().filter(e -> Integer.valueOf(VoucherRuleConditionTypeEnum.单据来源.getCode()).equals(e.getType())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(sourceConditions)) {
            return false;
        }
        // 不考虑规则乱配置的情况，目前只拿第一个即可
        VoucherRuleConditionOBV sourceCondition = sourceConditions.get(0);
        long sourceChargeCount = 0L;
        switch (VoucherRuleConditionMethodEnum.valueOfByCode(sourceCondition.getMethod())){
            case 包含:
                // 包含条件，过滤出有收费系统来源的过滤值，说明是包含收费系统来源过滤条件，直接返回true
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.收费系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount > 0;
            case 不包含:
                // 不包含条件，没有收费系统来源的过滤值，说明包含收费系统来源过滤条件，直接返回true
                sourceChargeCount = sourceCondition.getValues().stream().filter(e -> SysSourceEnum.收费系统.getCode().equals(Integer.valueOf(e.getId()))).count();
                return sourceChargeCount <= 0;
            default:
                return false;
        }
    }

    /**可优化成toSet
     * 过滤空置房账单
     * @return
     */
    public List<VoucherBusinessBill> filterVacantHouseBill(List<VoucherBusinessBill> billList) {
        List<Long> ids = billList.stream().filter(v-> v.getSettleState() == BillSettleStateEnum.未结算.getCode())
                .map(VoucherBusinessBill::getRoomId).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList());
        if (CollectionUtils.isEmpty(ids)) {
            return billList;
        }
        List<Long> roomIds = new ArrayList<>();
        //每次处理1000条
        if (ids.size() <= 1000) {
            roomIds.addAll(spaceClient.getDetails(ids).stream().filter(a -> (a.getHouseStatus() != null && (int)a.getHouseStatus() == 1)).map(SpaceDetails::getId).collect(Collectors.toList()));
        } else {
            for (int i = 0; i < ids.size(); i += 1000) {
                if (ids.size() - i > 1000) {
                    roomIds.addAll(spaceClient.getDetails(ids.subList(i, i + 1000)).stream().filter(a -> (a.getHouseStatus() != null && (int)a.getHouseStatus() == 1)).map(SpaceDetails::getId).collect(Collectors.toList()));
                } else {
                    roomIds.addAll(spaceClient.getDetails(ids.subList(i, ids.size())).stream().filter(a -> (a.getHouseStatus() != null && (int)a.getHouseStatus() == 1)).map(SpaceDetails::getId).collect(Collectors.toList()));
                }
            }
        }
        Map<Long, Long> map = roomIds.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        billList.removeIf(voucherBusinessBill -> map.containsKey(voucherBusinessBill.getRoomId())); //过滤空置房
        return billList;
    }

    /**过滤掉是开发商且未结算的
     *
     * @return
     */
    public List<VoucherBusinessBill> filterByCustomerType(List<VoucherBusinessBill> billList) {
        if (CollectionUtils.isEmpty(billList)){
            return new ArrayList<>(1);
        }

        return billList.stream()
                .filter(Objects::nonNull)
                .filter(v -> v.getCustomerType() != null && v.getSettleState() != null)
                .filter(v ->
                        // 保留业主
                        v.getCustomerType() == 0 ||
                                // 保留开发商结算和部分结算
                                (v.getCustomerType() == 1 && v.getSettleState() != BillSettleStateEnum.未结算.getCode()))
                .collect(Collectors.toList());
    }

}
