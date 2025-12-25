package com.wishare.contract.apps.service.revision.bond;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wishare.contract.apps.fo.revision.attachment.AttachmentSaveF;
import com.wishare.contract.apps.fo.revision.bond.BondVolumUpF;
import com.wishare.contract.apps.fo.revision.bond.CollectBondBillF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectAddF;
import com.wishare.contract.apps.fo.revision.bond.RevisionBondCollectEditF;
import com.wishare.contract.apps.remote.clients.*;
import com.wishare.contract.apps.remote.vo.TenantInfoRv;
import com.wishare.contract.domains.entity.revision.bond.CollectBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.RevisionBondCollectE;
import com.wishare.contract.domains.entity.revision.bond.pay.PayBondRelationBillE;
import com.wishare.contract.domains.entity.revision.bond.pay.RevisionBondPayE;
import com.wishare.contract.domains.entity.revision.income.ContractIncomeConcludeE;
import com.wishare.contract.domains.entity.revision.pay.ContractPayConcludeE;
import com.wishare.contract.domains.enums.revision.BondRefundStatusEnum;
import com.wishare.contract.domains.enums.revision.DictionaryCodeEnum;
import com.wishare.contract.domains.enums.revision.FileSaveTypeEnum;
import com.wishare.contract.domains.enums.revision.bond.BondActionTypeEnum;
import com.wishare.contract.domains.enums.revision.bond.BondDealWayEnum;
import com.wishare.contract.domains.enums.revision.bond.BondStatusEnum;
import com.wishare.contract.domains.enums.revision.bond.BondTypeEnum;
import com.wishare.contract.domains.mapper.revision.bond.CollectBondRelationBillMapper;
import com.wishare.contract.domains.mapper.revision.bond.RevisionBondCollectMapper;
import com.wishare.contract.domains.service.revision.attachment.AttachmentService;
import com.wishare.contract.domains.service.revision.bond.CollectBondRelationBillService;
import com.wishare.contract.domains.service.revision.bond.RevisionBondCollectService;
import com.wishare.contract.domains.service.revision.income.ContractIncomeConcludeService;
import com.wishare.contract.domains.service.revision.pay.ContractPayConcludeService;
import com.wishare.contract.domains.vo.revision.bond.BondCollectDetailV;
import com.wishare.contract.domains.vo.revision.bond.BondNumShowV;
import com.wishare.contract.domains.vo.revision.bond.pay.VolumeUpV;
import com.wishare.owl.enhance.IOwlApiBase;
import com.wishare.owl.exception.OwlBizException;
import com.wishare.owl.util.OptionalCollection;
import com.wishare.starter.Global;
import com.wishare.starter.beans.PageF;
import com.wishare.tools.starter.fo.search.Field;
import com.wishare.tools.starter.fo.search.SearchF;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * @version 1.0.0
 * @Description：
 * @Author： chentian
 * @since： 2023/7/26  11:19
 */
@Service
@Slf4j
public class BondCollectBusinessService implements IOwlApiBase {

    @Setter(onMethod_ = {@Autowired})
    private ContractPayConcludeService contractPayConcludeService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondCollectService revisionBondCollectService;

    @Setter(onMethod_ = {@Autowired})
    private RevisionBondCollectMapper revisionBondCollectMapper;

    @Setter(onMethod_ = {@Autowired})
    private CollectBondRelationBillMapper bondRelationBillMapper;

    @Setter(onMethod_ = {@Autowired})
    private CollectBondRelationBillService bondRelationBillService;

    @Setter(onMethod_ = {@Autowired})
    private OrgFeignClient orgFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private UserFeignClient userFeignClient;

    @Setter(onMethod_ = {@Autowired})
    private FinanceFeignClient financeFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private SpaceFeignClient spaceFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private ConfigFeignClient configFeignClient;

    @Setter(onMethod_ = {@Autowired})
    @Getter
    private AttachmentService attachmentService;

    /**
     * 新增方法
     * @param form 新增请求参数
     * @return 主键ID
     */
    @Transactional
    public String add(RevisionBondCollectAddF form) {
        RevisionBondCollectE map = Global.mapperFacade.map(form, RevisionBondCollectE.class);

        //-- 处理参数赋值信息
        dealValueForAdd(map);
        //-- 计划编码
        map.setCode(getCodeForAdd(tenantId()));
        //-- 状态
        map.setStatus(BondStatusEnum.待提交.getCode())
                .setTenantId(tenantId());

        //-- 校验保证金总额与计划金额是否相等
        if (map.getBondAmount().compareTo(map.getPlannedCollectionAmount()) != 0) {
            throw new OwlBizException("保证金总额须等于计划收款金额，请重新编辑");
        }

        map.setCollectAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setDeductionAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setRefundAmount(BigDecimal.ZERO);

        revisionBondCollectMapper.insert(map);

        //-- 非暂存，则执行提交操作
        if (!Boolean.TRUE.equals(form.getIsStash())) {
            postBondPlan(map.getId());
        }

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF().setBusinessType(FileSaveTypeEnum.保证金附件凭证.getCode())
                    .setFileVo(form.getFileVo())
                    .setBusinessId(map.getId()));
        }

        return map.getId();
    }

    public Boolean edit(RevisionBondCollectEditF form) {
        RevisionBondCollectE map = Global.mapperFacade.map(form, RevisionBondCollectE.class);

        //-- 校验保证金总额与计划金额是否相等
        if (map.getBondAmount().compareTo(map.getPlannedCollectionAmount()) != 0) {
            throw new OwlBizException("保证金总额须等于计划收款金额，请重新编辑");
        }

        //-- 处理参数赋值信息
        dealValueForEdit(map);

        revisionBondCollectService.updateById(map);

        //-- 非暂存，则执行提交操作
        if (!Boolean.TRUE.equals(form.getIsStash())) {
            postBondPlan(map.getId());
        }

        return Boolean.TRUE;
    }


    public void dealValueForAdd(RevisionBondCollectE map) {
        //-- 保证金类型
        map.setType(BondTypeEnum.parseName(map.getTypeCode()));
        //-- 供应商名称
        Optional.ofNullable(orgFeignClient.getSupplierVById(map.getSupplierId())).ifPresentOrElse(v -> {
            if (!v.getDisabled().equals(0)) {
                throw new OwlBizException("供应商数据已被禁用，请重新选择");
            }
            map.setSupplier(v.getName());
        }, () -> {throw new OwlBizException("根据供应商ID检索供应商数据失败");});
        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(map.getContractId())) {
            ContractPayConcludeE payConcludeE = contractPayConcludeService.getById(map.getContractId());
            if (Objects.isNull(payConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            map.setContractCode(payConcludeE.getContractNo())
                        .setContractName(payConcludeE.getName());
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(map.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(map.getCostCenterId()))).ifPresentOrElse(v -> {
                map.setCostCenterName(v.getNameCn());
                if (Objects.nonNull(v.getCommunityId())) {
                    map.setCommunityId(v.getCommunityId());
                    Optional.ofNullable(spaceFeignClient.getById(v.getCommunityId())).ifPresent(c -> {
                        map.setCommunityName(c.getName());
                    });
                }
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }        //-- 所属项目
        Optional.ofNullable(spaceFeignClient.getById(map.getCommunityId())).ifPresentOrElse(c -> {
            map.setCommunityName(c.getName());
        }, () -> {throw new OwlBizException("根据所属项目ID检索项目数据失败");});
        //-- 所属部门
        if (StringUtils.isNotBlank(map.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getOrgId()))).ifPresentOrElse(v -> {
                map.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        } else {
            throw new OwlBizException("所属部门不可为空");
        }
        //-- 负责人
        Optional.ofNullable(userFeignClient.getUsreInfoByUserId(map.getChargeManId())).ifPresentOrElse(v -> {
            map.setChargeMan(v.getUserName());
        }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
    }

    public void dealValueForEdit(RevisionBondCollectE map) {
        //-- 保证金类型
        map.setType(BondTypeEnum.parseName(map.getTypeCode()));
        //-- 供应商名称
        Optional.ofNullable(orgFeignClient.getSupplierVById(map.getSupplierId())).ifPresentOrElse(v -> {
            if (!v.getDisabled().equals(0)) {
                throw new OwlBizException("供应商数据已被禁用，请重新选择");
            }
            map.setSupplier(v.getName());
        }, () -> {throw new OwlBizException("根据供应商ID检索供应商数据失败");});
        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(map.getContractId())) {
            ContractPayConcludeE payConcludeE = contractPayConcludeService.getById(map.getContractId());
            if (Objects.isNull(payConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            map.setContractCode(payConcludeE.getContractNo())
                    .setContractName(payConcludeE.getName());
        }
        //-- 成本中心
        if (StringUtils.isNotBlank(map.getCostCenterId())) {
            Optional.ofNullable(orgFeignClient.getByFinanceCostId(Long.parseLong(map.getCostCenterId()))).ifPresentOrElse(v -> {
                map.setCostCenterName(v.getNameCn());
                if (Objects.nonNull(v.getCommunityId())) {
                    map.setCommunityId(v.getCommunityId());
                    Optional.ofNullable(spaceFeignClient.getById(v.getCommunityId())).ifPresent(c -> {
                        map.setCommunityName(c.getName());
                    });
                }
            }, () -> {throw new OwlBizException("根据成本中心ID检索成本中心数据失败");});
        }
        //-- 所属项目
        Optional.ofNullable(spaceFeignClient.getById(map.getCommunityId())).ifPresentOrElse(c -> {
            map.setCommunityName(c.getName());
        }, () -> {throw new OwlBizException("根据所属项目ID检索项目数据失败");});
        //-- 所属部门
        if (StringUtils.isNotBlank(map.getOrgId())) {
            Optional.ofNullable(orgFeignClient.getByOrgId(Long.parseLong(map.getOrgId()))).ifPresentOrElse(v -> {
                map.setOrgName(v.getOrgName());
            }, () -> {throw new OwlBizException("根据所属部门ID检索组织名称失败");});
        } else {
            throw new OwlBizException("所属部门不可为空");
        }
        //-- 负责人
        Optional.ofNullable(userFeignClient.getUsreInfoByUserId(map.getChargeManId())).ifPresentOrElse(v -> {
            map.setChargeMan(v.getUserName());
        }, () -> {throw new OwlBizException("根据负责人ID检索名称失败");});
    }

    public String getCodeForAdd(String tenantId) {
        String code = null;
        //生成保证金编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());


        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId);
        String nature = "";

        Integer count = 0;

        nature = "BZJSQ";
        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId)
                .ge(RevisionBondCollectE.GMT_CREATE, LocalDate.now().atStartOfDay());
        List<RevisionBondCollectE> list = revisionBondCollectService.list(queryWrapper);
        count = list.size();

        //主合同编号
        String code1 = String.format("%0" + 4 + "d", count + 1);
        if (null != tenantInfoRv && org.springframework.util.StringUtils.hasText(tenantInfoRv.getEnglishName())) {
            code = tenantInfoRv.getEnglishName() + nature + year + code1;
        } else {
            code = nature + year + code1;
        }

        return code;
    }

    public Boolean postBondPlan(String id){
        RevisionBondCollectE bondE = revisionBondCollectService.getById(id);
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("该保证金计划数据不存在");
        }

        //-- 默认审批通过
        bondE.setStatus(BondStatusEnum.未完成.getCode())
                .setCollectAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setDeductionAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setRefundAmount(BigDecimal.ZERO);

        revisionBondCollectService.updateById(bondE);

        return true;
    }

    public BondCollectDetailV detail(String id) {

        RevisionBondCollectE entity = revisionBondCollectService.getById(id);

        if (Objects.isNull(entity)) {
            return null;
        }

        BondCollectDetailV detailV = Global.mapperFacade.map(entity, BondCollectDetailV.class);

        detailV.setBondId(detailV.getId())
                .setFiles(attachmentService.listById(id))
                .setResidueAmount((detailV.getCollectAmount().subtract(detailV.getSettleTransferAmount())).subtract(detailV.getRefundAmount()));

        revisionBondCollectService.dealBtnShow(detailV);

        return detailV;
    }

    public String getBillCode(String actionType, String suffix) {

        String code = null;
        //生成保证金编号==客户(租户)简称+业务模块缩写+年后两位+月日+四位 ，如YYHT2208160001;
        String year = new SimpleDateFormat("yyMMdd", Locale.CHINESE).format(new Date());


        TenantInfoRv tenantInfoRv = orgFeignClient.getById(tenantId());

        Integer count = 0;
        List<CollectBondRelationBillE> list = bondRelationBillService.list(new QueryWrapper<CollectBondRelationBillE>()
                .eq(CollectBondRelationBillE.TENANT_ID, tenantId())
                .eq(CollectBondRelationBillE.TYPE_CODE, actionType)
                .ge(CollectBondRelationBillE.GMT_CREATE, LocalDate.now().atStartOfDay()));
        count = list.size();

        String code1 = String.format("%0" + 4 + "d", count + 1);
        if (null != tenantInfoRv && org.springframework.util.StringUtils.hasText(tenantInfoRv.getEnglishName())) {
            code = tenantInfoRv.getEnglishName() + suffix + year + code1;
        } else {
            code = suffix + year + code1;
        }

        return code;
    }

    @Transactional
    public Boolean collect(CollectBondBillF form) {
        CollectBondRelationBillE map = Global.mapperFacade.map(form, CollectBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getCollectAmount().compareTo(bondE.getPlannedCollectionAmount()) == 0 || BondStatusEnum.已完成.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金已经完成收款，无法进行收款操作");
        }
        if (bondE.getCollectAmount().add(map.getAmount()).compareTo(bondE.getPlannedCollectionAmount()) > 0) {
            throw new OwlBizException("已收金额 + 本次收款金额 大于计划收款金额，无法进行收款操作");
        }

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.收取类收款.getCode(), "BZJSKD"))
                .setTypeCode(BondActionTypeEnum.收取类收款.getCode())
                .setType(BondActionTypeEnum.收取类收款.getName())
                .setStatus(BondStatusEnum.已完成.getCode());

        OptionalCollection.ofNullable(configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收款方式.getCode(), map.getDealWayCode())).ifNotEmptyOrElse(v -> {
            map.setDealWay(v.get(0).getName());
        }, () -> {throw new OwlBizException("交易方式信息存在错误");});

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setCollectAmount(bondE.getCollectAmount().add(map.getAmount()));
        if (bondE.getCollectAmount().compareTo(bondE.getPlannedCollectionAmount()) == 0) {
            bondE.setStatus(BondStatusEnum.已完成.getCode());
        }
        if (!BondRefundStatusEnum.未退款.getCode().equals(bondE.getRefundStatus())) {
            boolean flag = bondE.getCollectAmount().subtract(bondE.getSettleTransferAmount().add(bondE.getRefundAmount())).compareTo(BigDecimal.ZERO) == 0;
            bondE.setRefundStatus(flag ? BondRefundStatusEnum.已退款.getCode() : BondRefundStatusEnum.部分退款.getCode());
        }
        revisionBondCollectService.updateById(bondE);

        return true;
    }

    @Transactional
    public Boolean transfer(CollectBondBillF form) {
        CollectBondRelationBillE map = Global.mapperFacade.map(form, CollectBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getCollectAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getRefundAmount())) == 0) {
            throw new OwlBizException("保证金计划已收金额为0 或 保证金可结转金额为0， 无法进行结转操作");
        }
        if (bondE.getRefundAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getCollectAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已退款金额 + 本次结转金额大于已收款金额，无法进行结转操作");
        }

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.收取类结转.getCode(), "BZJJZD"))
                .setTypeCode(BondActionTypeEnum.收取类结转.getCode())
                .setType(BondActionTypeEnum.收取类结转.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getCollectAmount().subtract(bondE.getSettleTransferAmount().add(map.getAmount())));

        map.setTenantId(tenantId());

        if (StringUtils.isBlank(map.getChargeItemId())) {
            throw new OwlBizException("结转费项信息不可为空");
        }
        Optional.ofNullable(financeFeignClient.chargeName(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
            map.setChargeItem(v);
        }, () -> {throw new OwlBizException("根据结转费项ID检索费项信息失败");});

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setSettleTransferAmount(bondE.getSettleTransferAmount().add(map.getAmount()));
        revisionBondCollectService.updateById(bondE);

        return true;
    }

    public Boolean refund(CollectBondBillF form) {
        CollectBondRelationBillE map = Global.mapperFacade.map(form, CollectBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getCollectAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getRefundAmount())) == 0) {
            throw new OwlBizException("保证金计划已收金额为0 或 保证金可退款金额为0， 无法进行退款操作");
        }
        if (bondE.getRefundAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getCollectAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已退款金额 + 本次退款金额大于已收款金额，无法进行退款操作");
        }
        //-- TODO 缴纳类保证金退款状态   未退款  部分退款  已退款

        //-- 生成明细记录
        map.setCode(getBillCode(BondActionTypeEnum.收取类退款.getCode(), "BZJTKD"))
                .setTypeCode(BondActionTypeEnum.收取类退款.getCode())
                .setType(BondActionTypeEnum.收取类退款.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getCollectAmount().subtract(bondE.getRefundAmount().add(map.getAmount())));

        OptionalCollection.ofNullable(configFeignClient.getKeyAndValueByCode(DictionaryCodeEnum.收款方式.getCode(), map.getDealWayCode())).ifNotEmptyOrElse(v -> {
            map.setDealWay(v.get(0).getName());
        }, () -> {throw new OwlBizException("交易方式信息存在错误");});

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setRefundAmount(bondE.getRefundAmount().add(map.getAmount()));
        boolean flag = bondE.getCollectAmount().subtract(bondE.getSettleTransferAmount().add(bondE.getRefundAmount())).compareTo(BigDecimal.ZERO) == 0;
        bondE.setRefundStatus(flag ? BondRefundStatusEnum.已退款.getCode() : BondRefundStatusEnum.部分退款.getCode());
        revisionBondCollectService.updateById(bondE);

        return true;
    }

    public Boolean receipt(CollectBondBillF form) {
        CollectBondRelationBillE map = Global.mapperFacade.map(form, CollectBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getCollectAmount().compareTo(bondE.getReceiptAmount()) == 0) {
            throw new OwlBizException("保证金计划已收金额为0 或 保证金可收据金额为0， 无法进行收据操作");
        }
        if (bondE.getReceiptAmount().add(map.getAmount()).compareTo(bondE.getCollectAmount()) > 0) {
            throw new OwlBizException("已收据金额 + 本次收据金额大于已收款金额，无法进行收据操作");
        }

        map.setCode(getBillCode(BondActionTypeEnum.收取类收据.getCode(), "BZJSJD"))
                .setTypeCode(BondActionTypeEnum.收取类收据.getCode())
                .setType(BondActionTypeEnum.收取类收据.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getCollectAmount().subtract(bondE.getReceiptAmount().add(map.getAmount())));

        map.setTenantId(tenantId());

        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }

        //-- 更新保证金计划
        bondE.setReceiptAmount(bondE.getReceiptAmount().add(map.getAmount()));
        revisionBondCollectService.updateById(bondE);

        return true;
    }

    public BondNumShowV getNumShowForPage(PageF<SearchF<RevisionBondCollectE>> request) {

        QueryWrapper<RevisionBondCollectE> queryWrapper = new QueryWrapper<>();
        SearchF<RevisionBondCollectE> conditions = request.getConditions();
        if (conditions != null) {
            List<Field> fields = conditions.getFields();
            if (CollectionUtils.isNotEmpty(fields)) {
                queryWrapper = conditions.getQueryModel();
            }
        }

        queryWrapper.eq(RevisionBondCollectE.TENANT_ID, tenantId())
                .orderByAsc(RevisionBondCollectE.STATUS)
                .orderByDesc(RevisionBondCollectE.GMT_CREATE);

        List<RevisionBondCollectE> list = revisionBondCollectService.list(queryWrapper);

        if (CollectionUtils.isEmpty(list)) {
            return new BondNumShowV()
                    .setAmount(BigDecimal.ZERO)
                    .setPlanAmount(BigDecimal.ZERO);
        }

        BigDecimal planAmount = BigDecimal.ZERO;
        BigDecimal amount = BigDecimal.ZERO;
        BigDecimal resAmount = BigDecimal.ZERO;

        for (RevisionBondCollectE bondE : list) {
            planAmount = planAmount.add(bondE.getPlannedCollectionAmount());
            amount = amount.add(bondE.getCollectAmount());
            resAmount = resAmount.add(bondE.getCollectAmount().subtract(bondE.getSettleTransferAmount()).subtract(bondE.getRefundAmount()));
        }

        return new BondNumShowV().setPlanAmount(planAmount).setAmount(amount).setResAmount(resAmount);
    }

    /**
     * 转履约
     * 对原保证金的数据进行更新
     * 生成一条新的保证金记录
     * 生成一条结转记录，并关联相关保证金记录数据
     * @param form 转履约传参
     * @return
     */
    @Transactional
    public Boolean volumeUp (BondVolumUpF form) {
        CollectBondRelationBillE map = Global.mapperFacade.map(form, CollectBondRelationBillE.class);
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(map.getBondId());
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        if (BondStatusEnum.待提交.getCode().equals(bondE.getStatus()) || BondStatusEnum.审批中.getCode().equals(bondE.getStatus()) || BondStatusEnum.已拒绝.getCode().equals(bondE.getStatus())) {
            throw new OwlBizException("保证金状态限制，不可进行相关业务操作");
        }
        if (bondE.getCollectAmount().compareTo(BigDecimal.ZERO) == 0 || bondE.getCollectAmount().compareTo(bondE.getSettleTransferAmount().add(bondE.getRefundAmount())) == 0) {
            throw new OwlBizException("保证金计划已收金额为0 或 保证金可结转金额为0， 无法进行结转操作");
        }
        if (bondE.getRefundAmount().add(bondE.getSettleTransferAmount().add(map.getAmount())).compareTo(bondE.getCollectAmount()) > 0) {
            throw new OwlBizException("已结转金额 + 已退款金额 + 本次结转金额大于已收款金额，无法进行结转操作");
        }

        //-- 对原保证金计划进行更新
        bondE.setSettleTransferAmount(bondE.getSettleTransferAmount().add(map.getAmount()));
        revisionBondCollectService.updateById(bondE);

        //-- 生成一条新的保证金记录
        RevisionBondCollectE addF = Global.mapperFacade.map(bondE, RevisionBondCollectE.class);
        addF.setTypeCode(BondTypeEnum.履约保证金.getCode())
                .setType(BondTypeEnum.履约保证金.getName())
                .setId(null)
                .setGmtCreate(null)
                .setGmtModify(null)
                .setCreator(null)
                .setCreatorName(null)
                .setOperator(null)
                .setOperatorName(null)
                .setTenantId(tenantId())
                .setCode(getCodeForAdd(tenantId()))
                .setStatus(BondStatusEnum.待提交.getCode())
                .setCollectAmount(BigDecimal.ZERO)
                .setReceiptAmount(BigDecimal.ZERO)
                .setSettleTransferAmount(BigDecimal.ZERO)
                .setCollectAmount(BigDecimal.ZERO)
                .setBondAmount(map.getAmount())
                .setPlannedCollectionAmount(map.getAmount());
        //-- 关联投标保证金相关字段赋值
        addF.setIsTender(1)
                .setTenderBondId(bondE.getId())
                .setTenderBondAmount(bondE.getBondAmount());

        //-- 合同数据检索赋值
        if (StringUtils.isNotBlank(form.getContractId())) {
            ContractPayConcludeE payConcludeE = contractPayConcludeService.getById(form.getContractId());
            if (Objects.isNull(payConcludeE)) {
                throw new OwlBizException("根据合同ID检索数据失败");
            }
            addF.setContractId(form.getContractId())
                    .setContractCode(payConcludeE.getContractNo())
                    .setContractName(payConcludeE.getName());
        }
        revisionBondCollectMapper.insert(addF);
        postBondPlan(addF.getId());

        //-- 生成一条结转记录
        map.setCode(getBillCode(BondActionTypeEnum.缴纳类结转.getCode(), "BZJJZD"))
                .setTypeCode(BondActionTypeEnum.缴纳类结转.getCode())
                .setType(BondActionTypeEnum.缴纳类结转.getName())
                .setStatus(BondStatusEnum.已完成.getCode())
                .setResidueAmount(bondE.getCollectAmount().subtract(bondE.getSettleTransferAmount().add(map.getAmount())));
        if (StringUtils.isNotBlank(map.getChargeItemId())) {
            Optional.ofNullable(financeFeignClient.chargeName(Long.parseLong(map.getChargeItemId()))).ifPresentOrElse(v -> {
                map.setChargeItem(v);
            }, () -> {throw new OwlBizException("根据结转费项ID检索费项信息失败");});
        }
        map.setTenantId(tenantId())
                .setVolumUpId(addF.getId());
        bondRelationBillMapper.insert(map);

        if (Objects.nonNull(form.getFileVo())) {
            attachmentService.save(new AttachmentSaveF()
                    .setBusinessId(map.getId())
                    .setBusinessType(FileSaveTypeEnum.保证金业务操作附件凭证.getCode())
                    .setFileVo(form.getFileVo()));
        }


        return true;
    }

    /**
     * 获取保证金转履约记录
     * @param id 保证金ID
     * @return 转履约记录
     */
    public List<VolumeUpV> getVolumRecord(String id) {
        //-- 校验关联保证金计划
        RevisionBondCollectE bondE = revisionBondCollectService.getById(id);
        if (Objects.isNull(bondE)) {
            throw new OwlBizException("检索保证金计划失败");
        }
        List<CollectBondRelationBillE> list = bondRelationBillService.list(new QueryWrapper<CollectBondRelationBillE>()
                .eq(CollectBondRelationBillE.BOND_ID, id)
                .isNotNull(CollectBondRelationBillE.VOLUM_UP_ID)
                .orderByDesc(CollectBondRelationBillE.GMT_CREATE));

        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }

        List<VolumeUpV> result = new ArrayList<>();

        for (CollectBondRelationBillE billE : list) {
            VolumeUpV volume = new VolumeUpV();
            volume.setId(bondE.getId())
                    .setCode(bondE.getCode())
                    .setGmtCreate(bondE.getGmtCreate())
                    .setCreator(bondE.getCreator())
                    .setCreatorName(bondE.getCreatorName())
                    .setDate(billE.getGmtCreate().toLocalDate());
            RevisionBondCollectE e = revisionBondCollectService.getById(billE.getVolumUpId());
            if (Objects.nonNull(e)) {
                volume.setTransferId(e.getId())
                        .setTransferCode(e.getCode())
                        .setAmount(e.getPlannedCollectionAmount());
            }
            result.add(volume);
        }

        return result;
    }

}
