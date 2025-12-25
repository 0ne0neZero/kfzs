package com.wishare.contract.domains.service.revision.bond;

import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.enums.revision.bond.BondStatusEnum;
import com.wishare.contract.domains.enums.revision.bond.BondTypeEnum;
import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.mapper.revision.bond.RevisionBondCollectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectV;
import com.wishare.contract.domains.vo.revision.bond.RevisionBondCollectListV;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectPageF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectSaveF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectUpdateF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectListF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
/**
 * <p>
 * 保证金改版-收取类保证金
 * </p>
 *
 * @author chenglong
 * @since 2023-07-26
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class RevisionBondCollectService extends ServiceImpl<RevisionBondCollectMapper, RevisionBondCollectE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionBondCollectMapper revisionBondCollectMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private CollectBondRelationBillService bondRelationBillService;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<RevisionBondCollectV> get(RevisionBondCollectF conditions){
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(RevisionBondCollectE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondCollectE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getSupplierId())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER_ID, conditions.getSupplierId());
        }

        if (StringUtils.isNotBlank(conditions.getSupplier())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER, conditions.getSupplier());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondCollectE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionDate())) {
            queryWrapper.gt(RevisionBondCollectE.PLANNED_COLLECTION_DATE, conditions.getPlannedCollectionDate());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.PLANNED_COLLECTION_AMOUNT, conditions.getPlannedCollectionAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondCollectE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondCollectE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondCollectE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getRefundAmount())) {
            queryWrapper.eq(RevisionBondCollectE.REFUND_AMOUNT, conditions.getRefundAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondCollectE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondCollectE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }

        if (Objects.nonNull(conditions.getDeductionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.DEDUCTION_AMOUNT, conditions.getDeductionAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondCollectE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_ACCOUNT, conditions.getBankAccount());
        }

        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId());

        RevisionBondCollectE revisionBondCollectE = revisionBondCollectMapper.selectOne(queryWrapper);
        if (revisionBondCollectE != null) {
            return Optional.of(Global.mapperFacade.map(revisionBondCollectE, RevisionBondCollectV.class));
        }else {
            return Optional.empty();
        }
    }

   /**
    * 列表接口，一般用于下拉列表
    *
    * @param conditions 根据Id更新
    * @return 下拉列表
    */
    @Nonnull
    public RevisionBondCollectListV list(RevisionBondCollectListF conditions){
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondCollectE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getSupplierId())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER_ID, conditions.getSupplierId());
        }

        if (StringUtils.isNotBlank(conditions.getSupplier())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER, conditions.getSupplier());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondCollectE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionDate())) {
            queryWrapper.gt(RevisionBondCollectE.PLANNED_COLLECTION_DATE, conditions.getPlannedCollectionDate());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.PLANNED_COLLECTION_AMOUNT, conditions.getPlannedCollectionAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondCollectE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondCollectE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondCollectE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getRefundAmount())) {
            queryWrapper.eq(RevisionBondCollectE.REFUND_AMOUNT, conditions.getRefundAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondCollectE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondCollectE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }

        if (Objects.nonNull(conditions.getDeductionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.DEDUCTION_AMOUNT, conditions.getDeductionAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondCollectE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_ACCOUNT, conditions.getBankAccount());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(RevisionBondCollectE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(RevisionBondCollectE.ID);
        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId());
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<RevisionBondCollectV> retVList = Global.mapperFacade.mapAsList(revisionBondCollectMapper.selectList(queryWrapper),RevisionBondCollectV.class);
        RevisionBondCollectListV retV = new RevisionBondCollectListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(RevisionBondCollectSaveF revisionBondCollectF){
        RevisionBondCollectE map = Global.mapperFacade.map(revisionBondCollectF, RevisionBondCollectE.class);
        revisionBondCollectMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param revisionBondCollectF 根据Id更新
    */
    public void update(RevisionBondCollectUpdateF revisionBondCollectF){
        if (revisionBondCollectF.getId() == null) {
            throw new IllegalArgumentException();
        }
        RevisionBondCollectE map = Global.mapperFacade.map(revisionBondCollectF, RevisionBondCollectE.class);
        revisionBondCollectMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){

        RevisionBondCollectE bondE = getById(id);

        if (Objects.isNull(bondE) || !BondStatusEnum.待提交.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("检索数据失败或数据状态不正确，不可被删除");
        }

        revisionBondCollectMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<RevisionBondCollectV> page(PageF<RevisionBondCollectPageF> request) {
        RevisionBondCollectPageF conditions = request.getConditions();
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondCollectE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondCollectE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getSupplierId())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER_ID, conditions.getSupplierId());
        }

        if (StringUtils.isNotBlank(conditions.getSupplier())) {
            queryWrapper.eq(RevisionBondCollectE.SUPPLIER, conditions.getSupplier());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondCollectE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondCollectE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondCollectE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondCollectE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionDate())) {
            queryWrapper.gt(RevisionBondCollectE.PLANNED_COLLECTION_DATE, conditions.getPlannedCollectionDate());
        }

        if (Objects.nonNull(conditions.getPlannedCollectionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.PLANNED_COLLECTION_AMOUNT, conditions.getPlannedCollectionAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondCollectE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondCollectE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondCollectE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondCollectE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondCollectE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getRefundAmount())) {
            queryWrapper.eq(RevisionBondCollectE.REFUND_AMOUNT, conditions.getRefundAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondCollectE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondCollectE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }

        if (Objects.nonNull(conditions.getDeductionAmount())) {
            queryWrapper.eq(RevisionBondCollectE.DEDUCTION_AMOUNT, conditions.getDeductionAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondCollectE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondCollectE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondCollectE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondCollectE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondCollectE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondCollectE.BANK_ACCOUNT, conditions.getBankAccount());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId());

        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(RevisionBondCollectE.GMT_CREATE);
        }
        Page<RevisionBondCollectE> page = revisionBondCollectMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), RevisionBondCollectV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<BondCollectDetailV> frontPage(PageF<SearchF<RevisionBondCollectE>> request) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        SearchF<RevisionBondCollectE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId())
                .orderByAsc(RevisionBondCollectE.STATUS)
                .orderByDesc(RevisionBondCollectE.GMT_CREATE);

        Page<RevisionBondCollectE> page = revisionBondCollectMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), BondCollectDetailV.class));
    }

    public Boolean dealBtnShow(BondCollectDetailV form) {

        form.setShowBtnPost(false)
                .setShowBtnEdit(false)
                .setShowBtnCollect(false)
                .setShowBtnDelete(false)
                .setShowBtnReceipt(false)
                .setShowBtnRefund(false)
                .setShowBtnTransfer(false)
                .setShowBtnVolumeUp(false)
                .setShowBtnVolumeUpRecord(false);

        if (BondStatusEnum.审批中.getCode().equals(form.getStatus())) {
            return true;
        }

        if (BondStatusEnum.已拒绝.getCode().equals(form.getStatus())) {
            form.setShowBtnPost(true)
                    .setShowBtnDelete(true);
            return true;
        }

        if (BondStatusEnum.待提交.getCode().equals(form.getStatus())) {
            form.setShowBtnPost(true)
                    .setShowBtnEdit(true)
                    .setShowBtnDelete(true);
            return true;
        }

        form.setShowBtnCollect(true)
                .setShowBtnTransfer(true)
                .setShowBtnRefund(true)
                .setShowBtnReceipt(true);

        if (form.getCollectAmount().compareTo(form.getPlannedCollectionAmount()) == 0 || BondStatusEnum.已完成.getCode().equals(form.getStatus())) {
            form.setShowBtnCollect(false);
        }

        if (form.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || form.getCollectAmount().compareTo(form.getSettleTransferAmount().add(form.getRefundAmount())) == 0) {
            form.setShowBtnTransfer(false)
                    .setShowBtnRefund(false);
        }

        if (form.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || form.getCollectAmount().compareTo(form.getReceiptAmount()) == 0) {
            form.setShowBtnReceipt(false);
        }

        if (BondTypeEnum.投标保证金.getCode().equals(form.getTypeCode()) && Objects.nonNull(form.getResidueAmount()) && form.getResidueAmount().compareTo(BigDecimal.ZERO) > 0) {
            form.setShowBtnVolumeUp(true);
        }

        if (BondTypeEnum.投标保证金.getCode().equals(form.getTypeCode()) && Objects.nonNull(form.getResidueAmount()) && form.getResidueAmount().compareTo(BigDecimal.ZERO) == 0) {
            long count = bondRelationBillService.count(new QueryWrapper<CollectBondRelationBillE>()
                    .eq(CollectBondRelationBillE.BOND_ID, form.getId())
                    .isNotNull(CollectBondRelationBillE.VOLUM_UP_ID));
            //-- 若转履约生成的结转单数量不为0，则展示转履约记录按钮
            if (count != 0) {
                form.setShowBtnVolumeUpRecord(true);
            }
        }

        return true;
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的RevisionBondCollectE中仅包含a字段的值
    *
    * @param fields RevisionBondCollectE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public RevisionBondCollectE selectOneBy(Consumer<QueryWrapper<RevisionBondCollectE>> consumer,String... fields) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionBondCollectMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionBondCollectE中id字段的值, select 指定字段
    *
    * @param fields RevisionBondCollectE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<RevisionBondCollectE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(revisionBondCollectMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<RevisionBondCollectE>> consumer) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        revisionBondCollectMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的RevisionBondCollectE中仅包含a字段的值
     *
     * @param fields RevisionBondCollectE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<RevisionBondCollectE> selectListBy(Consumer<QueryWrapper<RevisionBondCollectE>> consumer,String... fields) {
         QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return revisionBondCollectMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的RevisionBondCollectE中仅包含a字段的值
    *
    * @param fields RevisionBondCollectE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<RevisionBondCollectE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<RevisionBondCollectE>> consumer, String... fields) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionBondCollectMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionBondCollectE中id字段的值, select 指定字段
     *
     * @param fields RevisionBondCollectE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<RevisionBondCollectE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<RevisionBondCollectE> page = Page.of(pageNum, pageSize, count);
        Page<RevisionBondCollectE> queryPage = revisionBondCollectMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
