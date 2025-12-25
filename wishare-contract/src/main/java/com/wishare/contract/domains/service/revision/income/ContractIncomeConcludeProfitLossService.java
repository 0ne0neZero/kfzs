package com.wishare.contract.domains.service.revision.income;

import cn.hutool.core.builder.GenericBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.wishare.contract.apps.fo.revision.ContractPlanDateF;
import com.wishare.contract.apps.fo.revision.FinanceBillF;
import com.wishare.contract.apps.fo.revision.income.*;
import com.wishare.contract.apps.remote.clients.ExternalFeignClient;
import com.wishare.contract.apps.remote.finance.facade.Finance4JZFacade;
import com.wishare.contract.apps.remote.fo.ContractBasePullF;
import com.wishare.contract.apps.remote.vo.ReceivableBillDetailRv;
import com.wishare.contract.apps.service.revision.common.ContractInfoToFxmCommonService;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeProfitLossE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomePlanConcludeE;
import com.wishare.contract.domains.enums.revision.SplitEnum;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeMapper;
import com.wishare.contract.domains.mapper.revision.income.ContractIncomeConcludeProfitLossMapper;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomeConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.income.ContractIncomePlanConcludeV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.starter.exception.BizException;
import com.wishare.starter.exception.SysException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 合同收入损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContractIncomeConcludeProfitLossService extends ServiceImpl<ContractIncomeConcludeProfitLossMapper, ContractIncomeConcludeProfitLossE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeProfitLossMapper contractIncomeConcludeProfitLossMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private Finance4JZFacade finance4JZFacade;

    private final TransactionTemplate transactionTemplate;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractInfoToFxmCommonService contractInfoToFxmCommonService;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractIncomeConcludeMapper contractIncomeConcludeMapper;


    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ExternalFeignClient externalFeignClient;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractIncomeConcludeProfitLossV> get(ContractIncomeConcludeProfitLossF conditions){
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getPayNotecode())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.PAY_NOTECODE, conditions.getPayNotecode());
        }

        if (StringUtils.isNotBlank(conditions.getCustomer())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CUSTOMER, conditions.getCustomer());
        }

        if (StringUtils.isNotBlank(conditions.getCustomerName())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CUSTOMER_NAME, conditions.getCustomerName());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CONTRACT_NO, conditions.getContractNo());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CONTRACT_NAME, conditions.getContractName());
        }

        if (Objects.nonNull(conditions.getTermDate())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TERM_DATE, conditions.getTermDate());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionTime())) {
            queryWrapper.gt(ContractIncomeConcludeProfitLossE.PLANNED_COLLECTION_TIME, conditions.getPlannedCollectionTime());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.PLANNED_COLLECTION_AMOUNT, conditions.getPlannedCollectionAmount());
        }

        if (Objects.nonNull(conditions.getSettlementAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.SETTLEMENT_AMOUNT, conditions.getSettlementAmount());
        }

        if (Objects.nonNull(conditions.getDeductionAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.DEDUCTION_AMOUNT, conditions.getDeductionAmount());
        }

        if (Objects.nonNull(conditions.getInvoiceApplyAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.INVOICE_APPLY_AMOUNT, conditions.getInvoiceApplyAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getNoReceiptAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.NO_RECEIPT_AMOUNT, conditions.getNoReceiptAmount());
        }

        if (Objects.nonNull(conditions.getPlanStatus())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.PLAN_STATUS, conditions.getPlanStatus());
        }

        if (Objects.nonNull(conditions.getPaymentStatus())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.PAYMENT_STATUS, conditions.getPaymentStatus());
        }

        if (Objects.nonNull(conditions.getInvoiceStatus())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.INVOICE_STATUS, conditions.getInvoiceStatus());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (StringUtils.isNotBlank(conditions.getDepartName())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.DEPART_NAME, conditions.getDepartName());
        }

        if (Objects.nonNull(conditions.getSplitMode())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.SPLIT_MODE, conditions.getSplitMode());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (Objects.nonNull(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TAX_RATE, conditions.getTaxRate());
        }

        if (Objects.nonNull(conditions.getNoTaxAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.NO_TAX_AMOUNT, conditions.getNoTaxAmount());
        }

        if (Objects.nonNull(conditions.getTaxAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TAX_AMOUNT, conditions.getTaxAmount());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getHowOrder())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.HOW_ORDER, conditions.getHowOrder());
        }

        if (Objects.nonNull(conditions.getRatioAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.RATIO_AMOUNT, conditions.getRatioAmount());
        }

        if (Objects.nonNull(conditions.getServiceType())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.SERVICE_TYPE, conditions.getServiceType());
        }

        if (Objects.nonNull(conditions.getNoPlanAmount())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.NO_PLAN_AMOUNT, conditions.getNoPlanAmount());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateIdPath())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TAX_RATE_ID_PATH, conditions.getTaxRateIdPath());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractIncomeConcludeProfitLossE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractIncomeConcludeProfitLossE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractIncomeConcludeProfitLossE.GMT_MODIFY, conditions.getGmtModify());
        }

        ContractIncomeConcludeProfitLossE contractIncomeConcludeProfitLossE = contractIncomeConcludeProfitLossMapper.selectOne(queryWrapper);
        if (contractIncomeConcludeProfitLossE != null) {
            return Optional.of(Global.mapperFacade.map(contractIncomeConcludeProfitLossE, ContractIncomeConcludeProfitLossV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param contractIncomeConcludeProfitLossListF 根据Id更新
    * @return 下拉列表
    */

    public List<ContractIncomePlanConcludeV> list(ContractIncomeConcludeProfitLossListF contractIncomeConcludeProfitLossListF){
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVS = contractIncomeConcludeProfitLossMapper.getByContractId(contractIncomeConcludeProfitLossListF.getContractId());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractIncomePlanConcludeV s : contractIncomePlanConcludeVS){
            ssettlementAmount = ssettlementAmount.add(s.getReceiptAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for (ContractIncomePlanConcludeV s : contractIncomePlanConcludeVS) {
            s.setSsettlementAmount(ssettlementAmount);
            s.setSreceiptAmount(sreceiptAmount);
            try {
                if (StringUtils.isNotEmpty(s.getChargeItemId())) {
                    s.setChargeItemIdList(JSONObject.parseArray(s.getChargeItemId(), Long.class));
                }
            } catch (Exception e) {
                continue;
            }
        }
        return contractIncomePlanConcludeVS;
    }

    public BigDecimal getByContractList(String contractId) {
        LambdaQueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractIncomeConcludeProfitLossE::getContractId, contractId)
                .eq(ContractIncomeConcludeProfitLossE::getReviewStatus,2)
                .eq(ContractIncomeConcludeProfitLossE::getDeleted,0);
        List<ContractIncomeConcludeProfitLossE> contractPayPlanConcludeEList = list(queryWrapper);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(contractPayPlanConcludeEList)){
            for(ContractIncomeConcludeProfitLossE s : contractPayPlanConcludeEList){
                bigDecimal = bigDecimal.add(s.getPlannedCollectionAmount());
            }
        }
        return bigDecimal;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Boolean save(List<ContractIncomePlanAddF> addF){
        if (addF == null) {
            throw SysException.throw403("收款计划信息不能为空");
        }
        //费项id处理
        try {
            for (int i = 0; i < addF.size(); i++) {
                if (CollectionUtils.isNotEmpty(addF.get(0).getChargeItemIdList())) {
                    addF.get(i).setChargeItemId(JSON.toJSONString(addF.get(i).getChargeItemIdList()));
                }
            }
        } catch (Exception e) {
            log.info("费项id数组转换失败");
        }

        String saveType = addF.get(0).getSaveType();
        List<ContractIncomeConcludeProfitLossE> incomePlanConcludeES = Global.mapperFacade.mapAsList(addF, ContractIncomeConcludeProfitLossE.class);
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomeConcludeProfitLossMapper.getByContractId(addF.get(0).getContractId());
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            //删除财务中台数据
            List<Long> billIds = contractIncomePlanConcludeVList.stream().map(ContractIncomePlanConcludeV::getBillId).collect(Collectors.toList());
            finance4JZFacade.dealDelFinanceIncome(billIds);

            //删除历史记录
            for(ContractIncomePlanConcludeV s : contractIncomePlanConcludeVList){
                contractIncomeConcludeProfitLossMapper.deleteById(s.getId());
            }
        }

        for(ContractIncomeConcludeProfitLossE incomePlanConclude : incomePlanConcludeES){
            log.info("新增时转换的入库对象: {}", JSONObject.toJSONString(incomePlanConclude));
            incomePlanConclude.setPayNotecode(contractCode()) // 付款计划编号
                    .setTenantId(tenantId());    // 租户id
            if (Objects.equals("1", saveType)) {
                incomePlanConclude.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                incomePlanConclude.setReviewStatus(2);
            }
            incomePlanConclude.setReceiptAmount(BigDecimal.ZERO);
            incomePlanConclude.setNoReceiptAmount(incomePlanConclude.getPlannedCollectionAmount());
            incomePlanConclude.setGmtCreate(LocalDateTime.now());
            incomePlanConclude.setGmtModify(LocalDateTime.now());

            contractIncomeConcludeProfitLossMapper.insert(incomePlanConclude);

            ContractIncomeConcludeE contractIncomeConcludeE = contractIncomeConcludeMapper.selectById(incomePlanConclude.getContractId());

            // 损益管理收入计划同步枫行梦
            if(contractIncomeConcludeE.getSealType() != null && contractIncomeConcludeE.getSealType() == 1){
                FinanceBillF financeBillF = contractInfoToFxmCommonService.financeBillInfoToFxm(incomePlanConclude, 1, contractIncomeConcludeE);
                String requestBody = JSON.toJSONString(financeBillF);
                if (StringUtils.isNotEmpty(requestBody)) {
                    ContractBasePullF contractBasePullF = new ContractBasePullF();
                    contractBasePullF.setRequestBody(requestBody);
                    contractBasePullF.setType(0);
                    Boolean isSuccess = externalFeignClient.contractFinanceBill(contractBasePullF);
                    log.info("损益管理收入计划同步到枫行梦是否成功" + isSuccess);
                }
            }

        }

//        //财务中台账单数据推送
        List<ReceivableBillDetailRv> pullDataList = finance4JZFacade.dealAddFinanceIncome(incomePlanConcludeES);

        //更新billId账单id
        if(CollectionUtils.isNotEmpty(pullDataList)) {
            for (int i = 0; i < pullDataList.size(); i++) {
                ContractIncomeConcludeProfitLossE map = contractIncomeConcludeProfitLossMapper.selectById(incomePlanConcludeES.get(i).getId());
                if(null != map) {
                    map.setBillId(pullDataList.get(i).getId());
                    map.setBillNo(pullDataList.get(i).getBillNo());
                }
                contractIncomeConcludeProfitLossMapper.updateById(map);
            }
        }
//

        return Boolean.TRUE;
    }


   /**
    * 根据Id更新
    *
    * @param contractPayConcludeF 根据Id更新
    */
   public void update(List<ContractIncomePlanConcludeUpdateF> contractPayConcludeF) {
       if (contractPayConcludeF == null) {
           throw SysException.throw403("收款计划信息不能为空");
       }
       String saveType = contractPayConcludeF.get(0).getSaveType();
       List<ContractIncomeConcludeProfitLossE> mapAsList = Global.mapperFacade.mapAsList(contractPayConcludeF, ContractIncomeConcludeProfitLossE.class);
       for (ContractIncomeConcludeProfitLossE map : mapAsList) {
           if (Objects.equals("1", saveType)) {
               map.setReviewStatus(0);
           }
           if (Objects.equals("2", saveType)) {
               map.setReviewStatus(2);
           }
           map.setNoReceiptAmount(map.getPlannedCollectionAmount());
           contractIncomeConcludeProfitLossMapper.updateById(map);
       }
   }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractIncomeConcludeProfitLossMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param form 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomePlanConcludeV> page(PageF<SearchF<ContractIncomePlanConcludePageF>> form) {
        Page<ContractIncomePlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractIncomePlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        return transactionTemplate.execute(status -> {
            try {
                IPage<ContractIncomePlanConcludeV> pageList
                        = contractIncomeConcludeProfitLossMapper.collectionPlanDetailPage(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));

                try {
                    if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
                        pageList.getRecords().forEach(record -> {
                            if (StringUtils.isNotEmpty(record.getChargeItemId())) {
                                record.setChargeItemIdList(JSONObject.parseArray(record.getChargeItemId(), Long.class));
                            }
                        });
                    }
                } catch (Exception e) {

                }
                return PageV.of(form, pageList.getTotal(), pageList.getRecords());
            } catch (Exception ex) {
                status.setRollbackOnly();
                log.error("收入合同请求分页失败,已回滚，失败原因:{}", ex.getMessage());
                throw SysException.throw403("收入合同请求分页失败,已回滚");
            }
        });
    }


    private QueryWrapper<ContractIncomePlanConcludePageF> conditionPage(QueryWrapper<ContractIncomePlanConcludePageF> queryModel, String tenantId) {
        queryModel.eq("ccp.deleted", 0);
        queryModel.eq("ccp.tenantId", tenantId);
        return queryModel;
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<ContractIncomeConcludeProfitLossV> frontPage(PageF<SearchF<ContractIncomeConcludeProfitLossE>> request) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractIncomeConcludeProfitLossE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractIncomeConcludeProfitLossE> page = contractIncomeConcludeProfitLossMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractIncomeConcludeProfitLossV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractIncomeConcludeProfitLossE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeProfitLossE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractIncomeConcludeProfitLossE selectOneBy(Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>> consumer,String... fields) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeProfitLossMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeProfitLossE中id字段的值, select 指定字段
    *
    * @param fields ContractIncomeConcludeProfitLossE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractIncomeConcludeProfitLossMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>> consumer) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractIncomeConcludeProfitLossMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractIncomeConcludeProfitLossE中仅包含a字段的值
     *
     * @param fields ContractIncomeConcludeProfitLossE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractIncomeConcludeProfitLossE> selectListBy(Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>> consumer,String... fields) {
         QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractIncomeConcludeProfitLossMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractIncomeConcludeProfitLossE中仅包含a字段的值
    *
    * @param fields ContractIncomeConcludeProfitLossE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractIncomeConcludeProfitLossE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>> consumer, String... fields) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractIncomeConcludeProfitLossMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractIncomeConcludeProfitLossE中id字段的值, select 指定字段
     *
     * @param fields ContractIncomeConcludeProfitLossE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractIncomeConcludeProfitLossE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractIncomeConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractIncomeConcludeProfitLossE> page = Page.of(pageNum, pageSize, count);
        Page<ContractIncomeConcludeProfitLossE> queryPage = contractIncomeConcludeProfitLossMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {
        LocalDate planTime = LocalDate.now();
        List<ContractIncomePlanConcludeV> contractIncomePlanConcludeVList = contractIncomeConcludeProfitLossMapper.getByContractId(planDateF.getContractId());
        Integer termDate = 1;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            termDate = contractIncomePlanConcludeVList.stream().max(Comparator.comparing(ContractIncomePlanConcludeV :: getTermDate)).get().getTermDate() + 1;
            contractIncomePlanConcludeVList.sort((t1,t2) -> t2.getPlannedCollectionTime().compareTo(t1.getPlannedCollectionTime()));
            planTime = contractIncomePlanConcludeVList.get(0).getPlannedCollectionTime();
        }else{
            planTime = planDateF.getContractStartTime();
        }
        ArrayList<ContractPlanDateV> planDateList = Lists.newArrayList();

        switch (planDateF.getSplitMode()) {
            case 1: // 一次性收款,只有一条,前面已经塞进去了,这里不做处理
                break;
            case 2: // 按年收款：根据开始结束日期相减和1年比较
                // 每次加1年即12个月
                getSplitWay(planTime, SplitEnum.year.getWay(), planDateF, planDateList, termDate);
                break;
            case 3: // 按半年收款
                // 每次加半年即6个月
                getSplitWay(planTime, SplitEnum.halfYear.getWay(), planDateF, planDateList,termDate);
                break;
            case 4: // 按季度收款
                // 每次加1/4年即3个月
                getSplitWay(planTime, SplitEnum.quarter.getWay(), planDateF, planDateList,termDate);
                break;
            case 5: // 按月收款,一次加1个月
                getSplitWay(planTime, SplitEnum.month.getWay(), planDateF, planDateList,termDate);
                break;
            default:
                throw BizException.throw403("拆分方式错误,请检查拆分方式是否正确");
        }
        return planDateList;
    }

    /**
     * 拆分逻辑
     *
     * @param planTime     计划时间
     * @param monthsToAdd  拆分方式
     * @param planDateF    拆分依据
     * @param planDateList 拆分数据集合
     */
    private void getSplitWay(LocalDate planTime, int monthsToAdd, ContractPlanDateF planDateF, ArrayList<ContractPlanDateV> planDateList, Integer termDate) {

        // 先把第一条塞进去
        ContractPlanDateV first = GenericBuilder.of(ContractPlanDateV::new)
                .with(ContractPlanDateV::setTermDate, termDate)
                .with(ContractPlanDateV::setPlannedCollectionTime, planTime)
                .build();
        if(SplitEnum.once.getWay().equals(monthsToAdd)){
            first.setRatioAmount(new BigDecimal(100).setScale(2));
            first.setPlannedCollectionAmount(planDateF.getPlanAllAmount().setScale(2));
            planDateList.add(first);
            return;
        }
        planDateList.add(first);
        int j = 1;
        // 从2开始，是因为已经有第一期了，999是因为不可能有超过999年的合同
        for (int i = 2; i < 999; i++) {
            // 按拆分方式加固定年数
            planTime = planTime.plusMonths(monthsToAdd);
            // 计划时间小于等于，都可以继续拆分
            if (planTime.isBefore(planDateF.getContractEndTime()) || planTime.equals(planDateF.getContractEndTime())) {
                ContractPlanDateV planDateV = GenericBuilder.of(ContractPlanDateV::new)
                        .with(ContractPlanDateV::setTermDate, i)
                        .with(ContractPlanDateV::setPlannedCollectionTime, planTime)
                        .build();
                planDateList.add(planDateV);
                j += 1;
            } else {
                // 超出了，就不拆分
                break;
            }
        }
        BigDecimal splitPlanAmont = planDateF.getPlanAllAmount().divide(new BigDecimal(j),2, RoundingMode.HALF_UP);
        BigDecimal splitRatioAmount =  new BigDecimal(100).setScale(2).divide(new BigDecimal(j),2, RoundingMode.HALF_UP);
        for(int c = 0; c < j; c ++){
            ContractPlanDateV contractPlanDateV = planDateList.get(c);
            contractPlanDateV.setPlannedCollectionAmount(splitPlanAmont);
            contractPlanDateV.setRatioAmount(splitRatioAmount);
            planDateList.set(c,contractPlanDateV);
        }
    }

    private String contractCode() {
        //生成合同编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;子合同编号规则：主合同编号+两位数数值，如YYHT220816000101
        String year = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE).format(new Date());
        return "SKJH" + year;
    }

    public void sumbitId(String id) {
        ContractIncomeConcludeProfitLossE map = contractIncomeConcludeProfitLossMapper.selectById(id);
        map.setReviewStatus(2);
        contractIncomeConcludeProfitLossMapper.updateById(map);
    }
}
