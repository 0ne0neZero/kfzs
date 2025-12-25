package com.wishare.contract.domains.service.revision.pay;

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
import com.wishare.contract.apps.fo.revision.pay.*;
import com.wishare.contract.apps.remote.finance.facade.Finance4JZFacade;
import com.wishare.contract.apps.remote.vo.ReceivableBillDetailRv;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeProfitLossE;
import com.wishare.contract.domains.enums.revision.SplitEnum;
import com.wishare.contract.domains.mapper.revision.pay.ContractPayConcludeProfitLossMapper;
import com.wishare.contract.domains.vo.ContractPlanDateV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayConcludeProfitLossV;
import com.wishare.contract.domains.vo.revision.pay.ContractPayPlanConcludeV;
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
import org.springframework.context.annotation.Lazy;
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
 * 合同成本损益表
 * </p>
 *
 * @author chenglong
 * @since 2023-10-26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ContractPayConcludeProfitLossService extends ServiceImpl<ContractPayConcludeProfitLossMapper, ContractPayConcludeProfitLossE>  implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ContractPayConcludeProfitLossMapper contractPayConcludeProfitLossMapper;

    private final TransactionTemplate transactionTemplate;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    @Lazy
    private Finance4JZFacade finance4JZFacade;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<ContractPayConcludeProfitLossV> get(ContractPayConcludeProfitLossF conditions){
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractNo())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CONTRACT_NO, conditions.getContractNo());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getMerchant())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.MERCHANT, conditions.getMerchant());
        }

        if (StringUtils.isNotBlank(conditions.getMerchantName())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.MERCHANT_NAME, conditions.getMerchantName());
        }

        if (StringUtils.isNotBlank(conditions.getPayNotecode())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.PAY_NOTECODE, conditions.getPayNotecode());
        }

        if (Objects.nonNull(conditions.getTermDate())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.TERM_DATE, conditions.getTermDate());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionTime())) {
            queryWrapper.gt(ContractPayConcludeProfitLossE.PLANNED_COLLECTION_TIME, conditions.getPlannedCollectionTime());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.PLANNED_COLLECTION_AMOUNT, conditions.getPlannedCollectionAmount());
        }

        if (Objects.nonNull(conditions.getSettlementAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.SETTLEMENT_AMOUNT, conditions.getSettlementAmount());
        }

        if (Objects.nonNull(conditions.getDeductionAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.DEDUCTION_AMOUNT, conditions.getDeductionAmount());
        }

        if (Objects.nonNull(conditions.getInvoiceApplyAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.INVOICE_APPLY_AMOUNT, conditions.getInvoiceApplyAmount());
        }

        if (Objects.nonNull(conditions.getPaymentAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.PAYMENT_AMOUNT, conditions.getPaymentAmount());
        }

        if (Objects.nonNull(conditions.getPlanStatus())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.PLAN_STATUS, conditions.getPlanStatus());
        }

        if (Objects.nonNull(conditions.getPaymentStatus())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.PAYMENT_STATUS, conditions.getPaymentStatus());
        }

        if (Objects.nonNull(conditions.getInvoiceStatus())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.INVOICE_STATUS, conditions.getInvoiceStatus());
        }

        if (Objects.nonNull(conditions.getReviewStatus())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.REVIEW_STATUS, conditions.getReviewStatus());
        }

        if (Objects.nonNull(conditions.getSplitMode())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.SPLIT_MODE, conditions.getSplitMode());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItem())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CHARGE_ITEM, conditions.getChargeItem());
        }

        if (StringUtils.isNotBlank(conditions.getChargeItemId())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CHARGE_ITEM_ID, conditions.getChargeItemId());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRate())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.TAX_RATE, conditions.getTaxRate());
        }

        if (StringUtils.isNotBlank(conditions.getTaxRateId())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.TAX_RATE_ID, conditions.getTaxRateId());
        }

        if (Objects.nonNull(conditions.getNoTaxAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.NO_TAX_AMOUNT, conditions.getNoTaxAmount());
        }

        if (Objects.nonNull(conditions.getTaxAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.TAX_AMOUNT, conditions.getTaxAmount());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getNoPayAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.NO_PAY_AMOUNT, conditions.getNoPayAmount());
        }

        if (Objects.nonNull(conditions.getRatioAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.RATIO_AMOUNT, conditions.getRatioAmount());
        }

        if (Objects.nonNull(conditions.getHowOrder())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.HOW_ORDER, conditions.getHowOrder());
        }

        if (Objects.nonNull(conditions.getServiceType())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.SERVICE_TYPE, conditions.getServiceType());
        }

        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(ContractPayConcludeProfitLossE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(ContractPayConcludeProfitLossE.GMT_MODIFY, conditions.getGmtModify());
        }


        if (Objects.nonNull(conditions.getAcceptStatus())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.ACCEPT_STATUS, conditions.getAcceptStatus());
        }

        if (Objects.nonNull(conditions.getAcceptAmount())) {
            queryWrapper.eq(ContractPayConcludeProfitLossE.ACCEPT_AMOUNT, conditions.getAcceptAmount());
        }
        ContractPayConcludeProfitLossE contractPayConcludeProfitLossE = contractPayConcludeProfitLossMapper.selectOne(queryWrapper);
        if (contractPayConcludeProfitLossE != null) {
            return Optional.of(Global.mapperFacade.map(contractPayConcludeProfitLossE, ContractPayConcludeProfitLossV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param contractPayConcludeProfitLossListF 根据Id更新
    * @return 下拉列表
    */

    public List<ContractPayPlanConcludeV>  list(ContractPayConcludeProfitLossListF contractPayConcludeProfitLossListF){
        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList =  contractPayConcludeProfitLossMapper.getByContractId(contractPayConcludeProfitLossListF.getContractId());
        BigDecimal ssettlementAmount = BigDecimal.ZERO;
        BigDecimal sreceiptAmount = BigDecimal.ZERO;
        for(ContractPayPlanConcludeV s : contractPayPlanConcludeVList){
            ssettlementAmount = ssettlementAmount.add(s.getPaymentAmount());
            sreceiptAmount = sreceiptAmount.add(s.getSettlementAmount());
        }
        for(ContractPayPlanConcludeV s : contractPayPlanConcludeVList){
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
        return contractPayPlanConcludeVList;
    }

    @Transactional(rollbackFor = {Exception.class})
    public Boolean save(List<ContractPayPlanAddF> addF){
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
        List<ContractPayConcludeProfitLossE> payPlanConcludeES = Global.mapperFacade.mapAsList(addF, ContractPayConcludeProfitLossE.class);
        List<ContractPayPlanConcludeV> contractPayPlanConcludeVList = contractPayConcludeProfitLossMapper.getByContractId(addF.get(0).getContractId());
        if(ObjectUtils.isNotEmpty(contractPayPlanConcludeVList)){

            //删除财务中台数据
            List<Long> billIds = contractPayPlanConcludeVList.stream().map(ContractPayPlanConcludeV::getBillId).collect(Collectors.toList());
            finance4JZFacade.dealDelFinancePay(billIds);

            //删除历史记录
            for(ContractPayPlanConcludeV s : contractPayPlanConcludeVList){
                contractPayConcludeProfitLossMapper.deleteById(s.getId());
            }
        }
        for(ContractPayConcludeProfitLossE payPlanConclude : payPlanConcludeES){
            log.info("新增时转换的入库对象: {}", JSONObject.toJSONString(payPlanConclude));
            payPlanConclude.setPayNotecode(contractCode()) // 付款计划编号
                    .setTenantId(tenantId());    // 租户id
            if (Objects.equals("1", saveType)) {
                payPlanConclude.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                payPlanConclude.setReviewStatus(2);
            }
            payPlanConclude.setNoPayAmount(payPlanConclude.getSettlementAmount());
            payPlanConclude.setGmtCreate(LocalDateTime.now());
            payPlanConclude.setGmtModify(LocalDateTime.now());

            contractPayConcludeProfitLossMapper.insert(payPlanConclude);
        }


//        //财务中台账单数据推送
        List<ReceivableBillDetailRv> pullDataList = finance4JZFacade.dealAddFinancePay(payPlanConcludeES);

        //更新billId账单id
        if(CollectionUtils.isNotEmpty(pullDataList)) {
            for (int i = 0; i < pullDataList.size(); i++) {
                ContractPayConcludeProfitLossE map = contractPayConcludeProfitLossMapper.selectById(payPlanConcludeES.get(i).getId());
                if(null != map) {
                    map.setBillId(pullDataList.get(i).getId());
                    map.setBillNo(pullDataList.get(i).getBillNo());
                }
                contractPayConcludeProfitLossMapper.updateById(map);
            }
        }
//
        return Boolean.TRUE;
    }

    public BigDecimal getByContractList(String contractId) {
        LambdaQueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ContractPayConcludeProfitLossE::getContractId, contractId)
                .eq(ContractPayConcludeProfitLossE::getReviewStatus,2)
                .eq(ContractPayConcludeProfitLossE::getDeleted,0);
        List<ContractPayConcludeProfitLossE> contractPayPlanConcludeEList = list(queryWrapper);
        BigDecimal bigDecimal = BigDecimal.ZERO;
        if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(contractPayPlanConcludeEList)){
            for(ContractPayConcludeProfitLossE s : contractPayPlanConcludeEList){
                bigDecimal = bigDecimal.add(s.getPlannedCollectionAmount());
            }
        }
        return bigDecimal;
    }


   /**
    * 根据Id更新
    *
    * @param contractPayConcludeF 根据Id更新
    */
    public void update(List<ContractPayPlanConcludeUpdateF> contractPayConcludeF){
        if (contractPayConcludeF == null) {
            throw SysException.throw403("付款计划信息不能为空");
        }
        String saveType = contractPayConcludeF.get(0).getSaveType();
        List<ContractPayConcludeProfitLossE> maps = Global.mapperFacade.mapAsList(contractPayConcludeF, ContractPayConcludeProfitLossE.class);
        for(ContractPayConcludeProfitLossE map : maps){
            if (Objects.equals("1", saveType)) {
                map.setReviewStatus(0);
            }
            if (Objects.equals("2", saveType)) {
                map.setReviewStatus(2);
            }
            contractPayConcludeProfitLossMapper.updateById(map);
        }
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        contractPayConcludeProfitLossMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param form 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<ContractPayPlanConcludeV> page(PageF<SearchF<ContractPayPlanConcludePageF>> form) {
        Page<ContractPayPlanConcludePageF> pageF = Page.of(form.getPageNum(), form.getPageSize(), form.isCount());
        //-- 合同详情页面调用接口时需要替换相关参数，不影响其他地方调用
        for (Field field : form.getConditions().getFields()) {
            if ("detailTableId".equals(field.getName())) {
                field.setName("cc.id");
            }
        }
        QueryWrapper<ContractPayPlanConcludePageF> queryModel = form.getConditions().getQueryModel();
        if (getIdentityInfo().isEmpty()) {
            throw BizException.throw404("无法获取当前用户身份信息");
        }
        return transactionTemplate.execute(status -> {
            try {
                IPage<ContractPayPlanConcludeV> pageList
                        = contractPayConcludeProfitLossMapper.collectionPlanDetailPage(pageF, conditionPage(queryModel, getIdentityInfo().get().getTenantId()));

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
                log.error("支出合同请求分页失败,已回滚，失败原因:{}", ex.getMessage());
                throw SysException.throw403("支出合同请求分页失败,已回滚");
            }
        });

    }

    private QueryWrapper<ContractPayPlanConcludePageF> conditionPage(QueryWrapper<ContractPayPlanConcludePageF> queryModel, String tenantId) {
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
    public PageV<ContractPayConcludeProfitLossV> frontPage(PageF<SearchF<ContractPayConcludeProfitLossE>> request) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        SearchF<ContractPayConcludeProfitLossE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();
        Page<ContractPayConcludeProfitLossE> page = contractPayConcludeProfitLossMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), ContractPayConcludeProfitLossV.class));
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的ContractPayConcludeProfitLossE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeProfitLossE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public ContractPayConcludeProfitLossE selectOneBy(Consumer<QueryWrapper<ContractPayConcludeProfitLossE>> consumer,String... fields) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeProfitLossMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeProfitLossE中id字段的值, select 指定字段
    *
    * @param fields ContractPayConcludeProfitLossE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<ContractPayConcludeProfitLossE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(contractPayConcludeProfitLossMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<ContractPayConcludeProfitLossE>> consumer) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        contractPayConcludeProfitLossMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的ContractPayConcludeProfitLossE中仅包含a字段的值
     *
     * @param fields ContractPayConcludeProfitLossE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<ContractPayConcludeProfitLossE> selectListBy(Consumer<QueryWrapper<ContractPayConcludeProfitLossE>> consumer,String... fields) {
         QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return contractPayConcludeProfitLossMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的ContractPayConcludeProfitLossE中仅包含a字段的值
    *
    * @param fields ContractPayConcludeProfitLossE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<ContractPayConcludeProfitLossE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<ContractPayConcludeProfitLossE>> consumer, String... fields) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return contractPayConcludeProfitLossMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的ContractPayConcludeProfitLossE中id字段的值, select 指定字段
     *
     * @param fields ContractPayConcludeProfitLossE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<ContractPayConcludeProfitLossE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<ContractPayConcludeProfitLossE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<ContractPayConcludeProfitLossE> page = Page.of(pageNum, pageSize, count);
        Page<ContractPayConcludeProfitLossE> queryPage = contractPayConcludeProfitLossMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


    public List<ContractPlanDateV> calculate(ContractPlanDateF planDateF) {
        LocalDate planTime = LocalDate.now();
        List<ContractPayPlanConcludeV> contractIncomePlanConcludeVList = contractPayConcludeProfitLossMapper.getByContractId(planDateF.getContractId());
        Integer termDate = 1;
        if(ObjectUtils.isNotEmpty(contractIncomePlanConcludeVList)){
            termDate = contractIncomePlanConcludeVList.stream().max(Comparator.comparing(ContractPayPlanConcludeV :: getTermDate)).get().getTermDate() + 1;
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
        ContractPayConcludeProfitLossE map = contractPayConcludeProfitLossMapper.selectById(id);
        map.setReviewStatus(2);
        contractPayConcludeProfitLossMapper.updateById(map);

    }
}
