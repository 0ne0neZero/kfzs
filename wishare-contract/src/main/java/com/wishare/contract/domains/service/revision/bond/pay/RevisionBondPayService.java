package com.wishare.contract.domains.service.revision.bond.pay;

import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.enums.revision.bond.BondStatusEnum;
import com.wishare.contract.domains.enums.revision.bond.BondTypeEnum;
import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import com.wishare.contract.domains.vo.revision.bond.pay.BondPayDetailV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.mapper.revision.bond.pay.RevisionBondPayMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wishare.starter.Global;
import com.wishare.starter.beans.OrderBy;
import com.wishare.starter.beans.PageF;
import com.wishare.starter.beans.PageV;
import com.wishare.tools.starter.fo.search.SearchF;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayV;
import com.wishare.contract.domains.vo.revision.bond.pay.RevisionBondPayListV;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayPageF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPaySaveF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayUpdateF;
import com.wishare.contract.apps.fo.revision.bond.pay.RevisionBondPayListF;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.function.Consumer;
/**
 * <p>
 * 保证金改版-缴纳类保证金
 * </p>
 *
 * @author chenglong
 * @since 2023-07-28
 */
@Service
@Slf4j
@SuppressWarnings("unused")
public class RevisionBondPayService extends ServiceImpl<RevisionBondPayMapper, RevisionBondPayE> implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private RevisionBondPayMapper revisionBondPayMapper;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private PayBondRelationBillService bondRelationBillService;


    /**
    * 获取详细信息
    *
    * @param conditions 查询条件
    * @return option 如果查询不到，上层可自定义处理
    */
    @Nonnull
    public Optional<RevisionBondPayV> get(RevisionBondPayF conditions){
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.isNotBlank(conditions.getId())) {
            queryWrapper.eq(RevisionBondPayE.ID, conditions.getId());
        }

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondPayE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondPayE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondPayE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getCustomerId())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER_ID, conditions.getCustomerId());
        }

        if (StringUtils.isNotBlank(conditions.getCustomer())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER, conditions.getCustomer());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondPayE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedPayDate())) {
            queryWrapper.gt(RevisionBondPayE.PLANNED_PAY_DATE, conditions.getPlannedPayDate());
        }

        if (Objects.nonNull(conditions.getPlannedPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PLANNED_PAY_AMOUNT, conditions.getPlannedPayAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondPayE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondPayE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondPayE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondPayE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondPayE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondPayE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondPayE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondPayE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondPayE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondPayE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondPayE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondPayE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondPayE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondPayE.BANK_ACCOUNT, conditions.getBankAccount());
        }

        queryWrapper.eq(RevisionBondPayE.TENANT_ID, tenantId());

        RevisionBondPayE revisionBondPayE = revisionBondPayMapper.selectOne(queryWrapper);
        if (revisionBondPayE != null) {
            return Optional.of(Global.mapperFacade.map(revisionBondPayE, RevisionBondPayV.class));
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
    public RevisionBondPayListV list(RevisionBondPayListF conditions){
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondPayE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondPayE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondPayE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getCustomerId())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER_ID, conditions.getCustomerId());
        }

        if (StringUtils.isNotBlank(conditions.getCustomer())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER, conditions.getCustomer());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondPayE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedPayDate())) {
            queryWrapper.gt(RevisionBondPayE.PLANNED_PAY_DATE, conditions.getPlannedPayDate());
        }

        if (Objects.nonNull(conditions.getPlannedPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PLANNED_PAY_AMOUNT, conditions.getPlannedPayAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondPayE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondPayE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondPayE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondPayE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondPayE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondPayE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondPayE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondPayE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondPayE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondPayE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondPayE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondPayE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondPayE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondPayE.BANK_ACCOUNT, conditions.getBankAccount());
        }
        if (Objects.nonNull(conditions.getIndexId())) {
            queryWrapper.lt(RevisionBondPayE.ID, conditions.getIndexId());
        }
        queryWrapper.orderByDesc(RevisionBondPayE.ID);
        queryWrapper.eq(RevisionBondPayE.TENANT_ID, tenantId());
        queryWrapper.last("limit " + Optional.ofNullable(conditions.getLimit()).orElse(20));
        List<RevisionBondPayV> retVList = Global.mapperFacade.mapAsList(revisionBondPayMapper.selectList(queryWrapper),RevisionBondPayV.class);
        RevisionBondPayListV retV = new RevisionBondPayListV();

        if (!retVList.isEmpty()) {
            retV.setIndexId(retVList.get(retVList.size() - 1).getId());
        }
        retV.setInfoList(retVList);
        return retV;
    }

    public String save(RevisionBondPaySaveF revisionBondPayF){
        RevisionBondPayE map = Global.mapperFacade.map(revisionBondPayF, RevisionBondPayE.class);
        revisionBondPayMapper.insert(map);
        return map.getId();
    }


   /**
    * 根据Id更新
    *
    * @param revisionBondPayF 根据Id更新
    */
    public void update(RevisionBondPayUpdateF revisionBondPayF){
        if (revisionBondPayF.getId() == null) {
            throw new IllegalArgumentException();
        }
        RevisionBondPayE map = Global.mapperFacade.map(revisionBondPayF, RevisionBondPayE.class);
        revisionBondPayMapper.updateById(map);
    }

   /**
    *
    * @param id 根据Id删除
    * @return 删除结果
    */
    public boolean removeById(String id){
        revisionBondPayMapper.deleteById(id);
        return true;
    }


   /**
    * 该接口供给后端
    *
    * @param request 请求分页的参数
    * @return 查询出的分页列表
    */
    public PageV<RevisionBondPayV> page(PageF<RevisionBondPayPageF> request) {
        RevisionBondPayPageF conditions = request.getConditions();
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        

        if (StringUtils.isNotBlank(conditions.getCode())) {
            queryWrapper.eq(RevisionBondPayE.CODE, conditions.getCode());
        }

        if (StringUtils.isNotBlank(conditions.getTypeCode())) {
            queryWrapper.eq(RevisionBondPayE.TYPE_CODE, conditions.getTypeCode());
        }

        if (StringUtils.isNotBlank(conditions.getType())) {
            queryWrapper.eq(RevisionBondPayE.TYPE, conditions.getType());
        }

        if (StringUtils.isNotBlank(conditions.getCustomerId())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER_ID, conditions.getCustomerId());
        }

        if (StringUtils.isNotBlank(conditions.getCustomer())) {
            queryWrapper.eq(RevisionBondPayE.CUSTOMER, conditions.getCustomer());
        }

        if (StringUtils.isNotBlank(conditions.getContractId())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_ID, conditions.getContractId());
        }

        if (StringUtils.isNotBlank(conditions.getContractCode())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_CODE, conditions.getContractCode());
        }

        if (StringUtils.isNotBlank(conditions.getContractName())) {
            queryWrapper.eq(RevisionBondPayE.CONTRACT_NAME, conditions.getContractName());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterId())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_ID, conditions.getCostCenterId());
        }

        if (StringUtils.isNotBlank(conditions.getCostCenterName())) {
            queryWrapper.eq(RevisionBondPayE.COST_CENTER_NAME, conditions.getCostCenterName());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityId())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_ID, conditions.getCommunityId());
        }

        if (StringUtils.isNotBlank(conditions.getCommunityName())) {
            queryWrapper.eq(RevisionBondPayE.COMMUNITY_NAME, conditions.getCommunityName());
        }

        if (Objects.nonNull(conditions.getBondAmount())) {
            queryWrapper.eq(RevisionBondPayE.BOND_AMOUNT, conditions.getBondAmount());
        }

        if (Objects.nonNull(conditions.getPlannedPayDate())) {
            queryWrapper.gt(RevisionBondPayE.PLANNED_PAY_DATE, conditions.getPlannedPayDate());
        }

        if (Objects.nonNull(conditions.getPlannedPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PLANNED_PAY_AMOUNT, conditions.getPlannedPayAmount());
        }

        if (StringUtils.isNotBlank(conditions.getOrgId())) {
            queryWrapper.eq(RevisionBondPayE.ORG_ID, conditions.getOrgId());
        }

        if (StringUtils.isNotBlank(conditions.getOrgName())) {
            queryWrapper.eq(RevisionBondPayE.ORG_NAME, conditions.getOrgName());
        }

        if (StringUtils.isNotBlank(conditions.getChargeManId())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN_ID, conditions.getChargeManId());
        }

        if (StringUtils.isNotBlank(conditions.getChargeMan())) {
            queryWrapper.eq(RevisionBondPayE.CHARGE_MAN, conditions.getChargeMan());
        }

        if (StringUtils.isNotBlank(conditions.getRemark())) {
            queryWrapper.eq(RevisionBondPayE.REMARK, conditions.getRemark());
        }

        if (Objects.nonNull(conditions.getStatus())) {
            queryWrapper.eq(RevisionBondPayE.STATUS, conditions.getStatus());
        }

        if (Objects.nonNull(conditions.getPayAmount())) {
            queryWrapper.eq(RevisionBondPayE.PAY_AMOUNT, conditions.getPayAmount());
        }

        if (Objects.nonNull(conditions.getCollectAmount())) {
            queryWrapper.eq(RevisionBondPayE.COLLECT_AMOUNT, conditions.getCollectAmount());
        }

        if (Objects.nonNull(conditions.getReceiptAmount())) {
            queryWrapper.eq(RevisionBondPayE.RECEIPT_AMOUNT, conditions.getReceiptAmount());
        }

        if (Objects.nonNull(conditions.getSettleTransferAmount())) {
            queryWrapper.eq(RevisionBondPayE.SETTLE_TRANSFER_AMOUNT, conditions.getSettleTransferAmount());
        }


        if (StringUtils.isNotBlank(conditions.getTenantId())) {
            queryWrapper.eq(RevisionBondPayE.TENANT_ID, conditions.getTenantId());
        }

        if (StringUtils.isNotBlank(conditions.getCreator())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR, conditions.getCreator());
        }

        if (StringUtils.isNotBlank(conditions.getCreatorName())) {
            queryWrapper.eq(RevisionBondPayE.CREATOR_NAME, conditions.getCreatorName());
        }

        if (Objects.nonNull(conditions.getGmtCreate())) {
            queryWrapper.gt(RevisionBondPayE.GMT_CREATE, conditions.getGmtCreate());
        }

        if (StringUtils.isNotBlank(conditions.getOperator())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR, conditions.getOperator());
        }

        if (StringUtils.isNotBlank(conditions.getOperatorName())) {
            queryWrapper.eq(RevisionBondPayE.OPERATOR_NAME, conditions.getOperatorName());
        }

        if (Objects.nonNull(conditions.getGmtModify())) {
            queryWrapper.gt(RevisionBondPayE.GMT_MODIFY, conditions.getGmtModify());
        }

        if (Objects.nonNull(conditions.getBillId())) {
            queryWrapper.eq(RevisionBondPayE.BILL_ID, conditions.getBillId());
        }

        if (StringUtils.isNotBlank(conditions.getBillNo())) {
            queryWrapper.eq(RevisionBondPayE.BILL_NO, conditions.getBillNo());
        }

        if (StringUtils.isNotBlank(conditions.getBankName())) {
            queryWrapper.eq(RevisionBondPayE.BANK_NAME, conditions.getBankName());
        }

        if (StringUtils.isNotBlank(conditions.getBankAccount())) {
            queryWrapper.eq(RevisionBondPayE.BANK_ACCOUNT, conditions.getBankAccount());
        }
        if (CollectionUtils.isNotEmpty(conditions.getFields())) {
            queryWrapper.select(conditions.getFields().toArray(String[]::new));
        }
        List<OrderBy> orderBy = request.getOrderBy();
        queryWrapper.eq(RevisionBondPayE.TENANT_ID, tenantId());
        if (CollectionUtils.isNotEmpty(orderBy)) {
            orderBy.forEach(v -> queryWrapper.orderBy(true, v.isAsc(), v.getField()));
        } else {
            // 默认排序
            queryWrapper.orderByDesc(RevisionBondPayE.GMT_CREATE);
        }
        Page<RevisionBondPayE> page = revisionBondPayMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), RevisionBondPayV.class));
    }

   /**
    * 该接口供给给前端
    *
    * @param request 前端请求参数
    * @return 查询出的分页列表
    */
    public PageV<BondPayDetailV> frontPage(PageF<SearchF<RevisionBondPayE>> request) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        SearchF<RevisionBondPayE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }
        List<OrderBy> orderBy = request.getOrderBy();

        queryWrapper.eq(RevisionBondPayE.TENANT_ID, tenantId())
                .orderByAsc(RevisionBondPayE.STATUS)
                .orderByDesc(RevisionBondPayE.GMT_CREATE);

        Page<RevisionBondPayE> page = revisionBondPayMapper.selectPage(
                Page.of(request.getPageNum(), request.getPageSize(), request.isCount()), queryWrapper);
        return PageV.of(request, page.getTotal(), Global.mapperFacade.mapAsList(page.getRecords(), BondPayDetailV.class));
    }

    public Boolean dealBtnShow(BondPayDetailV form) {

        form.setShowBtnPost(false)
                .setShowBtnEdit(false)
                .setShowBtnCollect(false)
                .setShowBtnDelete(false)
                .setShowBtnReceipt(false)
                .setShowBtnPay(false)
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

        form.setShowBtnPay(true)
                .setShowBtnTransfer(true)
                .setShowBtnCollect(true)
                .setShowBtnReceipt(true);

        if (form.getPayAmount().compareTo(form.getPlannedPayAmount()) == 0 || BondStatusEnum.已完成.getCode().equals(form.getStatus())) {
            form.setShowBtnPay(false);
        }

        if (form.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || form.getPayAmount().compareTo(form.getSettleTransferAmount().add(form.getCollectAmount())) == 0) {
            form.setShowBtnTransfer(false)
                    .setShowBtnCollect(false);
        }

        if (form.getPayAmount().compareTo(BigDecimal.ZERO) == 0 || form.getPayAmount().compareTo(form.getReceiptAmount()) == 0) {
            form.setShowBtnReceipt(false);
        }

        if (BondTypeEnum.投标保证金.getCode().equals(form.getTypeCode()) && Objects.nonNull(form.getResidueAmount()) && form.getResidueAmount().compareTo(BigDecimal.ZERO) > 0) {
            form.setShowBtnVolumeUp(true);
        }

        if (BondTypeEnum.投标保证金.getCode().equals(form.getTypeCode()) && Objects.nonNull(form.getResidueAmount()) && form.getResidueAmount().compareTo(BigDecimal.ZERO) == 0) {
            long count = bondRelationBillService.count(new QueryWrapper<PayBondRelationBillE>()
                    .eq(PayBondRelationBillE.BOND_ID, form.getId())
                    .isNotNull(PayBondRelationBillE.VOLUM_UP_ID));
            //-- 若转履约生成的结转单数量不为0，则展示转履约记录按钮
            if (count != 0) {
                form.setShowBtnVolumeUpRecord(true);
            }
        }

        return true;
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，输入a字段名称，则仅查询出的RevisionBondPayE中仅包含a字段的值
    *
    * @param fields RevisionBondPayE 中的字段 即 表字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的单个数据
    */
    public RevisionBondPayE selectOneBy(Consumer<QueryWrapper<RevisionBondPayE>> consumer,String... fields) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionBondPayMapper.selectOne(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionBondPayE中id字段的值, select 指定字段
    *
    * @param fields RevisionBondPayE 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @param <T> 需要转成的类型
    * @return 查询出的列表
    */
    public <T> List<T> selectListBy(Consumer<QueryWrapper<RevisionBondPayE>> consumer,Class<T> retClazz,
            String... fields) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return Global.mapperFacade.mapAsList(revisionBondPayMapper.selectList(queryWrapper), retClazz);
    }


   /**
    * 根据条件删除数据
    *
    * @param consumer 消费者，可填充自定义条件
    */
    public void deleteBy(Consumer<QueryWrapper<RevisionBondPayE>> consumer) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        revisionBondPayMapper.delete(queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，出入a字段名称，则仅查询出的RevisionBondPayE中仅包含a字段的值
     *
     * @param fields RevisionBondPayE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @return 查询出的列表
     */
    public List<RevisionBondPayE> selectListBy(Consumer<QueryWrapper<RevisionBondPayE>> consumer,String... fields) {
         QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
         consumer.accept(queryWrapper);
         queryWrapper.select(fields);
         return revisionBondPayMapper.selectList(queryWrapper);
    }

   /**
    * 根据指定的fields查询表的指定字段的值，比如查询表中 a字段，传入a字段名称，则仅查询出的RevisionBondPayE中仅包含a字段的值
    *
    * @param fields RevisionBondPayE 中的字段 即 表字段 中的字段 即 表字段 select 指定字段
    * @param consumer 消费者，可填充自定义条件
    * @return 查询出的列表
    */
    public Page<RevisionBondPayE> selectPageBy(long pageNum, long pageSize,
        boolean count, Consumer<QueryWrapper<RevisionBondPayE>> consumer, String... fields) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        return revisionBondPayMapper.selectPage(Page.of(pageNum, pageSize, count),queryWrapper);
    }

    /**
     * 根据指定的fields查询表的指定字段的值，比如查询表中 id字段，传入"id",则仅查询出的RevisionBondPayE中id字段的值, select 指定字段
     *
     * @param fields RevisionBondPayE 中的字段 即 表字段 select 指定字段
     * @param consumer 消费者，可填充自定义条件
     * @param <T> 需要转成的类型
     * @return 查询出的列表
     */
    public <T> PageV<T> selectPageBy(long pageNum, long pageSize, boolean count, Consumer<QueryWrapper<RevisionBondPayE>>
                                consumer, Class<T> retClazz, String... fields) {
        QueryWrapper<RevisionBondPayE> queryWrapper = new QueryWrapper<>();
        consumer.accept(queryWrapper);
        queryWrapper.select(fields);
        Page<RevisionBondPayE> page = Page.of(pageNum, pageSize, count);
        Page<RevisionBondPayE> queryPage = revisionBondPayMapper.selectPage(page, queryWrapper);
        return PageV.of(pageNum, pageSize, queryPage.getTotal(),Global.mapperFacade.mapAsList(queryPage.getRecords(), retClazz));
    }


}
